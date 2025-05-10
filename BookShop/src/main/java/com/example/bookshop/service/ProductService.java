package com.example.bookshop.service;

import com.example.bookshop.entity.Book;
import com.example.bookshop.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public Book findById(int id) {
        return productRepo.findById(id);
    }


}
