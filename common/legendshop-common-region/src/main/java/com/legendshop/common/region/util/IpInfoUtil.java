/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.util;

import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.region.core.DataBlock;
import com.legendshop.common.region.core.IpInfoDTO;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 根据ip获取地区的工具类
 *
 * @author legendshop
 */
public class IpInfoUtil {
	private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");

	/**
	 * 将 DataBlock 转化为 IpInfo
	 *
	 * @param dataBlock DataBlock
	 * @return IpInfo
	 */
	@Nullable
	public static IpInfoDTO toIpInfo(@Nullable DataBlock dataBlock) {
		if (dataBlock == null) {
			return null;
		}
		IpInfoDTO ipInfoDTO = new IpInfoDTO();
		int cityId = dataBlock.getCityId();
		ipInfoDTO.setCityId(cityId == 0 ? null : cityId);
		ipInfoDTO.setDataPtr(dataBlock.getDataPtr());
		String region = dataBlock.getRegion();
		String[] splitInfos = SPLIT_PATTERN.split(region);
		// 补齐5位
		if (splitInfos.length < 5) {
			splitInfos = Arrays.copyOf(splitInfos, 5);
		}
		ipInfoDTO.setCountry(filterZero(splitInfos[0]));
		ipInfoDTO.setRegion(filterZero(splitInfos[1]));
		ipInfoDTO.setProvince(filterZero(splitInfos[2]));
		ipInfoDTO.setCity(filterZero(splitInfos[3]));
		ipInfoDTO.setIsp(filterZero(splitInfos[4]));
		return ipInfoDTO;
	}

	/**
	 * 数据过滤，因为 ip2Region 采用 0 填充的没有数据的字段
	 *
	 * @param info info
	 * @return info
	 */
	@Nullable
	private static String filterZero(@Nullable String info) {
		// null 或 0 返回 null
		if (info == null || StringConstant.ZERO.equals(info)) {
			return null;
		}
		return info;
	}

}
