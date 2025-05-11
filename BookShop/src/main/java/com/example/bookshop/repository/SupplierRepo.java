package com.example.bookshop.repository;

import com.example.bookshop.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier, Integer> {
    List<Supplier> findAll();

    Supplier save(Supplier supplier);
}

