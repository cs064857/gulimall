package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.constants.ProductConstant;
import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jdk.nashorn.internal.runtime.options.Options;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private AttrGroupDao attrGroupDao;

    @Override
    public void updateAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.updateById(attrEntity);
        //修改分組關聯表
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            //根據attr_id查詢 pms_attr_attrgroup_relation表中是否有紀錄,若有紀錄則修改;無紀錄則插入
            Integer count = attrAttrgroupRelationDao.selectCount(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
            if (count > 0) {//修改
                attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity, new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
            } else {//插入
                attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
            }
        }


    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        //設定修改分類時回顯的值
        //1、設置所屬分組信息 AttrGroupName
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectById(attrId);

            if (attrAttrgroupRelationEntity != null) {
                attrRespVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                if (attrGroupEntity != null) {
                    attrRespVo.setAttrGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }


        //2、設置所屬分類信息 完整路徑 CatelogPath
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrRespVo.setCatelogPath(catelogPath);

        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null) {
            String categoryName = categoryEntity.getName();
            attrRespVo.setCatelogName(categoryName);
        }

        return attrRespVo;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId, String attrType) {
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<AttrEntity>()
                //若attrType是base的話查詢attr_type=1(規格參數),否則查0(銷售屬性)
                .eq(AttrEntity::getAttrType, attrType.equalsIgnoreCase("base") ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if (catelogId >= 1) {//若catelogId大於等於1的話,使用catelogId查詢
            wrapper.eq(AttrEntity::getCatelogId, catelogId);
        }//else的話查詢全部數據

        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {//按照Name或者Attr_id模糊查詢
            wrapper.and((item) -> {
                item.like(AttrEntity::getAttrName, key).or().like(AttrEntity::getAttrId, key);
            });
        }
        //查出數據
        IPage<AttrEntity> iPage = this.page(new Query<AttrEntity>().getPage(params, "attr_id", true), wrapper);
        PageUtils pageUtils = new PageUtils(iPage);
        log.info("iPage:{}", pageUtils);
        //查出並設置冗於字段attr_group_name與catelog_name
        List<AttrEntity> records = iPage.getRecords();
        List<AttrRespVo> respVoList = records.stream().map((attrEntity -> {
            //透過pms_attr表中的catelogId字段查詢,表pms_attr_group:attr_group_name字段與表pms_category:catelog_name,
            //並利用Vo封裝傳遞給前端
            AttrRespVo attrRespVo = new AttrRespVo();
            //將AttrEntity中的值全部複製給Attr2Vo
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //1、根據catelogId獲取pms_category:catelog_name,並賦值給vo中的catelogName屬性
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            //2.1、根據attrId查詢attr_group_id
            //若是base(規則參數的話需要設定所屬分組),若是sale的話沒有所屬分組因此不需要設置
            if (attrType.equalsIgnoreCase("base")) {
                LambdaQueryWrapper<AttrAttrgroupRelationEntity> relationWrapper = new LambdaQueryWrapper<>();
                relationWrapper.eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId());
                AttrAttrgroupRelationEntity attrGroupId = attrAttrgroupRelationDao.selectOne(relationWrapper);
                if (attrGroupId != null) {
                    //2.2再根據attr_group_id查詢attr_group_name
                    LambdaQueryWrapper<AttrGroupEntity> attrGroupWrapper = new LambdaQueryWrapper<>();
                    attrGroupWrapper.eq(AttrGroupEntity::getAttrGroupId, attrGroupId.getAttrGroupId());
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectOne(attrGroupWrapper);
                    attrRespVo.setAttrGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            return attrRespVo;
        })).collect(Collectors.toList());
        //重新設置PageUtils的List
        pageUtils.setList(respVoList);
        log.info("PageUtils:{}", pageUtils);
        return pageUtils;
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attrVo) {
        //將AttrVo中的屬性值賦予Attr
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        //保存Attr表中的數據
        this.save(attrEntity);

        //保存Attr與AttrGroup關聯表的數據
        //若是base(規則參數的話需要設定所屬分組),若是sale的話沒有所屬分組因此不需要設置
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

}