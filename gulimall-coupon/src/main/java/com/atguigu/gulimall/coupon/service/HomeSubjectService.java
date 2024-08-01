package com.atguigu.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.coupon.entity.HomeSubjectEntity;

import java.util.Map;

/**
 * 首頁專題表【jd首頁下面很多專題，每個專題鏈接新的頁面，展示專題商品信息】
 *
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 22:37:49
 */
public interface HomeSubjectService extends IService<HomeSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

