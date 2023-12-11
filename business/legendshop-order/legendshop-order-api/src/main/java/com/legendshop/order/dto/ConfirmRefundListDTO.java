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
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 平台批量确认退款DTO
 *
 * @author legendshop
 */
@Data
public class ConfirmRefundListDTO implements Serializable {


	@Schema(description = "平台备注")
	private String adminMessage;

	private List<ConfirmRefundDTO> confirmRefundDTO;
}
