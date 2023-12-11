/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 营销活动sku校验器DTO
 *
 * @author legendshop
 */
@Data
public class ActivitySkuValidatorDTO {

	@Schema(description = "skuId集合")
	private List<Long> skuIds;

	@Schema(description = "开始时间")
	private Date begTime;

	@Schema(description = "结束时间")
	private Date endTime;

	@Schema(description = "活动原有的skuId")
	List<Long> originSkuIds;

	public ActivitySkuValidatorDTO() {
	}

	public ActivitySkuValidatorDTO(List<Long> skuIds, Date begTime, Date endTime, List<Long> originSkuIds) {
		this.skuIds = skuIds;
		this.begTime = begTime;
		this.endTime = endTime;
		this.originSkuIds = originSkuIds;
	}
}
