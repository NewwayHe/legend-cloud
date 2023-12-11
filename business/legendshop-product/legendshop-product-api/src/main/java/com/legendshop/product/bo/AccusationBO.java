/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import com.legendshop.product.enums.AccusationEnum;
import com.legendshop.product.enums.AccusationResultEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 举报表(Accusation)实体类，业务返回值BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "我的举报BO")
public class AccusationBO implements Serializable {

	private static final long serialVersionUID = -1281598141025659985L;

	/**
	 * 主键
	 */
	@Schema(description = "id")
	private Long id;

	/**
	 * 举报人ID
	 */
	@Schema(description = "举报人ID")
	private Long userId;

	/**
	 * 举报人类型（1、用户 2、平台）
	 */
	@Schema(description = "举报人类型（1、用户 2、平台）")
	private Integer userType;

	/**
	 * 用户名称
	 */
	@Schema(description = "用户名称")
	private String nickName;

	/**
	 * 产品ID
	 */
	@Schema(description = "产品ID")
	private Long productId;

	/**
	 * 举报类型ID
	 */
	@Schema(description = "举报类型ID")
	private Long typeId;

	/**
	 * 举报类型
	 */
	@Schema(description = "举报类型")
	private String typeName;

	/**
	 * 产品名称
	 */
	@Schema(description = "产品名称")
	private String productName;


	@Schema(description = "内容")
	private String content;

	/**
	 * 处理结果  {@link AccusationResultEnum}
	 */
	@Schema(description = "处理结果")
	private Integer result;

	/**
	 * 商品处理 0：不处理 1：违规下架
	 */
	@Schema(description = "商品处理 0：不处理 1：违规下架")
	private Integer illegalOff;

	/**
	 * 处理时间
	 */
	@Schema(description = "处理时间")
	private Date handleTime;

	/**
	 * 处理意见
	 */
	@Schema(description = "处理意见")
	private String handleInfo;

	/**
	 * 状态,0:未处理， 1:已经处理 {@link AccusationEnum}
	 */
	@Schema(description = "状态,0:未处理， 1:已经处理")
	private Integer status;

	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String pic;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 举报时间
	 */
	@Schema(description = "更新时间")
	private Date createTime;

	/**
	 * 举报时间
	 */
	@Schema(description = "举报时间")
	private Date updateTime;

	/**
	 * 用户是否删除,0未删除,1已经删除
	 */
	@Schema(description = "[用户是否删除,0未删除,1已经删除]")
	private Integer userDelStatus;

	@Schema(description = "照片凭证")
	private String image;

	@Schema(description = "照片凭证(前端展示)")
	private List<String> picList;
}
