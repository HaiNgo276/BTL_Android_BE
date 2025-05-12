package com.example.bookshop.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorRequest {
    private int id;
    private String name;
    private String description;
    private String avatar;
    private String fileName;
}
