package com.sparta.msa_exam.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private List<Long> productIds;
    private String message;
}
