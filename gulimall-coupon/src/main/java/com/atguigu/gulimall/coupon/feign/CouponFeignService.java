package com.atguigu.gulimall.coupon.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * ClassName: CouponFeignService
 * Description:
 *
 * @Create 2024/7/24 上午3:35
 */
//使用OpenFeign
@Service
@FeignClient("gulimall-coupon")//在yml中配置的application.name,並且需設置nacos配置中心
public interface CouponFeignService {
    //完整的要調用的方法名與Mapping完整路徑
    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupons();
}
