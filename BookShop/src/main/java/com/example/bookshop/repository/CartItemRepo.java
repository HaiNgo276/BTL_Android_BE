package com.example.bookshop.repository;

import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {
    CartItem findByBookIdAndCartCustomerId(int bookId, int customerId);

    List<CartItem> getAllByCartCustomerId(int customerId);

    CartItem findByIdAndCartCustomerId(int cartId, int customerId);

    @Query("SELECT b from Book b INNER JOIN CartItem ci ON b.id=ci.book.id INNER JOIN Cart c ON ci.cart.id=c.id " +
            "WHERE c.customer.id= :customerId")
    List<Book> getAllBooksInCart(int customerId);

    @Transactional
    void deleteByIdAndCartCustomerId(int cartItemId, int customerId);

    @Transactional
    void deleteByCartId(String cartId);
}
