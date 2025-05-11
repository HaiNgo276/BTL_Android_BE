package com.example.bookshop.repository;

import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepo extends JpaRepository<WishList, Integer> {

    WishList findByCustomerId(int customerId);


}
