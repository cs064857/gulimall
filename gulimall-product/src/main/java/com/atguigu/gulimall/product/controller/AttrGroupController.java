package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atguigu.gulimall.product.dto.AttrGroupRelationDto;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;



/**
 * 屬性分組
 *
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 21:10:02
 */
@Slf4j
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryServiceImpl categoryService;


//    /**
//     * 新增關聯時回顯attr數據,分頁
//     */
//    @GetMapping("/{attrGroupId}/noattr/relation")
//    public R listRelationPage(@PathVariable("attrGroupId") Long attrGroupId,@RequestParam Map<String,Object> params){
//        log.info("attrGroupId:{},params:{}",attrGroupId,params);
//        PageUtils page =attrGroupService.queryPage(attrGroupId,params);
//        return R.ok().put("page",page);
//    }
    /**
     * 新增 屬性分組(attrgroup)與屬性(attr)關聯分類
     */
    @PostMapping("/attr/relation")
    public R saveReletion(@RequestBody List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntity){
        log.info("attrAttrgroupRelationEntity:{}",attrAttrgroupRelationEntity);
        attrGroupService.saveReletion(attrAttrgroupRelationEntity);
        return null;
    }

    /**
     * 列表 屬性分組(attrgroup)與屬性(attr)關聯分類
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R listReletion(@PathVariable("attrGroupId") Long attrGroupId){
        log.info("路徑/{attrGroupId}/attr/relation,參數:attrGroupId:{}",attrGroupId);
        List<AttrEntity> attrEntity =attrGroupService.getAttrRelationList(attrGroupId);
        return R.ok().put("data",attrEntity);
    }
    /**
     * 根據菜單ID查菜單列表
     */
    @RequestMapping("/list/{catelogId}")
    public R listByCategoryId(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        System.out.println("catelogId參數:"+catelogId);
        PageUtils page = attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        //菜單完整路徑(用於前端修改時回顯)
        Long catelogId = attrGroup.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(catelogPath);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除屬性分組與屬性關聯關係
     */
    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrGroupRelationDto> attrGroupRelationDto){
        log.info("attrDto:{}", attrGroupRelationDto);
		attrGroupService.removeRelation(attrGroupRelationDto);

        return R.ok();
    }
    @PostMapping("/delete")
    public R deleteAttrGroup(@RequestBody Long[] attrGroupId){
        log.info("/delete:attrGroupId:{}",attrGroupId);
        attrGroupService.removeByIds(Arrays.asList(attrGroupId));
        return R.ok();
    }

}
