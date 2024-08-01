package com.atguigu.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 22:49:16
 */
@Data
@TableName("wms_purchase_detail")
public class PurchaseDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 採購單id
	 */
	private Long purchaseId;
	/**
	 * 採購商品id
	 */
	private Long skuId;
	/**
	 * 採購數量
	 */
	private Integer skuNum;
	/**
	 * 採購金額
	 */
	private BigDecimal skuPrice;
	/**
	 * 倉庫id
	 */
	private Long wareId;
	/**
	 * 狀態[0新建，1已分配，2正在採購，3已完成，4採購失敗]
	 */
	private Integer status;

}
