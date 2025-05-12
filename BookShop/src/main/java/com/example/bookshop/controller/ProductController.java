package com.example.bookshop.controller;

import com.example.bookshop.dto.objectdto.bookdto.*;
import com.example.bookshop.dto.response.Error;
import com.example.bookshop.dto.response.book.*;
import com.example.bookshop.dto.response.rating.RatingResponse;
import com.example.bookshop.entity.Book;
import com.example.bookshop.entity.Rating;
import com.example.bookshop.service.ProductService;
import com.example.bookshop.service.RatingService;
import com.example.bookshop.util.BookUtil;
import com.example.bookshop.util.RatingUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/products")
@AllArgsConstructor
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private RatingService ratingService;

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("description_length") int descriptionLength) {
        Page<Book> books = productService.getProducts(page, limit, descriptionLength);
        List<BookDto> bookDtos = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookDtos.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/new")
    public ResponseEntity<?> getProductsNew() {
        List<Book> products = productService.getProductsNew();
        List<BookHotNewDto> bookHotNewDtos = new BookUtil().addBookNewHot(products);
        BookHotNewResponse response = new BookHotNewResponse(products.size(), bookHotNewDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hot")
    public ResponseEntity<?> getProductsHot() {
        List<Book> products = productService.getProductsHot();
        List<BookHotNewDto> bookHotNewDtos = new BookUtil().addBookNewHot(products);
        BookHotNewResponse response = new BookHotNewResponse(products.size(), bookHotNewDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getSearchProduct(@RequestParam(name = "limit", defaultValue = "10") int limit,
                                              @RequestParam(name = "page", defaultValue = "1") int page,
                                              @RequestParam(name = "description_length", defaultValue = "100") int descriptionLength,
                                              @RequestParam(name = "query_string", defaultValue = "") String queryString,
                                              @RequestParam(name = "filter_type", defaultValue = "1") int filterType,
                                              @RequestParam(name = "price_sort_order", defaultValue = "asc") String priceSortOrder) {
        Page<Book> books;
        List<BookDto> bookDtos = new ArrayList<>();

        if (filterType == 1) {
            books = productService.getBooksNew(limit, page, descriptionLength, queryString);

        } else if (filterType == 2) {
            books = productService.getBooksSelling(limit, page, descriptionLength, queryString);
        } else {
            books = productService.getBooksPriceSortOrder(limit, page, descriptionLength, queryString, priceSortOrder);
        }
        bookDtos = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookDtos.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/author/search")
    public ResponseEntity<?> getSearchProductsByAuthor(@RequestParam("author_id") int authorId, @RequestParam("limit") int limit, @RequestParam("page") int page, @RequestParam("description_length") int descriptionLength, @RequestParam("query_string") String queryString) {
        Page<Book> books = productService.searchBooksByAuthor(authorId, limit, page, descriptionLength, queryString);
        List<BookDto> bookDtos = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookDtos.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/search")
    public ResponseEntity<?> getSearchProductsByCategory(@RequestParam("category_id") int categoryId, @RequestParam("limit") int limit, @RequestParam("page") int page, @RequestParam("description_length") int descriptionLength, @RequestParam("query_string") String queryString) {
        Page<Book> books = productService.searchBooksByCategory(categoryId, limit, page, descriptionLength, queryString);
        List<BookDto> bookDtos = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookDtos.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supplier/search")
    public ResponseEntity<?> getSearchProductsBySupplier(@RequestParam("supplier_id") int supplierId, @RequestParam("limit") int limit, @RequestParam("page") int page, @RequestParam("description_length") int descriptionLength, @RequestParam("query_string") String queryString) {
        Page<Book> books = productService.searchBooksBySupplier(supplierId, limit, page, descriptionLength, queryString);
        List<BookDto> bookDtos = new BookUtil().addBook(books.getContent());
        BookResponse response = new BookResponse(bookDtos.size(), bookDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/banner")
    public ResponseEntity<?> getProductsRecommend() {
        List<Book> products = productService.getProductsBanner();
        List<BookBannerDto> bookBannerDtos = new BookUtil().addBookBanner(products);
        BookBannerResponse response = new BookBannerResponse(bookBannerDtos.size(), bookBannerDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rating")
    public ResponseEntity<?> getRatingByBook(@RequestParam("bookId") int bookId,
                                             @RequestParam("limit") int limit,
                                             @RequestParam("page") int page) {
        Page<Rating> ratings = ratingService.getAllByBookId(bookId, limit, page);
        RatingResponse ratingResponse = new RatingResponse(ratings.getContent().size(), new RatingUtil().addToRatingDto(ratings.getContent()));
        return ResponseEntity.ok(ratingResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleFileSizeLimitExceededException() {
        return ResponseEntity.badRequest().body(new Error(400, "FILE_01", "Kích thước tệp tin vượt quá giới hạn cho phép(3MB).", "FILE"));
    }
}
