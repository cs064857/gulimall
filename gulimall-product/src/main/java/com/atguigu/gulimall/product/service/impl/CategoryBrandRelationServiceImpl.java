package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.BrandDao;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CategoryDao categoryDao;
    @Resource
    private BrandService brandService;
    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public List<BrandEntity> getBrandsBycatId(Long catId) {
        log.info("catId:{}",catId);
        //使用catelogId從pms_category_brand_relation表中獲取brand_id品牌ID
        List<Object> brandIds = categoryBrandRelationDao.selectObjs(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                        .select(CategoryBrandRelationEntity::getBrandId)
                .eq(CategoryBrandRelationEntity::getCatelogId, catId));
        log.info("brandIds:{}",brandIds);
        //將品牌的IDS從List<Object>轉成List<Long>
        List<Long> brandIdsL = brandIds.stream().map(brandId -> (Long) brandId).collect(Collectors.toList());
        //透過brand_id獲取pms_brand表中的品牌數據
        List<BrandEntity> brandEntities = brandDao.selectBatchIds(brandIdsL);
        return brandEntities;
    }

    @Override
    public List<CategoryBrandRelationEntity> getCatelogList(Long brandId) {
        LambdaQueryChainWrapper<CategoryBrandRelationEntity> lambdaQuery = this.lambdaQuery();

        lambdaQuery.eq(CategoryBrandRelationEntity::getBrandId,brandId);
        List<CategoryBrandRelationEntity> list = lambdaQuery.list();
        return list;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDeatils(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }
    @Transactional
    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
        categoryBrandRelation.setBrandId(brandId);
        categoryBrandRelation.setBrandName(name);

        LambdaQueryWrapper<CategoryBrandRelationEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        this.update(categoryBrandRelation,lambdaQueryWrapper.eq(CategoryBrandRelationEntity::getBrandId,brandId));
    }
    @Transactional
    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity categoryBrandRelation = new CategoryBrandRelationEntity();
        categoryBrandRelation.setCatelogName(name);
        categoryBrandRelation.setCatelogId(catId);

        this.update(categoryBrandRelation,
                new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getCatelogId,catId));
    }
}