/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Data
public class ProductDetailDTO {


	/**
	 * The rel prods.
	 */
	List<ProductDTO> relProds;

	/**
	 * 店铺类型
	 */
	private Integer shopType;

	////
	/**
	 * 单品列表
	 **/
	private List<SkuDTO> skuList;

	/**
	 * 将要更新的单品列表
	 **/
	private List<SkuDTO> updateSkuList;

	/** 产品图片 **/
//	private List<ImgFile> imgFileList;

	/**
	 * 发布商品时，存放页面返回的 image url
	 **/
	private String imagesPaths;

	/**
	 * 商品sku数据
	 **/
	private String skus;

	/**
	 * 用户自定义属性
	 **/
	private String userProperties;

	/**
	 * 删除用户自定义属性
	 **/
	private String deleteUserProperties;

	/**
	 * 商品属性值图片
	 **/
	private String valueImages;

	/**
	 * 用户自定义属性值名
	 **/
	private String valueAlias;

	/**
	 * 设定商品发布后状态 1：上线，2：设定：有记录开始时间，0：放入仓库
	 **/
	private Integer publishStatus;

	/**
	 * 建立时间
	 **/
	private String setUpTime;

	/**
	 * 商城分类
	 **/
	private String categoryName;

	/**
	 * 商城名称
	 */
	private String siteName;

	/**
	 * 商城标签
	 */
	private String tags;

	/**
	 * 品牌名称
	 **/
	private String brandName;

	/**
	 * 好评数量
	 **/
	private String goodCommentsPercent;

	/**
	 * 产品图片
	 **/
	private String proVideoUrl;

	/**
	 * 商品Id
	 */
	protected Long id;

	/**
	 * 版本号
	 */
	protected Integer version;

	/**
	 * 商品条形码.
	 */
	protected String modelId;

	/**
	 * 商家编码.
	 */
	protected String partyCode;

	/**
	 * 商品名称.
	 */
	protected String name;

	/**
	 * 销售价范围
	 */
	protected String price;

	/**
	 * 代理价.
	 */
	protected BigDecimal proxyPrice;

	/**
	 * 是否支持分销
	 **/
	protected int supportDist = 0;

	/**
	 * 分销佣金比例
	 **/
	protected Double distCommisRate;

	/**
	 * 运费.
	 */
	protected BigDecimal carriage;

	/**
	 * 简称.
	 */
	protected String brief;

	/**
	 * PC端详细描述
	 */
	protected String content;

	/**
	 * 手机端详细描述
	 */
	protected String contentM;

	/**
	 * 用户查看数.
	 */
	protected long views;

	/**
	 * 用户购买数量.
	 */
	protected long buys;

	/**
	 * 评论数.
	 */
	protected long comments;

	/**
	 * 评论总得分
	 **/
	protected int reviewScores = 0;

	/**
	 * 评论平均得分
	 **/
	protected int avgScores;

	/**
	 * The rec date.
	 */
	protected Date recDate;

	/**
	 * 商品的小图片.
	 */
	protected String smallPic;

	/**
	 * 是否使用小图片.
	 */
	protected String useSmallPic;

	/**
	 * 商品图片.
	 */
	protected String imgPath;

	/**
	 * 小图片
	 */
	private String pic;

	/**
	 * 商品状态.
	 */
	protected Integer status;

	/**
	 * 商品原状态.
	 */
	protected Integer preStatus;

	/**
	 * 修改时间.
	 */
	protected Date modifyDate;

	/**
	 * 用户Id.
	 */
	protected String userId;

	/**
	 * 用户名称.
	 */
	protected String userName;

	/**
	 * 开始时间，在商品为有时间段有效期.
	 */
	protected Date startDate;

	/**
	 * 结束时间，在商品为有时间段有效期.
	 */
	protected Date endDate;

	/**
	 * 库存.
	 */
	protected Integer stocks;

	/**
	 * 商品预警库存.
	 */
	protected Integer stocksArm;

	/**
	 * 商品类型，见ProductTypeEnum.
	 */
	protected String prodType;

	/**
	 * 活动Id [秒杀活动ID].
	 */
	protected Long acitveId;

	/**
	 * 参数属性列表
	 * property id: property value id
	 */
	protected String parameter;

	/**
	 * 用户自定义的参数属性列表, key:value 格式
	 */
	protected String userParameter;

	/**
	 * 品牌Id.
	 */
	protected Long brandId;

	/**
	 * 实际库存.
	 */
	protected Integer actualStocks;

	/**
	 * 产品缩略图.
	 */
	protected MultipartFile smallPicFile;

	/**
	 * 省份Id.
	 */
	protected Integer provinceid;

	/**
	 * 城市Id.
	 */
	protected Integer cityid;

	/**
	 * 地区Id.
	 */
	protected Integer areaid;

	/**
	 * 评论数
	 */
	protected Integer commentNum;

	/**
	 * 物流体积(立方米)
	 **/
	protected Double volume = 0d;

	/**
	 * 物流重量(千克)
	 **/
	protected Double weight = 0d;

	/**
	 * 有没发票
	 **/
	protected int hasInvoice;

	/**
	 * 是否保修
	 **/
	protected int hasGuarantee;

	/**
	 * 售后服务ID
	 **/
	protected Long afterSaleId;

	/**
	 * 库存计数方式，0：拍下减库存，1：付款减库存
	 **/
	protected int stockCounting;

	/**
	 * 退换货承诺
	 **/
	protected Integer rejectPromise;

	/**
	 * 服务保障
	 **/
	protected Integer serviceGuarantee;

	/**
	 * 商家ID
	 **/
	protected Long shopId;

	/**
	 * (全局商城)一级分类
	 */
	private Long globalFirstCatId;

	/**
	 * (全局商城)二级分类
	 */
	private Long globalSecondCatId;

	/**
	 * (全局商城)三级分类
	 */
	private Long globalThirdCatId;

	/**
	 * (商家小分类) 一级分类
	 **/
	protected Long shopFirstCatId;

	/**
	 * (商家小分类) 二级分类
	 **/
	protected Long shopSecondCatId;

	/**
	 * (商家小分类) 三级分类
	 **/
	protected Long shopThirdCatId;

	/**
	 * 货到付款; 0:普通商品 , 1:货到付款商品
	 **/
	protected int isSupportCod;

	/**
	 * seo的标题
	 **/
	private String metaTitle;

	/**
	 * seo的描述
	 **/
	private String metaDesc;

	/**
	 * 审核意见
	 **/
	private String auditOpinion;

	/**
	 * 是否参加团购
	 **/
	private Boolean isGroup;

	/**
	 * 运费模板ID
	 **/
	protected Long transportId;

	/**
	 * 是否免运费
	 **/
	private Boolean supportTransportFree;

	/**
	 * 是否免运费 ，参见TransportTypeEnum
	 **/
	private Boolean transportType;

	/**
	 * EMS运费
	 **/
	private BigDecimal emsTransFee;

	/**
	 * 快递运费
	 **/
	private BigDecimal expressTransFee;

	/**
	 * 邮件运费
	 **/
	private BigDecimal mailTransFee;

	/**
	 * 会员直接上级分佣比例
	 **/
	protected BigDecimal firstLevelRate;

	/**
	 * 会员上二级分佣比例
	 **/
	protected BigDecimal secondLevelRate;

	/**
	 * 会员上三级分佣比例
	 **/
	protected BigDecimal thirdLevelRate;

	/**
	 * 微信小程序码
	 */
	protected String wxAcode;

	/**
	 * 商品分组ID
	 */
	private Long groupId;

	/**
	 * 商家端微信小程序码
	 */
	protected String shopWxCode;

	/**
	 * 商品搜索权重
	 **/
	private Double searchWeight;
}
