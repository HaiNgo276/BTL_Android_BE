package com.example.bookshop.repository;

import com.example.bookshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, String> {
    Cart findByCustomerId(int customerId);
}
