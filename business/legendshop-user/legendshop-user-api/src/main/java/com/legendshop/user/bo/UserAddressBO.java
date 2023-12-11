/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;


import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户收货地址
 *
 * @author legendshop
 */
@Schema(description = "用户收货地址")
@Data
public class UserAddressBO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -28716747785460097L;


	@Schema(description = "ID")
	private Long id;


	@Schema(description = "用户ID")
	private Long userId;


	@Schema(description = "收货人名称")
	private String receiver;


	@Schema(description = "详细地址")
	private String detailAddress;


	@Schema(description = "省份")
	private String provinceName;


	@Schema(description = "城市")
	private String cityName;


	@Schema(description = "区域")
	private String areaName;


	@Schema(description = "街道")
	private String streetName;


	@Schema(description = "收货手机号码")
	private String mobile;


	@Schema(description = "是否常用地址")
	private Boolean commonFlag;


	@Schema(description = "地址全称")
	private String fullAddress;

	@Schema(description = "省份ID")
	private Long provinceId;

	@Schema(description = "城市ID")
	private Long cityId;

	@Schema(description = "区域ID")
	private Long areaId;

	@Schema(description = "街道ID")
	private Long streetId;


	public String getFullAddress() {
		return this.provinceName + this.cityName + this.areaName + this.streetName;
	}
}
