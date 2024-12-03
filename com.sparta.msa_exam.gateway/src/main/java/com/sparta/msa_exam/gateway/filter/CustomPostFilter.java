package com.sparta.msa_exam.gateway.filter;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class CustomPostFilter implements GlobalFilter, Ordered{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Object loadBalancerResponse = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayLoadBalancerResponse");
            if (loadBalancerResponse instanceof DefaultResponse) {
                ServiceInstance serviceInstance = ((DefaultResponse) loadBalancerResponse).getServer();
                if (serviceInstance != null) {
                    String instancePort = String.valueOf(serviceInstance.getPort());
                    exchange.getResponse().getHeaders().add("Server-Port", instancePort);
                }
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
