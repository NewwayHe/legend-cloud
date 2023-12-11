/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.product.bo.AccusationBO;
import com.legendshop.product.bo.IllegalProductBO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.dao.AccusationDao;
import com.legendshop.product.dao.AccusationTypeDao;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dto.AccusationDTO;
import com.legendshop.product.dto.AccusationUserTypeDTO;
import com.legendshop.product.dto.AccusationbatchHandleDTO;
import com.legendshop.product.entity.Accusation;
import com.legendshop.product.entity.AccusationType;
import com.legendshop.product.enums.AccusationEnum;
import com.legendshop.product.enums.AccusationIllegalOffEnum;
import com.legendshop.product.enums.AccusationResultEnum;
import com.legendshop.product.excel.IllegalExportDTO;
import com.legendshop.product.query.AccusationQuery;
import com.legendshop.product.query.AccusationReportQuery;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.AccusationService;
import com.legendshop.product.service.ProductService;
import com.legendshop.product.service.convert.AccusationConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 举报服务实现类.
 *
 * @author legendshop
 */
@Service
public class AccusationServiceImpl extends BaseServiceImpl<AccusationDTO, AccusationDao, AccusationConverter> implements AccusationService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private AccusationDao accusationDao;

	@Autowired
	private AccusationTypeDao accusationTypeDao;

	@Autowired
	private AccusationConverter accusationConverter;

	/**
	 * 商品服务
	 */
	@Autowired
	private ProductService productService;

	@Override
	public AccusationBO getAccusation(Long id) {
		AccusationBO accusation = accusationDao.getAccusation(id);
		if (ObjectUtil.isEmpty(accusation)) {
			return accusation;
		}
		if (StrUtil.isNotBlank(accusation.getPic())) {
			String pic = accusation.getPic();
			List<String> imgList = Arrays.asList(StringUtils.split(pic, ","));
			accusation.setPicList(imgList);
		}
		if (accusation.getStatus().equals(0)) {
			accusation.setIllegalOff(null);
		}
		return accusation;
	}

	@Override
	public PageSupport<AccusationBO> queryMyAccusation(AccusationQuery accusationQuery) {
		return accusationDao.queryMyAccusation(accusationQuery);
	}

	@Override
	public PageSupport<AccusationBO> queryAccusation(AccusationReportQuery accusationQuery) {
		return accusationDao.queryAccusation(accusationQuery);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R batchHandle(AccusationbatchHandleDTO accusationDTO) {
		Date date = new Date();
		if (accusationDTO.getAccusationIds().size() > 0) {
			List<Long> illegalIdList = new ArrayList<>();
			List<Accusation> accusations = accusationDao.queryAllByIds(accusationDTO.getAccusationIds());
			if (accusations.stream().anyMatch(e -> AccusationEnum.STATUS_YES.getValue().equals(e.getStatus()))) {
				return R.fail("存在已处理的商品举报，请勿重复处理！");
			}

			accusations.forEach(e -> {
				e.setHandleInfo(accusationDTO.getHandleInfo());
				e.setHandleTime(date);
				e.setUpdateTime(date);
				e.setResult(accusationDTO.getResult());
				e.setStatus(AccusationEnum.STATUS_YES.getValue());//状态为已处理
				e.setIllegalOff(accusationDTO.getIllegalOff());
				// 商品处理
				if (!AccusationIllegalOffEnum.NONE.getValue().equals(accusationDTO.getIllegalOff())) {
					//处理商品违规下架并更新索引
					illegalIdList.add(e.getProductId());
				}
			});
			if (CollUtil.isNotEmpty(illegalIdList)) {
				AuditDTO auditDTO = new AuditDTO();
				auditDTO.setAuditUsername(accusationDTO.getHandler());
				auditDTO.setAuditTime(DateUtil.date());
				auditDTO.setOpStatus(OpStatusEnum.DENY.getValue());
				auditDTO.setAuditType(AuditTypeEnum.PRODUCT.getValue());
				auditDTO.setAuditOpinion(accusationDTO.getHandleInfo());
				auditDTO.setIdList(accusationDTO.getAccusationIds());
				productService.illegalOffProduct(illegalIdList, accusationDTO.getIllegalOff(), auditDTO);

			}
			return R.ok(accusationDao.update(accusations));
		}
		return R.fail("批量处理举报失败");
	}

	@Override
	public PageSupport<AccusationBO> shopPage(AccusationQuery query) {
		return accusationDao.shopPage(query);
	}

	@Override
	public PageSupport<IllegalProductBO> illegalPage(ProductQuery query) {
		return accusationDao.illegalPage(query);
	}

	@Override
	public List<IllegalExportDTO> findIllegalExport(ProductQuery query) {
		List<ProductBO> productPage = productDao.getProductList(query);
		List<IllegalExportDTO> illegalList = new ArrayList<>();
		List<IllegalExportDTO> resultList = accusationConverter.convert2IllegalExportDTO(productPage);
		if (CollUtil.isNotEmpty(resultList)) {
			for (IllegalExportDTO illegalExportDTO : resultList) {
				if (OpStatusEnum.PROD_ILLEGAL_LOCK.getValue().equals(illegalExportDTO.getOpStatus())) {
					illegalExportDTO.setOpStatusDesc("违规锁定");
				}
				if (OpStatusEnum.PROD_ILLEGAL_OFF.getValue().equals(illegalExportDTO.getOpStatus())) {
					illegalExportDTO.setOpStatusDesc("违规下架");
				}
				illegalList.add(illegalExportDTO);
			}

		} else {
			illegalList = Collections.singletonList(new IllegalExportDTO());
		}

		return illegalList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R saveAccusationAndOffLine(AccusationDTO accusationDTO, AccusationUserTypeDTO userTypeDTO) {
		List<Accusation> accusationList = new ArrayList<>();
		List<Long> illegalIdList = new ArrayList<>();
		Date date = new Date();
		for (Long productId : accusationDTO.getIds()) {
			Accusation accusation = new Accusation();
			BeanUtil.copyProperties(accusationDTO, accusation, new CopyOptions().ignoreNullValue());
			accusation.setUserId(userTypeDTO.getUserId());
			accusation.setUserType(userTypeDTO.getUserType());
			accusation.setUserName(userTypeDTO.getUserName());
			accusation.setProductId(productId);
			accusation.setContent(accusationDTO.getHandleInfo());
			accusation.setStatus(AccusationEnum.STATUS_YES.value());
			accusation.setResult(AccusationResultEnum.VALID.value());
			accusation.setIllegalOff(accusationDTO.getIllegalOff());
			accusation.setCreateTime(date);
			accusation.setUpdateTime(date);
			accusation.setHandleTime(date);
			AccusationType accusationType = accusationTypeDao.getById(accusationDTO.getTypeId());
			if (accusationType == null) {
				return R.fail("举报类型错误");
			}
			accusation.setTypeName(accusationType.getName());
			accusation.setUserDelStatus(AccusationEnum.DEL_STATUS_NO.value());
			illegalIdList.add(accusation.getProductId());
			accusationList.add(accusation);
		}
		//产品确认，只有PC端能举报，平台和商家只能下架商品，违规下架和违规锁定不算举报
		if (!AccusationIllegalOffEnum.NONE.getValue().equals(accusationDTO.getIllegalOff())) {

			AuditDTO auditDTO = new AuditDTO();
			auditDTO.setAuditUsername(userTypeDTO.getUserName());
			auditDTO.setAuditTime(DateUtil.date());
			auditDTO.setOpStatus(OpStatusEnum.DENY.getValue());
			auditDTO.setAuditType(AuditTypeEnum.PRODUCT.getValue());
			auditDTO.setAuditOpinion(accusationDTO.getHandleInfo());
			auditDTO.setIdList(accusationDTO.getIds());

			/*下架商品*/
			productService.illegalOffProduct(illegalIdList, accusationDTO.getIllegalOff(), auditDTO);
		}
		return R.ok();
	}

	@Override
	public R saveAccusation(AccusationDTO accusationDTO, AccusationUserTypeDTO userTypeDTO) {
		if (StrUtil.isBlank(accusationDTO.getTypeName())) {
			return R.fail("举报类型名称不能为空");
		}
		Accusation accusation = accusationConverter.fromDTO(accusationDTO, userTypeDTO);
		if (CollUtil.isNotEmpty(accusationDTO.getPicList())) {
			StringBuffer stringBuffer = new StringBuffer();
			List<String> picList = accusationDTO.getPicList();
			for (String s : picList) {
				stringBuffer.append(s);
				stringBuffer.append(",");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			accusation.setPic(stringBuffer.toString());
		}

		return R.ok(accusationDao.save(accusation));
	}
}
