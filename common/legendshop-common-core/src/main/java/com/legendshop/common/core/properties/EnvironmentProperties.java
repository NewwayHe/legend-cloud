/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * @author legendshop
 */
@Data
@ConditionalOnExpression("!'${legendshop.global.environment.debug}'.isEmpty()")
@ConfigurationProperties(prefix = "legendshop.global.environment")
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
public class EnvironmentProperties {

	@Value("${debug:false}")
	private boolean debug = false;

}
