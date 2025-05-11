package com.example.bookshop.controller;

import com.example.bookshop.config.jwt.JwtUtil;
import com.example.bookshop.dto.objectdto.bookdto.BookInCartDto;
import com.example.bookshop.dto.objectdto.cartdto.CartItemDto;
import com.example.bookshop.dto.response.Error;
import com.example.bookshop.dto.response.Message;
import com.example.bookshop.dto.response.cart.CartResponse;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Cart;
import com.example.bookshop.entity.CartItem;
import com.example.bookshop.service.*;
import com.example.bookshop.util.BookUtil;
import com.example.bookshop.util.CartUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(path = "/shoppingCart")
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private WishListItemService wishListItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestHeader("user-key") String userKey,
                                           @RequestParam("product_id") int product_id) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Cart cart = cartService.findByCustomerId(customerId);
            Book book = productService.findById(product_id);
            CartItem cartItemExisted = cartItemService.findByBookIdAndCustomerId(product_id, customerId);
            if (cartItemExisted == null) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setBook(book);
                cartItem.setQuantity(1);
                cartItem.setAddOn(LocalDateTime.now());
                cartItemService.save(cartItem);
            } else {
                cartItemExisted.setQuantity(cartItemExisted.getQuantity() + 1);
                cartItemService.save(cartItemExisted);
            }
            book.setQuantitySold(book.getQuantitySold() + 1);
            productService.addBook(book);
            List<CartItem> cartItems = cartItemService.getAllByCustomerId(customerId);
            List<CartItemDto> cartItemDtos = new CartUtil().addToCartItemDto(cartItems);
            return ResponseEntity.ok(cartItemDtos);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @DeleteMapping("/removeProduct/{item_id}")
    public ResponseEntity<?> removeProductByItemId(@RequestHeader("user-key") String userKey,
                                                   @PathVariable("item_id") int itemId) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            CartItem cartItemExisted = cartItemService.findByIdAndCustomerId(itemId, customerId);
            if (cartItemExisted == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "CART_01", "Sản phẩm này không trong giỏ hàng của bạn!", "ITEM_ID"));
            } else {
                cartItemService.deleteByCartItemId(itemId, customerId);
                Book book = cartItemExisted.getBook();
                book.setQuantitySold(book.getQuantitySold() - 1);
                productService.addBook(book);
                return ResponseEntity.ok(new Message("Đã xóa item khỏi giỏ hàng của bạn!"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @DeleteMapping("/empty")
    public ResponseEntity<?> emptyCart(@RequestHeader("user-key") String userKey) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Cart cart = cartService.findByCustomerId(customerId);
            List<CartItem> cartItems = cartItemService.getAllByCustomerId(customerId);
            cartItemService.restoreQuantity(cartItems);
            cartItemService.emptyCart(cart.getId());
            return ResponseEntity.ok(new Message("Đã làm trống giỏ hàng của bạn"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> changeProductQuantity(@RequestHeader("user-key") String userKey,
                                                   @RequestParam("item_id") int itemId,
                                                   @RequestParam("quantity") int quantity) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            CartItem cartItem = cartItemService.findByIdAndCustomerId(itemId, customerId);
            if (cartItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404, "CART_01", "Sản phẩm này không trong giỏ hàng của bạn!", "ITEM_ID"));
            } else {
                int quantityBookBefore = cartItem.getQuantity();
                cartItem.setQuantity(quantity);
                Book book = cartItem.getBook();
                book.setQuantitySold(book.getQuantitySold() + (quantity - quantityBookBefore));
                productService.addBook(book);
                cartItemService.save(cartItem);
                return ResponseEntity.ok(new Message("Đã thay đổi số lượng"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }



}
