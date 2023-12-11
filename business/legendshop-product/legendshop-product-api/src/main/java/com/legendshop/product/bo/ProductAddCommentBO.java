/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品二次评论表BO
 *
 * @author legendshop
 */
@Data
public class ProductAddCommentBO implements Serializable {


	/**
	 * 主键ID
	 */
	private Long id;


	/**
	 * 商品评论ID
	 */
	private Long productCommentId;


	/**
	 * 评论内容
	 */
	private String content;


	/**
	 * 来源IP
	 */
	private String postIp;


	/**
	 * 评论图片, 多个图片以逗号隔开
	 */
	private String photos;


	/**
	 * 是否显示，1:为显示，0:待审核， -1：不通过审核，不显示。 如果需要审核评论，则是0,，否则1
	 */
	private Integer status;


	/**
	 * 创建时间
	 */
	private Date createTime;


	/**
	 * 商家是否已回复
	 */
	private Boolean replyFlag;


	/**
	 * 商家回复
	 */
	private String shopReplyContent;


	/**
	 * 商家回复时间
	 */
	private Date shopReplyTime;

}
