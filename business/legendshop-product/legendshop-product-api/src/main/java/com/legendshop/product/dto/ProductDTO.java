/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductSpecificationStyle;
import com.legendshop.product.enums.ProductTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品(Prod)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "商品DTO")
public class ProductDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -1240134144933160959L;
	/**
	 * 产品ID
	 */
	@Schema(description = "产品ID")
	private Long id;

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;

	/**
	 * 商家用户ID
	 */
	@Schema(description = "商家用户ID")
	private Long shopUserId;

	/**
	 * 品牌id
	 */
	@Schema(description = "品牌id")
	private Long brandId;

	@Schema(description = "品牌名称")
	private String brandName;

	/**
	 * 预约上架id
	 */
	@Schema(description = "预约上架id")
	private Long appointId;

	/**
	 * 规格样式 {@link com.legendshop.product.enums.ProductSpecificationStyle}
	 */
	@Schema(description = "规格样式： PIC：图片样式；TXT：文本样式（无图样式） GRAPHIC 图文样式")
	@EnumValid(target = ProductSpecificationStyle.class, message = "规格样式不匹配")
	private String specificationStyle;

	/**
	 * 商品上架方式
	 * {@link com.legendshop.product.enums.ProductOnSaleWayEnum}
	 */
	@Schema(description = "商品上架方式")
	@NotNull(message = "商品上架方式不能为空")
	private Integer onSaleWay;

	/**
	 * 预约上架时间
	 */
	@Schema(description = "预约上架时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date appointTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	@NotBlank(message = "商品名称不能为空")
	@Length(max = 300)
	private String name;

	/**
	 * (全局商城)分类
	 */
	@Schema(description = "(全局商城)分类")
	@NotEmpty(message = "(全局商城)分类不能为空")
	private List<Long> globalCategoryId;

	/**
	 * (全局商城)一级分类
	 */
	@Schema(description = "(全局商城)一级分类")
	private Long globalFirstCatId;

	/**
	 * (全局商城)二级分类
	 */
	@Schema(description = "(全局商城)二级分类")
	private Long globalSecondCatId;

	/**
	 * (全局商城)三级分类
	 */
	@Schema(description = "(全局商城)三级分类")
	private Long globalThirdCatId;

	/**
	 * 商家小分类数组
	 */
	@Schema(description = "商家小分类数组")
	private List<Long> shopCategoryId;

	/**
	 * (商家小分类)一级分类
	 */
	@Schema(description = "(商家小分类)一级分类")
	private Long shopFirstCatId;

	/**
	 * (商家小分类)二级分类
	 */
	@Schema(description = "(商家小分类)二级分类")
	private Long shopSecondCatId;

	/**
	 * (商家小分类)三级分类
	 */
	@Schema(description = "(商家小分类)三级分类")
	private Long shopThirdCatId;

	/**
	 * sku
	 */
	@Valid
	@NotNull(message = "sku组图不能为空")
	@Schema(description = "sku")
	private List<ProductPropertyImageDTO> imageList;

	/**
	 * 图片路径
	 */
	@Schema(description = "商品主图")
	private String imgPath;


	/**
	 * 图片路径
	 */
	@Schema(description = "商品主图")
	private List<String> img;

	/**
	 * 小图片
	 */
	@Schema(description = "搜索图片")
	private String pic;

	/**
	 * 商品视频
	 */
	@Schema(description = "商品视频")
	private String video;

	/**
	 * 商品参数 e.g [{"id":59,"name":"属性名称4","paramList":[{"valueId":17,"valueName":"属性值名称3"}]}]
	 */
	@Schema(description = "商品参数")
	@NotBlank(message = "商品参数")
	private String parameter;

	/**
	 * 用户自定义参数 e.g [{"name":"生产地","paramList":[{"valueName":"广州海珠区"}]}]
	 */
	@Schema(description = "用户自定义参数")
	private String userParameter;

	@Schema(description = "商品规格")
	private String specification;
	/**
	 * 参数组列表
	 */
	@Valid
	@Schema(description = "参数组列表")
	private List<ParamPropertyGroupDTO> paramGroupList;

	/**
	 * 参数组
	 */
	@Schema(description = "参数组")
	private String paramGroup;

	/**
	 * 用户自定义参数组
	 */
	@Schema(description = "用户自定义参数组")
	private String userParamGroup;

	/**
	 * 规格
	 */
	@Schema(description = "商品规格")
	@Valid
	private List<SpecificationDTO> specificationList;
	/**
	 * 自定义属性入参（规格，参数）
	 */
	@Schema(description = "自定义属性入参（规格，参数）")
	private List<ProductPropertyDTO> customPropertyValueList;

	/**
	 * sku
	 */
	@Valid
	@NotEmpty(message = "商品sku不能为空")
	@Schema(description = "sku")
	List<SkuDTO> sku;

	/**
	 * 商家编码
	 */
	@Schema(description = "商家编码")
	private String partyCode;

	/**
	 * 销售价范围
	 */
	@Schema(description = "销售价范围")
	private String price;

	/**
	 * 销售起步价
	 */
	@Schema(description = "销售起步价")
	private BigDecimal minPrice;

	/**
	 * 销售最高价
	 */
	@Schema(description = "销售最高价")
	private BigDecimal maxPrice;

	/**
	 * 简要描述、卖点
	 */
	@Schema(description = "简要描述、卖点")
	@Length(max = 500, message = "卖点描述不能超过500个字")
	private String brief;

	/**
	 * 详细描述(手机端)
	 */
	@Schema(description = "详细描述(手机端)")
	private String contentM;

	/**
	 * 详细描述
	 */
	@Schema(description = "详细描述")
	private String content;

	/**
	 * 观看人数，同一个IP半个小时之类只算一次
	 */
	@Min(value = 0, message = "观看人数不能小于0")
	@Schema(description = "观看人数")
	private Integer views;

	/**
	 * 已经销售数量
	 */
	@Min(value = 0, message = "已经销售数量不能小于0")
	@Schema(description = "已经销售数量")
	private Integer buys;

	/**
	 * 评论数
	 */
	@Min(value = 0, message = "评论数不能小于0")
	@Schema(description = "评论数")
	private Integer comments;

	/**
	 * 商品状态：
	 * {@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	@Schema(description = "商品状态 -10：未发布；0：下线；1：上线；3：全部")
	private Integer status;

	/**
	 * 审核操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "审核状态 -1：拒绝；0：待审核 ；1：通过；2：违规下架；3：违规锁定；")
	private Integer opStatus;

	/**
	 * 删除操作状态
	 * {@link ProductDelStatusEnum}
	 */
	@Schema(description = "删除操作状态 -2：永久删除；-1：删除；1：正常；")
	private Integer delStatus;

	/**
	 * 店铺状态
	 */
	private Integer shopStatus;

	@Schema(description = "草稿状态，-10: 商品未发布、-1: 拒绝、0: 待审核、1: 通过")
	private Integer draftStatus;

	/**
	 * 库存量
	 */
	@Schema(description = "库存量")
	@Min(value = 0, message = "库存量不能小于0")
	@Max(value = 999999, message = "库存量不能大于999999")
	private Integer stocks;

	/**
	 * 实际库存
	 */
	@Schema(description = "实际库存")
	@Min(value = 0, message = "实际库存不能小于0")
	@Max(value = 999999, message = "实际库存不能大于999999")
	private Integer actualStocks;

	/**
	 * 库存警告
	 */
	@Schema(description = "库存警告")
	@Min(value = 0, message = "库存警告不能小于0")
	@Max(value = 999999, message = "库存量不能大于999999")
	private Integer stocksArm;

	/**
	 * 商品类型
	 * {@link com.legendshop.product.enums.ProductTypeEnum }
	 */
	@Schema(description = "商品类型Enum E.实物商品、V:虚拟商品")
	@EnumValid(target = ProductTypeEnum.class, message = "商品类型不匹配")
	private String prodType;


	/**
	 * 是否预售商品
	 */
	@Schema(description = "是否预售商品")
	@NotNull(message = "是否预售商品不能为空")
	private Boolean preSellFlag;

	/**
	 * 是否多规格(0单规格，1多规格)
	 */
	@Schema(description = "是否多规格(0单规格，1多规格)")
	@NotNull(message = "是否单规格不能为空")
	private Boolean multipleSpecificationFlag;

	/**
	 * 预售信息
	 */
	@Schema(description = "预售信息")
	@Valid
	private PreSellProductDTO preSellProductDTO;

	/**
	 * 评论得分
	 */
	@Schema(description = "评论得分")
	private Integer reviewScores;

	/**
	 * 运费模板ID
	 */
	@Schema(description = "运费模板ID")
	@NotNull(message = "运费模板ID不能为空")
	private Long transId;


	/**
	 * 运费模板名称
	 */
	@Schema(description = "运费模板名称")
	private String transName;

	/**
	 * 库存计数方式，0：拍下减库存，1：付款减库存
	 */
	@Schema(description = " 库存计数方式，0：拍下减库存，1：付款减库存")
	@NotNull(message = "是库存计数方式不能为空")
	private Boolean stockCounting;

	/**
	 * 微信小程序码
	 */
	@Schema(description = "微信小程序码")
	private String wxACode;

	/**
	 * 商家端微信小程序码
	 */
	@Schema(description = "商家端微信小程序码")
	private String shopWxCode;

	/**
	 * sku营销活动类型
	 * {@link com.legendshop.product.enums.SkuActiveTypeEnum }
	 */
	@Schema(description = "sku营销活动类型")
	private String skuType;

	/**
	 * 主规格id
	 */
	@Schema(description = "主规格id")
	private Long mainSpecificationId;

	/**
	 * 评论综合得分
	 */
	@Schema(description = "评论综合得分")
	private Double comScore;

	@Schema(description = "版本号")
	private Integer version;

	@Schema(description = "是否开启分销")
	private Boolean openDistribution;

	@Schema(description = "分佣比例")
	private BigDecimal commissionRatio;

	@Schema(description = "限购对象")
	private ProductQuotaDTO productQuotaDTO;

	@Schema(description = "是否参与分销 0:否, 1:是")
	private Integer distributionFlag;

	@Schema(description = "sku数量")
	private Integer skuCount;

	@Schema(description = "分享次数")
	private Integer shareCount;

	@Schema(description = "起步成本价")
	private BigDecimal minCost;

	@Schema(description = "最大成本价")
	private BigDecimal maxCost;

	@Schema(description = "最大分销金额")
	private BigDecimal maxCommission;

	@Schema(description = "最小分销金额")
	private BigDecimal minCommission;

	@Schema(description = "提审")
	private Boolean arraignment;

	/**
	 * 是否批量发布
	 */
	private Boolean batchFlag;

	/**
	 * 配送类型 {@link com.legendshop.product.enums.ProductDeliveryTypeEnum}
	 */
	@Schema(description = "配送方式  0: 商家配送 10:到店自提  20:商家配送及到店自提")
	private Integer deliveryType;

	public ProductDTO() {
		this.batchFlag = false;
		this.arraignment = false;
	}


}
