/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 协议DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "协议信息DTO")
public class ProtocolDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 4180855545282325067L;

	@Schema(description = "主键ID")
	private Long id;


	@Schema(description = "协议代号")
	private String code;


	@Schema(description = "协议名称")
	private String name;

	@Schema(description = "协议类型  0 链接 1 富文本")
	private Integer type;


	@Schema(description = "协议链接")
	private String url;

	@Schema(description = "协议内容")
	private String text;


}
