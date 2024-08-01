package com.atguigu.gulimall.gulimallcommon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class GulimallCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallCommonApplication.class, args);
    }

}
