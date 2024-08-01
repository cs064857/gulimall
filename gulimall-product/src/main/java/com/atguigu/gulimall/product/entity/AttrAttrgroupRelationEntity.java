package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 屬性&屬性分組關聯
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 21:10:02
 */
@Data
@TableName("pms_attr_attrgroup_relation")
public class AttrAttrgroupRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 屬性id
	 */
	private Long attrId;
	/**
	 * 屬性分組id
	 */
	private Long attrGroupId;
	/**
	 * 屬性組內排序
	 */
	private Integer attrSort;

}
