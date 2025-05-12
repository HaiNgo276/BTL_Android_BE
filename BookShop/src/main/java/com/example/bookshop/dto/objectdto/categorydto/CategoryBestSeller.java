package com.example.bookshop.dto.objectdto.categorydto;

import com.example.bookshop.entity.Category;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryBestSeller {
    private Category category;
    private Long totalSold;
}
