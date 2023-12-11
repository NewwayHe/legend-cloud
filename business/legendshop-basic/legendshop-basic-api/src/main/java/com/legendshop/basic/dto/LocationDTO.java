/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.basic.enums.LocationGradeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 地区DTO
 *
 * @author legendshop
 */
@Schema(description = "地区DTO")
@Data
public class LocationDTO implements Serializable {

	private static final long serialVersionUID = 1424578356384515422L;

	/**
	 * 地区的ID
	 */
	@Schema(description = "地区的ID")
	private Long id;


	/**
	 * 地区的编码
	 */
	@Schema(description = "地区编码")
	private String code;

	/**
	 * 地区的名字
	 */
	@Schema(description = "地区名")
	private String name;

	/**
	 * 父级id
	 */
	@Schema(description = "父级id")
	private Long parentId;

	/**
	 * 地区级别 1：省级 2：市级 3：区级 4：街道 {@link LocationGradeEnum}
	 */
	@Schema(description = "地区级别")
	private Integer grade;
}
