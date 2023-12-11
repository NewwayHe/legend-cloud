/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.common.core.validator.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户配送地址(UserAddr)实体类
 *
 * @author legendshop
 */
@Schema(description = "用户配送地址DTO")
@Data
public class UserAddressDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -28716747785460097L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	@NotNull(message = "id不能为空", groups = Update.class)
	private Long id;


	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;


	/**
	 * 接受人名称
	 */
	@Schema(description = "收货人名称")
	@NotBlank(message = "收货人名称不能为空")
	private String receiver;


	/**
	 * 详情地址
	 */
	@Schema(description = "详情地址")
	@NotBlank(message = "详情地址不能为空")
	private String detailAddress;


	/**
	 * 省份编码
	 */
	@NotNull(message = "省份id不能为空")
	@Schema(description = "省份id")
	private Long provinceId;

	/**
	 * 省份名称
	 */
	@Schema(description = "省份名称")
	private String provinceName;


	/**
	 * 城市id
	 */
	@NotNull(message = "城市id不能为空")
	@Schema(description = "城市id")
	private Long cityId;

	/**
	 * 城市名称
	 */
	@Schema(description = "城市名称")
	private String cityName;


	/**
	 * 区域id
	 */
	@Schema(description = "区域id")
	private String areaId;

	/**
	 * 区域名称
	 */
	@Schema(description = "区域名称")
	private String areaName;

	/**
	 * 街道id
	 */
	@Schema(description = "街道id")
	private String streetId;

	/**
	 * 街道名称
	 */
	@Schema(description = "街道名称")
	private String streetName;

	/**
	 * 手机
	 */
	@Schema(description = "手机")
	@NotBlank(message = "手机不能为空")
	private String mobile;


	/**
	 * 是否常用地址
	 */
	@NotNull(message = "是否常用地址不能为空")
	@Schema(description = "是否常用地址")
	private Boolean commonFlag;


	/**
	 * 建立时间
	 */
	@Schema(description = "建立时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;


	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;


}
