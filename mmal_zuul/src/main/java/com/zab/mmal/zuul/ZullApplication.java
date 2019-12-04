package com.zab.mmal.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableZuulProxy //启用网关
@EnableDiscoveryClient
@EnableRedisHttpSession
public class ZullApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZullApplication.class, args);
    }

}
