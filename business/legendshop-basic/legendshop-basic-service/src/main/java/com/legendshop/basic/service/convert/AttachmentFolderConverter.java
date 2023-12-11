/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.convert;

import com.legendshop.basic.dto.AttachmentFolderDTO;
import com.legendshop.basic.entity.AttachmentFolder;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;

/**
 * (AttachmentFolder)转换器
 *
 * @author legendshop
 * @since 2021-07-06 17:34:37
 */
@Mapper
public interface AttachmentFolderConverter extends BaseConverter<AttachmentFolder, AttachmentFolderDTO> {
}

