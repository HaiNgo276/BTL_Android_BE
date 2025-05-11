package com.example.bookshop.service;

import com.example.bookshop.entity.OrderStatus;
import com.example.bookshop.repository.OrderStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusService {
    @Autowired
    private OrderStatusRepo orderStatusRepo;

    public OrderStatus findById(int orderStatusId) {
        return orderStatusRepo.findById(orderStatusId);
    }
}
