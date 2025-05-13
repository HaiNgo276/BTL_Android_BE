package com.example.bookshop.dto.objectdto.ratingdto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RatingDto {
    private int id;
    private int ratingLevel;
    private String comment;
    private String nameUser;
    private String avatarUser;
    private String createTime;
}
