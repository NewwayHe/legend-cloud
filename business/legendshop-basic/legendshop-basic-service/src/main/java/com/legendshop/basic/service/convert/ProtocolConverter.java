/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.convert;

import com.legendshop.basic.dto.ProtocolDTO;
import com.legendshop.basic.entity.Protocol;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * (Protocol)转换器
 *
 * @author legendshop
 */
@Mapper
public interface ProtocolConverter extends BaseConverter<Protocol, ProtocolDTO> {


}
