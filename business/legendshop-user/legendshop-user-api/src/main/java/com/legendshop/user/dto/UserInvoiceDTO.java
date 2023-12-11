/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import com.legendshop.common.core.validator.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 发票信息表(Invoice)实体类
 *
 * @author legendshop
 */
@Data
public class UserInvoiceDTO implements Serializable {


	@Schema(description = "ID")
	@NotNull(message = "id不能为空", groups = Update.class)
	private Long id;


	@Schema(description = "用户ID")
	private Long userId;


	@Schema(description = "发票类型，NORMAL:增值税普票 DEDICATED:增值税专票")
	@NotBlank(message = "发票类型不能为空")
	private String type;


	@Schema(description = "普票类型，PERSONAL:个人，COMPANY:单位")
	private String titleType;


	@Schema(description = "个人普票：发票抬头信息 公司普票：发票抬头信息 增值税专票：公司名称")
	@NotBlank(message = "发票抬头信息不能为空")
	private String company;


	@Schema(description = "是否默认")
	private Boolean commonInvoiceFlag;


	@Schema(description = "纳税人号")
	private String invoiceHumNumber;


	@Schema(description = "注册地址（增值税专票）")
	private String registerAddr;


	@Schema(description = "注册电话（增值税专票）")
	@Length(max = 20, message = "注册电话最多为20位")
	private String registerPhone;


	@Schema(description = "开户银行（增值税专票）")
	private String depositBank;


	@Schema(description = "开户银行账号（增值税专票）")
	private String bankAccountNum;


	@Schema(description = "创建时间")
	private Date createTime;


	@Schema(description = "更新时间")
	private Date updateTime;

	@Schema(description = "邮箱")
	@NotBlank(message = "邮箱不能为空")
	private String email;
}
