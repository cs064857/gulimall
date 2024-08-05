package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.dto.AttrGroupRelationDto;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrAttrgroupRelationService;
import com.atguigu.gulimall.product.service.AttrService;
import com.atguigu.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private AttrAttrgroupRelationDao attrgroupRelationDao;
    @Autowired
    private AttrDao attrDao;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    private AttrGroupDao attrGroupDao;

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(Long catelogId) {
        //根據catelogId從pms_attr_group表中獲得attrGroupEntity數據
        List<AttrGroupEntity> attrGroupEntities = this.list(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catelogId));

        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = attrGroupEntities.stream().map(attrGroupEntity -> {
            log.info("attrGroupEntity.getAttrGroupId:{}",attrGroupEntity.getAttrGroupId());
            //封裝成AttrGroupWithAttrsVo
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupWithAttrsVo);
            //根據attrGroupEntites中的attrGroupIds查詢pms_attr_attrgroup_relation表中的attrIds數據
            List<Object> attrIds = attrAttrgroupRelationService.listObjs(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .select(AttrAttrgroupRelationEntity::getAttrId)
                    .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupEntity.getAttrGroupId()));
            log.info("attrIds:{}",attrIds);
            //根據attrIds數據查詢pms_attr中的所有attr屬性
            List<AttrEntity> attrs = attrDao.selectList(new LambdaQueryWrapper<AttrEntity>()
                    .in(AttrEntity::getAttrId, attrIds));
            log.info("attrs:{}",attrs);

            //根據catelogId從pms_attr表中獲取attrGroup的attr屬性列表
//            List<AttrEntity> attrEntities = attrDao.selectList(new LambdaQueryWrapper<AttrEntity>()
//                    .eq(AttrEntity::getCatelogId, catelogId));

//            List<Object> attrIds = attrDao.selectObjs(new LambdaQueryWrapper<AttrEntity>()
//                    .select(AttrEntity::getAttrId)
//                    .eq(AttrEntity::getCatelogId, catelogId));

//            List<Object> attr_id = attrAttrgroupRelationService.listObjs(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrGroupId,attrIds));






//            List<AttrEntity> attrEntities = attrDao.selectList(new LambdaQueryWrapper<AttrEntity>()
//                    .eq(AttrEntity::getCatelogId, catelogId));

            attrGroupWithAttrsVo.setAttrs(attrs);
            log.info("attrGroupWithAttrsVo:{}",attrGroupWithAttrsVo);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());
        return attrGroupWithAttrsVos;
    }

    @Transactional
    @Override
    public void removeAttrGroup(Long[] attrGroupId) {
        //刪除屬性分組數據
        this.removeByIds(Arrays.asList(attrGroupId));
        //關聯表數據
        attrgroupRelationDao.delete(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .in(AttrAttrgroupRelationEntity::getAttrGroupId,attrGroupId));
    }

    @Override
    public void saveReletion(List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntity) {
        attrAttrgroupRelationService.saveBatch(attrAttrgroupRelationEntity);
    }

    @Override
    public PageUtils queryPage(Long attrGroupId, Map<String, Object> params) {


//        //根據attrGroupId從pms_attr_attrgroup_relation表中獲取pms_attr表中的attr_id(一個attr_group可能有多個attr)
//        List<Object> attrIds = attrgroupRelationDao.selectObjs(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
//                .select(AttrAttrgroupRelationEntity::getAttrId)
//                .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId));
//
//        if(!CollectionUtils.isEmpty(attrIds)){//若attrIds不為空
//            //將attrIds轉成Long型,確保類型安全
//            attrIds = attrIds.stream().map(attrId -> (Long) attrId).collect(Collectors.toList());
//        }
//        log.info("attrIds:{}",attrIds);
//        //分頁查詢
//        long current = Long.parseLong(params.get("page").toString());
//        long size = Long.parseLong(params.get("limit").toString());
//        log.info("current:{},size:{}",current,size);
//        Page<AttrEntity> page = new Page<>(current,size);
//        LambdaQueryWrapper<AttrEntity> lambdaQueryWrapper = new LambdaQueryWrapper<AttrEntity>()
//                .in(AttrEntity::getAttrId, attrIds);
//        //若key不為空,添加使用模糊查詢判斷
//        String key = (String)params.get("key");
//        if(!StringUtils.isEmpty(key)){
//            lambdaQueryWrapper.and(item ->{
//               item.like(AttrEntity::getAttrName,key)
//               .or()
//               .like(AttrEntity::getValueSelect,key).or()
//               .like(AttrEntity::getAttrId,key);
//            });
//        }
//
//        IPage<AttrEntity> iPage = attrDao.selectPage(page, lambdaQueryWrapper);
//        PageUtils pageUtils = new PageUtils(iPage);
//        log.info("pageUtils:{}",pageUtils);
//        return pageUtils;
        return null;
    }

    @Override
    public void removeRelation(List<AttrGroupRelationDto> attrGroupRelationDto) {
        //根據attrGroupId與attrId刪除關聯關係表
        attrGroupRelationDto.forEach(attr -> {
            attrgroupRelationDao.delete(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId())
                    .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attr.getAttrGroupId()));
        });


    }

    @Override
    public List<AttrEntity> getAttrRelationList(Long attrGroupId) {
        //根據attrGroupId查出關聯表中的attr_id
        //SELECT attr_id FROM pms_attr_attrgroup_relation WHERE attrGroupId=attrGroupId
        List<Object> attrIds = attrgroupRelationDao.selectObjs(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .select(AttrAttrgroupRelationEntity::getAttrId)
                .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId));
        log.info("objs:{}", attrIds);
        //將List<Object>轉成List<Long>存放attr_id數據,以確保類型安全

        attrIds = attrIds.stream()
                .map(obj -> (Long) obj).collect(Collectors.toList());
        //根據關聯表中的attr_id查出attr數據,SELECT * FROM pms_attr WHERE attr_id IN (?,?,?...);


        List<AttrEntity> attrEntities = attrDao.selectList(new LambdaQueryWrapper<AttrEntity>().in(AttrEntity::getAttrId, attrIds));
        log.info("attrEntities:{}", attrEntities);

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