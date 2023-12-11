/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.shop.bo.PlateCapitalFlowAmountBO;
import com.legendshop.shop.dao.PlateCapitalFlowDao;
import com.legendshop.shop.dto.PlateCapitalFlowDTO;
import com.legendshop.shop.entity.PlateCapitalFlow;
import com.legendshop.shop.enums.DealTypeEnum;
import com.legendshop.shop.enums.FlowTypeEnum;
import com.legendshop.shop.excel.PlateCapitalFlowExportDTO;
import com.legendshop.shop.query.PlateCapitalFlowQuery;
import com.legendshop.shop.service.PlateCapitalFlowService;
import com.legendshop.shop.service.convert.PlateCapitalFlowConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 平台资金流水(PlateCapitalFlow)表服务实现类
 *
 * @author legendshop
 * @since 2020-09-18 17:26:10
 */
@Service
public class PlateCapitalFlowServiceImpl implements PlateCapitalFlowService {

	@Autowired
	private PlateCapitalFlowDao plateCapitalFlowDao;

	@Autowired
	private PlateCapitalFlowConverter plateCapitalFlowConverter;

	@Override
	public PlateCapitalFlowDTO getById(Long id) {
		PlateCapitalFlow capitalFlow = plateCapitalFlowDao.getById(id);
		return plateCapitalFlowConverter.to(capitalFlow);
	}

	@Override
	public int deleteById(Long id) {
		return plateCapitalFlowDao.deleteById(id);
	}

	@Override
	public Long save(PlateCapitalFlowDTO f) {
		String remark = DealTypeEnum.getRemarks(f.getDealTypeEnum(), f.getAmount(), f.getUserId(), f.getOrderNumber(), f.getFlag());
		f.setDealType(f.getDealTypeEnum().value());
		PlateCapitalFlow capitalFlow = plateCapitalFlowConverter.from(f);
		capitalFlow.setRemark(remark);
		return plateCapitalFlowDao.save(capitalFlow);
	}

	@Override
	public int update(PlateCapitalFlowDTO plateCapitalFlowDTO) {
		return plateCapitalFlowDao.update(plateCapitalFlowConverter.from(plateCapitalFlowDTO));
	}


	@Override
	public PageSupport<PlateCapitalFlowDTO> queryPage(PlateCapitalFlowQuery plateCapitalFlowQuery) {
		PageSupport<PlateCapitalFlowDTO> page = plateCapitalFlowConverter.page(plateCapitalFlowDao.queryPage(plateCapitalFlowQuery));

		if (CollUtil.isNotEmpty(page.getResultList())) {
			//数据渲染
			page.getResultList().forEach(plateCapital -> {
				//支付方式
				plateCapital.setPayType(PayTypeEnum.getDesc(plateCapital.getPayType()));
			});
		}

		return page;
	}


	@Override
	public List<PlateCapitalFlowDTO> queryList(PlateCapitalFlowQuery plateCapitalFlowQuery) {
		return plateCapitalFlowConverter.to(plateCapitalFlowDao.queryList(plateCapitalFlowQuery));
	}

	@Override
	public List<PlateCapitalFlowExportDTO> queryExportList(PlateCapitalFlowQuery plateCapitalFlowQuery) {
		List<PlateCapitalFlowDTO> plateCapitalFlowDtoList = this.queryList(plateCapitalFlowQuery);
		List<PlateCapitalFlowExportDTO> result = plateCapitalFlowConverter.toExportDTO(plateCapitalFlowDtoList);
		result.forEach(e -> {
			e.setDealType(DealTypeEnum.getDes(e.getDealType()));
			e.setFlowType(FlowTypeEnum.getDes(e.getFlowType()));
		});
		return result;
	}

	@Override
	public BigDecimal getSumAmount(String flowType) {
		return plateCapitalFlowDao.getSumAmount(flowType);
	}


	@Override
	public PlateCapitalFlowAmountBO getPlateAmount() {
		PlateCapitalFlowAmountBO bo = new PlateCapitalFlowAmountBO();
		BigDecimal sumIncomeAmount = getSumAmount(FlowTypeEnum.INCOME.getValue());
		BigDecimal sumSpendAmount = getSumAmount(FlowTypeEnum.SPEND.getValue());
		BigDecimal sub = NumberUtil.sub(sumIncomeAmount, sumSpendAmount);
		bo.setSumIncomeAmount(sumIncomeAmount);
		bo.setSumSpendAmount(sumSpendAmount);
		bo.setSumPlateAmount(sub);
		return bo;
	}
}
