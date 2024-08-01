package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 訂單項信息
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 22:39:51
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
