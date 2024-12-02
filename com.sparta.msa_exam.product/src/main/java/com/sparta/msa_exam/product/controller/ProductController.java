package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.ProductCreateRequestDto;
import com.sparta.msa_exam.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody  ProductCreateRequestDto requestDto) {
        System.out.println("컨트롤러 Name: " + requestDto.getName());
        System.out.println("컨트롤러임돠Supply Price: " + requestDto.getSupplyPrice());
        return ResponseEntity.ok(productService.createProduct(requestDto));
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "supplyPrice") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc
    ) {
        return ResponseEntity.ok(productService.getProducts(page, size, sortBy ,isAsc));
    }
}
