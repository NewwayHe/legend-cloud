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
import com.legendshop.product.bo.TransportBO;
import com.legendshop.product.dto.TransportDTO;
import com.legendshop.product.entity.Transport;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface TransportConverter extends BaseConverter<Transport, TransportDTO> {

	/**
	 * to bo
	 *
	 * @param transport
	 * @return
	 */
	TransportBO convert2BO(Transport transport);

	/**
	 * to bo List
	 *
	 * @param transportList
	 * @return
	 */
	List<TransportBO> convert2BoList(List<Transport> transportList);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<TransportBO> convert2BoPageList(PageSupport<Transport> ps);
}
