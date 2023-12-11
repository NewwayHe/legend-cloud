/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.data;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamItemApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.BaiDuMobileStatisticsSettingDTO;
import com.legendshop.basic.dto.BatchUpdateSysParamDTO;
import com.legendshop.basic.dto.SysParamValueItemDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.data.api.BaiDuCountApi;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 百度（移动）统计定时任务
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BaiduViewJob {

	private final SysParamsApi sysParamsApi;
	private final SysParamItemApi sysParamItemApi;
	private final BaiDuCountApi baiDuCountApi;

	private final String url = "http://openapi.baidu.com/oauth/2.0/token";

	/**
	 * 百度统计归档
	 *
	 * @param param 格式： yyyy-MM-dd&yyyy-MM-dd
	 * @return
	 */
	@XxlJob("baiduStatisticsArchive")
	public ReturnT<String> baiduStatisticsArchive(String param) {

		Date startDate = DateUtil.beginOfDay(DateUtil.yesterday());
		Date endDate = DateUtil.endOfDay(DateUtil.yesterday());
		if (StrUtil.isNotBlank(param) && param.contains(StringConstant.AND)) {
			// todo 动态时间归档百度统计
			List<String> split = StrUtil.split(param, StringConstant.AND);
			if (split.size() == 2) {
				startDate = DateUtil.beginOfDay(DateUtil.parseDate(split.get(0)));
				endDate = DateUtil.endOfDay(DateUtil.parseDate(split.get(1)));
			}
		}

		R r = baiDuCountApi.baiduStatisticsArchive(startDate, endDate);
		if (!r.getSuccess()) {
			XxlJobHelper.log(r.getMsg());
			return ReturnT.FAIL;
		}

		return ReturnT.SUCCESS;
	}

	/**
	 * 百度移动统计token刷新
	 * https://mtj.baidu.com/static/userguide/book/api/outline/Usage.html
	 *
	 * @param param
	 * @return
	 */
	@XxlJob("baiduViewRefreshToken")
	public ReturnT baiduViewRefreshToken(String param) {

		R<BaiDuMobileStatisticsSettingDTO> result = sysParamsApi.getNotCacheConfigByName(SysParamNameEnum.BAIDU_MOBILE_STATISTICS.name(), BaiDuMobileStatisticsSettingDTO.class);
		if (!result.getSuccess()) {
			log.info("获取百度移动统计配置失败~");
			XxlJobHelper.log("获取百度移动统计配置失败~");
			return ReturnT.FAIL;
		}

		// 处理拆分为微服务后，强转失败问题
		ObjectMapper mapper = new ObjectMapper();

		BaiDuMobileStatisticsSettingDTO settingDTO = mapper.convertValue(result.getData(), BaiDuMobileStatisticsSettingDTO.class);

		JSONObject params = new JSONObject();
		params.set("grant_type", "refresh_token");
		params.set("refresh_token", settingDTO.getRefreshToken());
		params.set("client_id", settingDTO.getApiKey());
		params.set("client_secret", settingDTO.getSecretKey());

		log.info("开始刷新百度移动统计token，请求参数: {}", params);
		XxlJobHelper.log("开始刷新百度移动统计token，请求参数: {}", params);
		String json = HttpUtil.get(url, params);
		log.info("百度移动统计刷新token响应：{}", json);
		XxlJobHelper.log("百度移动统计刷新token响应：{}", json);

		JSONObject jsonObject = JSONUtil.parseObj(json);

		BatchUpdateSysParamDTO paramDTO = new BatchUpdateSysParamDTO();
		paramDTO.setName(SysParamNameEnum.BAIDU_MOBILE_STATISTICS.name());

		List<SysParamValueItemDTO> sysParamValueItemDTOList = new ArrayList<>();
		SysParamValueItemDTO accessToken = new SysParamValueItemDTO();
		accessToken.setId(1450L);
		accessToken.setValue(jsonObject.getStr("access_token"));
		sysParamValueItemDTOList.add(accessToken);
		SysParamValueItemDTO refreshToken = new SysParamValueItemDTO();
		refreshToken.setId(1451L);
		refreshToken.setValue(jsonObject.getStr("refresh_token"));
		sysParamValueItemDTOList.add(refreshToken);

		paramDTO.setSysParamValueItemDTOS(sysParamValueItemDTOList);
		sysParamItemApi.updateValueOnlyById(paramDTO);
		return ReturnT.SUCCESS;
	}
}
