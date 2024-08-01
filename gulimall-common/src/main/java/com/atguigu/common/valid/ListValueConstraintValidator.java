package com.atguigu.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassName: ListValueConstraintValidator
 * Description:自訂義註解的校驗器
 *
 * @Create 2024/7/31 上午3:44
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {


    private Set<Integer> set = new HashSet<>();

    //初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for(int val:vals){
            set.add(val);
        }
    }


    /**
     * 判斷是否校驗成功
     * @param value object to validate ,在ListValue形參中提交的值
     * @param context context in which the constraint is evaluated
     *
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        //set中若包含value的值則返回true否則false
        return set.contains(value);
    }


}
