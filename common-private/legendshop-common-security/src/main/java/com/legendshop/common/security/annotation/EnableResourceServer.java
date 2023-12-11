/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.annotation;

import com.legendshop.common.security.config.ResourceServerAutoConfiguration;
import com.legendshop.common.security.config.ResourceServerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启资源服务鉴权注解
 *
 * @author legendshop
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ResourceServerAutoConfiguration.class, ResourceServerConfiguration.class})
public @interface EnableResourceServer {

}
