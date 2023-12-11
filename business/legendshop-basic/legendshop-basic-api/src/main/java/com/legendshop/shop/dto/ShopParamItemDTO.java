/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家配置项(ShopParamItem)DTO
 *
 * @author legendshop
 * @since 2020-11-03 11:03:29
 */
@Data
@Schema(description = "商家配置项DTO")
public class ShopParamItemDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -50206559392015648L;

	private Long id;

	private Long parentId;

	/**
	 * 键
	 */
	@Schema(description = "键")
	private String keyWord;

	/**
	 * 值
	 */
	@Schema(description = "值")
	private String value;

	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String des;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer sort;

	private Date updateTime;

	/**
	 * 对于java类型
	 */
	@Schema(description = "对于java类型")
	private String dataType;

}
