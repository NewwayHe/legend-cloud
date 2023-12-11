/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.service;

import com.legendshop.common.logistics.kuaidi100.request.QueryTrackReq;
import com.legendshop.common.logistics.kuaidi100.response.QueryTrackResp;

/**
 * 实时查询的接口
 *
 * @author legendshop
 */
public interface QueryTrackService {

	/**
	 * 查询实时物流
	 */
	QueryTrackResp queryTrack(QueryTrackReq queryTrackReq);
}
