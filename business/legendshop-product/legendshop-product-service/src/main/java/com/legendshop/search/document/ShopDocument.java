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
@Document(indexName = "#{@shopIndexName}")
public class ShopDocument implements Serializable {

	private static final long serialVersionUID = -2149560282353061238L;

	@Id
	@Field(type = FieldType.Long)
	private Long shopId;

	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String shopName;

	/**
	 * 店铺类型0.专营店1.旗舰店2.自营店
	 */
	@Field(type = FieldType.Integer)
	private Integer shopType;

	/**
	 * 店铺入驻类型  1个人用户 2商家用户 3分销商   {@link com.legendshop.shop.enums.ApplyForTypeEnum}
	 */
	@Field(type = FieldType.Integer)
	private Integer applyForType;

	@Field(type = FieldType.Text)
	private String pic;

	/**
	 * 物流评分
	 */
	@Field(type = FieldType.Double)
	private BigDecimal dvyTypeAvg;

	/**
	 * 服务评分
	 */
	@Field(type = FieldType.Double)
	private BigDecimal shopCommAvg;

	/**
	 * 描述评分
	 */
	@Field(type = FieldType.Double)
	private BigDecimal productCommentAvg;

	/**
	 * 综合评分
	 */
	@Field(type = FieldType.Double)
	private BigDecimal score;
}




