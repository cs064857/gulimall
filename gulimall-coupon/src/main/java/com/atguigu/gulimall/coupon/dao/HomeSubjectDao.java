package com.atguigu.gulimall.coupon.dao;

import com.atguigu.gulimall.coupon.entity.HomeSubjectEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 首頁專題表【jd首頁下面很多專題，每個專題鏈接新的頁面，展示專題商品信息】
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 22:37:49
 */
@Mapper
public interface HomeSubjectDao extends BaseMapper<HomeSubjectEntity> {
	
}
