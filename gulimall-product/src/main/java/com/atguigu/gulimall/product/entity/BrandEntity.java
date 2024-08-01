package com.atguigu.gulimall.product.entity;

import com.atguigu.common.valid.AddGroup;
import com.atguigu.common.valid.ListValue;
import com.atguigu.common.valid.UpdateStatusGroup;
import com.atguigu.common.valid.UpdateGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 20:15:56
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "修改必須指定品牌ID",groups = {UpdateGroup.class, UpdateStatusGroup.class})
	@Null(message = "新增時不能指定品牌ID",groups = {AddGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能為空",groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "品牌Logo地址新增時不能為空",groups = {AddGroup.class})
	@URL(message = "品牌Logo地址必須是個合法的URL地址",groups = {AddGroup.class, UpdateGroup.class})
	private String logo;
	/**
	 * 介紹
	 */
	private String descript;
	/**
	 * 顯示狀態[0-不顯示；1-顯示]
	 */

	//只能是0或者1
//	@NotNull
//	@Min(0)
//	@Max(1)
	@ListValue(vals = {0,1},groups = {AddGroup.class, UpdateStatusGroup.class})//使用自訂義的校驗註解
	private Integer showStatus;
	/**
	 * 檢索首字母
	 */
	//只能是一個英文字母
	@NotBlank
	@Pattern(regexp = "^[a-zA-z]${1,1}",message = "檢索首字母必須是一個英文字母且不能為空")
	private String firstLetter;
	/**
	 * 排序
	 */
	//只能是一個或多個大於等於0的數字
	@NotNull
	@Min(value = 0,message = "排序必須大於等於0且不能為空")
	private Integer sort;

}
