package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private AttrAttrgroupRelationDao attrgroupRelationDao;
    @Autowired
    private AttrDao attrDao;
    @Override
    public List<AttrEntity> getAttrRelationList(Long attrGroupId) {
        //根據attrGroupId查出關聯表中的attr_id
        //SELECT attr_id FROM pms_attr_attrgroup_relation WHERE attrGroupId=attrGroupId
        List<Object> objs = attrgroupRelationDao.selectObjs(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .select(AttrAttrgroupRelationEntity::getAttrId)
                .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId));
        log.info("objs:{}",objs);
        //將List<Object>轉成List<Long>存放attr_id數據,以確保類型安全
        List<Long> attrIds = objs.stream()
        .map(obj -> (Long) obj).collect(Collectors.toList());
        //根據關聯表中的attr_id查出attr數據,SELECT * FROM pms_attr WHERE attr_id IN (?,?,?...);
        List<AttrEntity> attrEntities = attrDao.selectList(new LambdaQueryWrapper<AttrEntity>().in(AttrEntity::getAttrId,attrIds));
        log.info("attrEntities:{}",attrEntities);

        return attrEntities;
    }

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