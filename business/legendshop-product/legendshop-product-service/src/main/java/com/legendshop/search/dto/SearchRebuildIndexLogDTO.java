/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 搜索重建索引日志(SearchRebuildIndexLog)DTO
 *
 * @author legendshop
 * @since 2022-02-18 10:53:59
 */
@Data
@Schema(description = "搜索重建索引日志DTO")
public class SearchRebuildIndexLogDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 391538923923973604L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * IndexTypeEnum
	 */
	@Schema(description = "IndexTypeEnum")
	private String indexType;

	/**
	 * IndexTargetMethodEnum
	 */
	@Schema(description = "10 Create , 20 Delete, 30 Update")
	private Integer targetMethod;

	/**
	 * 如果是营销索引，会有不同的营销活动类型，具体见ActivityEsTypeEnum，其它没有
	 */
	@Schema(description = "如果是营销索引，会有不同的营销活动类型，具体见ActivityEsTypeEnum，其它没有")
	private Integer targetType;

	/**
	 * 对应类型的主键ID
	 */
	@Schema(description = "对应类型的主键ID")
	private String targetId;

	/**
	 * 待执行 10 ，已执行完成 20
	 */
	@Schema(description = "待执行 10 ，已执行完成 20")
	private Integer status;

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
	 * 备注信息
	 */
	@Schema(description = "备注信息")
	private String remark;

}
