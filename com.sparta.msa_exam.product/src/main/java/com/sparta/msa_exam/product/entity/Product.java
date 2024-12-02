package com.sparta.msa_exam.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    private Integer supplyPrice;

    public static Product createProduct(String name, Integer supply_price) {
        return Product.builder()
                .name(name)
                .supplyPrice(supply_price)
                .build();
    }


}
