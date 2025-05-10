package com.example.bookshop.repository;

import com.example.bookshop.entity.Customer;
import com.example.bookshop.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for WishList entity
 * Handles database operations for wishlists
 */
@Repository
public interface WishListRepo extends JpaRepository<WishList, Integer> {

    /**
     * Find a wishlist by customer ID
     *
     * @param customerId the ID of the customer
     * @return the wishlist associated with the customer
     */
    WishList findByCustomerId(int customerId);

    /**
     * Find a wishlist by customer entity
     *
     * @param customer the customer entity
     * @return the wishlist associated with the customer
     */
    WishList findByCustomer(Customer customer);

    /**
     * Check if a wishlist exists for a customer
     *
     * @param customerId the ID of the customer
     * @return true if a wishlist exists, false otherwise
     */
    boolean existsByCustomerId(int customerId);

    /**
     * Count the number of items in a customer's wishlist
     *
     * @param customerId the ID of the customer
     * @return the number of items in the wishlist
     */
    @Query("SELECT COUNT(wi) FROM Wishlistitem wi WHERE wi.wishList.customer.id = :customerId")
    int countItemsByCustomerId(int customerId);
}
