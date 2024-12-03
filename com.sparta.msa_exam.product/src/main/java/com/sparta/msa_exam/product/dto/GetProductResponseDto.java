package com.sparta.msa_exam.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetProductResponseDto {
    private Long productId;
    private String name;
    private Integer supplyPrice;
}
