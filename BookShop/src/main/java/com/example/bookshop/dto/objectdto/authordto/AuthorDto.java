package com.example.bookshop.dto.objectdto.authordto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorDto {
    private int author_id;
    private String author_name;
}
