/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletOperationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户钱包收支记录详情(UserWalletDetails)Query分页查询对象
 *
 * @author legendshop
 * @since 2021-03-13 14:44:02
 */
@Data
public class UserWalletDetailsQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = -39456185163011484L;

	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "流水号")
	private Long serialNo;

	@Schema(description = "业务编码")
	private Long businessId;

	@Schema(description = "业务类型")
	private WalletBusinessTypeEnum businessType;

	@Schema(description = "操作类型")
	private WalletOperationTypeEnum operationType;

	@Schema(description = "记录状态 WalletDetailsStateEnum")
	private Integer state;

	/**
	 * 金额类型(冻结金额，已结算金额)
	 * UserWalletAmountTypeEnum
	 */
	@Schema(description = "金额类型(冻结金额，已结算金额)")
	private String amountType;

	@Schema(description = "开始日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;

	@Schema(description = "结束日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;

	@Schema(description = "用户手机号")
	private String mobile;

	@Schema(description = "用户昵称")
	private String nickName;

	@Schema(description = "分销等级")
	private Integer grade;

	@Schema(description = "平台的操作状态 0待审核 1通过 2拒绝")
	private Integer opStatus;

	@Schema(description = "支付开始日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payStartDate;

	@Schema(description = "支付结束日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payEndDate;

	@Schema(description = "支付方式")
	private Integer style;

}
