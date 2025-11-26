package com.portfolio.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.ecommerce.model.CartItem;
import com.portfolio.ecommerce.model.Product;
import com.portfolio.ecommerce.model.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    // SQL automático: SELECT * FROM cart_items WHERE user_id = ?
    List<CartItem> findByUser(User user);
    
    // SQL automático: SELECT * FROM cart_items WHERE user_id = ? AND product_id = ?
    // Usamos Optional porque o item pode não existir ainda
    Optional<CartItem> findByUserAndProduct(User user, Product product);
}