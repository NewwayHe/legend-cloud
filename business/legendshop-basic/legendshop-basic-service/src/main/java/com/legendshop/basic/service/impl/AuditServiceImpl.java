/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.bo.PendingMattersAdminBO;
import com.legendshop.basic.dao.AuditDao;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.entity.Audit;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.basic.query.AuditQuery;
import com.legendshop.basic.service.AuditService;
import com.legendshop.basic.service.convert.AuditConverter;
import com.legendshop.common.core.constant.R;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class AuditServiceImpl implements AuditService {

	private final AuditDao auditDao;
	private final AuditConverter auditConverter;

	@Override
	public R audit(AuditDTO auditDTO) {
		auditDao.save(auditConverter.from(auditDTO));
		return R.ok();
	}

	@Override
	public R audit(List<AuditDTO> auditDTOList) {
		auditDao.save(auditConverter.from(auditDTOList));
		return R.ok();
	}

	@Override
	public String getAuditOptionByShopId(Long shopId) {
		return auditDao.getAuditOptionByShopId(shopId);
	}

	@Override
	public AuditDTO getByAuditTypeAndCommonId(Long commonId, AuditTypeEnum auditTypeEnum) {
		return auditDao.getByAuditTypeAndCommonId(commonId, auditTypeEnum);
	}

	@Override
	public Long save(AuditDTO auditDTO) {
		return this.auditDao.save(this.auditConverter.from(auditDTO));
	}

	@Override
	public PageSupport<AuditDTO> page(AuditQuery auditQuery) {
		return this.auditConverter.page(this.auditDao.page(auditQuery));
	}

	@Override
	public R batchSave(AuditDTO auditDTO) {
		List<Audit> list = new ArrayList<>();
		for (Long id : auditDTO.getIdList()) {
			Audit audit = this.auditConverter.from(auditDTO);
			audit.setCommonId(id);
			list.add(audit);
		}
		return R.ok(this.auditDao.save(list));
	}

	@Override
	public List<AuditDTO> getAuditDtoListByAuditTypeAndCommonId(Long commonId, Integer auditType) {
		return auditDao.getAuditDtoListByAuditTypeAndCommonId(commonId, auditType);
	}

	@Override
	public List<AuditDTO> checkWalletDetails(List<Long> commonId) {
		return auditDao.getAuditDtoListByCommonId(commonId, AuditTypeEnum.WITHDRAWAL.getValue());
	}

	@Override
	public PendingMattersAdminBO getPending() {

		return PendingMattersAdminBO
				.builder()
				.AuditedShopCount(auditDao.getAuditedShopCount())
				.AuditedBrandCount(auditDao.getAuditedBrandCount())
				.AuditedProductCount(auditDao.getAuditedProductCount())
				.AuditedReportProductCount(auditDao.getAuditedReportProductCount())
				.AuditedProductCommentCount(auditDao.getAuditedProductCommentCount())
				.AuditedAfterSaleCount(auditDao.getAuditedAfterSaleCount())
				.AuditedBillingCount(auditDao.getAuditedBillingCount())
				.AuditedBalanceWithdrawCount(auditDao.getAuditedBalanceWithdrawCount())
				.AuditedFeedbackCount(auditDao.getAuditedFeedbackCount())
				.build();
	}

	@Override
	public List<AuditDTO> queryNewByAuditTypeAndCommonId(List<Long> commonId, AuditTypeEnum auditTypeEnum) {
		return auditDao.queryNewByAuditTypeAndCommonId(commonId, auditTypeEnum.getValue());
	}
}
