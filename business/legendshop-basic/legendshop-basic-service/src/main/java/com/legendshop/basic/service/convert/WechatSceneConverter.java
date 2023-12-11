/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.convert;

import com.legendshop.basic.dto.WechatSceneDTO;
import com.legendshop.basic.entity.WechatScene;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * 微信短连接(WechatScene)转换器
 *
 * @author legendshop
 * @since 2021-03-16 15:08:44
 */
@Mapper
public interface WechatSceneConverter extends BaseConverter<WechatScene, WechatSceneDTO> {
}
