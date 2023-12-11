/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.user.bo.MenuBO;
import com.legendshop.user.dto.ShopMenuDTO;
import com.legendshop.user.dto.TreeSelectMenuDTO;
import com.legendshop.user.entity.ShopMenu;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface ShopMenuConverter extends BaseConverter<ShopMenu, ShopMenuDTO> {
	List<MenuBO> toMenuBOList(List<ShopMenu> menuList);

	List<TreeSelectMenuDTO> convert2TreeSelectShopMenuDTOList(List<ShopMenuDTO> convertResult);
}
