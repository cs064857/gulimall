package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * ClassName: AttrGroupWithAttrsVo
 * Description:
 *
 * @Create 2024/8/5 上午5:03
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分組id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 組名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 組圖標
     */
    private String icon;
    /**
     * 所屬分類id
     */
    private Long catelogId;
    /**
     * 分類下所有屬性
     */
    private List<AttrEntity> attrs;
}
