package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.dto.AttrGroupRelationDto;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 屬性分組
 *
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 20:15:56
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);


    List<AttrEntity> getAttrRelationList(Long attrGroupId);

    void removeRelation(List<AttrGroupRelationDto> attrGroupRelationDto);

    PageUtils queryPage(Long attrGroupId, Map<String, Object> params);

    void saveReletion(List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntity);

    void removeAttrGroup(Long[] attrGroupId);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(Long catelogId);
}

