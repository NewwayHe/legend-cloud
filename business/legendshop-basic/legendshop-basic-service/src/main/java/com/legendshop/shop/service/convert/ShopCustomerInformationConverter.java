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
import com.legendshop.shop.dto.ContactInformationDTO;
import com.legendshop.shop.entity.ContactInformation;
import org.mapstruct.Mapper;

/**
 * 微信联系方式存储表(ContactInformation)转换器
 *
 * @author legendshop
 * @since 2021-12-27 09:30:28
 */
@Mapper
public interface ShopCustomerInformationConverter extends BaseConverter<ContactInformation, ContactInformationDTO> {
}
