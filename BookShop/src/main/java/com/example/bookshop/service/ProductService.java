package com.example.bookshop.service;

import com.example.bookshop.entity.Book;
import com.example.bookshop.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public Book findById(int id) {
        return productRepo.findById(id);
    }

    public Book addBook(Book book) {
        return productRepo.save(book);
    }

    public Page<Book> getProducts(int page, int limit, int descriptionLength) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return productRepo.getProducts(descriptionLength, pageable);
    }

     public Book findByName(String bookName) {
        return productRepo.findByName(bookName);
    }

    public void deleteBook(int bookId) {
        productRepo.deleteById(bookId);
    }

    public List<Book> getProductsNew() {
        return productRepo.findTop20ByOrderByIdDesc();
    }

    public List<Book> getProductsHot() {
        return productRepo.findTop20ByOrderByQuantitySoldDesc();
    }

    public List<Book> getProductsBanner() {
        return productRepo.findTop5ByBannerIsNotNullOrderByIdDesc();
    }

    public List<Book> getBookByQuantitySold() {
        return productRepo.findTop5ByOrderByQuantitySoldDesc();
    }

    public Page<Book> findProductsByCategory(
            int categoryId, int descriptionLength, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return productRepo.findByCategoryIdAndDescriptionLengthGreaterThanEqual(
                categoryId, descriptionLength, pageable);
    }

    public Page<Book> findProductsBySupplier(int supplierId, int limit, int page, int descriptionLength) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return productRepo.findBySupplierIdAndDescriptionLengthGreaterThanEqual(supplierId, descriptionLength, pageable);
    }

    public Page<Book> findProductsByAuthor(int authorId, int limit, int page, int descriptionLength) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return productRepo.findByAuthorIdAndDescriptionLengthGreaterThanEqual(authorId, descriptionLength, pageable);
    }

    public Page<Book> getBooksNew(int limit, int page, int descriptionLength, String query) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return productRepo.findByProductNews(descriptionLength, query, pageable);
    }

    public Page<Book> getBooksSelling(int limit, int page, int descriptionLength, String query) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return productRepo.findByProductSelling(descriptionLength, query, pageable);
    }

    public Page<Book> getBooksPriceSortOrder(int limit, int page, int descriptionLength, String query, String priceSortOrder) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (priceSortOrder.equalsIgnoreCase("asc")) {
            return productRepo.findByProductPriceSortAsc(descriptionLength, query, pageable);
        } else {
            return productRepo.findByProductPriceSortDesc(descriptionLength, query, pageable);
        }
    }

    public Page<Book> searchBooksByCategory(int categoryId, int limit, int page, int descriptionLength, String query) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return productRepo.searchProductByCategory(descriptionLength, query, categoryId, pageable);
    }

    public Page<Book> searchBooksBySupplier(int supplierId, int limit, int page, int descriptionLength, String query) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return productRepo.searchProductBySupplier(descriptionLength, query, supplierId, pageable);
    }

    public Page<Book> searchBooksByAuthor(int authorId, int limit, int page, int descriptionLength, String query) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return productRepo.searchProductByAuthor(descriptionLength, query, authorId, pageable);
    }
}
