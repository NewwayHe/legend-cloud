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
 * 商家管理菜单项.
 *
 * @author legendshop
 */
@Data
public class ShopMenu implements Serializable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 9159434666445897447L;

	private String htmlId;

	private String target = "_self";

	/**
	 * 英文标签，唯一的ID.
	 */
	private String label;

	/**
	 * 顺序.
	 */
	private Integer seq;

	/**
	 * 页面显示的名字.
	 */
	private String name;

	/**
	 * 插件ID
	 */
	private String pluginId;

	/**
	 * 页面里对应的URL，第一个为菜单的URL. 查看权限保护的url集合
	 */
	private List<String> urlList;

	/**
	 * 编辑权限保护的url集合
	 */
	private List<String> editorUrlList;

	/**
	 * 是否选择中.
	 */
	private boolean isSelected;

	/**
	 * 是否选中查看权限
	 */
	private boolean checkFunc;

	/**
	 * 是否选中编辑权限
	 */
	private boolean editorFunc;

	/**
	 * 菜单子权限
	 */
	private String functions;

}
