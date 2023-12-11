/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

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
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.dao.ProductConsultDao;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dto.ProductConsultDTO;
import com.legendshop.product.entity.ProductConsult;
import com.legendshop.product.query.ProductConsultQuery;
import com.legendshop.product.service.ProductConsultService;
import com.legendshop.product.service.convert.ProductConsultConvert;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.user.api.UserDetailApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Service
public class ProductConsultServiceImpl implements ProductConsultService {

	@Autowired
	private ProductConsultDao productConsultDao;

	@Autowired
	private ProductConsultConvert productConsultConvert;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private UserDetailApi userDetailApi;
	@Autowired
	private ShopDetailApi shopDetailApi;
	@Autowired
	private MessageApi messagePushClient;


	@Override
	public R<Long> addConsult(ProductConsultDTO productConsultDTO) {
		productConsultDTO.setRecDate(new Date());
		productConsultDTO.setReplySts(0);
		productConsultDTO.setDelSts(0);
		productConsultDTO.setStatus(1);
		productConsultDTO.setPointType(1);
		ProductConsult productConsult = productConsultConvert.from(productConsultDTO);

		Long save = productConsultDao.save(productConsult);

		if (save < 1) {
			return R.fail("新增商品咨询失败");
		}
		List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
				.setReceiveIdArr(new Long[]{productConsult.getShopId()})
				.setMsgSendType(MsgSendTypeEnum.PROD_CONSULT_NOTIFY)
				.setSysParamNameEnum(SysParamNameEnum.PROD_CONSULT_NOTIFY_TO_SHOP)
				.setMsgSendParamDTOList(msgSendParamDTOS)
				.setDetailId(productConsult.getId())
		);
		return R.ok();
	}

	@Override
	public PageSupport<ProductConsultDTO> getUserProductConsultPage(ProductConsultQuery query) {
		query.setDelSts(0);
		query.setReplySts(1);
		query.setStatus(1);
		return productConsultDao.queryUserProductConsultPage(query);
	}


	@Override
	public R<Integer> offlineById(Long id, Integer status) {
		if (status != 0 && status != 1) {
			return R.fail("参数不正确！");
		}

		ProductConsult productConsult = productConsultDao.getById(id);
		if (null == productConsult) {
			return R.fail("该商品咨询不存在！");
		}

		productConsult.setStatus(status);
		int update = productConsultDao.update(productConsult);

		if (update <= 0) {
			return R.fail("更新失败！");
		}

		return R.ok();
	}

	@Override
	public R<Integer> deleteById(Long id) {
		ProductConsult productConsult = productConsultDao.getById(id);
		if (productConsult == null) {
			return R.fail("该商品咨询不存在！");
		}

		productConsult.setDelSts(1);
		int update = productConsultDao.update(productConsult);
		if (update > 0) {
			sendMessagePushClient(productConsult, "删除");
			return R.ok();
		}
		return R.fail("删除失败");

	}


	@Override
	public R<Integer> replyById(Long id, String content) {
		if (ObjectUtil.isEmpty(content)) {
			return R.fail("咨询回复内容不能为空");
		}

		ProductConsult productConsult = productConsultDao.getById(id);
		if (null == productConsult) {
			return R.fail("该商品咨询不存在!");
		}

		productConsult.setReplySts(1);
		productConsult.setAnswer(content);
		productConsult.setAnswerTime(new Date());
		productConsult.setReplyName(SecurityUtils.getUserName());
		int update = productConsultDao.update(productConsult);
		if (update > 0) {
			// 通知给用户
			sendMessagePushClient(productConsult, "回复");
			return R.ok();
		}
		return R.fail("回复失败");
	}

	@Override
	public PageSupport<ProductConsultDTO> getProductConsultCenterPage(ProductConsultQuery query) {
		return productConsultDao.queryProductConsultPage(query);
	}

	private void sendMessagePushClient(ProductConsult productConsult, String str) {
		List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
		msgSendParamDTOS.add(new MsgSendParamDTO(MsgSendParamEnum.STATUS, str, "black"));
		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
				.setReceiveIdArr(new Long[]{productConsult.getAskUserId()})
				.setMsgSendType(MsgSendTypeEnum.PROD_CONSULT_NOTIFY)
				.setSysParamNameEnum(SysParamNameEnum.PROD_CONSULT_NOTIFY_TO_USER)
				.setMsgSendParamDTOList(msgSendParamDTOS)
				.setDetailId(productConsult.getId())
		);
	}

}
