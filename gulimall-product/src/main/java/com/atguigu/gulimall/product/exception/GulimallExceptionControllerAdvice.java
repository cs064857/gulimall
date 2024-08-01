package com.atguigu.gulimall.product.exception;

import com.atguigu.common.utils.R;
import com.atguigu.common.exception.BizCodeEnume;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: GulimallExceptionControllerAdvice
 * Description:集中處理product中controller的所有異常
 *
 * @Create 2024/7/31 上午1:15
 */
//宣告異常處理器
//@ControllerAdvice(basePackages = "com.atguigu.gulimall.product.controller")

//宣告異常處理器(使用JSON)
@Slf4j
@RestControllerAdvice(basePackages = "com.atguigu.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {
    /**
     * 處理數據校驗異常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("數據校驗出現問題:{},異常類型:{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach(item -> {
            String defaultMessage = item.getDefaultMessage();
            String field = item.getField();
            map.put(field, defaultMessage);
        });
        return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(), BizCodeEnume.VALID_EXCEPTION.getMsg()).put("data", map);
    }
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        log.info("未知異常:{}",throwable.getMessage());
        return R.error(BizCodeEnume.UNKNOWN_EXCEPTION.getCode(),BizCodeEnume.UNKNOWN_EXCEPTION.getMsg());
    }
}
