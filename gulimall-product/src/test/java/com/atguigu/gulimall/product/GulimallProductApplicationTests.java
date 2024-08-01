package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.dao.BrandDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@MapperScan("com.atguigu.gulimall.product.dao")
@SpringBootTest(classes = GulimallProductApplication.class)
class GulimallProductApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(GulimallProductApplicationTests.class);
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @Test
    public void testGetCategoryPaths(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路徑:{}", Arrays.asList(catelogPath));
    }
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
