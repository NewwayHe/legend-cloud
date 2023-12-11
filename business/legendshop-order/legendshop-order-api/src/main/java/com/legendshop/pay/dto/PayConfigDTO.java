/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.legendshop.basic.dto.SysParamItemDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Data
public class PayConfigDTO implements Serializable {

	private static final long serialVersionUID = 6937329084720309159L;
	private String name;

	private List<SysParamItemDTO> itemList;

	private Map<String, String> map;

	public String getValue(String key) {
		return getMap().get(key);
	}

	public Map<String, String> getMap() {
		if (map == null && itemList != null) {
			this.map = this.itemList.stream().collect(Collectors.toMap(SysParamItemDTO::getKeyWord, SysParamItemDTO::getValue));
		}
		return map;
	}
}
