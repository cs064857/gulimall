package com.atguigu.gulimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//繼承common但不使用mybatis因此使用此方式排除,或者直接在pom.xml中排除mybatis
public class GulimallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallGatewayApplication.class, args);
    }

}
