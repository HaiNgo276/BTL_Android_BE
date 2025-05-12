package com.example.bookshop.dto.response.book;

import com.example.bookshop.dto.objectdto.authordto.AuthorDto;
import com.example.bookshop.dto.objectdto.bookdto.BookDetailDto;
import com.example.bookshop.dto.objectdto.supplierdto.SupplierDto;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDetailResponse {
    private BookDetailDto product;
    private SupplierDto supplier;
    private AuthorDto author;
}
