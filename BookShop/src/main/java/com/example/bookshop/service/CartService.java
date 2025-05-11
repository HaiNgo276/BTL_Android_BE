package com.example.bookshop.service;

import com.example.bookshop.entity.Cart;
import com.example.bookshop.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;

    public Cart findByCustomerId(int customerId){
        return cartRepo.findByCustomerId(customerId);
    }
}
