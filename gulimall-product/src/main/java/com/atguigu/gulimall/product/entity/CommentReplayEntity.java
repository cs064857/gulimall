package com.atguigu.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品評價回覆關係
 * 
 * @author shijiawei
 * @email passerby064857@gmail.com
 * @date 2024-07-23 20:15:56
 */
@Data
@TableName("pms_comment_replay")
public class CommentReplayEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 評論id
	 */
	private Long commentId;
	/**
	 * 回覆id
	 */
	private Long replyId;

}
