package com.portfolio.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // Diz ao Spring que isso é uma tabela do banco
@Table(name = "products") // Nome exato da tabela no Postgres
@Data // Lombok: Gera Getters, Setters, ToString, etc. automaticamente
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco gera o ID (Serial)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price; // Sempre use BigDecimal para dinheiro!

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity; // O JPA vai mapear stockQuantity para stock_quantity

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}