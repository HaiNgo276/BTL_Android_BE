package com.example.bookshop.dto.response.book;

import com.example.bookshop.dto.objectdto.bookdto.BookBannerDto;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookBannerResponse {
    private int count;
    private List<BookBannerDto> products;
}
