package com.sparta.msa_exam.order.controller;


import com.sparta.msa_exam.order.dto.AddProductRequestDto;
import com.sparta.msa_exam.order.dto.OrderCreateRequestDto;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequestDto requestDto) {
        return ResponseEntity.ok(orderService.createOrder(requestDto));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> addProduct(@PathVariable Long orderId, @RequestBody AddProductRequestDto requestDto) {
        orderService.addProduct(orderId, requestDto);
        return ResponseEntity.ok("상품이 추가되었습니다.");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
}
