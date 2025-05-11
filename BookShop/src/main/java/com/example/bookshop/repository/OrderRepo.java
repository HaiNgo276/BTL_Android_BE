package com.example.bookshop.repository;

import com.example.bookshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    Order save(Order order);

    List<Order> getAllByCustomerIdOrderByCreateOnDesc(int customerId);

    Order getOrderById(int orderId);

    List<Order> getAllByOrderStatusIdOrderByCreateOnDesc(int orderStatusId);
    List<Order> getAllByOrderStatusIdOrderByShippedOnDesc(int orderStatusId);

    @Query("SELECT o FROM Order o where o.createOn >= :startDate AND o.createOn <= :endDate and o.orderStatus.id=3")
    List<Order> getOrderByTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
