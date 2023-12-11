/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.UserInvoiceBO;
import com.legendshop.user.dao.UserInvoiceDao;
import com.legendshop.user.dto.OrderInvoiceDTO;
import com.legendshop.user.dto.OrderItemInvoiceDTO;
import com.legendshop.user.dto.UserInvoiceDTO;
import com.legendshop.user.dto.UserInvoiceDetailDTO;
import com.legendshop.user.entity.UserInvoice;
import com.legendshop.user.query.UserInvoiceQuery;
import com.legendshop.user.service.UserInvoiceService;
import com.legendshop.user.service.convert.UserInvoiceConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 发票服务实现
 *
 * @author legendshop
 */
@Service
public class UserInvoiceServiceImpl implements UserInvoiceService {

	@Autowired
	private UserInvoiceDao userInvoiceDao;

	@Autowired
	private UserInvoiceConverter userInvoiceConverter;

	@Override
	public Long saveInvoice(UserInvoiceDTO invoice) {

		invoice.setCreateTime(new Date());
		Long invoiceId = userInvoiceDao.save(userInvoiceConverter.from(invoice));
		//检查当前保存的发票是否选择设置为默认
		if (invoice.getCommonInvoiceFlag()) {
			this.updateDefaultInvoice(invoiceId, invoice.getUserId());
		}
		return invoiceId;
	}

	@Override
	public Integer updateInvoice(UserInvoiceDTO invoice) {
		invoice.setUpdateTime(new Date());
		int result = userInvoiceDao.update(userInvoiceConverter.from(invoice));
		//检查当前保存的发票是否选择设置为默认
		if (invoice.getCommonInvoiceFlag()) {
			this.updateDefaultInvoice(invoice.getId(), invoice.getUserId());
		}
		return result;
	}

	@Override
	public int updateDefaultInvoice(Long invoiceId, Long userId) {
		return userInvoiceDao.updateDefaultInvoice(invoiceId, userId);
	}

	@Override
	public R deleteById(Long id) {
		UserInvoice userInvoice = userInvoiceDao.getById(id);
		if (ObjectUtil.isNull(userInvoice)) {
			return R.fail("发票不存在或已被删除，刷新后重试");
		}
		int result = userInvoiceDao.deleteById(id);
		if (result <= 0) {
			return R.fail("删除失败");
		}
		return R.ok();
	}

	@Override
	public UserInvoiceDTO getDefaultInvoice(Long userId) {
		return userInvoiceConverter.to(userInvoiceDao.getDefaultInvoice(userId));
	}

	@Override
	public UserInvoiceDTO getById(Long id) {
		return userInvoiceConverter.to(userInvoiceDao.getById(id));
	}


	@Override
	public boolean removeDefaultInvoiceStatus(Long userId) {
		int result = userInvoiceDao.removeDefaultInvoiceStatus(userId);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public PageSupport<UserInvoiceDTO> queryPage(UserInvoiceQuery query) {
		return userInvoiceConverter.page(userInvoiceDao.queryPage(query));
	}

	@Override
	public UserInvoiceBO getUserInvoiceForOrder(Long userId, List<String> invoiceTypeList) {

		UserInvoice userInvoice;
		// 获取默认发票
		userInvoice = userInvoiceDao.getDefaultInvoiceForOrder(userId, invoiceTypeList);
		// 如果没有默认发票，取最新的发票
		if (ObjectUtil.isNull(userInvoice)) {
			List<UserInvoice> userInvoiceForOrder = userInvoiceDao.getUserInvoiceForOrder(userId, invoiceTypeList);
			if (CollectionUtil.isNotEmpty(userInvoiceForOrder)) {
				userInvoice = userInvoiceForOrder.get(0);
			}
		}
		return userInvoiceConverter.toBo(userInvoice);
	}

	@Override
	public UserInvoiceBO getInvoiceBoById(Long invoiceId) {
		return userInvoiceConverter.toBo(userInvoiceDao.getById(invoiceId));
	}

	@Override
	public PageSupport<UserInvoiceBO> queryUserInvoiceOrderById(UserInvoiceQuery query) {
		PageSupport<UserInvoiceBO> pageSupport = userInvoiceDao.queryUserInvoiceOrderById(query);
		List<UserInvoiceBO> resultList = pageSupport.getResultList();
		for (UserInvoiceBO userInvoiceBO : resultList) {
			userInvoiceBO.setOrderProductPics(userInvoiceDao.queryUserInvoiceOrderPics(userInvoiceBO.getOrderId()));
		}
		return pageSupport;
	}

	@Override
	public R<UserInvoiceDetailDTO> getDetail(Long id) {
		UserInvoiceDetailDTO detail = userInvoiceDao.getDetail(id);
		List<Long> longList = Arrays.asList(detail.getProvinceId(), detail.getCityId(), detail.getAreaId(), detail.getStreetId());
		longList.sort(Long::compareTo);
		List<String> address = userInvoiceDao.getAddress(longList);

		StringBuilder sb = new StringBuilder();
		for (String s : address) {
			if (StrUtil.isEmpty(s)) {
				continue;
			}
			sb.append(s);

		}
		sb.append(detail.getAddress());
		detail.setAddress(sb.toString());

		// 新增订单项商品信息
		List<OrderItemInvoiceDTO> orderItem = userInvoiceDao.getOrderItem(detail.getOrderId());
		OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO();
		orderInvoiceDTO.setProductQuantity(detail.getCounts());
		orderInvoiceDTO.setActualTotalPrice(detail.getTotalPrice());
		orderInvoiceDTO.setShopName(detail.getShopName());
		orderInvoiceDTO.setOrderItemList(orderItem);
		detail.setOrderInvoiceDTO(orderInvoiceDTO);
		return R.ok(detail);
	}
}
