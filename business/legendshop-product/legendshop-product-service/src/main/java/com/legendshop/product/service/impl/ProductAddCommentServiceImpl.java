/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.OrderSettingDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.order.api.OrderItemApi;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.product.dao.ProductAddCommentDao;
import com.legendshop.product.dao.ProductCommentDao;
import com.legendshop.product.dto.ProductAddCommentDTO;
import com.legendshop.product.entity.ProductAddComment;
import com.legendshop.product.entity.ProductComment;
import com.legendshop.product.enums.ProductCommStatusEnum;
import com.legendshop.product.service.ProductAddCommentService;
import com.legendshop.product.service.ProductCommentService;
import com.legendshop.product.service.convert.ProductAddCommentConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 商品二次评论服务
 *
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class ProductAddCommentServiceImpl extends BaseServiceImpl<ProductAddCommentDTO, ProductAddCommentDao, ProductAddCommentConverter> implements ProductAddCommentService {

	final ProductAddCommentDao productAddCommentDao;

	final ProductCommentDao productCommentDao;

	final ProductAddCommentConverter productAddCommentConverter;

	final ProductCommentService productCommentService;

	final MessageApi messagePushClient;

	final SysParamsApi sysParamsApi;

	final OrderItemApi orderItemApi;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addProdComm(ProductAddCommentDTO productAddCommentDTO) {

		// 查询用户是否已追评 false 已追评 true未追评
		boolean canAdd = productCommentService.isCanAddComment(productAddCommentDTO.getProductCommentId(), productAddCommentDTO.getUserId());
		//查看追评之前的追评内容
		ProductAddComment productAddComment = productAddCommentDao.getByProductCommentId(productAddCommentDTO.getProductCommentId());
		if (!canAdd && ObjectUtil.isNotEmpty(productAddComment) && productAddComment.getStatus().equals(1)) {
			throw new BusinessException("您已追评,不可重复追评~");
		}
		//判断图片数量
		if (ObjectUtil.isNotEmpty(productAddCommentDTO.getPhotos())) {
			List<String> photoList = JSONUtil.toList(JSONUtil.parseArray(productAddCommentDTO.getPhotos()), String.class);
			if (photoList.size() > 5) {
				throw new BusinessException("评论图片不能超过5张");
			}
		}

		//判断视频数量
		if (ObjectUtil.isNotEmpty(productAddCommentDTO.getVideo())) {
			List<String> videoList = JSONUtil.toList(JSONUtil.parseArray(productAddCommentDTO.getVideo()), String.class);
			if (videoList.size() > 5) {
				throw new BusinessException("评论图片不能超过5张");
			}
		}
		// 获取订单评论的系统配置
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
		Boolean commentNeedReview = Optional.ofNullable(orderSetting).map(OrderSettingDTO::getCommentNeedReview).orElse(true);
		Integer status = ProductCommStatusEnum.WAIT_AUDIT.value();

		// 处理评论内容
		String html = productAddCommentDTO.getContent().trim();
		StringBuilder nicksb = new StringBuilder();
		int l = html.length();
		for (int i = 0; i < l; i++) {
			char _s = html.charAt(i);
			if (isEmojiCharacter(_s)) {
				nicksb.append(_s);
			}
		}
		productAddCommentDTO.setContent(nicksb.toString());
		productAddCommentDTO.setStatus(status);
		productAddCommentDTO.setCreateTime(DateUtil.date());
		productAddCommentDTO.setReplyFlag(Boolean.FALSE);
		productAddCommentDTO.setDeleteType(0);
		if (!commentNeedReview) {
			productAddCommentDTO.setAuditTime(DateUtil.date());
		}

		//判断追评是否存在
		if (ObjectUtil.isNotEmpty(productAddComment)) {
			//如果追平存在修改追评 目前只支持追评一次
			productAddCommentDTO.setId(productAddComment.getId());
			int update = productAddCommentDao.update(productAddCommentConverter.from(productAddCommentDTO));
			if (update > 0) {
				return true;
			}
		} else {
			//如果追评不存在新增追评
			Long id = productAddCommentDao.save(productAddCommentConverter.from(productAddCommentDTO));
			if (id > 0) {
				int update = productCommentDao.updateAddCommFlag(true, productAddCommentDTO.getProductCommentId());
				//如果不是审核拒绝不可以新增追评
				if (update <= 0 && productAddComment.getStatus() != -1) {
					throw new BusinessException("跟新追评失败");
				}
				if (!commentNeedReview) {
					this.auditAddComment(id, ProductCommStatusEnum.SUCCESS.value());
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean auditAddComment(Long addId, Integer status) {
		ProductAddComment productAddComment = productAddCommentDao.getById(addId);
		if (ObjectUtil.isEmpty(productAddComment)) {
			throw new BusinessException("审核失败，该追评已不存在");
		}
		ProductComment productComment = productCommentDao.getById(productAddComment.getProductCommentId());
		if (ObjectUtil.isEmpty(productComment)) {
			throw new BusinessException("审核失败，该评论已不存在");
		}
		if (!ProductCommStatusEnum.WAIT_AUDIT.value().equals(productAddComment.getStatus())) {
			throw new BusinessException("请匆重复审核！");
		}
		productAddComment.setStatus(status);
		productAddComment.setAuditTime(new Date());
		if (productAddCommentDao.update(productAddComment) > 0) {
			R<OrderItemDTO> itemDTOR = orderItemApi.getById(productComment.getOrderItemId());

			//发送审核站内信给用户
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
					.setReceiveIdArr(new Long[]{productComment.getUserId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_COMMENT)
					.setDetailId(itemDTOR.getData().getOrderId())
					.setSysParamNameEnum(SysParamNameEnum.ORDER_COMMENT_AUDIT_USER)
			);
			if (ProductCommStatusEnum.SUCCESS.value().equals(status)) {
				//发送审核站内信给商家
				messagePushClient.push(new MessagePushDTO()
						.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
						.setReceiveIdArr(new Long[]{productComment.getShopId()})
						.setMsgSendType(MsgSendTypeEnum.ORDER_COMMENT)
						.setDetailId(itemDTOR.getData().getOrderId())
						.setSysParamNameEnum(SysParamNameEnum.ORDER_COMMENT_AUDIT_SHOP)
				);
			}
		}
		return productAddCommentDao.update(productAddComment) > 0;
	}


	@Override
	public boolean replyAdd(Long addId, String content, Long shopId) {
		ProductAddComment productAddComment = productAddCommentDao.getById(addId);
		if (ObjectUtil.isEmpty(productAddComment)) {
			throw new BusinessException("回复失败，该追评已不存在");
		}
		if (productAddComment.getReplyFlag()) {
			throw new BusinessException("该评论已回复，不能重复回复");
		}
		ProductComment productComment = productCommentDao.getByProperties(new EntityCriterion()
				.eq("id", productAddComment.getProductCommentId()).eq("shopId", shopId));
		if (ObjectUtil.isEmpty(productComment)) {
			throw new BusinessException("回复失败，该评论已不存在");
		}
		// 处理评论内容
		String html = content.trim();
		StringBuilder nickSb = new StringBuilder();
		int l = html.length();
		for (int i = 0; i < l; i++) {
			char _s = html.charAt(i);
			if (isEmojiCharacter(_s)) {
				nickSb.append(_s);
			}
		}
		productAddComment.setShopReplyContent(content);
		productAddComment.setReplyFlag(Boolean.TRUE);
		productAddComment.setShopReplyTime(DateUtil.date());
		boolean flag = productAddCommentDao.update(productAddComment) > 0;
		if (flag) {
			R<OrderItemDTO> itemDTOR = orderItemApi.getById(productComment.getOrderItemId());
			// 发送评论回复通知站内信给用户
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
					.setReceiveIdArr(new Long[]{productComment.getUserId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_COMMENT)
					.setDetailId(itemDTOR.getData().getOrderId())
					.setSysParamNameEnum(SysParamNameEnum.ORDER_COMMENT_REPLY)
			);
		}
		return flag;
	}

	@Override
	public ProductAddCommentDTO getProductCommentById(Long id) {
		return productAddCommentDao.getAddComment(id);
	}


	/**
	 * 是否是表情符号
	 *
	 * @param codePoint
	 * @return
	 */
	public boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}
}
