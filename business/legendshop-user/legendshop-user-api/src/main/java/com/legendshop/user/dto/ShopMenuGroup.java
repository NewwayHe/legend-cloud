/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商家菜单组
 *
 * @author legendshop
 */
@Data
public class ShopMenuGroup implements Serializable {

	private static final long serialVersionUID = -7220521000262406270L;
	/**
	 * 英文标签，唯一的ID
	 */
	private String label;
	/**
	 * 页面显示的名字
	 */
	private String name;

	private List<ShopMenu> shopMenuList;

	private boolean isSelected = false;

	private Integer seq;


	/**
	 * 拷贝一个新的菜单组
	 */
	public ShopMenuGroup copy(List<ShopMenu> shopMenuList) {
		ShopMenuGroup group = new ShopMenuGroup();
		group.setLabel(this.getLabel());
		group.setName(this.getName());
		group.setShopMenuList(shopMenuList);
		return group;
	}
}
