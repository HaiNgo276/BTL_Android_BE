package com.example.bookshop.service;

import com.example.bookshop.dto.request.RatingRequest;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.Order;
import com.example.bookshop.entity.Rating;
import com.example.bookshop.repository.CustomerRepo;
import com.example.bookshop.repository.OrderRepo;
import com.example.bookshop.repository.ProductRepo;
import com.example.bookshop.repository.RatingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepo ratingRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CustomerRepo customerRepo;

    public Page<Rating> getAllByBookId(int bookId, int limit, int page) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return ratingRepo.getAllByBook_IdOrderByCreateTimeDesc(bookId, pageable);
    }

    public Page<Rating> getAllRatingByUser(int userId, int limit, int page) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return ratingRepo.getAllByCustomerIdOrderByCreateTimeDesc(userId, pageable);
    }

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

//    public void createRating(List<RatingRequest> ratingRequests) {
//        for (RatingRequest ratingRequest : ratingRequests) {
//            Rating rating = new Rating();
//            Book book = productRepo.findById(ratingRequest.getBookId());
//            Customer customer = customerRepo.findById(ratingRequest.getUserId());
//            Order order = orderRepo.getOrderById(ratingRequest.getOrderId());
//            rating.setRatingLevel(ratingRequest.getRatingLevel());
//            rating.setBook(book);
//            rating.setComment(ratingRequest.getComment());
//            rating.setCustomer(customer);
//            rating.setCreateTime(new Date());
//            order.setIsRating(1);
//            orderRepo.save(order);
//            ratingRepo.save(rating);
//        }
//    }
}
