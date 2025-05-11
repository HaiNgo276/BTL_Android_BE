package com.example.bookshop.dto.response.category;

import com.example.bookshop.entity.Category;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryResponse {
    private int count;
    private List<Category> rows;
}
