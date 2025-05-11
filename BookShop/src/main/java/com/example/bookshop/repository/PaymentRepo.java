package com.example.bookshop.repository;

import com.example.bookshop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    Payment findById(int paymentId);
}
