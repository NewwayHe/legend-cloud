/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.convert;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.dto.ShopSelectDTO;
import com.legendshop.shop.entity.ShopDetail;
import com.legendshop.shop.vo.ShopDetailVO;
import com.legendshop.shop.vo.ShopInfoVO;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface ShopDetailConverter extends BaseConverter<ShopDetail, ShopDetailDTO> {


	ShopDetailVO toVO(ShopDetail shopDetail);

	PageSupport<ShopSelectDTO> convert2SelectDTO(PageSupport<ShopDetailDTO> ps);

	ShopInfoVO toShopInfoVO(ShopDetail shopDetail);
}
