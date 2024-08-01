package com.atguigu.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.atguigu.common.valid.AddGroup;
import com.atguigu.common.valid.UpdateGroup;
import com.atguigu.common.valid.UpdateStatusGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;


/**
 * 品牌
 *
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-25 23:26:16
 */
@Slf4j
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        System.out.println("接收到的參數：" + params); // 添加日誌
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);
        log.info("LogoURL地址:{}",brand.getLogo());
        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     * 提示:
     * 1、JSR303校驗數據
     * 包:javax.validation,聽說高版本需求依賴:spring-boot-starter-validation
     * ,@Valid為開啟校對數據註解,需要在entity中使用某種校對方式,此處為@NotBlank不可為null或者空格,需有一個文字
     * ,@Validated是開啟分組校對數據註解,需要使用接口來充當媒介,但是在此處僅限於Entity中使用含有Groups={AddGroup.class}的對象才進行校對
     * 在需要校驗數據的形參後聲明BindingResult,可以得到校驗結果
     * 可在entity中的註解實參中自訂訊息名稱(message="品牌名不可為空")
     */
    @RequestMapping("/save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand){
        //使用異常處理器取代
        //BindingResult result
//        //若校驗數據有誤的話
//        if(result.hasErrors()){
//            Map<String,String> map = new HashMap<>();
//            result.getFieldErrors().forEach((item)->{
//                String defaultMessage = item.getDefaultMessage();//獲得錯誤Message提示
//                String field = item.getField();//獲得某個錯誤對象名稱,例如:Name
//                map.put(field,defaultMessage);
//            });
//            return R.error(400,"提交的數據不合法").put("data",map);
//        }
		brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改品牌及修改品牌關聯分類關係
     */
    @RequestMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand){
		brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 修改顯示狀態
     */
    @RequestMapping("/update/status")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand){
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
