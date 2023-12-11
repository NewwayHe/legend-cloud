/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * 根据Sku找到对应的属性和属性值ID
 *
 * @author legendshop
 */
@Data
public class PropertiesAndValueIdListDTO implements Serializable {

	private static final long serialVersionUID = 2726826075479931756L;
	/**
	 * skuId: prop id set
	 */
	private Map<Long, List<Long>> propIdMap = new HashMap<Long, List<Long>>();

	/**
	 * skuId: prop value id set
	 */
	private Map<Long, List<Long>> propValueIdMap = new HashMap<Long, List<Long>>();

	/**
	 * 属性ID列表
	 */
	private Set<Long> propIdList = new HashSet<Long>();

	/**
	 * 属性值ID列表
	 */
	private Set<Long> propValueIdList = new HashSet<Long>();
	;


	public void addPropIdList(Long skuId, List<Long> propId) {
		propIdMap.put(skuId, propId);
		this.propIdList.addAll(propId);
	}

	public void addPropValueIdList(Long skuId, List<Long> propValueId) {
		propValueIdMap.put(skuId, propValueId);
		this.propValueIdList.addAll(propValueId);
	}

}
