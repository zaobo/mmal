package com.zab.mmal.providerdb;

import com.zab.mmal.common.config.FeignHystrixConcurrencyStrategy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@MapperScan("com.zab.mmal.core.mapper")
@ComponentScan("com.zab.mmal")
@EnableDiscoveryClient
@EnableCircuitBreaker //开启断路器
@EnableHystrix // 开启hystrix 断路器
@SpringBootApplication
@EnableAutoConfiguration
public class ProviderDBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderDBApplication.class, args);
    }

    @Bean
    public FeignHystrixConcurrencyStrategy PfeignHystrixConcurrencyStrategy() {
        return new FeignHystrixConcurrencyStrategy();
    }

}
