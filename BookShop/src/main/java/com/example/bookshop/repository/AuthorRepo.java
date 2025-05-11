package com.example.bookshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bookshop.entity.Author;

import java.util.List;

public interface AuthorRepo extends JpaRepository<Author, Integer> {
    @Query("SELECT a FROM Author a WHERE MOD(a.id, 3) = 1")
    List<Author> findFamousAuthor();

    Author findById(int id);

    List<Author> findAll();

    @Query("SELECT a from Author a")
    Page<Author> getAllAuthor(Pageable pageable);

    Author save(Author author);
}
