package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三級分類
 *
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 20:15:56
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> list);

    /**
     * 找到cateLogId的完整路徑;
     * [父/子/孫]
     * @param attrGroupId
     * @return
     */
    Long[] findCatelogPath(Long attrGroupId);
}

