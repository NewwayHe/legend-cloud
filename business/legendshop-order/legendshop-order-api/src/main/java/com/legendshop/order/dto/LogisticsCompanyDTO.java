/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 物流公司DTO
 *
 * @author legendshop
 */
@Data
public class LogisticsCompanyDTO implements Serializable {

	private static final long serialVersionUID = -59471836297256286L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "所属店铺", hidden = true)
	private Long shopId;

	@NotBlank(message = "物流公司名称不能为空")
	@Schema(description = "物流公司名称")
	private String name;

	@NotBlank(message = "物流公司官网URL不能为空")
	@Schema(description = "物流公司官网URL")
	private String companyHomeUrl;

	@NotBlank(message = "物流公司编号不能为空")
	@Schema(description = "物流公司编号根据快递100查询")
	private String companyCode;

	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "修改时间")
	private Date modifyTime;

	@Schema(description = "平台模板id")
	private Long parentId;

	@Schema(description = "true允许选择,false不允许")
	private Boolean allowFlag;

	@Schema(description = "引用次数")
	private Integer useCount;
}
