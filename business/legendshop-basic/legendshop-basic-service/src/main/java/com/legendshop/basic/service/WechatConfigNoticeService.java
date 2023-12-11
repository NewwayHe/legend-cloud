/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.common.core.config.sys.params.WxConfig;

/**
 * @author legendshop
 */
public interface WechatConfigNoticeService {

	void weChatConfigPush(String name, WxConfig wxConfig);

}
