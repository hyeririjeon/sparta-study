package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.GetProductResponseDto;
import com.sparta.msa_exam.product.dto.ProductCreateRequestDto;
import com.sparta.msa_exam.product.dto.ProductCreateResponseDto;
import com.sparta.msa_exam.product.dto.SearchProductResponseDto;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductCreateResponseDto createProduct(ProductCreateRequestDto requestDto) {

        Product product = Product.createProduct(requestDto.getName(), requestDto.getSupplyPrice());

        Product savedProduct = productRepository.save(product);

        return ProductCreateResponseDto.builder()
                .name(savedProduct.getName())
                .supplyPrice(savedProduct.getSupplyPrice())
                .build();
    }

    @Transactional(readOnly = true)
    public GetProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));

        return GetProductResponseDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .supplyPrice(product.getSupplyPrice())
                .build();
    }

    @Transactional(readOnly = true)
    public List<GetProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> GetProductResponseDto.builder()
                        .productId(product.getProductId())
                        .supplyPrice(product.getSupplyPrice())
                        .name(product.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public Page<SearchProductResponseDto> getProducts(int page, int size, String sortBy, boolean isAsc) {

        Sort sort = isAsc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> findProducts = productRepository.findAll(pageable);

        return findProducts.map(product -> SearchProductResponseDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .supplyPrice(product.getSupplyPrice())
                .build());

    }

}
