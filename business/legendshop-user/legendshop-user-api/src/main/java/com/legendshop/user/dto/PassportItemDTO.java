/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import com.legendshop.common.core.dto.BaseDTO;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class PassportItemDTO extends BaseDTO {

	private static final long serialVersionUID = -3888840859032202373L;
	private Long id;

	private Long passPortId;

	private String thirdPartyIdentifier;

	private String source;

}
