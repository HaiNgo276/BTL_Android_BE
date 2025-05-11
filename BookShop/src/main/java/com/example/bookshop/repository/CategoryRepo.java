package com.example.bookshop.repository;

import com.example.bookshop.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    List<Category> findAll();

    List<Category> findTop8ByOrderByIdAsc();

    Category save(Category category);

    void deleteCategoryById(int idCategory);

    @Query("SELECT c, SUM(b.quantitySold) AS totalSold FROM Category c " +
            "INNER JOIN Book b ON c.id = b.category.id" +
            " GROUP BY c.id ORDER BY totalSold DESC")
    List<Object[]> findTop3CategoryByQuantitySold(Pageable pageable);
}

