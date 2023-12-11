/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.bo.MsgBO;
import com.legendshop.basic.bo.UserMsgBo;
import com.legendshop.basic.dao.MsgDao;
import com.legendshop.basic.dto.*;
import com.legendshop.basic.entity.Msg;
import com.legendshop.basic.enums.*;
import com.legendshop.basic.query.MsgQuery;
import com.legendshop.basic.service.MessageConsumer;
import com.legendshop.basic.service.MessageService;
import com.legendshop.basic.service.ReceiverService;
import com.legendshop.basic.service.convert.MsgConverter;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.SkuApi;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.user.api.AdminUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 站内信服务.
 *
 * @author legendshop
 */
@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MsgDao msgDao;

	@Autowired
	private MsgConverter msgConverter;

	@Autowired
	private MessageConsumer messageConsumer;

	@Autowired
	private AdminUserApi adminUserApi;

	@Autowired
	private ReceiverService receiverService;

	@Autowired
	private ProductApi productApi;

	@Autowired
	private SkuApi skuApi;


	/**
	 * 加到缓存里，20秒有效
	 */
	@Override
	@Cacheable(value = "AdminMessage")
	public AdminMessageDTOList getMessage() {

		Integer auditShopNumber = msgDao.getAuditShop();
		Integer productConsultNum = msgDao.getProductConsultCount();
		Integer auditProductNum = msgDao.getAuditProductCount();
		Integer auditBrandNum = msgDao.getAuditBrandCount();
		Integer returnGoodsNum = msgDao.getReturnGoodsCount();
		Integer returnMoneyNum = msgDao.getReturnMoneyCount();
		Integer accusationNum = msgDao.getAccusationCount();
		Integer shopBillNum = msgDao.getShopBillCount();
		Integer shopBillConfirmNum = msgDao.getShopBillConfirmCount();
		Integer auctionNum = msgDao.getAuctionCount();
		Integer productCommentNum = msgDao.getProductCommentCount();
		Integer preSellProdNum = msgDao.getPreSellProdCount();
		Integer userCommisNum = msgDao.getUserCommisCount();
		Integer userFeedBackNum = msgDao.getUserFeedBackCount();
		Integer totalNum = auditShopNumber + productConsultNum + auditProductNum + auditBrandNum + returnGoodsNum + returnMoneyNum
				+ accusationNum + shopBillNum + shopBillConfirmNum + auctionNum + productCommentNum
				+ preSellProdNum + userCommisNum + userFeedBackNum;

		List<AdminMessageDTO> adminMessageList = new ArrayList<AdminMessageDTO>();
		AdminMessageDTO adminMessageDto = new AdminMessageDTO();
		adminMessageDto.setName(AdminMessageEnum.AUDITUING_SHOP.value());
		adminMessageDto.setUrl(AdminMessageEnum.AUDITUING_SHOP.url());
		adminMessageDto.setNum(auditShopNumber);
		adminMessageList.add(adminMessageDto);

		AdminMessageDTO adminMessageDto2 = new AdminMessageDTO();
		adminMessageDto2.setName(AdminMessageEnum.PROD_CONSULT.value());
		adminMessageDto2.setUrl(AdminMessageEnum.PROD_CONSULT.url());
		adminMessageDto2.setNum(productConsultNum);
		adminMessageList.add(adminMessageDto2);

		AdminMessageDTO adminMessageDto3 = new AdminMessageDTO();
		adminMessageDto3.setName(AdminMessageEnum.AUDITUING_PROD.value());
		adminMessageDto3.setUrl(AdminMessageEnum.AUDITUING_PROD.url());
		adminMessageDto3.setNum(auditProductNum);
		adminMessageList.add(adminMessageDto3);

		AdminMessageDTO adminMessageDto4 = new AdminMessageDTO();
		adminMessageDto4.setName(AdminMessageEnum.AUDITUING_BRAND.value());
		adminMessageDto4.setUrl(AdminMessageEnum.AUDITUING_BRAND.url());
		adminMessageDto4.setNum(auditBrandNum);
		adminMessageList.add(adminMessageDto4);

		AdminMessageDTO adminMessageDto5 = new AdminMessageDTO();
		adminMessageDto5.setName(AdminMessageEnum.RETURNGOODS.value());
		adminMessageDto5.setUrl(AdminMessageEnum.RETURNGOODS.url());
		adminMessageDto5.setNum(returnGoodsNum);
		adminMessageList.add(adminMessageDto5);

		AdminMessageDTO adminMessageDto6 = new AdminMessageDTO();
		adminMessageDto6.setName(AdminMessageEnum.RETURNMONEY.value());
		adminMessageDto6.setUrl(AdminMessageEnum.RETURNMONEY.url());
		adminMessageDto6.setNum(returnMoneyNum);
		adminMessageList.add(adminMessageDto6);

		AdminMessageDTO adminMessageDto7 = new AdminMessageDTO();
		adminMessageDto7.setName(AdminMessageEnum.ACCUSATION.value());
		adminMessageDto7.setUrl(AdminMessageEnum.ACCUSATION.url());
		adminMessageDto7.setNum(accusationNum);
		adminMessageList.add(adminMessageDto7);

		AdminMessageDTO adminMessageDto8 = new AdminMessageDTO();
		adminMessageDto8.setName(AdminMessageEnum.SHOP_BILL.value());
		adminMessageDto8.setUrl(AdminMessageEnum.SHOP_BILL.url());
		adminMessageDto8.setNum(shopBillNum);
		adminMessageList.add(adminMessageDto8);

		AdminMessageDTO adminMessageDto9 = new AdminMessageDTO();
		adminMessageDto9.setName(AdminMessageEnum.SHOP_BILL_CONFIRM.value());
		adminMessageDto9.setUrl(AdminMessageEnum.SHOP_BILL_CONFIRM.url());
		adminMessageDto9.setNum(shopBillConfirmNum);
		adminMessageList.add(adminMessageDto9);

		AdminMessageDTO adminMessageDto13 = new AdminMessageDTO();
		adminMessageDto13.setName(AdminMessageEnum.PRESELL_PROD.value());
		adminMessageDto13.setUrl(AdminMessageEnum.PRESELL_PROD.url());
		adminMessageDto13.setNum(preSellProdNum);
		adminMessageList.add(adminMessageDto13);

		AdminMessageDTO adminMessageDto14 = new AdminMessageDTO();
		adminMessageDto14.setName(AdminMessageEnum.USERCOMMIS.value());
		adminMessageDto14.setUrl(AdminMessageEnum.USERCOMMIS.url());
		adminMessageDto14.setNum(userCommisNum);
		adminMessageList.add(adminMessageDto14);

		AdminMessageDTO adminMessageDto15 = new AdminMessageDTO();
		adminMessageDto15.setName(AdminMessageEnum.USER_FEEDBACK.value());
		adminMessageDto15.setUrl(AdminMessageEnum.USER_FEEDBACK.url());
		adminMessageDto15.setNum(userFeedBackNum);
		adminMessageList.add(adminMessageDto15);

		AdminMessageDTO adminMessageDto16 = new AdminMessageDTO();
		adminMessageDto16.setName(AdminMessageEnum.PRODUCT_COMMENT.value());
		adminMessageDto16.setUrl(AdminMessageEnum.PRODUCT_COMMENT.url());
		adminMessageDto16.setNum(productCommentNum);
		adminMessageList.add(adminMessageDto16);

		AdminMessageDTOList adminMessageDtoList = new AdminMessageDTOList();
		adminMessageDtoList.setList(adminMessageList);
		adminMessageDtoList.setTotalMessage(totalNum);
		return adminMessageDtoList;
	}

	@Override
	public int getReturnGoodsCount() {
		return msgDao.getReturnGoodsCount();
	}

	@Override
	public int getReturnMoneyCount() {
		return msgDao.getReturnMoneyCount();
	}

	@Override
	public int getShopBillCount() {
		return msgDao.getShopBillCount();
	}

	@Override
	public int getShopBillConfirmCount() {
		return msgDao.getShopBillConfirmCount();
	}

	@Override
	public int getPreSellProdCount() {
		return msgDao.getPreSellProdCount();
	}

	@Override
	public PageSupport<MsgBO> queryMyMsgPage(MsgQuery query) {
		PageSupport<MsgBO> page = msgDao.queryMyMsgPage(query);
		page.getResultList().forEach(a -> a.setType(MsgSendTypeEnum.getParentType(a.getType())));
		return page;
	}

	@Override
	public MsgBO getByMsgId(Long userId, Long msgId, Integer type) {

		MsgBO msgBO = msgDao.getByMsgId(msgId);
		if (ObjectUtil.isNull(msgBO)) {
			return null;
		}
		if (MsgSendTypeEnum.getParentType(msgBO.getType()).equals(ParentMsgSendTypeEnum.PRODUCT.getValue())) {
			// 如果是用户到货提醒，则detailId是skuId
			Long productId = msgBO.getDetailId();
			if (MsgSendTypeEnum.PROD_ARRIVAL.getValue().equals(msgBO.getType())) {
				R<SkuBO> skuResult = skuApi.getSkuById(productId);
				SkuBO skuBO = skuResult.getData();
				productId = skuBO.getProductId();
				msgBO.setDetailId(productId);
				msgBO.setDetailItemId(skuBO.getId());
			}

			if (productId != null) {
				ProductDTO product = productApi.getDtoByProductId(productId).getData();
				Optional<ProductDTO> optional = Optional.ofNullable(product);
				optional.ifPresent(productDTO -> {
					msgBO.setPic(product.getPic());
					msgBO.setProductName(product.getName());
				});
			}
		}
		msgBO.setTypeDetail(msgBO.getType());
		msgBO.setType(MsgSendTypeEnum.getParentType(msgBO.getType()));

		//根据userId和msgId查询为空的已读记录 为空添加记录
		if (!receiverService.getByUserIdAndMsgId(userId, msgId, ReceiverBusinessTypeEnum.SYSTEM_MESSAGE.value())) {
			//更新已读消息表
			ReceiverDTO receiver = new ReceiverDTO();
			receiver.setUserType(MsgReceiverTypeEnum.ORDINARY_USER.value());
			receiver.setUserId(userId);
			receiver.setBusinessId(msgId);
			receiver.setCreateTime(new Date());
			receiver.setStatus(1);
			receiver.setBusinessType(ReceiverBusinessTypeEnum.SYSTEM_MESSAGE.value());
			R<Boolean> save = receiverService.save(receiver);
			if (save.getSuccess() && !save.getData()) {
				throw new BusinessException("更新消息为已读失败");
			}
		} else {
			// 更新阅标识
			ReceiverDTO byId = receiverService.getBusinessId(msgId);
			byId.setStatus(1);
			receiverService.update(byId);
			msgDao.updateMsgRead(userId, msgId, type);
		}

		return msgBO;
	}

	@Override
	public R push(MessagePushDTO messagePushDTO) {

		MsgSendDTO msgSendDTO = new MsgSendDTO();
		//替换参数内容
		msgSendDTO.setMsgSendParamDTOList(messagePushDTO.getMsgSendParamDTOList());
		msgSendDTO.setUrlParamList(messagePushDTO.getUrlParamList());
		//系统通知类型
		msgSendDTO.setMsgSendType(messagePushDTO.getMsgSendType());
		//推送通知类型 通过类型获取通知模板
		msgSendDTO.setSysParamNameEnum(messagePushDTO.getSysParamNameEnum());
		msgSendDTO.setDetailId(messagePushDTO.getDetailId());
		List<SysMsgReceiverDTO> sysMsgReceiverDtoList = new ArrayList<>();
		SysMsgReceiverDTO sysMsgReceiverDTO = new SysMsgReceiverDTO();
		sysMsgReceiverDTO.setReceiveUserIds(messagePushDTO.getReceiveIdArr());
		//接收通知的角色
		sysMsgReceiverDTO.setReceiverType(messagePushDTO.getMsgReceiverTypeEnum().value());
		sysMsgReceiverDtoList.add(sysMsgReceiverDTO);
		msgSendDTO.setSysMsgReceiverDTOS(sysMsgReceiverDtoList);
		msgSendDTO.setReceiveUserPhoneNumbers(messagePushDTO.getReceiveUserPhoneNumbers());
		msgSendDTO.setReceiveUserWxOpenIds(messagePushDTO.getReceiveUserWxOpenIds());
		messageConsumer.consumeMsg(msgSendDTO);
		return R.ok();
	}

	@Override
	public R<Integer> unreadMsg(Long userId, MsgReceiverTypeEnum msgReceiverTypeEnum) {
		return R.ok(this.msgDao.unreadMsg(userId, msgReceiverTypeEnum));
	}

	@Override
	public R<UserMsgBo> userUnread(Long userId) {
		UserMsgBo userMsgBo = new UserMsgBo();
		userMsgBo.setNoticeUnreadMsgCount(Optional.ofNullable(msgDao.getNoticeUnreadMsgCount(userId)).orElse(0));
		userMsgBo.setSystemUnreadMsgCount(Optional.ofNullable(msgDao.getSystemUnreadMsgCount(userId)).orElse(0));
		userMsgBo.setTotalUnreadMsgCount(userMsgBo.getNoticeUnreadMsgCount() + userMsgBo.getSystemUnreadMsgCount());
		return R.ok(userMsgBo);
	}

	@Override
	public Boolean sendToAllAdmin(String content, Long detailId, MsgSendTypeEnum msgSendTypeEnum) {
		R<List<Long>> adminUserIdsR = adminUserApi.queryUserIdsByMenuName("站内信");
		if (adminUserIdsR.getSuccess() && adminUserIdsR.getData().size() > 0) {
			List<Long> adminUserIds = adminUserIdsR.getData();
			MsgDTO msgDTO = new MsgDTO();
			msgDTO.setContent(content);
			msgDTO.setDetailId(detailId);
			msgDTO.setStatus(0);
			msgDTO.setDeleteStatus(0);
			msgDTO.setGlobalFlag(Boolean.TRUE);
			msgDTO.setRecDate(DateUtil.date());
			msgDTO.setMsgSendTypeEnum(msgSendTypeEnum);
			Msg msg = msgConverter.from(msgDTO);
			msg.setType(msgSendTypeEnum.getValue());
			msg.setTitle(msgDTO.getMsgSendTypeEnum().getName());
			Long msgId = msgDao.saveSystemMessages(msg, adminUserIds.toArray(new Long[adminUserIds.size()]), MsgReceiverTypeEnum.ADMIN_USER.value());
			if (ObjectUtil.isEmpty(msgId)) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public Integer getUnAllReadNum(Long id) {
		return msgDao.getUnAllReadNum(id);
	}

	@Override
	public Integer getAdminUnreadNum(Long id) {
		return msgDao.getAdminUnreadNum(id);
	}

	@Override
	public void cleanUnread(long userId, Integer userType) {
		msgDao.cleanUnread(userId, userType);
	}

}
