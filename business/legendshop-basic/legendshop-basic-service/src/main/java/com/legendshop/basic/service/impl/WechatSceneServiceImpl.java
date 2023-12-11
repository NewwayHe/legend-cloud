/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;


import com.legendshop.basic.dao.WechatSceneDao;
import com.legendshop.basic.dto.WechatSceneDTO;
import com.legendshop.basic.service.WechatSceneService;
import com.legendshop.basic.service.convert.WechatSceneConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class WechatSceneServiceImpl implements WechatSceneService {

	private final WechatSceneDao wechatSceneDao;
	private final WechatSceneConverter wechatSceneConverter;


	@Override
	public WechatSceneDTO getBySceneAndPage(String scene, String page) {
		return wechatSceneConverter.to(wechatSceneDao.getBySceneAndPage(scene, page));
	}

	@Override
	public Long save(WechatSceneDTO wechatSceneDTO) {
		return wechatSceneDao.save(wechatSceneConverter.from(wechatSceneDTO));
	}

	@Override
	public String getWechatScene(String md5) {
		return wechatSceneDao.getByMd5(md5).getScene();
	}
}
