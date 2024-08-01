package com.atguigu.gulimall.product.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ClassName: MyBatisPlusConfig
 * Description:MyBatis Plus使用分頁插件
 *
 * @Create 2024/8/1 下午2:48
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.atguigu.gulimall.product.dao")
public class MyBatisConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //設置請求的葉面大於最大頁後操作,true調回到首頁,false繼續請求 默認false
        paginationInterceptor.setOverflow(true);
        //設置最大單業限制數量,默認500條,-1不受限制
        paginationInterceptor.setLimit(1000);
        System.out.println("PaginationInterceptor 加載成功"); // 添加日誌
        return paginationInterceptor;
    }
}
