package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * ClassName: Attr2Vo
 * Description:
 *
 * @Create 2024/8/2 下午4:51
 */
@Data
public class AttrRespVo extends AttrVo {
    private String catelogName;
    private String attrGroupName;
    private Long[] catelogPath;
}
