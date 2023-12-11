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
import com.legendshop.user.dto.AdminMenuDTO;
import com.legendshop.user.dto.TreeSelectMenuDTO;
import com.legendshop.user.entity.AdminMenu;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface AdminMenuConverter extends BaseConverter<AdminMenu, AdminMenuDTO> {

	List<MenuBO> toMenuBOList(List<AdminMenu> menuList);

	List<TreeSelectMenuDTO> convert2TreeSelectAdminMenuDTOList(List<AdminMenuDTO> adminMenuDTOList);
}
