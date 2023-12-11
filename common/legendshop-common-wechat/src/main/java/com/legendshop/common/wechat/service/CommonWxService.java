/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.service;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import com.legendshop.common.core.dto.BasePage;
import com.legendshop.common.core.dto.BasePageRequest;
import com.legendshop.common.wechat.model.bo.WeChatLiveReplayBO;
import com.legendshop.common.wechat.model.bo.WeChatLiveRoomBO;
import com.legendshop.common.wechat.model.dto.WeChatLiveReplayDTO;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * 微信服务入口
 *
 * @author legendshop
 */
public interface CommonWxService {

	/**
	 * 获取直播房间列表.（分页）
	 *
	 * @param pageRequest (每页大小, 建议 100 以内)
	 * @return .
	 * @throws WxErrorException
	 */
	BasePage<WeChatLiveRoomBO> getLiveInfo(BasePageRequest pageRequest) throws WxErrorException;

	/**
	 * 获取直播房间回放
	 *
	 * @param weChatLiveReplayDTO (每页大小, 建议 100 以内)
	 * @return .
	 * @throws WxErrorException
	 */
	BasePage<WeChatLiveReplayBO> getLiveReplay(WeChatLiveReplayDTO weChatLiveReplayDTO) throws WxErrorException;

	WxJsapiSignature createJsapiSignature(String url) throws WxErrorException;

	String getAccessToken() throws WxErrorException;

	WxMaQrcodeService getQrcodeService();
}
