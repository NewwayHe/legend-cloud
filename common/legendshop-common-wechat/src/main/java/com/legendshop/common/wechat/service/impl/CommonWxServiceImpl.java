/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.service.impl;


import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.bean.WxMaLiveResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.legendshop.common.core.dto.BasePage;
import com.legendshop.common.core.dto.BasePageRequest;
import com.legendshop.common.wechat.WechatSDKClient;
import com.legendshop.common.wechat.model.bo.WeChatLiveReplayBO;
import com.legendshop.common.wechat.model.bo.WeChatLiveRoomBO;
import com.legendshop.common.wechat.model.dto.WeChatLiveReplayDTO;
import com.legendshop.common.wechat.model.enums.WeChatLiveRoomStatusEnum;
import com.legendshop.common.wechat.service.CommonWxService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供基础的微信服务
 * 需要实现WechatSDKFactory的setOptions方法啊
 *
 * @author legendshop
 */
@Slf4j
public class CommonWxServiceImpl implements CommonWxService {
	/**
	 * 使用 WechatSDKFactory 代替 获取外部化配置
	 * this.wxMaService = WechatSDKFactory.wxMaService();
	 * this.wxMpService = WechatSDKFactory.wxMpService();
	 */


	@Override
	public BasePage<WeChatLiveRoomBO> getLiveInfo(BasePageRequest pageRequest) throws WxErrorException {
		Integer start = (pageRequest.getCurPage() - 1) * pageRequest.getPageSize();
		WxMaLiveResult wxMaLiveResult = WechatSDKClient.wxMaService().getLiveService().getLiveInfo(start, pageRequest.getPageSize());
		List<WxMaLiveResult.RoomInfo> roomInfos = wxMaLiveResult.getRoomInfos();
		List<WeChatLiveRoomBO> result = new ArrayList<>();
		for (WxMaLiveResult.RoomInfo roomInfo : roomInfos) {
			WeChatLiveRoomBO bo = new WeChatLiveRoomBO();
			// 微信返回的是秒，需要换算成毫秒
			roomInfo.setStartTime(roomInfo.getStartTime() * 1000);
			roomInfo.setEndTime(roomInfo.getEndTime() * 1000);
			BeanUtil.copyProperties(roomInfo, bo);
			WeChatLiveRoomStatusEnum statusEnum = WeChatLiveRoomStatusEnum.fromCode(bo.getLiveStatus());
			if (null != statusEnum) {
				bo.setLiveStatusDesc(statusEnum.getDesc());
			}
			result.add(bo);
		}

		return new BasePage<>(pageRequest, result, wxMaLiveResult.getTotal());
	}

	@Override
	public BasePage<WeChatLiveReplayBO> getLiveReplay(WeChatLiveReplayDTO weChatLiveReplayDTO) throws WxErrorException {
		Integer start = (weChatLiveReplayDTO.getCurPage() - 1) * weChatLiveReplayDTO.getPageSize();
		WxMaLiveResult wxMaLiveResult = WechatSDKClient.wxMaService().getLiveService().getLiveReplay(weChatLiveReplayDTO.getRoomId(), start, weChatLiveReplayDTO.getPageSize());

		List<WxMaLiveResult.LiveReplay> liveReplays = wxMaLiveResult.getLiveReplay();
		List<WeChatLiveReplayBO> result = new ArrayList<>();
		int i = start;
		for (WxMaLiveResult.LiveReplay liveReplay : liveReplays) {
			WeChatLiveReplayBO bo = new WeChatLiveReplayBO();
			liveReplay.setCreateTime(DateUtil.formatDateTime(DateUtil.parseUTC(liveReplay.getCreateTime())));
			liveReplay.setExpireTime(DateUtil.formatDateTime(DateUtil.parseUTC(liveReplay.getExpireTime())));
			liveReplay.setMediaUrl(liveReplay.getMediaUrl().replace("http://", "https://"));
			BeanUtil.copyProperties(liveReplay, bo);
			bo.setId(++i);
			result.add(bo);
		}
		return new BasePage<>(weChatLiveReplayDTO, result, wxMaLiveResult.getTotal());
	}

	@Override
	public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException {
		return WechatSDKClient.wxMpService().createJsapiSignature(url);
	}

	@Override
	public String getAccessToken() throws WxErrorException {
		return WechatSDKClient.wxMaService().getAccessToken();
	}

	@Override
	public WxMaQrcodeService getQrcodeService() {
		return WechatSDKClient.wxMaService().getQrcodeService();
	}
}
