/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.Date;

/**
 * 商品二次评论表(ProdAddComm)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_add_comment")
public class ProductAddComment implements GenericEntity<Long> {

	private static final long serialVersionUID = -37753905842051072L;

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROD_ADD_COMM_SEQ")
	private Long id;


	/**
	 * 商品评论ID
	 */
	@Column(name = "product_comment_id")
	private Long productCommentId;


	/**
	 * 评论内容
	 */
	@Column(name = "content")
	private String content;


	/**
	 * 来源IP
	 */
	@Column(name = "post_ip")
	private String postIp;


	/**
	 * 评论图片, 多个图片以逗号隔开
	 */
	@Column(name = "photos")
	private String photos;


	/**
	 * 0待审核 1审核通过 -1 未通过
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 商家是否已回复
	 */
	@Column(name = "reply_flag")
	private Boolean replyFlag;


	/**
	 * 商家回复
	 */
	@Column(name = "shop_reply_content")
	private String shopReplyContent;


	/**
	 * 商家回复时间
	 */
	@Column(name = "shop_reply_time")
	private Date shopReplyTime;

	/**
	 * 评论审核时间
	 */
	@Column(name = "audit_time")
	private Date auditTime;

	/**
	 * 删除类型  默认o 删除1
	 */
	@Column(name = "delete_type")
	private Integer deleteType;

	/**
	 * 评论视频, 多个图片以逗号隔开
	 */
	@Column(name = "video")
	private String video;
}
