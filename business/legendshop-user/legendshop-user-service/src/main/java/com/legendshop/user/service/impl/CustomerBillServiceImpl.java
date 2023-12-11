/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.user.bo.CustomerBillBO;
import com.legendshop.user.dao.CustomerBillDao;
import com.legendshop.user.dto.CustomerBillCreateDTO;
import com.legendshop.user.dto.CustomerBillDTO;
import com.legendshop.user.dto.CustomerBillGroupDTO;
import com.legendshop.user.entity.CustomerBill;
import com.legendshop.user.enums.ModeTypeEnum;
import com.legendshop.user.query.CustomerBillQuery;
import com.legendshop.user.service.CustomerBillService;
import com.legendshop.user.service.convert.CustomerBillConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 客户账单服务
 *
 * @author legendshop
 */
@Slf4j
@Service
public class CustomerBillServiceImpl implements CustomerBillService {

	@Autowired
	private CustomerBillDao customerBillDao;

	@Autowired
	private CustomerBillConverter customerBillConverter;


	@Override
	public PageSupport<CustomerBillDTO> queryPage(CustomerBillQuery expensesRecordQuery) {
		return customerBillConverter.page(customerBillDao.queryPage(expensesRecordQuery));
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateDelFlag(Long id) {
		return customerBillDao.updateDelFlag(id);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public int batchUpdateDelFlag(List<Long> ids) {
		List<CustomerBill> list = customerBillDao.queryAllByIds(ids);
		list.forEach(e -> {
			e.setDelFlag(true);
		});
		return customerBillDao.update(list);
	}

	@Override
	@Async
	public void save(CustomerBillCreateDTO customerBillCreateDTO) {
		Long saveResult = customerBillDao.save(customerBillConverter.toEntity(customerBillCreateDTO));
		if (saveResult <= 0) {
			log.error("异步保存账单失败：保存信息{}", JSONUtil.toJsonStr(customerBillCreateDTO));
		}
	}

	@Override
	@Async
	public void save(List<CustomerBillCreateDTO> customerBillCreateList) {
		List<Long> saveResult = customerBillDao.save(customerBillConverter.toEntity(customerBillCreateList));
		if (CollectionUtils.isEmpty(saveResult)) {
			log.error("异步保存账单失败：保存信息{}", JSONUtil.toJsonStr(customerBillCreateList));
		}
	}


	@Override
	public PageSupport<CustomerBillGroupDTO> queryPageGroupByDate(CustomerBillQuery customerBillQuery) {

		PageSupport<CustomerBillDTO> pageSupport = customerBillConverter.page(this.customerBillDao.queryPage(customerBillQuery));
		if (ObjectUtil.isEmpty(pageSupport) || CollUtil.isEmpty(pageSupport.getResultList())) {
			return null;
		}
		PageSupport<CustomerBillGroupDTO> ps = new PageSupport<>();
		ps.setCurPageNO(pageSupport.getCurPageNO());
		ps.setPageSize(pageSupport.getPageSize());
		ps.setPageCount(pageSupport.getPageCount());
		ps.setOffset(pageSupport.getOffset());
		ps.setTotal(pageSupport.getTotal());
		ps.setResultList(convertResult(pageSupport.getResultList()));
		return ps;
	}

	@Override
	public R<CustomerBillBO> getDetailById(Long id) {

		CustomerBill customerBill = this.customerBillDao.getById(id);
		if (ObjectUtil.isNull(customerBill)) {
			throw new BusinessException("该账单记录已不存在或被删除，请刷新后重试");
		}
		// 获取关联记录
		List<CustomerBillBO> relatedBizOrderList = this.getRelatedBizOrderList(customerBill.getId(), customerBill.getRelatedBizOrderNo());
		CustomerBillBO customerBillBO = customerBillConverter.toBO(customerBill);
		customerBillBO.setRelatedBizOrderList(relatedBizOrderList);
		return R.ok(customerBillBO);
	}

	@Override
	public List<CustomerBillBO> getRelatedBizOrderList(Long id, String relatedBizOrderNo) {
		List<CustomerBill> customerBillList = this.customerBillDao.getRelatedBizOrderList(relatedBizOrderNo);
		List<CustomerBillBO> customerBillBOList = customerBillConverter.toBOList(customerBillList);
		customerBillBOList.forEach(e -> {
			if (e.getId().equals(id)) {
				e.setCurrentFlag(true);
			}
		});
		return customerBillBOList;
	}

	/**
	 * 转换处理返回结果
	 *
	 * @param customerBillDTOList
	 * @return
	 */
	private List<CustomerBillGroupDTO> convertResult(List<CustomerBillDTO> customerBillDTOList) {

		// 返回结果集合
		CustomerBillDTO customerBillDTO = customerBillDTOList.get(0);
		Long ownerId = customerBillDTO.getOwnerId();
		List<CustomerBillGroupDTO> resultList = new ArrayList<>();
		Map<String, List<CustomerBillDTO>> collect = customerBillDTOList.stream()
				.collect(Collectors.groupingBy(o -> DateUtil.format(o.getCreateTime(), "yyyy-MM")));
		collect.forEach((key, value) -> {
//			String reg = "[\u4e00-\u9fa5]";  时间格式换为yyyy-MM,所以用下面的正则去掉 -
			String reg = "[a-zA-Z\\\\-]";
			Pattern pat = Pattern.compile(reg);
			Matcher mat = pat.matcher(key);
			String rePickStr = mat.replaceAll("");

			// 统计收入支出
			BigDecimal incomeAmount = this.customerBillDao.calculateAmountByMonthAndMode(key, ModeTypeEnum.INCOME.value(), ownerId);
			if (ObjectUtil.isNull(incomeAmount)) {
				incomeAmount = BigDecimal.ZERO;
			}
			BigDecimal expenditureAmount = this.customerBillDao.calculateAmountByMonthAndMode(key, ModeTypeEnum.EXPENDITURE.value(), ownerId);
			if (ObjectUtil.isNull(expenditureAmount)) {
				expenditureAmount = BigDecimal.ZERO;
			}

			value.stream().filter(e -> e.getPayTypeName().contains("余额钱包,免付")).forEach(e -> e.setPayTypeName(e.getPayTypeName().replace("余额钱包,免付", "余额钱包")));

			resultList.add(new CustomerBillGroupDTO(key, Integer.parseInt(rePickStr), expenditureAmount, incomeAmount, value));
		});
		return resultList.stream().sorted(Comparator.comparing(CustomerBillGroupDTO::getDateGroupId).reversed()).collect(Collectors.toList());
	}

}
