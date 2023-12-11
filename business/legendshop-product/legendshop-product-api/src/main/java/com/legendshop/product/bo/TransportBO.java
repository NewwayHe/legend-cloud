/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import com.legendshop.product.dto.*;
import com.legendshop.product.enums.TransTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 运费模板DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "运费模板DTO")
public class TransportBO implements Serializable {

	private static final long serialVersionUID = -4800563724301883438L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;


	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;


	/**
	 * 类型
	 */
	@Schema(description = "类型")
	private String type;


	/**
	 * 运费名称
	 */
	@Schema(description = "运费名称")
	private String transName;


	/**
	 * 状态
	 */
	@Schema(description = "状态")
	private Integer status;


	/**
	 * 配送时间
	 */
	@Schema(description = "配送时间")
	private Integer transTime;


	/**
	 * 计价方式 {@link TransTypeEnum}
	 */
	@Schema(description = "计价方式 1、按件数 2、按重量 3、按体积 4、固定运费")
	private String transType;

	/**
	 * {@link com.legendshop.product.enums.TransFreeTypeEnum}
	 */
	@Schema(description = "条件包邮类型 1、满件包邮 2、满金额包邮")
	private Integer transFreeType;


	/**
	 * 是否包邮
	 */
	@Schema(description = "是否包邮")
	private Boolean freeFlag;


	/**
	 * 指定地区设置运费，0：关闭 1：开启
	 */
	@Schema(description = "指定地区设置运费，0：关闭 1：开启")
	private Boolean chooseAreaFlag;

	/**
	 * 条件包邮 0：关闭 1：开启
	 */
	@Schema(description = "条件包邮 0：关闭 1：开启")
	private Boolean conditionFreeFlag;


	/**
	 * 是否区域限售0，不支持，1，支持
	 */
	@Schema(description = "是否区域限售0，不支持，1，支持")
	private Boolean regionalSalesFlag;


	/**
	 * 记录时间
	 */
	@Schema(description = "记录时间")
	private Date recDate;

	/**
	 * 是否为邮政
	 */
	@Schema(description = "是否为邮政")
	private Boolean transMail;

	/**
	 * 是否为快递
	 */
	@Schema(description = "是否为快递")
	private Boolean transExpress;

	/**
	 * 是否为ems
	 */
	@Schema(description = "是否为ems")
	private Boolean transEms;

	/**
	 * 包邮情况下的区域限售
	 */
	@Schema(description = "包邮情况下的区域限售")
	private List<TransCityDTO> transCityDTOList;

	/**
	 * 包邮情况下的区域限售
	 */
	@NotEmpty(message = "省份不能为空")
	@Schema(description = "包邮情况下的区域限售")
	private List<TransProvinceDTO> locationRestrictedSalesList;

	/**
	 * 条件包邮
	 */
	@Schema(description = "条件包邮")
	private List<TransFreeDTO> transFreeDTOList;

	/**
	 * 运费计算
	 */
	@Schema(description = "运费计算")
	private List<TransFeeDTO> transFeeDTOList;


	/**
	 * 固定运费
	 */
	@Schema(description = "固定运费")
	private List<TransConstFeeDTO> transConstFeeDTOList;


	/**
	 * 配送至
	 */
	@Schema(description = "配送至")
	private String area;
}
