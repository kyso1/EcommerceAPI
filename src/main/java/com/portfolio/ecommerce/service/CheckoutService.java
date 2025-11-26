package com.portfolio.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.ecommerce.model.CartItem;
import com.portfolio.ecommerce.model.Order;
import com.portfolio.ecommerce.model.OrderItem;
import com.portfolio.ecommerce.model.Product;
import com.portfolio.ecommerce.model.User;
import com.portfolio.ecommerce.repository.CartItemRepository;
import com.portfolio.ecommerce.repository.OrderItemRepository;
import com.portfolio.ecommerce.repository.OrderRepository;
import com.portfolio.ecommerce.repository.ProductRepository;
import com.portfolio.ecommerce.repository.UserRepository;

@Service
public class CheckoutService {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    @Transactional // <--- A MÁGICA: Ou tudo funciona, ou nada é salvo no banco!
    public Order checkout(Long userId) {
        // 1. Buscar usuário
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Buscar itens do carrinho
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("O carrinho está vazio!");
        }

        // 3. Calcular Total e criar o Pedido (Order)
        BigDecimal total = BigDecimal.ZERO;
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStatus("PENDING");
        
        // Vamos salvar primeiro para ter o ID do pedido gerado
        // O total real calculamos no loop abaixo
        newOrder.setTotalAmount(BigDecimal.ZERO); 
        newOrder = orderRepository.save(newOrder);

        // 4. Mover itens do Carrinho para Itens do Pedido (OrderItems)
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            // Validação de Estoque
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + product.getName());
            }

            // Atualizar Estoque do Produto
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            // Criar OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice()); // CONGELANDO O PREÇO
            
            // Somar ao total
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
            total = total.add(itemTotal);

            orderItemRepository.save(orderItem);
        }

        // 5. Atualizar o valor total do pedido e salvar novamente
        newOrder.setTotalAmount(total);
        orderRepository.save(newOrder);

        // 6. Esvaziar o carrinho (Obrigatório!)
        cartItemRepository.deleteAll(cartItems);

        return newOrder;
    }
}