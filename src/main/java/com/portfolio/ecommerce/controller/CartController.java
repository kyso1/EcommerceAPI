package com.portfolio.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.ecommerce.dto.AddToCartRequest;
import com.portfolio.ecommerce.model.CartItem;
import com.portfolio.ecommerce.model.Product;
import com.portfolio.ecommerce.model.User;
import com.portfolio.ecommerce.repository.CartItemRepository;
import com.portfolio.ecommerce.repository.ProductRepository;
import com.portfolio.ecommerce.repository.UserRepository;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // --- GET: Ver o carrinho ---
    @GetMapping
    public List<CartItem> getMyCart() {
        // SIMULAÇÃO: Pegamos sempre o usuário ID 1 (Alice)
        // Na vida real, esse ID viria do Token JWT
        User user = userRepository.findById(1L).orElseThrow();
        
        return cartItemRepository.findByUser(user);
    }

    // --- POST: Adicionar item ao carrinho ---
    @PostMapping
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request) {
        // 1. Identificar o Usuário (Mockado como Alice ID 1)
        User user = userRepository.findById(1L).orElseThrow();

        // 2. Buscar o produto no banco
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // 3. Verificar se o item JÁ existe no carrinho desse usuário
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            // CENÁRIO A: O item já existe. Vamos apenas somar a quantidade.
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            // CENÁRIO B: Item novo. Vamos criar do zero.
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }

        return ResponseEntity.ok("Item adicionado ao carrinho!");
    }
    
    // --- DELETE: Remover item do carrinho ---
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long itemId) {
        cartItemRepository.deleteById(itemId);
        return ResponseEntity.ok("Item removido!");
    }
}