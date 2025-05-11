package com.example.bookshop.controller;

import com.example.bookshop.dto.response.Message;
import com.example.bookshop.dto.response.category.CategoryResponse;
import com.example.bookshop.entity.Category;
import com.example.bookshop.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/category")
@AllArgsConstructor
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> getCategoryAll() {
        List<Category> categories = categoryService.getCategoryAll();
        CategoryResponse response = new CategoryResponse(categories.size(), categories);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Category categoryRequest) {
        List<Category> categories = categoryService.getCategoryAll();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        if (categoryNames.contains(categoryRequest.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Message("Thể loại này đã tồn tại trong hệ thống"));
        } else {
            categoryService.addCategory(categoryRequest);
            return ResponseEntity.ok(new Message("Đã thêm thể loại sách thành công!"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestParam("idCategory") int idCategory) {
        categoryService.deleteCategory(idCategory);
        return ResponseEntity.ok(new Message("Đã xóa thành công"));
    }

}

