/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.basic.entity.WechatScene;

/**
 * 微信短连接(WechatScene)表数据库访问层
 *
 * @author legendshop
 * @since 2021-03-16 15:08:42
 */
public interface WechatSceneDao extends GenericDao<WechatScene, Long> {
	WechatScene getBySceneAndPage(String scene, String page);

	WechatScene getByMd5(String md5);
}
