package com.example.bookshop.dto.response.book;

import com.example.bookshop.dto.objectdto.bookdto.BookHotNewDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class BookInCategoryResponse {
    private int count;
    private List<BookHotNewDto> rows;
}
