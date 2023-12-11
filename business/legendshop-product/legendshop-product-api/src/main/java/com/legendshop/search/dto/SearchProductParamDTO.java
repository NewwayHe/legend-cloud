/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@AllArgsConstructor
public class SearchProductParamDTO implements Serializable {

	private static final long serialVersionUID = -6198240020502788930L;
	private Long paramId;

	private String paramName;

	public SearchProductParamDTO() {

	}

//	private List<SearchProductParamValueDTO> searchProductParamValueDTOList;
}
