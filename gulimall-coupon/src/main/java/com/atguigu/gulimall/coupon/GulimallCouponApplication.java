package com.atguigu.gulimall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 一、想要遠程調用別的服務
 * 1、引入open-feign依賴(spring-cloud-starter-openfeign)
 * 2、編寫一個接口,告訴SpringCloud這個接口需要調用遠程服務
 *      1、聲明接口的每個方法都是調用哪個遠程服務的請求
 * 3、開啟遠程調用功能@EnableFeignClients(basePackages = "com.atguigu.gulimall.coupon.feign")//開啟Open-Feign,若在feign包中的接口上加入@Service的話,basePackages可省
 */

/**
 * 二、如何使用Nacos作為配置中心統一管理配置
 * 1、引入依賴
 *         <dependency>
 *             <groupId>com.alibaba.cloud</groupId>
 *             <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
 *         </dependency>
 *2、創建bootstrap.properties配置文件,並配置以下內容
 *          spring.application.name=gulimall-coupon
 *          spring.cloud.nacos.config.server-addr=127.0.0.1:8848
 *3、在Nacos控制台->配置管理->配置列表,在配置列表中新增配置,
 * Data ID為當前應用名加properties,例如:gulimall-coupon.properties
 * 配置格式選擇properties,配置內容填入配置內容,並且保存。
 *4、在需要動態配置的類中使用
 *          @RefreshScope
 *          @Value(${})
 */
//@EnableFeignClients(basePackages = "com.atguigu.gulimall.coupon.feign")//開啟Open-Feign,若在feign包中的接口上加入@Service的話,basePackages可省
@EnableDiscoveryClient//nacos註冊
@SpringBootApplication
public class GulimallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallCouponApplication.class, args);
    }

}
