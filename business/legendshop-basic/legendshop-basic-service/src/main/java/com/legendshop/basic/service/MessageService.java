/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.bo.MsgBO;
import com.legendshop.basic.bo.UserMsgBo;
import com.legendshop.basic.dto.AdminMessageDTOList;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.query.MsgQuery;
import com.legendshop.common.core.constant.R;

/**
 * 站内信服务.
 *
 * @author legendshop
 */
public interface MessageService {

	/**
	 * 获取后台消息箱数据
	 *
	 * @return 后台消息箱数据
	 */
	AdminMessageDTOList getMessage();

	/**
	 * 查询退货申请数量
	 *
	 * @return 退货申请数量
	 */
	int getReturnGoodsCount();

	/**
	 * 查询退款申请数量
	 *
	 * @return 退款申请数量
	 */
	int getReturnMoneyCount();

	/**
	 * 查询待确认的结算单数量
	 *
	 * @return 待确认的结算单数量
	 */
	int getShopBillCount();

	/**
	 * 查询待审核的结算单数量
	 *
	 * @return 待审核的结算单数量
	 */
	int getShopBillConfirmCount();

	/**
	 * 待预售活动管理
	 *
	 * @return 待预售活动管理
	 */
	int getPreSellProdCount();

	/**
	 * 我的系统通知
	 *
	 * @param query
	 * @return
	 */
	PageSupport<MsgBO> queryMyMsgPage(MsgQuery query);

	/**
	 * 获取站内信详情
	 *
	 * @param userId 用户ID
	 * @param msgId  消息ID
	 * @param type   用户类型
	 * @return
	 */
	MsgBO getByMsgId(Long userId, Long msgId, Integer type);

	/**
	 * 消息推送
	 *
	 * @param messagePushDTO
	 * @return
	 */
	R push(MessagePushDTO messagePushDTO);

	R<Integer> unreadMsg(Long userId, MsgReceiverTypeEnum msgReceiverTypeEnum);

	/**
	 * 用户PC端我的消息
	 *
	 * @param userId
	 * @return
	 */
	R<UserMsgBo> userUnread(Long userId);

	/**
	 * 给品台用户所有具站内信权限的用户发送站内信
	 *
	 * @param content  发送内容
	 * @param detailId 详情ID 商品传商品ID 团购活动传团购活动ID
	 * @return
	 */
	Boolean sendToAllAdmin(String content, Long detailId, MsgSendTypeEnum msgSendTypeEnum);

	Integer getUnAllReadNum(Long id);

	Integer getAdminUnreadNum(Long id);

	/**
	 * 清除未读标记
	 *
	 * @param userId
	 * @param userType
	 * @return
	 */
	void cleanUnread(long userId, Integer userType);
}
