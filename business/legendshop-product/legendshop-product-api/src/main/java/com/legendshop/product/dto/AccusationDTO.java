/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.product.enums.AccusationIllegalOffEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 举报表(Accusation)实体类
 *
 * @author legendshop
 */
@Schema(description = "商品举报DTO")
@Data
public class AccusationDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -499998527925300918L;
	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 产品ID
	 */
	@Schema(description = "产品ID")
	private Long productId;

	/**
	 * 举报类型ID
	 */
	@Schema(description = "举报类型ID")
	@NotNull(message = "举报类型不能为空")
	private Long typeId;

	/**
	 * 举报类型名称
	 */
	@Schema(description = "举报类型名称")
	private String typeName;

	/**
	 * 举报内容
	 */
	@Schema(description = "举报内容")
	@NotBlank(message = "举报内容不能为空")
	private String content;


	/**
	 * 处理结果  -1:无效举报 1：有效举报  -2：恶意举报
	 * {@link com.legendshop.product.enums.AccusationResultEnum}
	 */
	@Schema(description = "处理结果  -1:无效举报 1：有效举报  -2：恶意举报")
	private Integer result;


	/**
	 * 处理时间
	 */
	@Schema(description = "处理时间")
	private Date handleTime;


	/**
	 * 处理意见
	 */
	@Schema(description = "处理意见")
	private String handleInfo;


	/**
	 * 状态,0:未处理， 1:已经处理
	 * {@link com.legendshop.product.enums.AccusationEnum}
	 */
	@Schema(description = "状态,0:未处理， 1:已经处理")
	private Integer status;


	/**
	 * 用户是否删除,0未删除,1已经删除
	 * {@link com.legendshop.product.enums.AccusationEnum}
	 */
	@Schema(description = "用户是否删除,0未删除,1已经删除")
	private Integer userDelStatus;


	/**
	 * 商品处理 0：不处理 1：违规下架
	 */
	@Schema(description = "商品处理 0：不处理 1：违规下架 2: 锁定")
	@EnumValid(target = AccusationIllegalOffEnum.class, message = "处理状态不匹配")
	private Integer illegalOff;


	/**
	 *	id集合
	 */
	@Schema(description = "id集合")
	private List<Long> ids;

	private String pic;

	@Schema(description = "举报图片")
	private List<String> picList;
}
