/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import cn.hutool.core.collection.CollUtil;
import com.legendshop.product.enums.TransFreeTypeEnum;
import com.legendshop.product.enums.TransTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TransportDTO implements Serializable {

	private static final long serialVersionUID = 3974610791069432944L;

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
	 * 运费名称
	 */
	@Schema(description = "模板名称")
	@NotBlank(message = "模板名称不能为空")
	private String transName;

	/**
	 * 计价方式 {@link TransTypeEnum}
	 */
	@NotBlank(message = "计价方式不能为空")
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
	@NotNull(message = "是否包邮不能为空")
	@Schema(description = "是否包邮")
	private Boolean freeFlag;


	/**
	 * 条件包邮 false：关闭 true：开启
	 */
	@Schema(description = "条件包邮 false：关闭 true：开启")
	private Boolean conditionFreeFlag;


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
	 * 记录时间
	 */
	@Schema(description = "记录时间")
	private Date recDate;

	/**
	 * 包邮情况下的区域限售
	 */
	@Schema(description = "包邮情况下的区域限售")
	private List<TransCityDTO> transCityDTOList;

	/**
	 * 包邮情况下的区域限售
	 */
	@Valid
	@Schema(description = "包邮情况下的区域限售")
	private List<TransProvinceDTO> locationRestrictedSalesList;

	/**
	 * 条件包邮
	 */
	@Valid
	@Schema(description = "条件包邮")
	private List<TransFreeDTO> transFreeDTOList;

	/**
	 * 运费计算方式
	 */
	@Schema(description = "运费计算方式")
	private List<TransFeeDTO> transFeeDTOList;

	/**
	 * 固定运费
	 */
	@Valid
	@Schema(description = "固定运费")
	private List<TransConstFeeDTO> transConstFeeDTOList;

	public void setTransFreeDTOList(List<TransFreeDTO> transFreeDTOList) {
		this.transFreeDTOList = transFreeDTOList;
	}

	public void setTransFeeDTOList(List<TransFeeDTO> transFeeDTOList) {
		this.transFeeDTOList = transFeeDTOList;
	}

	public void setTransConstFeeDTOList(List<TransConstFeeDTO> transConstFeeDTOList) {
		this.transConstFeeDTOList = transConstFeeDTOList;
	}

	@AssertTrue(message = "支持销售地区列表不能为空")
	public Boolean getCheckLocationRestrictedSales() {
		if (null != freeFlag && freeFlag) {
			if (CollUtil.isEmpty(locationRestrictedSalesList)) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	@AssertTrue(message = "计价方式不正确")
	public Boolean getCheckTransType() {
		if (null != freeFlag && !freeFlag) {
			if (null == TransTypeEnum.fromCode(transType)) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	@AssertTrue(message = "条件包邮城市列表不能为空")
	public Boolean getCheckTransFreeList() {
		if (null == freeFlag || !freeFlag) {
			if (null != conditionFreeFlag && conditionFreeFlag) {
				if (CollUtil.isEmpty(transFreeDTOList)) {
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}

	@AssertTrue(message = "条件包邮类型不正确")
	public Boolean getCheckTransFreeType() {
		if (null == freeFlag || !freeFlag) {
			if (null != conditionFreeFlag && conditionFreeFlag) {
				if (null == TransFreeTypeEnum.fromCode(transFreeType)) {
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}

	@AssertTrue(message = "运费计算列表不能为空")
	public Boolean getCheckTransFeeList() {
		if (null == freeFlag || !freeFlag) {
			TransTypeEnum typeEnum = TransTypeEnum.fromCode(transType);
			if (null != typeEnum && !typeEnum.value().equals(TransTypeEnum.CONSTANT.value())) {
				if (CollUtil.isEmpty(transFeeDTOList)) {
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}

	@AssertTrue(message = "固定运费列表不能为空")
	public Boolean getCheckTransConstFeeList() {
		if (null == freeFlag || !freeFlag) {
			TransTypeEnum typeEnum = TransTypeEnum.fromCode(transType);
			if (null != typeEnum && typeEnum.value().equals(TransTypeEnum.CONSTANT.value())) {
				if (CollUtil.isEmpty(transConstFeeDTOList)) {
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}
}
