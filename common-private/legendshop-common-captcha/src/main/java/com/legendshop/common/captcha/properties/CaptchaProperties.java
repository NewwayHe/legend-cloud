/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.captcha.properties;

import com.anji.captcha.model.common.CaptchaTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.legendshop.common.captcha.properties.CaptchaProperties.PREFIX;
import static com.legendshop.common.captcha.properties.CaptchaProperties.StorageType.local;

/**
 * 验证码的自定义配置
 *
 * @author legendshop
 */
@ConfigurationProperties(PREFIX)
@Data
public class CaptchaProperties {


	public static final String PREFIX = "legendshop.captcha";

	/**
	 * 验证码类型.
	 */
	private CaptchaTypeEnum type = CaptchaTypeEnum.DEFAULT;


	/**
	 * 点选文字底图路径.
	 */
	private String picClick = "";


	/**
	 * 右下角水印文字(我的水印).
	 */
	private String waterMark = "legendshop";

	/**
	 * 右下角水印字体(宋体).
	 */
	private String waterFont = "宋体";

	/**
	 * 点选文字验证码的文字字体(宋体).
	 */
	private String fontType = "宋体";

	/**
	 * 校验滑动拼图允许误差偏移量(默认5像素).
	 */
	private String slipOffset = "5";

	/**
	 * aes加密坐标开启或者禁用(true|false).
	 */
	private Boolean aesStatus = true;

	/**
	 * 滑块干扰项(0/1/2)
	 */
	private String interferenceOptions = "0";

	/**
	 * local缓存的阈值
	 */
	private String cacheNumber = "1000";

	/**
	 * 定时清理过期local缓存(单位秒)
	 */
	private String timingClear = "180";

	/**
	 * 缓存类型redis/local/....
	 */
	private StorageType cacheType = local;

	@NoArgsConstructor
	public enum StorageType {
		/**
		 * 内存.
		 */
		local,
		/**
		 * redis缓存.
		 */
		redis
	}
}
