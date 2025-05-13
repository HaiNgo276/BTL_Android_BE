package com.example.bookshop.service;

import com.example.bookshop.entity.Rating;
import com.example.bookshop.repository.RatingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepo ratingRepo;

    public double ratingLevel(int bookId) {
        int totalRating = 0;
        List<Rating> ratings = ratingRepo.getAllByBook_Id(bookId);
        if (ratings.size() > 0) {
            for (Rating rating : ratings) {
                totalRating += rating.getRatingLevel();
            }
            return Math.round(totalRating * 10.0 / ratings.size()) / 10.0;
        }
        return 5.0;
    }
}
