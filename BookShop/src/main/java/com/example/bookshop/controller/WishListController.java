package com.example.bookshop.controller;

import com.example.bookshop.config.jwt.JwtUtil;
import com.example.bookshop.dto.objectdto.bookdto.BookHotNewDto;
import com.example.bookshop.dto.response.Message;
import com.example.bookshop.dto.response.book.BookHotNewResponse;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.WishList;
import com.example.bookshop.entity.Wishlistitem;
import com.example.bookshop.service.ProductService;
import com.example.bookshop.service.WishListItemService;
import com.example.bookshop.service.WishListService;
import com.example.bookshop.util.BookUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bookshop.dto.response.Error;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/wishlist")
@AllArgsConstructor
@Slf4j
public class WishListController {
    @Autowired
    private WishListItemService wishListItemService;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ProductService productService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<?> getMyWishList(@RequestHeader("user-key") String userKey,
                                           @RequestParam("limit") int limit,
                                           @RequestParam("page") int page,
                                           @RequestParam("description_length") int descriptionLength) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Page<Book> books = wishListItemService.getMyWishList(customerId, limit, page, descriptionLength);
            List<BookHotNewDto> bookHotNewDtos = new BookUtil().addBookNewHot(books.getContent());
            BookHotNewResponse bookResponse = new BookHotNewResponse(bookHotNewDtos.size(), bookHotNewDtos);
            return ResponseEntity.ok(bookResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToWishList(@RequestHeader("user-key") String userKey,
                                               @RequestParam("product_id") int productId) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            Wishlistitem existingItem = wishListItemService.findByBookIdAndCustomerId(productId, customerId);
            if (existingItem == null) {
                WishList wishList = wishListService.findByCustomerId(customerId);
                Book book = productService.findById(productId);
                Wishlistitem wishlistitem = new Wishlistitem();
                wishlistitem.setBook(book);
                wishlistitem.setWishList(wishList);
                wishListItemService.addWishList(wishlistitem);
                return ResponseEntity.ok(new Message("Đã thêm vào wishlist của bạn!"));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(409, "PRO_01", "Sản phẩm này đã nằm trong wishlist của bạn!", ""));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }

    @DeleteMapping("/remove/{product_id}")
    public ResponseEntity<?> removeItemFromWishList(@PathVariable int product_id,
                                                    @RequestHeader("user-key") String userKey) {
        if (jwtUtil.isTokenExpired(userKey.replace("Bearer ", ""))) {
            int customerId = Integer.parseInt(jwtUtil.extractId(userKey.replace("Bearer ", "")));
            wishListItemService.deleteItemInWishlist(product_id, customerId);
            return ResponseEntity.ok(new Message("Đã xóa khỏi wishlist của bạn!"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(401, "AUT_02", "Userkey không hợp lệ hoặc đã hết hạn!", "USER_KEY"));
        }
    }
}
