package com.example.bookshop.repository;

import com.example.bookshop.entity.Rating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepo extends JpaRepository<Rating, Integer> {
    List<Rating> getAllByBook_Id(int bookId);
}
