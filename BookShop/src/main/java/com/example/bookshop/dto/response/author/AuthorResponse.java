package com.example.bookshop.dto.response.author;

import com.example.bookshop.entity.Author;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorResponse {
    private List<Author> authors;
}
