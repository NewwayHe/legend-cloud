/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.convert;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.product.bo.ParamGroupBO;
import com.legendshop.product.dto.ParamGroupDTO;
import com.legendshop.product.entity.ParamGroup;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 参数组转换器
 *
 * @author legendshop
 */
@Mapper
public interface ParamGroupConverter extends BaseConverter<ParamGroup, ParamGroupDTO> {


	/**
	 * to bo list
	 *
	 * @param paramGroupList
	 * @return
	 */
	List<ParamGroupBO> convert2BoList(List<ParamGroup> paramGroupList);

	/**
	 * to bo
	 *
	 * @param paramGroup
	 * @return
	 */
	ParamGroupBO convert2Bo(ParamGroup paramGroup);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ParamGroupBO> convert2BoPageList(PageSupport<ParamGroup> ps);
}
