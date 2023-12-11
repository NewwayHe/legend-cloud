/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 限购对象
 *
 * @author legendshop
 */
@Data
public class ProductQuotaDTO implements Serializable {
	private static final long serialVersionUID = 7413827539355113325L;
	/**
	 * 是否限购(N:无限购，O:每单限购，D:每日限购，W:每周限购，M:每月限购，Y:每年限购，A:终身限购)
	 */
	@Schema(description = "是否限购(null:无限购，O:每单限购，D:每日限购，W:每周限购，M:每月限购，Y:每年限购，A:终身限购)")
	private String quotaType;

	/**
	 * 限购数量
	 */
	@Schema(description = "限购数量")
	@Max(value = 999999, message = "限购件数不能大于999999")
	private Integer quotaCount;

	/**
	 * 限购时间
	 */
	@Schema(description = "限购时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date quotaTime;
}
