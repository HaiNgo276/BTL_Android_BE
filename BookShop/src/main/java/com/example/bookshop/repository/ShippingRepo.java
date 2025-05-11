package com.example.bookshop.repository;

import com.example.bookshop.entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ShippingRepo extends JpaRepository<Shipping, Integer> {
    List<Shipping> findAll();
    Shipping findById(int shippingId);
}
