/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 */
@Getter
@Setter
public class ShopMenuGroupDTO implements Serializable {

	private static final long serialVersionUID = -8875514820215847211L;
	/**
	 * 英文标签，唯一的ID
	 */
	private String label;
	/**
	 * 页面显示的名字
	 */
	private String name;

	private List<ShopMenuDTO> shopMenuDTOList;

	private boolean isSelected = false;

	/**
	 * 顺序.
	 */
	private Integer seq;


	/**
	 * 拷贝一个新的菜单组
	 */
	public ShopMenuGroupDTO copy(List<ShopMenuDTO> shopMenuList) {
		ShopMenuGroupDTO group = new ShopMenuGroupDTO();
		group.setLabel(this.getLabel());
		group.setName(this.getName());
		group.setShopMenuDTOList(shopMenuList);
		return group;
	}

}
