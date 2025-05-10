package com.example.bookshop.service;

import com.example.bookshop.entity.WishList;
import com.example.bookshop.repository.WishListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
    @Autowired
    private WishListRepo wishListRepo;

    public WishList findByCustomerId(int customerId) {
        return wishListRepo.findByCustomerId(customerId);
    }

    public WishList save(WishList wishList) {
        return wishListRepo.save(wishList);
    }
}
