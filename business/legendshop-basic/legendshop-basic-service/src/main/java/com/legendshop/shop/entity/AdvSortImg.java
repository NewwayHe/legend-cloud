/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * (AdvSortImg)实体类
 *
 * @author legendshop
 * @since 2021-07-09 15:48:51
 */
@Data
@Entity
@Table(name = "ls_adv_sort_img")
public class AdvSortImg extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -69383428503748464L;

	/**
	 * 主键id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "advSortImg_SEQ")
	private Long id;

	/**
	 * 图片名字
	 */
	@Column(name = "adv_img_name")
	private String advImgName;

	/**
	 * 图片跳转链接
	 */
	@Column(name = "adv_url")
	private String advUrl;

	/**
	 * 图片地址
	 */
	@Column(name = "adv_path")
	private String advPath;

	/**
	 * 关联分类id
	 */
	@Column(name = "category_id")
	private Long categoryId;

	/**
	 * 状态  1：上线   0：下线
	 */
	@Column(name = "status")
	private Integer status;

}
