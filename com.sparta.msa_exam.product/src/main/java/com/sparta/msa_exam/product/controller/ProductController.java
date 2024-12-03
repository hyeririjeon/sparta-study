package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.GetProductResponseDto;
import com.sparta.msa_exam.product.dto.ProductCreateRequestDto;
import com.sparta.msa_exam.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody  ProductCreateRequestDto requestDto) {
        return ResponseEntity.ok(productService.createProduct(requestDto));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetProductResponseDto> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping
    public ResponseEntity<?> getPageProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "supplyPrice") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc
    ) {
        return ResponseEntity.ok(productService.getProducts(page, size, sortBy ,isAsc));
    }
}
