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
import com.legendshop.basic.entity.ActiveBanner;

import java.util.List;

/**
 * 活动的banner图Dao
 *
 * @author legendshop
 */
public interface ActiveBannerDao extends GenericDao<ActiveBanner, Long> {

	/**
	 * 根据banner类型获取结果
	 *
	 * @param type 参见ActiveBannerTypeEnum
	 * @return
	 */
	List<ActiveBanner> getBannerList(String type);


	List<String> queryAttachmentByUrl(String fileName);

}
