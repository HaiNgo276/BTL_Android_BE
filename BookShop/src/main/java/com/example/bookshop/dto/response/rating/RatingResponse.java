package com.example.bookshop.dto.response.rating;

import com.example.bookshop.dto.objectdto.ratingdto.RatingDto;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RatingResponse {
    private int totalRating;
    private List<RatingDto> ratingDtos;
}
