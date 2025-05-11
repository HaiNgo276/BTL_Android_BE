package com.example.bookshop.repository;

import com.example.bookshop.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Book, Integer> {
    @Query("SELECT p FROM Book p WHERE LENGTH(p.description) >= :descriptionLength")
    Page<Book> getProducts(
            int descriptionLength, Pageable pageable);

    Book save(Book book);

    Book findById(int bookId);

    Book findByName(String bookName);

    List<Book> findTop20ByOrderByIdDesc();

    List<Book> findTop20ByOrderByQuantitySoldDesc();

    List<Book> findTop5ByBannerIsNotNullOrderByIdDesc();

    List<Book> findTop5ByOrderByQuantitySoldDesc();

    @Query("SELECT b from Book  b where length(b.description)>= :descriptionLength and b.name like %:query% ORDER BY b.id DESC")
    Page<Book> findByProductNews(int descriptionLength, String query, Pageable pageable);

    @Query("SELECT b from Book b where length(b.description)>= :descriptionLength and b.name like %:query% ORDER BY b.quantitySold DESC")
    Page<Book> findByProductSelling(int descriptionLength, String query, Pageable pageable);

    @Query("SELECT b from Book b where length(b.description)>= :descriptionLength and b.name like %:query% ORDER BY b.discounted_price ASC ")
    Page<Book> findByProductPriceSortAsc(int descriptionLength, String query, Pageable pageable);

    @Query("SELECT b from Book b where length(b.description)>= :descriptionLength and b.name like %:query% ORDER BY b.discounted_price DESC ")
    Page<Book> findByProductPriceSortDesc(int descriptionLength, String query, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.author.id= :authorId AND LENGTH(b.description)>= :descriptionLength")
    Page<Book> findByAuthorIdAndDescriptionLengthGreaterThanEqual(
            int authorId, int descriptionLength, Pageable pageable);

    @Query("SELECT b from Book b where length(b.description)>= :descriptionLength and b.name like %:query% and b.author.id= :authorId")
    Page<Book> searchProductByAuthor(int descriptionLength, String query, int authorId, Pageable pageable);

    @Query("SELECT p FROM Book p WHERE p.category.id = :categoryId AND LENGTH(p.description) >= :descriptionLength")
    Page<Book> findByCategoryIdAndDescriptionLengthGreaterThanEqual(
            int categoryId, int descriptionLength, Pageable pageable);

    @Query("SELECT b from Book b where b.category.id= :categoryId and length(b.description)>= :descriptionLength and b.name like %:query%")
    Page<Book> searchProductByCategory(int descriptionLength, String query, int categoryId, Pageable pageable);

    @Query("SELECT b from Book b where length(b.description)>= :descriptionLength and b.name like %:query% and b.supplier.id= :supplierId")
    Page<Book> searchProductBySupplier(int descriptionLength, String query, int supplierId, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.supplier.id= :supplierId AND LENGTH(b.description)>= :descriptionLength")
    Page<Book> findBySupplierIdAndDescriptionLengthGreaterThanEqual(
            int supplierId, int descriptionLength, Pageable pageable);
}
