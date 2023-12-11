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
import com.legendshop.common.core.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌表(Brand)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_brand")
public class Brand extends BaseDTO implements Serializable, GenericEntity<Long> {

	private static final long serialVersionUID = -99603709737533102L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "BRAND_SEQ")
	private Long id;


	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 品牌名称
	 */
	@Column(name = "brand_name")
	private String brandName;


	/**
	 * PC品牌logo
	 */
	@Column(name = "brand_pic")
	private String brandPic;

	/**
	 * 顺序
	 */
	@Column(name = "seq")
	private Integer seq;

	/**
	 * 品牌状态：{@link com.legendshop.product.enums.BrandStatusEnum}
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 审核操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Column(name = "op_status")
	private Integer opStatus;

	/**
	 * 是否推荐 1:是 0：否
	 */
	@Column(name = "commend_flag")
	private Boolean commendFlag;

	/**
	 * 是否删除 1:是 0：否
	 */
	@Column(name = "delete_flag")
	private Boolean deleteFlag;

	/**
	 * 简要描述
	 */
	@Column(name = "brief")
	private String brief;


	/**
	 * 品牌大图（这张图片的尺寸是:770*350）
	 */
	@Column(name = "big_image")
	private String bigImage;

	@Column(name = "end_time")
	private Date endTime;


	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "registration_people")
	private String registrationPeople;

	@Column(name = "registration_pic")
	private String registrationPic;

	@Column(name = "trademarking_number")
	private String trademarkingNumber;

	@Column(name = "registration_addess")
	private String registrationAddess;
}
