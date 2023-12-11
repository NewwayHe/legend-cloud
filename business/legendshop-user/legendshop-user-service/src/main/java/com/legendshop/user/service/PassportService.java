/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.PassportBO;
import com.legendshop.user.dto.PassportDTO;
import com.legendshop.user.dto.PassportItemDTO;
import com.legendshop.user.dto.WxUserInfo;

import java.util.List;

/**
 * @author legendshop
 */
public interface PassportService {

	/**
	 * 根据用户Id和请求端，获取微信openId
	 */
	R<String> getOpenIdByUserId(Long userId, String source);

	R<PassportDTO> getByUserIdAndType(Long userId, String typeEnum);

	R<PassportBO> updateAuthInfo(PassportBO bo);

	R<PassportDTO> saveToWxUserInfo(WxUserInfo wxUserInfo);

	R<Long> savePassportItem(PassportItemDTO itemDTO);

	/**
	 * 通过第三方用户标识获取用户信息
	 */
	PassportDTO getPassByIdentifier(String identifier);

	int update(PassportDTO passportDTO);


	R<Void> updateItemBindParent(Long passportId, String thirdPartyIdentifier);


	R<Void> clearPassportItem(Long userId, String source);

	/**
	 * 根据用户id集合和来源获取用户openId
	 *
	 * @param userIds
	 * @param source
	 * @return
	 */
	R<List<String>> getOpenIdByUserIds(List<Long> userIds, String source);

	/**
	 * 解绑定用户第三方登录通行证信息
	 *
	 * @param userId
	 * @param source
	 * @return
	 */
	R<Boolean> unboundUserPassport(Long userId, String type, String source);

	/**
	 * 根据用户ID获取第三方登录通行证进行
	 *
	 * @param userId
	 * @return
	 */
	PassportDTO getByUserId(Long userId);
}
