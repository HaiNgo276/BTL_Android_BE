package com.example.bookshop.util;

import com.example.bookshop.dto.objectdto.ratingdto.RatingDto;
import com.example.bookshop.entity.Rating;

import java.util.ArrayList;
import java.util.List;

public class RatingUtil {
    public List<RatingDto> addToRatingDto(List<Rating> ratings) {
        List<RatingDto> ratingDtos = new ArrayList<>();
        for (Rating rating : ratings) {
            RatingDto ratingDto = new RatingDto();
            ratingDto.setId(rating.getId());
            ratingDto.setComment(rating.getComment());
            ratingDto.setRatingLevel(rating.getRatingLevel());
            ratingDto.setAvatarUser(rating.getCustomer().getAvatar());
            ratingDto.setNameUser(rating.getCustomer().getName());
            ratingDto.setCreateTime(new ConvetDateTimeUTC().convertDateTimeUTC(rating.getCreateTime()));
            ratingDtos.add(ratingDto);
        }
        return ratingDtos;
    }
}
