/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service.converter;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.search.dto.SearchRebuildIndexLogDTO;
import com.legendshop.search.entity.SearchRebuildIndexLog;
import org.mapstruct.Mapper;

/**
 * 搜索重建索引日志(SearchRebuildIndexLog)转换器
 *
 * @author legendshop
 * @since 2022-02-18 10:53:58
 */
@Mapper
public interface SearchRebuildIndexLogConverter extends BaseConverter<SearchRebuildIndexLog, SearchRebuildIndexLogDTO> {
}

