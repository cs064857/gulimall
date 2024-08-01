package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params, "catelog_id", true),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {


        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        //模糊查詢
        String key = (String) params.get("key");//前端傳的搜尋參數
        if (!StringUtils.isEmpty(key)) {//若key不為null,查詢其他欄位中帶有key的數據
            wrapper.and((obj) -> {
                obj.eq(AttrGroupEntity::getAttrGroupId, key).or().like(AttrGroupEntity::getAttrGroupName, key);
            });
        }
        //根據catelogId查詢數據列表,若為0則查詢全部
        if (catelogId >= 1) {//若catelogId不為0 根據catelog_id查詢,
            //SELECT * FROM pms_attr_group WHERE catelog_id=? and(attr_group_id=key or attr_group_name LIKE key)
            wrapper.eq(AttrGroupEntity::getCatelogId, catelogId);
        }//else若catelogId為0的話,查出所有SELECT * FROM pms_attr_group WHERE (attr_group_id=key or attr_group_name LIKE key)

        //數據列表按照catelog_id升序
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params, "catelog_id", true), wrapper);
        return new PageUtils(page);
    }


}