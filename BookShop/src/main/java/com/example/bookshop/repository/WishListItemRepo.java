package com.example.bookshop.repository;

import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Wishlistitem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface WishListItemRepo extends JpaRepository<Wishlistitem, Integer> {

    @Query("SELECT b from Book b INNER JOIN Wishlistitem wi ON b.id=wi.book.id INNER JOIN WishList w ON wi.wishList.id=w.id " +
            "WHERE w.customer.id= :customerId and length(b.description)>= :descriptionLength")
    Page<Book> getMyWishList(int customerId, int descriptionLength, Pageable pageable);

    @Query("select w from Wishlistitem w where w.book.id= :bookId and w.wishList.customer.id= :customerId")
    Wishlistitem findByBookAndCustomerId(int bookId, int customerId);

    @Query("SELECT b from Book b INNER JOIN Wishlistitem wi ON b.id=wi.book.id INNER JOIN WishList w ON wi.wishList.id=w.id " +
            "WHERE w.customer.id= :customerId")
    List<Book> getAllBooksInWishlist(int customerId);

    @Transactional
    void deleteByBookIdAndWishListCustomerId(int bookId, int customerId);
}
