package com.portfolio.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.ecommerce.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}