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
 * 举报表(Accusation)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_accusation")
public class Accusation implements GenericEntity<Long> {

	private static final long serialVersionUID = -93335361790244455L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ACCUSATION_SEQ")
	private Long id;


	/**
	 * 举报人ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 举报人名称
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 举报人类型（1、用户 2、平台）
	 */
	@Column(name = "user_type")
	private Integer userType;


	/**
	 * 产品ID
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 举报类型名称
	 */
	@Column(name = "type_name")
	private String typeName;

	/**
	 * 举报类型ID
	 */
	@Column(name = "type_id")
	private Long typeId;


	/**
	 * 举报内容
	 */
	@Column(name = "content")
	private String content;


	/**
	 * 处理结果  -1:无效举报 1：有效举报  -2：恶意举报
	 * {@link com.legendshop.product.enums.AccusationResultEnum}
	 */
	@Column(name = "result")
	private Integer result;


	/**
	 * 处理时间
	 */
	@Column(name = "handle_time")
	private Date handleTime;


	/**
	 * 处理意见
	 */
	@Column(name = "handle_info")
	private String handleInfo;


	/**
	 * 状态,0:未处理， 1:已经处理
	 * {@link com.legendshop.product.enums.AccusationEnum}
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 用户是否删除,0未删除,1已经删除
	 * {@link com.legendshop.product.enums.AccusationEnum}
	 */
	@Column(name = "user_del_status")
	private Integer userDelStatus;


	/**
	 * 商品处理 0：不处理 1：违规下架 2：锁定
	 */
	@Column(name = "illegal_off")
	private Integer illegalOff;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 举报图片
	 */
	@Column(name = "pic")
	private String pic;

}
