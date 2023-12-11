/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.MsgSendDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.dto.SysMsgReceiverDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendParamEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.rabbitmq.producer.MessageProducerService;
import com.legendshop.product.bo.ProductArrivalNoticeBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dao.ProductArrivalNoticeDao;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dao.SkuDao;
import com.legendshop.product.dto.ProductArrivalNoticeDTO;
import com.legendshop.product.entity.Product;
import com.legendshop.product.entity.ProductArrivalNotice;
import com.legendshop.product.entity.Sku;
import com.legendshop.product.query.ProductArrivalNoticeQuery;
import com.legendshop.product.service.ProductArrivalNoticeService;
import com.legendshop.product.service.convert.ProductArrivalNoticeConverter;
import com.legendshop.user.api.OrdinaryUserApi;
import com.legendshop.user.dto.OrdinaryUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品到货通知
 *
 * @author legendshop
 */
@Service
public class ProductArrivalNoticeServiceImpl implements ProductArrivalNoticeService {

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private OrdinaryUserApi ordinaryUserApi;

	@Autowired
	private ProductArrivalNoticeDao arrivalNoticeDao;

	@Autowired
	private ProductArrivalNoticeConverter converter;

	@Autowired
	private MessageProducerService messageProducerService;

	@Autowired
	private ProductArrivalNoticeDao productArrivalNoticeDao;

	@Override
	public PageSupport<ProductArrivalNoticeBO> getSelectArrival(ProductArrivalNoticeQuery productArrivalNoticeQuery) {
		return productArrivalNoticeDao.getSelectArrival(productArrivalNoticeQuery);
	}

	@Override
	public PageSupport<SkuBO> productPage(ProductArrivalNoticeQuery query) {
		return productArrivalNoticeDao.productPage(query);
	}

	@Override
	public void noticeUser(List<SkuBO> arrivalSkuIdList) {
		List<ProductArrivalNotice> arrivalNoticeList = productArrivalNoticeDao.queryBySkuId(arrivalSkuIdList.stream()
				.map(SkuBO::getId).distinct().collect(Collectors.toList()));
		//根据skuId进行分组
		Map<Long, List<ProductArrivalNotice>> skuMap = arrivalNoticeList.stream().collect(Collectors.groupingBy(ProductArrivalNotice::getSkuId));
		//获取所有的用户，查询并封装成map
		List<Long> userIds = arrivalNoticeList.stream().map(ProductArrivalNotice::getUserId).distinct().collect(Collectors.toList());
		if (CollUtil.isNotEmpty(userIds)) {
			R<List<OrdinaryUserDTO>> userListResult = ordinaryUserApi.getByIds(userIds);
			if (!userListResult.getSuccess()) {
				throw new BusinessException(userListResult.getMsg());
			}
			List<OrdinaryUserDTO> userList = userListResult.getData();
			Map<Long, List<OrdinaryUserDTO>> userMap = userList.stream().collect(Collectors.groupingBy(OrdinaryUserDTO::getId));

			List<MsgSendDTO> msgSendList = new ArrayList<>();
			arrivalSkuIdList.forEach(sku -> {
				if (skuMap.get(sku.getId()) != null) {
					MsgSendDTO msgSendDTO = new MsgSendDTO();
					MsgSendParamDTO msgSendParamDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, sku.getProductName() + (ObjectUtil.isNotEmpty(sku.getCnProperties()) ? "【" + sku.getCnProperties() + "】" : ""), "black");
					List<MsgSendParamDTO> MsgSendParamDTOS = new ArrayList<>();
					MsgSendParamDTOS.add(msgSendParamDTO);
					//设置消息模板
					msgSendDTO.setMsgSendParamDTOList(MsgSendParamDTOS);
					msgSendDTO.setMsgSendType(MsgSendTypeEnum.PROD_ARRIVAL);
					msgSendDTO.setDetailId(sku.getId());
					//推送方式
					msgSendDTO.setSysParamNameEnum(SysParamNameEnum.PROD_ARRIVAL);
					List<Long> userIdList = skuMap.get(sku.getId()).stream().map(ProductArrivalNotice::getUserId).collect(Collectors.toList());
					List<SysMsgReceiverDTO> sysMsgReceiverDTOS = new ArrayList<>();
					SysMsgReceiverDTO sysMsgReceiverDTO = new SysMsgReceiverDTO();
					Long[] userIdsList = new Long[userIdList.size()];
					sysMsgReceiverDTO.setReceiveUserIds(userIdList.toArray(userIdsList));
					sysMsgReceiverDTO.setReceiverType(MsgReceiverTypeEnum.ORDINARY_USER.value());
					sysMsgReceiverDTOS.add(sysMsgReceiverDTO);
					msgSendDTO.setSysMsgReceiverDTOS(sysMsgReceiverDTOS);
					msgSendDTO.setReceiveUserPhoneNumbers(userIdList.stream().map(skuId -> userMap.get(skuId).get(0).getMobile()).collect(Collectors.toList()));
					msgSendList.add(msgSendDTO);
				}
			});
			messageProducerService.sendMessage(JSONUtil.toJsonStr(msgSendList));

			//修改为已发状态
			List<Long> needUpdateIds = arrivalNoticeList.stream().map(ProductArrivalNotice::getId).collect(Collectors.toList());
			productArrivalNoticeDao.updateStatusByIds(needUpdateIds, 1);
		}
	}


	@Override
	public R<Long> saveProdArriInfo(ProductArrivalNoticeDTO productArrivalNoticeDTO) {
		ProductArrivalNotice arrivalNotice = productArrivalNoticeDao.getAlreadySaveUser(productArrivalNoticeDTO.getUserId(),
				productArrivalNoticeDTO.getSkuId(), 0);
		if (ObjectUtil.isNotEmpty(arrivalNotice)) {
			return R.fail("已经提交了到货通知，请勿重复提交！");
		}
		Sku sku = skuDao.getById(productArrivalNoticeDTO.getSkuId());
		Product product = productDao.getById(sku.getProductId());
		productArrivalNoticeDTO.setShopId(product.getShopId());
		productArrivalNoticeDTO.setProductId(product.getId());
		//Email 为空，没有用户邮箱数据
		productArrivalNoticeDTO.setStatus(0);
		productArrivalNoticeDTO.setCreateTime(new Date());
		return R.ok(arrivalNoticeDao.save(converter.from(productArrivalNoticeDTO)));
	}
}
