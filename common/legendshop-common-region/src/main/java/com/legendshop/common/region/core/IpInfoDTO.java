/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.core;

import com.legendshop.common.core.constant.StringConstant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ip地址库DTO
 *
 * @author legendshop
 */
@Data
public class IpInfoDTO {

	/**
	 * 城市id
	 */
	private Integer cityId;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 区域
	 */
	private String region;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 运营商
	 */
	private String isp;
	/**
	 * region ptr in the db file
	 */
	private int dataPtr;

	/**
	 * 拼接完整的地址
	 *
	 * @return address
	 */
	public String getAddress() {
		Set<String> regionSet = new LinkedHashSet<>();
		regionSet.add(country);
		regionSet.add(region);
		regionSet.add(province);
		regionSet.add(city);
		regionSet.add(isp);
		regionSet.removeIf(Objects::isNull);
		return StringUtils.join(regionSet, StringConstant.SPACE);
	}
}
