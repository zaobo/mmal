package com.zab.mmal.manager;

import com.zab.mmal.common.config.FeignHystrixConcurrencyStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.zab.mmal.manager.feign")
@ComponentScan("com.zab.mmal")
@EnableCircuitBreaker //开启断路器
@EnableHystrix // 开启hystrix 断路器
@EnableAutoConfiguration
@EnableRedisHttpSession
@EnableWebMvc
public class ManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

    @Bean
    public FeignHystrixConcurrencyStrategy PfeignHystrixConcurrencyStrategy() {
        return new FeignHystrixConcurrencyStrategy();
    }

}
