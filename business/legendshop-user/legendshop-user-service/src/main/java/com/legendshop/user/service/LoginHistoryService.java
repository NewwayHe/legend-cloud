/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.user.dto.LoginHistoryDTO;
import com.legendshop.user.query.LoginHistoryQuery;

/**
 * 登录历史服务.
 *
 * @author legendshop
 */
public interface LoginHistoryService {

	/**
	 * 保存用户登录历史记录
	 *
	 * @param systemLogDTO 包含登录信息的DTO对象
	 * @param status       登录状态（成功/失败等）
	 */
	void saveLoginHistory(SystemLogDTO systemLogDTO, Integer status);

	/**
	 * 根据用户名获取用户的上一次登录时间信息
	 *
	 * @param userName 用户名
	 * @return 上一次登录时间的DTO对象
	 */
	LoginHistoryDTO getLastLoginTime(String userName);

	/**
	 * 获取登录历史记录的分页信息
	 *
	 * @param loginHistoryQuery 查询登录历史记录的查询条件
	 * @return 分页支持的登录历史记录DTO对象
	 */
	PageSupport<LoginHistoryDTO> getLoginHistoryPage(LoginHistoryQuery loginHistoryQuery);


}
