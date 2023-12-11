/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 获取系统的配置 包括数据库和配置文件的配置
 *
 * @author legendshop
 */
@Data
@RefreshScope
@Component("propertiesUtil")
public class PropertiesUtil {

	/**
	 * 压缩包上传的本地路径，最好是nfs下的路径，不带/结束
	 */
	@Value("${legendshop.common.business.switch.uploadZipToLocalPath:}")
	private String uploadZipToLocalPath;
}
