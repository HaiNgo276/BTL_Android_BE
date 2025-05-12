package com.example.bookshop.repository;

import com.example.bookshop.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepo extends JpaRepository<Rating, Integer> {

    Page<Rating> getAllByBook_IdOrderByCreateTimeDesc(int bookId, Pageable pageable);
    List<Rating> getAllByBook_Id(int bookId);

    Rating save(Rating rating);

    Page<Rating> getAllByCustomerIdOrderByCreateTimeDesc(int userId, Pageable pageable);
}
