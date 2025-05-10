package com.example.bookshop.repository;

import com.example.bookshop.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Book, Integer> {
    Book save(Book book);

//    void deleteById(int bookId);

    Book findById(int bookId);


}
