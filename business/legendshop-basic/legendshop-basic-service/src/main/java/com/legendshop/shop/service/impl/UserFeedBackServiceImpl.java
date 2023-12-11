/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendParamEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dao.UserFeedBackDao;
import com.legendshop.shop.dto.UserFeedbackBatchHandleDTO;
import com.legendshop.shop.dto.UserFeedbackDTO;
import com.legendshop.shop.entity.UserFeedBack;
import com.legendshop.shop.query.UserFeedBackQuery;
import com.legendshop.shop.service.UserFeedBackService;
import com.legendshop.shop.service.convert.UserFeedbackConverter;
import com.legendshop.user.api.OrdinaryUserApi;
import com.legendshop.user.dto.OrdinaryUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户反馈服务.
 *
 * @author legendshop
 */
@Service
public class UserFeedBackServiceImpl implements UserFeedBackService {

	@Autowired
	private UserFeedBackDao userFeedBackDao;

	@Autowired
	private UserFeedbackConverter userFeedbackConverter;

	@Autowired
	private OrdinaryUserApi ordinaryUserApi;

	@Autowired
	private MessageApi messagePushClient;

	@Override
	public UserFeedbackDTO getById(Long id) {
		return userFeedbackConverter.to(userFeedBackDao.getById(id));
	}

	@Override
	public void deleteById(Long id) {
		userFeedBackDao.deleteById(id);
	}

	@Override
	public Long save(UserFeedbackDTO userFeedBack) {
		return userFeedBackDao.save(userFeedbackConverter.from(userFeedBack));
	}

	@Override
	public void update(UserFeedbackDTO userFeedBack) {
		userFeedBackDao.update(userFeedbackConverter.from(userFeedBack));
	}

	@Override
	public PageSupport<UserFeedbackDTO> getUserFeedBackPage(UserFeedBackQuery userFeedBackQuery) {
		return userFeedbackConverter.page(userFeedBackDao.getUserFeedBackPage(userFeedBackQuery));
	}

	/**
	 * 反馈意见分页查询
	 *
	 * @param userFeedBackQuery
	 * @return
	 */
	@Override
	public PageSupport<UserFeedbackDTO> page(UserFeedBackQuery userFeedBackQuery) {
		return userFeedbackConverter.page(userFeedBackDao.page(userFeedBackQuery));
	}

	/**
	 * 回复反馈意见
	 *
	 * @param userFeedbackDTO
	 * @return
	 */
	@Override
	@Transactional
	public int updateReply(UserFeedbackDTO userFeedbackDTO) {
		userFeedbackDTO.setRespDate(new Date());
		int updateReply = userFeedBackDao.updateReply(userFeedbackDTO);
		// 推送反馈信息给用户
		List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
		//替换参数内容
		MsgSendParamDTO feedbackContentDTO = new MsgSendParamDTO(MsgSendParamEnum.FEEDBACK_CONTENT, userFeedbackDTO.getContent(), "black");
		MsgSendParamDTO feedbackReplyContentDTO = new MsgSendParamDTO(MsgSendParamEnum.FEEDBACK_REPLY_CONTENT, userFeedbackDTO.getRespContent(), "black");
		msgSendParamDTOS.add(feedbackContentDTO);
		msgSendParamDTOS.add(feedbackReplyContentDTO);
		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
				.setReceiveIdArr(new Long[]{userFeedbackDTO.getUserId()})
				.setMsgSendType(MsgSendTypeEnum.FEEDBACK_REPLY)
				.setSysParamNameEnum(SysParamNameEnum.FEEDBACK_REPLY)
				.setMsgSendParamDTOList(msgSendParamDTOS)
		);
		return updateReply;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveUserFeedback(UserFeedbackDTO userFeedbackDTO) {
		//如果用户已登录，则自动获取手机号和姓名
		if (ObjectUtil.isNotEmpty(userFeedbackDTO.getUserId()) && userFeedbackDTO.getUserId() != 0) {
			R<OrdinaryUserDTO> ordinaryUserDTOResult = ordinaryUserApi.getById(userFeedbackDTO.getUserId());
			OrdinaryUserDTO ordinaryUserDTO = new OrdinaryUserDTO();
			if (ordinaryUserDTOResult.getSuccess()) {
				ordinaryUserDTO = ordinaryUserDTOResult.getData();
			}
			userFeedbackDTO.setName(ordinaryUserDTO.getUsername());
			userFeedbackDTO.setMobile(ordinaryUserDTO.getMobile());
		}
		userFeedbackDTO.setStatus(0);
		userFeedbackDTO.setCreateTime(DateUtil.date());
		return userFeedBackDao.save(userFeedbackConverter.from(userFeedbackDTO));
	}

	@Override
	@Transactional
	public boolean updateListById(UserFeedbackBatchHandleDTO userFeedbackBatchHandleDTO) {

		Date date = new Date();
		if (ObjectUtil.isNotEmpty(userFeedbackBatchHandleDTO.getIds())) {
			userFeedbackBatchHandleDTO.getIds().forEach(e -> {
				UserFeedBack userFeedBack = userFeedBackDao.getById(e);
				userFeedBack.setRespDate(date);
				userFeedBack.setStatus(1);
				userFeedBack.setRespMgntId(userFeedbackBatchHandleDTO.getRespUserId());
				userFeedBack.setRespContent(userFeedbackBatchHandleDTO.getRespContent());
				userFeedBackDao.update(userFeedBack);
				// 推送反馈信息给用户
				List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
				//替换参数内容
				MsgSendParamDTO feedbackContentDTO = new MsgSendParamDTO(MsgSendParamEnum.FEEDBACK_CONTENT, userFeedBack.getContent(), "black");
				MsgSendParamDTO feedbackReplyContentDTO = new MsgSendParamDTO(MsgSendParamEnum.FEEDBACK_REPLY_CONTENT, userFeedBack.getRespContent(), "black");
				msgSendParamDTOS.add(feedbackContentDTO);
				msgSendParamDTOS.add(feedbackReplyContentDTO);
				messagePushClient.push(new MessagePushDTO()
						.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
						.setReceiveIdArr(new Long[]{userFeedBack.getUserId()})
						.setMsgSendType(MsgSendTypeEnum.FEEDBACK_REPLY)
						.setSysParamNameEnum(SysParamNameEnum.FEEDBACK_REPLY)
						.setMsgSendParamDTOList(msgSendParamDTOS)
				);

			});
			return true;
		}
		return false;
	}
}
