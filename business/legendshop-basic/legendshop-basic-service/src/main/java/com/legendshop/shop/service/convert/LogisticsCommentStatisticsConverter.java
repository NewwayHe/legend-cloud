/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.shop.bo.LogisticsCommentStatisticsBO;
import com.legendshop.shop.dto.LogisticsCommentStatisticsDTO;
import com.legendshop.shop.entity.LogisticsCommentStatistics;
import org.mapstruct.Mapper;

/**
 * 物流评分转换器
 *
 * @author legendshop
 */
@Mapper
public interface LogisticsCommentStatisticsConverter extends BaseConverter<LogisticsCommentStatistics, LogisticsCommentStatisticsDTO> {


	/**
	 * to bo
	 *
	 * @param logisticsCommentStatisticsByShopId
	 * @return
	 */
	LogisticsCommentStatisticsBO convertLogisticsCommStatBO(LogisticsCommentStatistics logisticsCommentStatisticsByShopId);


}
