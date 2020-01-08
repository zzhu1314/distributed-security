package com.cigit.distributed.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author:zhuzhou
 * @Date: 2020/1/7  15:44
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class OrderServer {
    public static void main(String[] args) {
        SpringApplication.run(OrderServer.class,args);
    }
}
