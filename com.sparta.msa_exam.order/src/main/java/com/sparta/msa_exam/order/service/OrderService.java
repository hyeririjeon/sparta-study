package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.client.GetProductResponseDto;
import com.sparta.msa_exam.order.client.ProductClient;
import com.sparta.msa_exam.order.dto.AddProductRequestDto;
import com.sparta.msa_exam.order.dto.OrderCreateRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import com.sparta.msa_exam.order.repository.OrderProductRepository;
import com.sparta.msa_exam.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @PostConstruct
    public void registerEventListener() {
        circuitBreakerRegistry.circuitBreaker("orderService").getEventPublisher()
                .onStateTransition(event -> log.info("#######CircuitBreaker State Transition: {}", event)) // 상태 전환 이벤트 리스너
                .onFailureRateExceeded(event -> log.info("#######CircuitBreaker Failure Rate Exceeded: {}", event)) // 실패율 초과 이벤트 리스너
                .onCallNotPermitted(event -> log.info("#######CircuitBreaker Call Not Permitted: {}", event)) // 호출 차단 이벤트 리스너
                .onError(event -> log.info("#######CircuitBreaker Error: {}", event)); // 오류 발생 이벤트 리스너
    }


    @Transactional
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackCreateOrder")
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto) {

        Order order = new Order();

        Order savedOrder = orderRepository.save(order);

        List<GetProductResponseDto> allProducts = Optional.ofNullable(productClient.getAllProducts().getBody())
                .orElseThrow(() -> new RuntimeException("Product list is null"));


        if (allProducts.isEmpty()) {
            throw new RuntimeException("Product list is empty");
        }

        for(Long productId : requestDto.getProductIds()) {

            if (allProducts.stream().anyMatch(product -> product.getProductId().equals(productId))) {

                OrderProduct savedOrderProduct = orderProductRepository.save(OrderProduct.createOrderProduct(productId, order));

                order.addOrderProduct(savedOrderProduct);

            }
        }


        return OrderResponseDto.builder()
                .orderId(savedOrder.getOrderId())
                .productIds(
                        savedOrder.getProductIds().stream()
                                .map(OrderProduct::getProductId)
                                .toList()
                )
                .message("주문이 추가 되었습니다.")
                .build();
    }

    public OrderResponseDto fallbackCreateOrder(OrderCreateRequestDto requestDto , Throwable t) {
        log.error("Fallback triggered for createOrder due to: {}", t.getMessage());

        return OrderResponseDto.builder()
                .message("잠시 후에 주문 추가를 요청 해주세요.")
                .build();
    }

    @Transactional
    public void addProduct(Long orderId, AddProductRequestDto requestDto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("order not found"));

        OrderProduct savedOrderProduct = orderProductRepository.save(OrderProduct.createOrderProduct(requestDto.getProductId(), order));
        order.addOrderProduct(savedOrderProduct);

    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "orderCache", key = "#orderId")
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("order not found"));

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .productIds(
                        order.getProductIds().stream()
                                .map(OrderProduct::getProductId)
                                .toList()
                )
                .build();
    }
}
