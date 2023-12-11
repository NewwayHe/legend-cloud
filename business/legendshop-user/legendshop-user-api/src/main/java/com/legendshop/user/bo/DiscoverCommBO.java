/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 发现文章评论
 *
 * @author legendshop
 */
@Data
public class DiscoverCommBO implements Serializable {

	/**
	 * id
	 */
	private Long id;


	/**
	 * 发现文章id
	 */
	private Long disId;


	/**
	 * 用户id
	 */
	private Long userId;


	/**
	 * 评论内容
	 */
	private String content;


	/**
	 * 创建时间
	 */
	private Date createTime;


	/**
	 * 评论状态（0）未审核（1）审核
	 */
	private Integer status;


	/**
	 * 用户头像
	 */
	private String userImage;


	/**
	 * 昵称
	 */
	private String nickName;
}
