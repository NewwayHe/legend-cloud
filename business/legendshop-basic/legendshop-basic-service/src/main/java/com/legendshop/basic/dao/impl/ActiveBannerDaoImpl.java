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
import com.legendshop.basic.dao.ActiveBannerDao;
import com.legendshop.basic.entity.ActiveBanner;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动广告Dao实现类
 *
 * @author legendshop
 */
@Repository
public class ActiveBannerDaoImpl extends GenericDaoImpl<ActiveBanner, Long> implements ActiveBannerDao {

	@Override
	public List<ActiveBanner> getBannerList(String type) {
		return this.queryByProperties(new EntityCriterion().eq("bannerType", type).addAscOrder("seq"));
	}

	@Override
	public List<String> queryAttachmentByUrl(String fileName) {
		String sql = "select name from ls_product_property_image where img_file like ?";
		List<Object> obj = new ArrayList<>();
		obj.add("%" + fileName + "%");
		return this.query(sql, String.class, obj.toArray());
	}
}
