/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class IndexCountDTO implements Serializable {

	@Schema(description = "es索引信息")
	private EsIndexDocumentDTO esIndexDocumentDTO;

	@Schema(description = "数据库索引信息")
	private IndexDocumentDTO indexDocumentDTO;

}
