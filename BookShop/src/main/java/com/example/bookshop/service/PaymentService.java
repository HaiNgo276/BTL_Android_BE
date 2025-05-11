package com.example.bookshop.service;

import com.example.bookshop.entity.Payment;
import com.example.bookshop.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;
    public Payment getPaymentById(int paymentId){
        return paymentRepo.findById(paymentId);
    }
}
