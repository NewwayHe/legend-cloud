/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author legendshop
 */
@Configuration
@Data
public class BaiDuPropertiesDTO {
	/**
	 * 百度账号：用户名
	 */
	@Value("${BaiDu.BD_USERNAME}")
	private String userName;

	/**
	 * 百度账号：密码
	 */
	@Value("${BaiDu.BD_PASSWORD}")
	private String password;

	/**
	 * 百度统计站点Id
	 */
	@Value("${BaiDu.SITE_ID}")
	private String siteId;

	/**
	 * 百度统计token
	 */
	@Value("${BaiDu.BD_TOKEN}")
	private String token;

	/**
	 * 百度地图AK
	 */
	@Value("${BaiDu.BD_AK}")
	private String ak;

	/**
	 * 百度统计URL
	 */
	@Value("${BaiDu.BD_TJ_URL}")
	private String TongJiUrl;

	/**
	 * 百度地图Url
	 */
	@Value("${BaiDu.BD_DT_URL}")
	private String DiTuUrl;

	/**
	 * 网站上线日期 格式: 20190101
	 */
	@Value("${BaiDu.PROJECT_START_DATE}")
	private String projectStartDate;

	/**
	 * 百度移动统计URL
	 */
	@Value("${BaiDu.BD_MOBILE_TJ_URL}")
	private String mobileTongJiUrl;

	/**
	 * 小程序 appKey
	 */
	@Value("${BaiDu.MINI_APP_KEY}")
	private String miniAppKey;
}
