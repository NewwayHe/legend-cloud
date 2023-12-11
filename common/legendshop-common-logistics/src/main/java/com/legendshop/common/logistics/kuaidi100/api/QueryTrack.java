/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.api;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.legendshop.common.logistics.kuaidi100.contant.ApiInfoConstant;
import com.legendshop.common.logistics.kuaidi100.pojo.HttpResult;
import com.legendshop.common.logistics.kuaidi100.request.QueryTrackReq;
import com.legendshop.common.logistics.kuaidi100.response.QueryTrackResp;
import com.legendshop.common.logistics.kuaidi100.utils.HttpUtils;
import org.springframework.stereotype.Component;

/**
 * 实时查询
 *
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-14 16:27
 */
@Component
public class QueryTrack {


	public QueryTrackResp queryTrack(QueryTrackReq queryTrackReq) {
		QueryTrackResp queryTrackResp = new QueryTrackResp();
		if (queryTrackReq == null) {
			return null;
		}

		HttpResult httpResult = HttpUtils.doPost(ApiInfoConstant.QUERY_URL, queryTrackReq);

		if (httpResult.getStatus() == 200 && StrUtil.isNotBlank(httpResult.getBody())) {
			queryTrackResp = new Gson().fromJson(httpResult.getBody(), QueryTrackResp.class);
		}
		return queryTrackResp;
	}

}
