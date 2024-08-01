package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 訂單
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 22:39:51
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
