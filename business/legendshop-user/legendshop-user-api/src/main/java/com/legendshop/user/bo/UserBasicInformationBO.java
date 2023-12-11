/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import com.legendshop.user.dto.OrdinaryRoleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 后台-用户基本资料
 *
 * @author legendshop
 */
@Data
@Schema(description = "普通用户基本资料")
public class UserBasicInformationBO implements Serializable {

	private static final long serialVersionUID = 283585057288465193L;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "昵称")
	private String nickName;

	@Schema(description = "头像")
	private String avatar;

	@Schema(description = "注册手机号码")
	private String mobile;

	@Schema(description = "电子邮箱")
	private String email;

	@Schema(description = "注册时间")
	private Date createTime;

	@Schema(description = "可用预存款金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal availablePreDeposit;

	@Schema(description = "累计消费金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal cumulativeConsumptionAmount;

	@Schema(description = "累计订单数量")
	private Integer cumulativeOrderQuantity;

	@Schema(description = "最近消费时间")
	private Date lastConsumptionTime;

	@Schema(description = "累计获得佣金")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal accumulatedCommission;

	@Schema(description = "累计发展下级")
	private Integer cumulativeDevelopmentOfSubordinates;

	@Schema(description = "分销申请时间")
	private Date distributionApplicationTime;

	@Schema(description = "普通用户角色")
	private List<OrdinaryRoleDTO> ordinaryRoleDTOList;

	@Schema(description = "菜单")
	private List<MenuBO> menuList;

	public UserBasicInformationBO() {
		this.availablePreDeposit = BigDecimal.ZERO;
		this.cumulativeConsumptionAmount = BigDecimal.ZERO;
		this.cumulativeOrderQuantity = 0;
		this.accumulatedCommission = BigDecimal.ZERO;
		this.cumulativeDevelopmentOfSubordinates = 0;
	}
}
