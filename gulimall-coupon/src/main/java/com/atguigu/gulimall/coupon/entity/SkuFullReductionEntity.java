package com.atguigu.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品滿減信息
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 22:37:49
 */
@Data
@TableName("sms_sku_full_reduction")
public class SkuFullReductionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * spu_id
	 */
	private Long skuId;
	/**
	 * 滿多少
	 */
	private BigDecimal fullPrice;
	/**
	 * 減多少
	 */
	private BigDecimal reducePrice;
	/**
	 * 是否參與其他優惠
	 */
	private Integer addOther;

}
