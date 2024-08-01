package com.atguigu.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 倉庫信息
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 22:49:16
 */
@Data
@TableName("wms_ware_info")
public class WareInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 倉庫名
	 */
	private String name;
	/**
	 * 倉庫地址
	 */
	private String address;
	/**
	 * 區域編碼
	 */
	private String areacode;

}
