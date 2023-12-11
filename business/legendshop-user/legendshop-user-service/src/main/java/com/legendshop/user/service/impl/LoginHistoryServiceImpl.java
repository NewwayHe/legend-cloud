/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.common.region.core.IpInfoDTO;
import com.legendshop.common.region.service.RegionSearcherService;
import com.legendshop.user.dao.LoginHistoryDao;
import com.legendshop.user.dao.UserDetailDao;
import com.legendshop.user.dto.LoginHistoryDTO;
import com.legendshop.user.entity.LoginHistory;
import com.legendshop.user.entity.UserDetail;
import com.legendshop.user.query.LoginHistoryQuery;
import com.legendshop.user.service.LoginHistoryService;
import com.legendshop.user.service.convert.LoginHistoryConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 登录历史服务.
 *
 * @author legendshop
 */
@Slf4j
@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {

	@Autowired
	private UserDetailDao userDetailDao;

	@Autowired
	private LoginHistoryDao loginHistoryDao;

	@Autowired
	private LoginHistoryConverter loginHistoryConverter;

	@Autowired
	private RegionSearcherService regionSearcherService;


	/**
	 * 记录登录历史
	 */
	@Override
	public void saveLoginHistory(SystemLogDTO systemLogDTO, Integer status) {

		String ip = systemLogDTO.getRemoteIp();
		if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
			String[] ipArr = ip.split(",");
			log.info("存在多个IP地址, 默认获取第一个IP地址, {}", JSONUtil.toJsonStr(ipArr));
			ip = ipArr[0];
		}
		Long userId = systemLogDTO.getUserId();
		String nickName = null;
		UserDetail userDetailById = userDetailDao.getUserDetailById(userId);
		if (userDetailById != null) {
			nickName = userDetailById.getNickName();
		} else {
			nickName = systemLogDTO.getRequestUser();
		}

		LoginHistory loginHistory = new LoginHistory();
		//本地测试
		if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
			loginHistory.setArea("CZ88.NET");
			loginHistory.setCountry("本机地址");
			loginHistory.setIp("127.0.0.1");
		} else {
			IpInfoDTO result = regionSearcherService.binarySearch(ip);
			if (null != result) {
				loginHistory.setCountry(result.getCountry());
				loginHistory.setArea(result.getCity());
			}
			loginHistory.setIp(ip);
		}
		loginHistory.setUserId(userId);
		loginHistory.setNickName(nickName);
		loginHistory.setTime(systemLogDTO.getCreateTime());
		loginHistory.setStatus(status);
		loginHistory.setLoginType(systemLogDTO.getUserType());
		loginHistory.setLoginSource(systemLogDTO.getSource());
		if (StrUtil.isBlank(loginHistory.getCountry())) {
			loginHistory.setCountry("IANA");
			loginHistory.setArea("未知地区");
		}
		//保存登录历史
		loginHistoryDao.saveLoginHistory(loginHistory);
	}

	@Override
	@Cacheable(value = "LoginHistory")
	public LoginHistoryDTO getLastLoginTime(String userName) {
		return loginHistoryConverter.to(loginHistoryDao.getLastLoginTime(userName));
	}

	@Override
	public PageSupport<LoginHistoryDTO> getLoginHistoryPage(LoginHistoryQuery loginHistoryQuery) {
		return loginHistoryConverter.page(loginHistoryDao.getLoginHistoryPage(loginHistoryQuery));
	}


}
