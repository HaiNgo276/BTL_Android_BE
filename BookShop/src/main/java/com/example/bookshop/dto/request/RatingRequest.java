package com.example.bookshop.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RatingRequest {
    private int id;
    private String comment;
    private int ratingLevel;
    private int bookId;
    private int userId;
    private int orderId;
}
