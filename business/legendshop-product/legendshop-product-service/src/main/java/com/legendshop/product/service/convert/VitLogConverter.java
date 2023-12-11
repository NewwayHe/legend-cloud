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
import com.legendshop.product.bo.VitLogBO;
import com.legendshop.product.dto.VitLogDTO;
import com.legendshop.product.dto.VitLogRecordDTO;
import com.legendshop.product.dto.VitLogUserHistoryDTO;
import com.legendshop.product.entity.VitLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author legendshop
 */
@Mapper
public interface VitLogConverter extends BaseConverter<VitLog, VitLogDTO> {

	/**
	 * 转换为用户足迹
	 *
	 * @param vitLog
	 * @return
	 */
	@Mapping(target = "dateTime", expression = "java(cn.hutool.core.date.DateUtil.formatDate(vitLog.getCreateTime()))")
	VitLogUserHistoryDTO convert2UserHistory(VitLog vitLog);

	/**
	 * to bo VitLogBO
	 *
	 * @param vitLog
	 * @return
	 */
	VitLogBO convert2VitLogBO(VitLog vitLog);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<VitLogBO> convert2BoPageList(PageSupport<VitLog> ps);

	/**
	 * to entity
	 *
	 * @param vitLogRecordDTO
	 * @return
	 */
	VitLog convert2Entity(VitLogRecordDTO vitLogRecordDTO);


	/**
	 * 转换为用户足迹
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<VitLogUserHistoryDTO> convert2UserHistoryList(PageSupport<VitLog> ps);


}
