/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 单品SKU表(Sku)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "单品SKU表DTO")
public class SkuDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -4250454033508327751L;
	/**
	 * 单品ID
	 */
	@Schema(description = "单品ID")
	private Long id;

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * SKU名称
	 */
	@Schema(description = "SKU名称")

	private String name;

	/**
	 * 原价
	 */
	@Schema(description = "原价")
	@DecimalMin(value = "0.00", inclusive = false, message = "原价不能小于0")
	@Digits(integer = 7, fraction = 2, message = "原价保留2位小数且不能大于7位数")
	private BigDecimal originalPrice;

	/**
	 * 销售价
	 */
	@Schema(description = "销售价")
	@DecimalMin(value = "0.00", inclusive = false, message = "销售价不能小于0")
	@Digits(integer = 7, fraction = 2, message = "销售价保留2位小数且不能大于7位数")
	@NotNull(message = "销售价不能为空")
	private BigDecimal price;

	/**
	 * 成本价
	 */
	@Schema(description = "成本价")
	@DecimalMin(value = "-1.00", inclusive = false, message = "成本价不能小于0")
	@Digits(integer = 7, fraction = 2, message = "成本价保留2位小数且不能大于7位数")
	@NotNull(message = "成本价不能为空")
	private BigDecimal costPrice;

	/**
	 * 商品规格属性组合（中文）e.g 颜色:黑色;尺码:M
	 */
	@Schema(description = "商品规格属性组合（中文）e.g 颜色:黑色;尺码:M")
	private String cnProperties;


	/**
	 * 商品规格属性id组合（颜色，大小，等等，可通过类目API获取某类目下的销售属性）,e.g 223:673;224:689
	 */
	@Schema(description = "商品规格属性id组合")
	private String properties;


	/**
	 * 用户自定义的销售属性，key:value 格式
	 */
	@Schema(description = "用户自定义的销售属性")
	private String userProperties;

	/**
	 * 销售库存（商品在付款减库存的状态下，该sku上未付款的订单数量）
	 */
	@Schema(description = "销售库存")
	@Max(value = 999999, message = "销售库存不能大于999999")
	private Integer stocks;


	/**
	 * 实际库存
	 */
	@Schema(description = "实际库存")
	@Min(value = 0, message = "实际库存不能小于0")
	@Max(value = 999999, message = "销售库存不能大于999999")
	private Integer actualStocks;

	/**
	 * 已经销售数量
	 */
	@Schema(description = "已经销售数量")
	private Integer buys;

	/**
	 * 商家编码
	 */
	@Schema(description = "商家编码")
	private String partyCode;


	/**
	 * 商品条形码
	 */
	@Schema(description = "商品条形码")
	private String modelId;


	/**
	 * sku图片
	 */
	@Schema(description = "sku图片")
	private String pic;

	/**
	 * 物流体积
	 */
	@Schema(description = "物流体积")
	@Min(value = 0, message = "物流体积不能小于0")
	private Double volume;


	/**
	 * 物流重量(千克)
	 */
	@Schema(description = "物流重量(千克)")
	@Min(value = 0, message = "物流重量不能小于0")
	private Double weight;


	/**
	 * sku营销活动类型 {@link com.legendshop.product.enums.SkuActiveTypeEnum}
	 */
	@Schema(description = "sku营销活动类型")
	private String skuType;

	/**
	 * 操作库存数
	 */
	@Schema(description = "操作库存数")
	@Min(value = 0, message = "库存数必须大于等于0")
	private Integer editStocks;

	/**
	 * 入库标识
	 */
	@Schema(description = "入库标识：true增加 false减少，默认true")
	private Boolean putStorageFlag;


	@Schema(description = "是否被选了积分商品")
	private Boolean integralFlag;

	@Schema(description = "是否被选了积分抵扣商品")
	private Boolean integralDeductionFlag;

	public SkuDTO() {
		this.putStorageFlag = true;
	}

	@Override
	public boolean equals(Object o) {
		SkuDTO skuDTO = (SkuDTO) o;
		return getId().equals(skuDTO.getId()) &&
				getProductId().equals(skuDTO.getProductId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getId(), getProductId());
	}
}
