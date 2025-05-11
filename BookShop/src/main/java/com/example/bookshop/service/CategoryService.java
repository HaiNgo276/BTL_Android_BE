package com.example.bookshop.service;

import com.example.bookshop.entity.Category;
import com.example.bookshop.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

