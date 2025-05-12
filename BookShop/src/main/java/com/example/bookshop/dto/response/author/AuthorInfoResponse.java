package com.example.bookshop.dto.response.author;

import com.example.bookshop.entity.Author;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorInfoResponse {
    private Author result;
}

