package com.example.bookshop.service;

import com.example.bookshop.entity.Category;
import com.example.bookshop.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    public List<Category> getCategoryAll() {
        return categoryRepo.findAll();
    }

    public Category addCategory(Category category) {
        return categoryRepo.save(category);
    }

    public void deleteCategory(int idCategory) {
        categoryRepo.deleteCategoryById(idCategory);
    }

    public List<Category> getCategoryHot() {
        return categoryRepo.findTop8ByOrderByIdAsc();
    }

    public List<Object[]> getCategoryBestSeller(){
        Pageable pageable = PageRequest.of(0, 3);
        return categoryRepo.findTop3CategoryByQuantitySold(pageable);
    }
}

