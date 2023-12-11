/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.basic.enums.SysParamGroupEnum;
import com.legendshop.basic.enums.SysParamTypeEnum;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysParams)DTO
 *
 * @author legendshop
 * @since 2020-08-28 12:05:05
 */
@Data
@Schema(description = "主配置dto")
public class SysParamsDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 725169526659808533L;

	@Schema(description = "id")
	private Long id;

	/**
	 * 名称
	 */
	@Schema(description = "名称")
	@NotBlank(message = "名称不能为空")
	private String name;

	/**
	 * 描述
	 */
	@Schema(description = "描述")
	@NotBlank(message = "简介不能为空")
	private String des;

	/**
	 * 类型 {@link SysParamTypeEnum}
	 */
	@Schema(description = "类型")
	private Integer type;

	/**
	 * {@link SysParamGroupEnum}
	 * 分组
	 */
	@Schema(description = "分组")
	private String groupBy;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	@NotNull(message = "排序不能为空")
	private Integer sort;


	@Schema(description = "更新时间")
	private Date updateTime;


	@Schema(description = "备注")
	private String remark;

	@Schema(description = "是否启用")
	private Boolean enabled;
}
