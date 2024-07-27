package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.dao.BrandDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@MapperScan("com.atguigu.gulimall.product.dao")
@SpringBootTest(classes = GulimallProductApplication.class)
class GulimallProductApplicationTests {
    @Autowired
    private BrandService brandService;
    @Test
    public void test(){
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("華為");
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("華為");
////        brandService.save(brandEntity);
////        System.out.println("保存成功");
//        brandService.updateById(brandEntity);

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(System.out::println);


    }

}
