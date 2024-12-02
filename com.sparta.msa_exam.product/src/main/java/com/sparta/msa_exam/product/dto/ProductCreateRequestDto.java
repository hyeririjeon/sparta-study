package com.sparta.msa_exam.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCreateRequestDto {

    @NotNull
    private String name;
    @NotNull
    private Integer supplyPrice;

}
