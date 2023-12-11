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
 * 商品点评(ProdComm)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_comment")
public class ProductComment implements GenericEntity<Long> {

	private static final long serialVersionUID = -14914294462506312L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROD_COMM_SEQ")
	private Long id;


	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * 所属商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 点评人ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 评论内容
	 */
	@Column(name = "content")
	private String content;


	/**
	 * IP来源
	 */
	@Column(name = "post_ip")
	private String postIp;


	/**
	 * 宝贝得分，1-5分
	 */
	@Column(name = "score")
	private Integer score;


	/**
	 * 店铺评分, 1-5分
	 */
	@Column(name = "shop_score")
	private Integer shopScore;


	/**
	 * 物流评分，1-5分
	 */
	@Column(name = "logistics_score")
	private Integer logisticsScore;


	/**
	 * 订单项id
	 */
	@Column(name = "order_item_id")
	private Long orderItemId;


	/**
	 * 评论图片, 多个图片以逗号隔开
	 */
	@Column(name = "photos")
	private String photos;


	/**
	 * 是否匿名(1:是  0:否)
	 */
	@Column(name = "anonymous_flag")
	private Boolean anonymousFlag;


	/**
	 * 是否已追加评论
	 */
	@Column(name = "add_comm_flag")
	private Boolean addCommFlag;


	/**
	 * 是否显示，1:为显示，0:待审核， -1：不通过审核，不显示。 如果需要审核评论，则是0,，否则1
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 评论时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 商家是否已回复
	 */
	@Column(name = "reply_flag")
	private Boolean replyFlag;


	/**
	 * 商家回复内容
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
	 * 运费模板ID
	 */
	@Column(name = "logistics_company_id")
	private Long logisticsCompanyId;


	/**
	 * 删除类型  默认o 删除1
	 */
	@Column(name = "delete_type")
	private Integer deleteType;


	/**
	 * 评论图片, 多个图片以逗号隔开
	 */
	@Column(name = "video")
	private String video;
}
