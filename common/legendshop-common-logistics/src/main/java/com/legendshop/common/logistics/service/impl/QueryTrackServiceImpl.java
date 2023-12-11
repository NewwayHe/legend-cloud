/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.service.impl;

import com.legendshop.common.logistics.kuaidi100.api.QueryTrack;
import com.legendshop.common.logistics.kuaidi100.request.QueryTrackReq;
import com.legendshop.common.logistics.kuaidi100.response.QueryTrackResp;
import com.legendshop.common.logistics.service.QueryTrackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Component
@AllArgsConstructor
public class QueryTrackServiceImpl implements QueryTrackService {

	private final QueryTrack queryTrack;

	@Override
	public QueryTrackResp queryTrack(QueryTrackReq queryTrackReq) {
		return queryTrack.queryTrack(queryTrackReq);
	}
}
