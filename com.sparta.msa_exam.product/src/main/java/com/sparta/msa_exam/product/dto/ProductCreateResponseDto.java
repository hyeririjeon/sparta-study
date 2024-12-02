package com.sparta.msa_exam.product.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCreateResponseDto {
    private String name;
    private Integer supplyPrice;
}
