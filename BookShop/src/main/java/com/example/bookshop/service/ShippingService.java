package com.example.bookshop.service;

import com.example.bookshop.entity.Shipping;
import com.example.bookshop.repository.ShippingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingService {
    @Autowired
    private ShippingRepo shippingRepo;
    public List<Shipping> getAll(){
        return shippingRepo.findAll();
    }
    public Shipping findById(int shippingId){
        return shippingRepo.findById(shippingId);
    }
}
