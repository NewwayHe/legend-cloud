/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 协议搜索DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "协议搜索参数")
public class ProtocolQuery extends PageParams implements Serializable {


	@Schema(description = "协议代号")
	private String code;


	@Schema(description = "协议名称")
	private String name;

}
