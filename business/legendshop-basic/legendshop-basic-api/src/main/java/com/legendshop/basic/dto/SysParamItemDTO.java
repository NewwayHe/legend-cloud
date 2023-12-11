/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * (SysParamItem)DTO
 *
 * @author legendshop
 * @since 2020-08-28 14:17:43
 */
@Data
@Schema(description = "配置项dto")
public class SysParamItemDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 926080135256523287L;


	/**
	 * 主配置id
	 */
	@Schema(description = "主配置id")
	@NotNull(message = "主配置id不能为空")
	private Long parentId;


	/**
	 * 描述
	 */
	@Schema(description = "描述")
	@NotBlank(message = "描述不能为空")
	private String des;

	/**
	 * key
	 */
	@Schema(description = "keyWord")
	@NotBlank(message = "keyWord不能为空")
	private String keyWord;

	/**
	 * 值
	 */
	@Schema(description = "value")
	@NotBlank(message = "value不能为空")
	private String value;


	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为空")
	@Schema(description = "排序")
	private Integer sort;


	@Schema(description = "数据类型")
	@NotBlank(message = "数据类型不能为空")
	private String dataType;

	/**
	 * 备注
	 */
	private String remark;


	//private List<SysParamItemCheckBoxDTO> sysParamItemCheckBoxDTOS;

	/**
	 * checkBox的选项
	 */
	private List<SysParamValueItemDTO> sysParamValueItemDTOS;

	/**
	 * checkBox选中的id
	 */
	private List<Long> checkBoxIds;
}
