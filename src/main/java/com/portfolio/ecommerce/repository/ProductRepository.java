package com.portfolio.ecommerce.repository;

import com.portfolio.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Só de estender JpaRepository, você já ganhou:
    // .findAll(), .save(), .findById(), .deleteById()... mágica!
} 