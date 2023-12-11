/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.annotation.EnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 审核表(Audit)实体类
 *
 * @author legendshop
 */
@Schema(description = "审核Dto")
@Data
public class AuditDTO implements Serializable {

	private static final long serialVersionUID = -3629216946081080771L;
	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * id集合（用户批量审核）
	 */
	@Schema(description = "id集合")
	private List<Long> idList;
	/**
	 * 通用ID(审核类型为商品，就为商品id)
	 */
	@Schema(description = "通用ID(审核类型为商品，就为商品id)")
	private Long commonId;


	/**
	 * 审核操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "审核操作状态 0等待审核 1审核通过 -1拒绝")
	@EnumValid(target = OpStatusEnum.class, message = "操作状态必须为拒绝或通过")
	private Integer opStatus;


	/**
	 * 审核类型
	 * {@link com.legendshop.basic.enums.AuditTypeEnum}
	 */
	@Schema(description = "审核类型")
	private Integer auditType;

	/**
	 * 审核意见
	 */
	@Schema(description = "审核意见")
	private String auditOpinion;

	/**
	 * 审核人
	 */
	@Schema(description = "审核人")
	private String auditUsername;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	private Date auditTime;


}
