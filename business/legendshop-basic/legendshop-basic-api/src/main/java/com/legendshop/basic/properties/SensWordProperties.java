/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.properties;


import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 是否开启默认敏感词过滤
 *
 * @author legendshop
 */
@RefreshScope
@Configuration
@Data
@ConditionalOnExpression("!'${legendshop.basic.sensword.enable}'.isEmpty()")
@ConfigurationProperties(prefix = "legendshop.basic.sensword")
public class SensWordProperties {

	/**
	 * 是否开启默认敏感词过滤
	 */
	Boolean enable;

}
