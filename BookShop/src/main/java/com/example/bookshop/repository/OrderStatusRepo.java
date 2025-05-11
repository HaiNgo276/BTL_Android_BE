package com.example.bookshop.repository;

import com.example.bookshop.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepo extends JpaRepository<OrderStatus, Integer> {
    OrderStatus findById(int orderStatusId);
}
