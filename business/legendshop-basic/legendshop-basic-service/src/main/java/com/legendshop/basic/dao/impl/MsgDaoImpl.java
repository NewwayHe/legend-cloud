/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.basic.bo.MsgBO;
import com.legendshop.basic.dao.MsgDao;
import com.legendshop.basic.dao.MsgReceiverDao;
import com.legendshop.basic.dao.ReceiverDao;
import com.legendshop.basic.entity.Msg;
import com.legendshop.basic.entity.Receiver;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.basic.enums.ReceiverBusinessTypeEnum;
import com.legendshop.basic.query.MsgQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 站内信Dao.
 *
 * @author legendshop
 */
@Repository
public class MsgDaoImpl extends GenericDaoImpl<Msg, Long> implements MsgDao {


	@Autowired
	private MsgReceiverDao msgReceiverDao;
	@Autowired
	private ReceiverDao receiverDao;


	@Override
	public int updateMsgRead(Long userId, Long msgId, Integer type) {
		return update("update ls_receiver set status = 1 where user_id = ? and business_id = ? and user_type=?", userId, msgId, type);
	}

	@Override
	public Long saveSystemMessages(Msg msg, Long[] receiverIds, Integer type) {
		Long msgId = this.save(msg);
		for (int i = 0; i < receiverIds.length; i++) {
			saveMsgReceiver(msgId, receiverIds[i], type);
		}
		return msgId;
	}

	private Long saveMsgReceiver(Long msgId, Long userId, Integer type) {
		if (userId == null || msgId == null) {
			return 0L;
		}

		if (ObjectUtil.isNull(type)) {
			type = MsgReceiverTypeEnum.ORDINARY_USER.value();
		}
		Receiver receiver = new Receiver();
		receiver.setBusinessId(msgId);
		receiver.setUserId(userId);
		receiver.setStatus(0);
		receiver.setUserType(type);
		receiver.setBusinessType(ReceiverBusinessTypeEnum.SYSTEM_MESSAGE.value());
		receiver.setCreateTime(new Date());
		return receiverDao.save(receiver);
	}

	@Override
	public int getAuditShop() {
		String sql = "SELECT COUNT(1) FROM ls_shop_detail WHERE STATUS = -1";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getProductConsultCount() {
		String sql = "select count(*) from ls_product p,ls_product_cons pc,ls_shop_detail sd, ls_user_detail ud where p.id = pc.product_id and sd.shop_id=p.shop_id and pc.ask_user_name=ud.user_name and reply_sts = 0";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getAuditProductCount() {
		String sql = "SELECT COUNT(1) FROM ls_product WHERE op_status = ?";
		return this.get(sql, Integer.class, OpStatusEnum.WAIT.getValue());
	}

	@Override
	public int getAuditBrandCount() {
		String sql = "SELECT COUNT(1) FROM ls_brand WHERE STATUS = -1";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getReturnGoodsCount() {
		String sql = "SELECT COUNT(1) FROM ls_order_refund_return WHERE apply_state = 2 AND apply_type=2";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getReturnMoneyCount() {
		String sql = "SELECT COUNT(1) FROM ls_order_refund_return WHERE apply_state = 2 AND apply_type=1";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getAccusationCount() {
		String sql = "SELECT COUNT(1) FROM ls_accusation la,ls_product lp,ls_accu_subject las,ls_shop_detail lsd, ls_user_detail lud WHERE la.product_id = lp.product_id AND la.subject_id = las.as_id AND lsd.shop_id=lp.shop_id AND lud.user_id=la.user_id     AND la.status = 0 ORDER BY la.rec_date DESC";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getShopBillCount() {
		String sql = "SELECT COUNT(1) FROM ls_shop_order_bill WHERE STATUS = 1";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getShopBillConfirmCount() {
		String sql = "SELECT COUNT(1) FROM ls_shop_order_bill WHERE STATUS = 2";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getAuctionCount() {
		String sql = "SELECT COUNT(1) FROM ls_auction WHERE STATUS = -1 and end_time > now()";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getProductCommentCount() {
		String sql = "SELECT COUNT(1) FROM ls_product_comm c INNER JOIN ls_order_item subItem ON subItem.sub_item_id = c.sub_item_id INNER JOIN ls_shop_detail sd ON sd.user_name = c.owner_name LEFT JOIN ls_product_add_comm ac ON ac.prod_comm_id = c.id WHERE 1 = 1 AND (c.status = 0 OR (c.status = 1 AND ac.status = 0))";

		return this.get(sql, Integer.class);
	}

	@Override
	public int getPreSellProdCount() {
		String sql = "SELECT COUNT(1) FROM ls_pre_sell_product pp,ls_product p WHERE pp.product_id = p.id AND pp. STATUS = -1 AND (pp.pre_sale_start >= ? AND pp.pre_sale_end > ?)";
		Date date = new Date();
		return this.get(sql, Integer.class, date, date);
	}

	@Override
	public int getUserCommisCount() {
		String sql = "SELECT COUNT(1) FROM ls_user_commis WHERE promoter_sts = -2";
		return this.get(sql, Integer.class);
	}

	@Override
	public int getUserFeedBackCount() {
		String sql = "SELECT COUNT(1) FROM ls_usr_feedback WHERE STATUS = 0";
		return this.get(sql, Integer.class);
	}

	@Override
	public PageSupport<MsgBO> queryMyMsgPage(MsgQuery query) {
		SimpleSqlQuery q = new SimpleSqlQuery(MsgBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("receiverId", query.getReceiverId());
		map.put("userType", query.getUserType());
		if (query.getUserType().equals(MsgReceiverTypeEnum.ADMIN_USER.value())) {
			map.put("type", MsgSendTypeEnum.PROD_AUDIT.getValue());
		}
		map.put("businessType", ReceiverBusinessTypeEnum.SYSTEM_MESSAGE.value());
		q.setSqlAndParameter("Message.queryMyMsg", map);
		return querySimplePage(q);
	}

	@Override
	public MsgBO getByMsgId(Long msgId) {
		String sql = "select id,title,content,type,detail_id,rec_date from ls_msg where id = ? limit 1";
		return get(sql, MsgBO.class, msgId);
	}

	@Override
	public Integer unreadMsg(Long userId, MsgReceiverTypeEnum msgReceiverTypeEnum) {
		if (msgReceiverTypeEnum == null) {
			return 0;
		}
		return super.get("SELECT COUNT(*) FROM ls_msg_receiver WHERE user_id = ? AND status = 0 AND type  = ?", Integer.class, userId, msgReceiverTypeEnum.value());
	}

	@Override
	public Integer getNoticeUnreadMsgCount(Long userId) {
		return get("SELECT COUNT(*) FROM ls_pub_receiver lr LEFT JOIN ls_pub lp ON lp.id = lr.pub_id WHERE lr.user_id = ? AND lp.status = 1 AND lr.status=0", Integer.class, userId);

	}

	@Override
	public Integer getSystemUnreadMsgCount(Long userId) {
		return get("SELECT COUNT(*) FROM ls_msg m INNER JOIN ls_receiver r ON m.id = r.business_id WHERE 1 = 1 AND r.status =0 AND r.user_id=?", Integer.class, userId);
	}


	@Override
	public Integer getUnAllReadNum(Long id) {
		return get("SELECT COUNT(*)FROM ls_msg m INNER JOIN ls_receiver r ON m.id = r.business_id AND r.user_id = ? AND r.user_type =2 AND r.business_type=2 AND r.status=0 ", Integer.class, id);
	}

	@Override
	public Integer getAdminUnreadNum(Long id) {
		return get("SELECT COUNT(*)FROM ls_msg m INNER JOIN ls_receiver r ON m.id = r.business_id AND r.user_id = ? AND r.user_type =3 AND r.business_type=2 AND r.status=0 AND m.type=130", Integer.class, id);
	}

	@Override
	public void cleanUnread(long userId, Integer userType) {
		update("update ls_receiver set status = 1 where status = 0 and user_id = ? and user_type=?", userId, userType);
	}
}
