package com.example.bookshop.service;

import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Wishlistitem;
import com.example.bookshop.repository.WishListItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListItemService {
    @Autowired
    private WishListItemRepo wishListItemRepo;

    public Page<Book> getMyWishList(int customerId, int limit, int page, int descriptionLength) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return wishListItemRepo.getMyWishList(customerId, descriptionLength, pageable);
    }

    public Wishlistitem addWishList(Wishlistitem wishlistitem) {
        return wishListItemRepo.save(wishlistitem);
    }

    public Wishlistitem findByBookIdAndCustomerId(int bookId, int customerId) {
        return wishListItemRepo.findByBookAndCustomerId(bookId, customerId);
    }

    public List<Book> getAllBooksInWishlist(int customerId) {
        return wishListItemRepo.getAllBooksInWishlist(customerId);
    }

    public void deleteItemInWishlist(int bookId, int customerId) {
        wishListItemRepo.deleteByBookIdAndWishListCustomerId(bookId, customerId);
    }
}
