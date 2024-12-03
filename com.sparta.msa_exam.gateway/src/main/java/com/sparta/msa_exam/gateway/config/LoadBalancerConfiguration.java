package com.sparta.msa_exam.gateway.config;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.WeightedServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadBalancerConfiguration {

    @Bean
    public ServiceInstanceListSupplier serviceInstanceListSupplier(ServiceInstanceListSupplier delegate) {
        return new WeightedServiceInstanceListSupplier(delegate);
    }
}
