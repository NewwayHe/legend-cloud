/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.excel;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.legendshop.pay.enums.WalletDetailsAuditEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
public class UserWalletDetailsExcelDTO {


	private Long id;

	/**
	 * 用户Id
	 */
	@ColumnWidth(20)
	@ExcelProperty("用户Id")
	@Schema(description = "用户Id")
	private Long userId;

	@ColumnWidth(20)
	@ExcelProperty("用户手机号")
	@Schema(description = "用户手机号")
	private String mobile;

	@ColumnWidth(20)
	@ExcelProperty("用户昵称")
	@Schema(description = "用户昵称")
	private String nickName;

	/**
	 * 操作金额
	 */
	@ColumnWidth(20)
	@ExcelProperty("操作金额")
	@NumberFormat("#.##")
	@Schema(description = "操作金额")
	private BigDecimal amount;

	/**
	 * 创建时间
	 */
	@ColumnWidth(20)
	@ExcelProperty("创建时间")
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@ColumnWidth(20)
	@ExcelProperty("审核时间")
	@Schema(description = "更新时间")
	private Date updateTime;

	@Schema(description = "平台的操作状态 0待审核 1通过 2拒绝")
	@ExcelIgnore
	private Integer opStatus;

	@ColumnWidth(20)
	@ExcelProperty("审核状态")
	@Schema(description = "平台的操作状态 0待审核 1通过 2拒绝")
	private String opStatusType;

	/**
	 * WalletDetailsStateEnum
	 */
	@Schema(description = "记录状态（-1：失效） 1：完成、0：默认、999：异常")
	@ExcelIgnore
	private Integer state;

	@ColumnWidth(20)
	@ExcelProperty("支付状态")
	@Schema(description = "记录状态（-1：失效） 1：完成、0：默认、999：异常")
	private String stateType;

	@ColumnWidth(20)
	@ExcelProperty("记录备注")
	@Schema(description = "记录备注")
	private String remarks;


	public Integer getOpStatus() {
		if (ObjectUtil.isEmpty(opStatus)) {
			return WalletDetailsAuditEnum.WAIT.getCode();
		}
		return opStatus;
	}
}
