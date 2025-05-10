package com.example.bookshop.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Error {
    private int status;
    private String code;
    private String message;
    private String field;
}
