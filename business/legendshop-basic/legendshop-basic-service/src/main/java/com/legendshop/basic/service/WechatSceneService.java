/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.WechatSceneDTO;

/**
 * 微信短连接(WechatScene)表服务接口
 *
 * @author legendshop
 * @since 2021-03-16 15:08:43
 */
public interface WechatSceneService {

	WechatSceneDTO getBySceneAndPage(String scene, String page);

	Long save(WechatSceneDTO wechatSceneDTO);

	String getWechatScene(String md5);
}
