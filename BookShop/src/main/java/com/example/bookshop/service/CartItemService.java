package com.example.bookshop.service;

import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Cart;
import com.example.bookshop.entity.CartItem;
import com.example.bookshop.repository.CartItemRepo;
import com.example.bookshop.repository.CartRepo;
import com.example.bookshop.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ProductRepo productRepo;

    public CartItem save(CartItem cartItem) {
        return cartItemRepo.save(cartItem);
    }

    public CartItem findByIdAndCustomerId(int cartItemId, int customerId) {
        return cartItemRepo.findByIdAndCartCustomerId(cartItemId, customerId);
    }

    public CartItem findByBookIdAndCustomerId(int bookId, int customerId) {
        return cartItemRepo.findByBookIdAndCartCustomerId(bookId, customerId);
    }

    public List<CartItem> getAllByCustomerId(int customerId) {
        return cartItemRepo.getAllByCartCustomerId(customerId);
    }

    public void deleteByCartItemId(int itemCartId, int customerId) {
        cartItemRepo.deleteByIdAndCartCustomerId(itemCartId, customerId);
    }

    public void emptyCart(String cartId) {
        cartItemRepo.deleteByCartId(cartId);
    }


    public void restoreQuantity(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            book.setQuantitySold(book.getQuantitySold() - cartItem.getQuantity());
            productRepo.save(book);
        }
    }
}
