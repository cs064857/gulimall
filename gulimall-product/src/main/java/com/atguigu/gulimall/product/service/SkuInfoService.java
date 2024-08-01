package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;

import java.util.Map;

/**
 * sku信息
 *
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 21:10:02
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

