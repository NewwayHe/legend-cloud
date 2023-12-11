/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.dto.BrandDTO;
import com.legendshop.product.excel.BrandExportDTO;
import com.legendshop.product.query.BrandQuery;

import java.util.List;

/**
 * 品牌服务
 *
 * @author legendshop
 */
public interface BrandService {

	/**
	 * 获取品牌信息
	 *
	 * @param id
	 * @return
	 */
	BrandBO getById(Long id);

	/**
	 * 根据主键ID删除品牌
	 *
	 * @param id 主键ID
	 * @return
	 */
	R deleteById(Long id);

	/**
	 * 保存品牌
	 *
	 * @param brandDTO
	 * @return
	 */
	R save(BrandDTO brandDTO);

	/**
	 * 更新品牌
	 *
	 * @param brandDTO
	 * @return
	 */
	R update(BrandDTO brandDTO);

	/**
	 * 根据shopId获取所有品牌,shopId为0时查询全部
	 *
	 * @return
	 */
	List<BrandBO> getAllOnline(Long shopId, String brandName);

	/**
	 * 根据ID集合获取品牌列表
	 *
	 * @param brandIds 品牌id集合
	 * @return
	 */
	List<BrandBO> getBrandByIds(List<Long> brandIds);

	/**
	 * 根据条件连表查询分页列表
	 *
	 * @param brandQuery
	 * @return
	 */
	PageSupport<BrandBO> queryPage(BrandQuery brandQuery);

	/**
	 * 检查名称是否已存在（与审核通过的品牌比对)
	 *
	 * @param brandName
	 * @param brandId
	 * @return
	 */
	boolean checkBrandByName(String brandName, Long brandId);

	/**
	 * 获取导出品牌列表
	 *
	 * @param brandQuery
	 * @return
	 */
	List<BrandExportDTO> getExportBrands(BrandQuery brandQuery);

	/**
	 * 根据id更新品牌状态
	 *
	 * @param status
	 * @param id
	 * @return
	 */
	R updateStatus(Integer status, Long id);

	/**
	 * 执行品牌审核操作。
	 *
	 * @param auditDTO 品牌审核所需的数据传输对象
	 * @return 审核结果
	 */
	R audit(AuditDTO auditDTO);


}
