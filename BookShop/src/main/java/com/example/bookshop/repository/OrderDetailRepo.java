package com.example.bookshop.repository;

import com.example.bookshop.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {
    OrderDetail save(OrderDetail orderDetail);
    List<OrderDetail> getAllByOrderCustomerId(int customerId);
    List<OrderDetail> getAllByOrderId(int orderId);
}
