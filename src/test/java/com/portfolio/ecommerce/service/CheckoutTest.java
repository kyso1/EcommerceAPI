package com.portfolio.ecommerce.service; 

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portfolio.ecommerce.model.CartItem;
import com.portfolio.ecommerce.model.Order;
import com.portfolio.ecommerce.model.Product;
import com.portfolio.ecommerce.model.User;
import com.portfolio.ecommerce.repository.CartItemRepository;
import com.portfolio.ecommerce.repository.OrderItemRepository;
import com.portfolio.ecommerce.repository.OrderRepository;
import com.portfolio.ecommerce.repository.ProductRepository;
import com.portfolio.ecommerce.repository.UserRepository;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class CheckoutServiceTest {

    // @Mock cria uma versão falsa do repositório
    @Mock private CartItemRepository cartItemRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;

    // @InjectMocks pega a classe real e injeta os mocks nela
    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    @DisplayName("Deve finalizar o pedido com sucesso quando o carrinho não estiver vazio")
    void shouldCheckoutSuccessfully() {
        // --- 1. ARRANGE (Preparar o cenário) ---
        
        // Criar um usuário falso
        User fakeUser = new User();
        fakeUser.setId(1L);

        // Criar um produto falso com estoque de 10
        Product fakeProduct = new Product();
        fakeProduct.setId(100L);
        fakeProduct.setPrice(new BigDecimal("50.00"));
        fakeProduct.setStockQuantity(10);

        // Criar um item no carrinho falso
        CartItem fakeCartItem = new CartItem();
        fakeCartItem.setProduct(fakeProduct);
        fakeCartItem.setQuantity(2); // Compraremos 2 itens
        
        // Ensinar os Mocks a responderem (Comportamento esperado)
        when(userRepository.findById(1L)).thenReturn(Optional.of(fakeUser));
        when(cartItemRepository.findByUser(fakeUser)).thenReturn(Arrays.asList(fakeCartItem));
        
        // Quando salvar o pedido, retorne o próprio pedido (para não dar erro de null)
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // --- 2. ACT (Executar a ação) ---
        Order result = checkoutService.checkout(1L);

        // --- 3. ASSERT (Verificar o resultado) ---
        
        // Verifica se o resultado não é nulo e status é PENDING
        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        
        // O valor total deve ser 100.00 (2 * 50.00)
        assertEquals(new BigDecimal("100.00"), result.getTotalAmount());

        // VERIFICAÇÕES CRUCIAIS:
        // 1. Ver se o estoque do produto foi salvo (atualizado)
        verify(productRepository, times(1)).save(fakeProduct);
        
        // 2. Ver se o item do pedido foi salvo
        verify(orderItemRepository, times(1)).save(any());
        
        // 3. Ver se o carrinho foi limpo no final (deleteALl foi chamado?)
        verify(cartItemRepository, times(1)).deleteAll(anyList());
    }
    
    @Test
    @DisplayName("Deve lançar erro se o carrinho estiver vazio")
    void shouldThrowErrorEmptyCart() {
        // --- ARRANGE ---
        User fakeUser = new User();
        fakeUser.setId(1L);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(fakeUser));
        // Retorna lista vazia
        when(cartItemRepository.findByUser(fakeUser)).thenReturn(Collections.emptyList());

        // --- ACT & ASSERT ---
        // Verifica se lança RuntimeException
        Exception exception = assertThrows(RuntimeException.class, () -> {
            checkoutService.checkout(1L);
        });

        assertEquals("O carrinho está vazio!", exception.getMessage());
    }
}