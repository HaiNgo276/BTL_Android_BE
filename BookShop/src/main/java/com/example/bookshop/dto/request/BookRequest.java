package com.example.bookshop.dto.request;

import com.example.bookshop.entity.Author;
import com.example.bookshop.entity.Category;
import com.example.bookshop.entity.Supplier;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discounted_price;
    private int quantity;
    private String image;
    private Author author;
    private Supplier supplier;
    private Category category;
    private Boolean isBannerSelected;
    private String fileName;
}
