/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import cn.legendshop.jpaplus.support.PageParams;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.product.enums.VitLogPageEnum;
import com.legendshop.product.enums.VitLogSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户浏览历史(VitLog)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "浏览历史")
public class VitLogQuery extends PageParams {


	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 店铺ID
	 */
	private Long shopId;

	/**
	 * 用户ID
	 */
	private Long userId;


	/**
	 * 商城名称
	 */
	private String shopName;


	/**
	 * 商品ID
	 */
	private Long productId;


	/**
	 * 产品名称
	 */
	@Schema(description = "商品名称")
	private String productName;


	/**
	 * IP
	 */
	private String ip;


	/**
	 * 获得IP所在国家，如果在中国，直接显示省市
	 */
	private String country;


	/**
	 * 获得IP所在区域
	 */
	private String area;


	/**
	 * 产品图片
	 */
	private String pic;


	/**
	 * 产品售价
	 */
	private BigDecimal price;


	/**
	 * 访问页面
	 * {@link VitLogPageEnum}
	 */
	@Schema(description = "访问的页面类型, 0: 商品页, 1: 店铺页")
	private Integer page;

	/**
	 * 来源
	 * {@link VitLogSourceEnum}
	 */
	@Schema(description = "参数为pc,mobile")
	private String source;


	/**
	 * 访问时间
	 */
	private Date recDate;


	/**
	 * 访问次数
	 */
	private Integer visitNum;

	/**
	 * 用户是否已删除
	 */
	private Boolean userDelFlag;

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endTime;


}
