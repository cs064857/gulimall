package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 屬性分組
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 20:15:56
 */
@Data
@TableName("pms_attr_group")
public class AttrGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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

	@TableField(exist = false)//在數據庫中不存在
	private Long[] catelogPath;//前端修改回顯時使用

}
