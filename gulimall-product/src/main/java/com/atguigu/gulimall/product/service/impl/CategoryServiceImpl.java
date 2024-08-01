package com.atguigu.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Override
    public void removeMenuByIds(List<Long> list) {
        //TODO 1、檢查當前刪除的菜單,是否被別的地方引用

        //邏輯刪除(使用show_status作為判斷依據)
        baseMapper.deleteBatchIds(list);

    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、列出所有分類數據
        List<CategoryEntity> categoryEntityList = baseMapper.selectList(null);
        //2.1、將數據組裝成父子樹類結構

        //2.2、找出所有一級分類
//        List<CategoryEntity> level1Menus = categoryEntityList.stream().filter(categoryEntity -> {
//            return categoryEntity.getParentCid() == 0;
//        }).collect(Collectors.toList());

        List<CategoryEntity> level1Menus = categoryEntityList.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() .equals(0L)
        ).map((menu)->{
            menu.setChildren(getChildrens(menu,categoryEntityList));//將所有菜單的子菜單找出並設置,若有多個子菜單則用遞歸方式
            return menu;
        }).sorted((menu1,menu2) ->{
            //先判斷若為空則設置為0進行比較並排序
            return (menu1.getSort()==null?0: menu1.getSort())-(menu2.getSort()==null?0: menu2.getSort());
        }).collect(Collectors.toList());


        return level1Menus;
    }
    //遞歸查找所有菜單的子菜單
    public List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> categoryEntityList){
        List<CategoryEntity> children = categoryEntityList.stream().filter((categoryEntity) -> {
            //若List<CategoryEntity>中categoryEntity的父類id等於上一級菜單的id時
            //(第一次執行相當於是找出為二級菜單的數據,若有第3.4.5...級菜單則使用遞歸往下找)
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(categoryEntity -> {//把找到的下一級菜單數據的子類設定為下下一級菜單(子菜單)
            categoryEntity.setChildren(getChildrens(categoryEntity, categoryEntityList));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //先判斷若為空則設置為0進行比較並排序
            return (menu1.getSort()==null?0: menu1.getSort())-(menu2.getSort()==null?0: menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);

        return parentPath.toArray(new Long[parentPath.size()]);
    }
    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        paths.add(catelogId);
        CategoryEntity categoryEntity = this.getById(catelogId);
        if(categoryEntity.getParentCid()!=0){
            findParentPath(categoryEntity.getParentCid(),paths);
        }
        return paths;
    }
}