/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 店铺的索引实体
 *
 * @author legendshop
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "#{@couponIndexName}")
public class CouponDocument implements Serializable {

	private static final long serialVersionUID = -2149560282353061238L;

	@Id
	@Field(type = FieldType.Long)
	private Long id;

	/**
	 * 优惠券id
	 */
	@Field(type = FieldType.Long)
	private Long couponId;


	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 选中的skuId
	 */
	@Field(type = FieldType.Nested)
	private List<Long> skuIds;

	/**
	 * 选中的店铺ID
	 */
	@Field(type = FieldType.Nested)
	private List<Long> shopIds;


	/**
	 * 指定用户{@link com.legendshop.activity.enums.CouponDesignateEnum}
	 */
	private Integer designatedUser;

	/**
	 * 优惠券提供方是否为店铺
	 */
	private Boolean shopProviderFlag;

	/**
	 * 优惠券使用类型
	 * {@link com.legendshop.activity.enums.CouponUseTypeEnum}
	 */
	private Integer useType;


	/**
	 * 状态：{@link com.legendshop.activity.enums.CouponStatusEnum}
	 */
	private Integer status;

	/**
	 * 优惠券标题
	 */
	private String title;

	/**
	 * 优惠券备注
	 */
	private String remark;


	/**
	 * 面额
	 */
	private BigDecimal amount;

	/**
	 * 使用门槛，0.00为无门槛
	 */
	private BigDecimal minPoint;


	/**
	 * 领取开始时间
	 */
	private Date receiveStartTime;

	/**
	 * 领取结束时间
	 */
	private Date receiveEndTime;

	/**
	 * 使用开始时间
	 */
	private Date useStartTime;

	/**
	 * 使用结束时间
	 */
	private Date useEndTime;


}




