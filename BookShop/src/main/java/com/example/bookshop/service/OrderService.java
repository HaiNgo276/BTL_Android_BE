package com.example.bookshop.service;

import com.example.bookshop.dto.objectdto.orderdto.OrderDto;
import com.example.bookshop.entity.Order;
import com.example.bookshop.repository.OrderDetailRepo;
import com.example.bookshop.repository.OrderRepo;
import com.example.bookshop.util.ConvetDateTimeUTC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderDetailRepo orderDetailRepo;

    public Order save(Order order) {
        return orderRepo.save(order);
    }

    public List<Order> getAllByCustomerId(int customerId) {
        return orderRepo.getAllByCustomerIdOrderByCreateOnDesc(customerId);
    }

    public Order getOrderById(int orderId) {
        return orderRepo.getOrderById(orderId);
    }

}
