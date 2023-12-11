/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.URLUtil;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.common.core.enums.ServiceNameEnum;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.util.IPUtil;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

/**
 * 日志构建工具类
 *
 * @author legendshop
 */
@UtilityClass
public class SystemLogUtil {

	public SystemLogDTO build(HttpServletRequest request, BaseUserDetail userDetail) {
		SystemLogDTO sysLog = new SystemLogDTO();

		String serviceName = request.getHeader(RequestHeaderConstant.SERVICE_NAME_KEY);
		String source = request.getHeader(RequestHeaderConstant.SOURCE_KEY);


		sysLog.setService(ServiceNameEnum.getCnByRequestUrl(serviceName));
		sysLog.setSource(VisitSourceEnum.getDescByName(source));

		// 获取 Request
		sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
		sysLog.setMethod(request.getMethod());
		sysLog.setRemoteIp(IPUtil.getIpAddress(request));
		sysLog.setUserAgent(request.getHeader("user-agent"));

		sysLog.setCreateTime(DateUtil.date());

		// 设置请求用户信息
		if (userDetail == null) {
			sysLog.setRequestUser("anonymousUser");
			return sysLog;
		}
		// TODO 需要改造，不依赖security来获取
		sysLog.setRequestUser(userDetail.getUsername());
		sysLog.setUserId(userDetail.getUserId());
		sysLog.setUserType(userDetail.getUserType());
		return sysLog;
	}

	public SystemLogDTO build(HttpServletRequest request) {
		// TODO 需要改造，不依赖security来获取
		BaseUserDetail userDetail = SecurityUtils.getBaseUser();
		return build(request, userDetail);
	}
}
