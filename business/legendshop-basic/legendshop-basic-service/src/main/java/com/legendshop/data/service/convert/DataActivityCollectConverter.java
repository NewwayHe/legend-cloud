/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.data.dto.DataActivityCollectDTO;
import com.legendshop.data.entity.DataActivityCollect;
import org.mapstruct.Mapper;

/**
 * 营销活动汇总表(DataActivityCollect)转换器
 *
 * @author legendshop
 * @since 2021-06-30 20:35:13
 */
@Mapper
public interface DataActivityCollectConverter extends BaseConverter<DataActivityCollect, DataActivityCollectDTO> {
}
