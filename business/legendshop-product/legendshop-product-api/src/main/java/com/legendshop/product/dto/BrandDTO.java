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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "商品品牌DTO")
public class BrandDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1848280186219964159L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 商家ID(平台创建的品牌id为0)
	 */
	@Schema(description = "商家ID(平台创建的品牌id为0)")
	private Long shopId;


	/**
	 * 品牌名称
	 */
	@Schema(description = "品牌名称")
	@NotBlank(message = "品牌名称不能为空")
	@Length(max = 50, message = "品牌名称不能超过50个字符")
	private String brandName;


	/**
	 * 品牌logo
	 */
	@Schema(description = "品牌logo")
	@NotBlank(message = "品牌logo不能为空")
	private String brandPic;

	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	@NotNull(message = "顺序不能为空")
	private Integer seq;

	/**
	 * 审核操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "审核操作状态")
	private Integer opStatus;

	/**
	 * 品牌状态：{@link com.legendshop.product.enums.BrandStatusEnum}
	 */
	@Schema(description = "品牌状态")
	private Integer status;


	/**
	 * 是否推荐 1:是 0：否
	 */
	@Schema(description = "是否推荐")
	private Boolean commendFlag;


	/**
	 * 简要描述
	 */
	@Schema(description = "简要描述")
	@NotBlank(message = "简要描述不能为空")
	@Length(max = 300, min = 1, message = "简要描述长度应在1~50之间")
	private String brief;


	/**
	 * 品牌大图（这张图片的尺寸是:770*350）
	 */
	@Schema(description = "品牌大图（这张图片的尺寸是:770*350）")
	@NotBlank(message = "品牌大图不能为空")
	private String bigImage;

	/**
	 * 是否删除
	 */
	@Schema(description = "是否删除")
	private boolean deleteFlag;

	@Schema(description = "结束时间")
	private Date endTime;


	@Schema(description = "开始时间")
	private Date startTime;

	@Schema(description = "注册地址")
	private String registrationAddess;

	@Schema(description = "注册人")
	private String registrationPeople;

	@Schema(description = "注册证图片")
	private String registrationPic;

	@Schema(description = "注册号")
	private String trademarkingNumber;
}
