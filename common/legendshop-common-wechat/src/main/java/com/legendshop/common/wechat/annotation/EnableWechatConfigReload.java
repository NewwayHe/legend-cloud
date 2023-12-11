/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.annotation;

import com.legendshop.common.wechat.config.WechatConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用动态路由
 *
 * @author legendshop
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(WechatConfig.class)
public @interface EnableWechatConfigReload {
}
