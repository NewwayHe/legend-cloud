/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商家订单设置信息")
public class ShopOrderSettingDTO implements Serializable {

	@Schema(description = "商家ID")
	private Long shopId;

	@Schema(description = "是否开启发票")
	private Boolean invoiceFlag;


	@Schema(description = "允许开具发票类型  发票类型，NORMAL:增值税普票 DEDICATED:增值税专票")
	private String invoiceType;


	@Schema(description = "退货收货人")
	@Length(max = 10, min = 1, message = "退货收货人名字长度应在1~10之间")
	private String returnConsignee;


	@Schema(description = "退货收货人手机号码")
	private String returnConsigneePhone;


	@Schema(description = "退货省份Id")
	protected Long returnProvinceId;


	@Schema(description = "退货城市Id")
	protected Long returnCityId;


	@Schema(description = "退货地区Id")
	protected Long returnAreaId;


	@Schema(description = "退货地区Id")
	protected Long returnStreetId;


	@Schema(description = "退货地址")
	@Length(max = 50, min = 1, message = "退货地址应在1~50之间")
	protected String returnShopAddr;


	@Schema(description = "退货省份(回显用)")
	protected String returnProvince;


	@Schema(description = "退货城市(回显用)")
	protected String returnCity;


	@Schema(description = "退货地区(回显用)")
	protected String returnArea;


	@Schema(description = "退货街道(回显用)")
	protected String returnStreet;


}
