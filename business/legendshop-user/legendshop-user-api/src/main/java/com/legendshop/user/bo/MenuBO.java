/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单的业务对象
 *
 * @author legendshop
 */
@Data
public class MenuBO implements Serializable {

	private static final long serialVersionUID = -1142808156597711758L;
	/**
	 * 菜单ID
	 */
	@Schema(description = "菜单id")
	private Long id;

	/**
	 * 菜单名称
	 */
	@Schema(description = "菜单名称")
	private String name;

	/**
	 * 菜单权限标识
	 */
	@Schema(description = "菜单权限标识")
	private String permission;

	/**
	 * 菜单描述
	 */
	@Schema(description = "菜单描述")
	private String menuDesc;

	/**
	 * 父菜单ID
	 */
	@Schema(description = "父菜单id")
	private Long parentId;

	/**
	 * 图标
	 */
	@Schema(description = "图标")
	private String icon;

	/**
	 * 前端路由标识路径
	 */
	@Schema(description = "前端路由标识路径")
	private String path;

	/**
	 * 前端路由标识路径
	 */
	@Schema(description = "路由名称")
	private String component;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Float sort;

	/**
	 * 菜单类型 （0菜单 1按钮）
	 */
	@Schema(description = "菜单类型,0:菜单 1:按钮")
	private String type;

	/**
	 * 是否缓冲
	 */
	@Schema(description = "路由缓冲")
	private String keepAlive;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;


	/**
	 * 隐藏或显示
	 */
	@Schema(description = "删除标记,1:已删除,0:正常")
	private Boolean hiddenFlag;

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 * menuId 相同则相同
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MenuBO) {
			Long targetMenuId = ((MenuBO) obj).id;
			return id.equals(targetMenuId);
		}
		return super.equals(obj);
	}

}
