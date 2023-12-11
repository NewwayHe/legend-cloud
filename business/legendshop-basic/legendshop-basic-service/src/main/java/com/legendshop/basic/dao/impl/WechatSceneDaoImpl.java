/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.basic.dao.WechatSceneDao;
import com.legendshop.basic.entity.WechatScene;
import org.springframework.stereotype.Repository;

/**
 * @author legendshop
 */
@Repository
public class WechatSceneDaoImpl extends GenericDaoImpl<WechatScene, Long> implements WechatSceneDao {


	@Override
	public WechatScene getBySceneAndPage(String scene, String page) {
		return this.getByProperties(new EntityCriterion().eq("scene", scene).eq("page", page));
	}

	@Override
	public WechatScene getByMd5(String md5) {
		return this.getByProperties(new EntityCriterion().eq("md5", md5));
	}
}
