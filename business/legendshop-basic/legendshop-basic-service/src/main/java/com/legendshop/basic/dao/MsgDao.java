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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.bo.MsgBO;
import com.legendshop.basic.entity.Msg;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.query.MsgQuery;

/**
 * 站内信Dao.
 *
 * @author legendshop
 */
public interface MsgDao extends GenericDao<Msg, Long> {

	/**
	 * 将邮件设置为已读
	 *
	 * @param userId
	 * @param msgId
	 */
	int updateMsgRead(Long userId, Long msgId, Integer type);

	/**
	 * 保存系统通知
	 *
	 * @param msg
	 * @param receiverIds
	 * @param type        {@link MsgReceiverTypeEnum}
	 * @return
	 */
	Long saveSystemMessages(Msg msg, Long[] receiverIds, Integer type);

	int getAuditShop();

	int getProductConsultCount();

	int getAuditProductCount();

	int getAuditBrandCount();

	int getReturnGoodsCount();

	int getReturnMoneyCount();

	int getAccusationCount();

	int getShopBillCount();

	int getShopBillConfirmCount();

	int getAuctionCount();

	int getProductCommentCount();

	int getPreSellProdCount();

	int getUserCommisCount();

	int getUserFeedBackCount();

	/**
	 * 我的系统通知
	 *
	 * @param query
	 * @return
	 */
	PageSupport<MsgBO> queryMyMsgPage(MsgQuery query);


	/**
	 * 获取站内信
	 *
	 * @param msgId
	 * @return
	 */
	MsgBO getByMsgId(Long msgId);

	Integer unreadMsg(Long userId, MsgReceiverTypeEnum msgReceiverTypeEnum);

	/**
	 * 获得公告未读数量
	 *
	 * @param userId
	 * @return
	 */
	Integer getNoticeUnreadMsgCount(Long userId);

	/**
	 * 获得系统通知未读数量
	 *
	 * @param userId
	 * @return
	 */
	Integer getSystemUnreadMsgCount(Long userId);


	Integer getUnAllReadNum(Long id);

	Integer getAdminUnreadNum(Long id);

	void cleanUnread(long userId, Integer userType);
}
