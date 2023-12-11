/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 忽略商品结果集配置
 *
 * @author legendshop
 */
@RefreshScope
@Configuration
@ConditionalOnExpression("!'${legendshop.basic.allowed-file-types}'.isEmpty()")
@ConfigurationProperties(prefix = "legendshop.basic")
public class AllowedFileTypesProperties {

	/**
	 * 允许上传的文件类型
	 */
	@Getter
	@Setter
	List<String> allowedFileTypes;

}
