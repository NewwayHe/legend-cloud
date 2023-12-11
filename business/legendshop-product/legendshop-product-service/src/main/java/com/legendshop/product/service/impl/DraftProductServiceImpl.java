/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.google.common.collect.ImmutableList;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.product.bo.*;
import com.legendshop.product.dao.*;
import com.legendshop.product.dto.*;
import com.legendshop.product.entity.*;
import com.legendshop.product.enums.*;
import com.legendshop.product.mq.producer.ProductProducerService;
import com.legendshop.product.service.DraftProductService;
import com.legendshop.product.service.DraftSkuService;
import com.legendshop.product.service.ProductPropertyValueService;
import com.legendshop.product.service.convert.DraftProductConverter;
import com.legendshop.product.service.convert.DraftSkuConverter;
import com.legendshop.product.service.convert.ProductPropertyImageConverter;
import com.legendshop.search.constants.AmqpConst;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import com.legendshop.user.api.AdminUserApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品SPU草稿表(DraftProduct)表服务实现类
 *
 * @author legendshop
 * @since 2022-05-08 09:37:11
 */
@Slf4j
@Service
public class DraftProductServiceImpl implements DraftProductService {

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private AppointDao appointDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private DraftSkuDao draftSkuDao;

	@Autowired
	private TransportDao transportDao;

	@Autowired
	private DraftProductDao draftProductDao;

	@Autowired
	private DraftSkuService draftSkuService;

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	@Autowired
	private ShopDetailApi shopDetailApi;

	@Autowired
	private PreSellProductDao preSellProductDao;

	@Autowired
	private DraftSkuConverter draftSkuConverter;

	@Autowired
	private DraftProductConverter draftProductConverter;

	@Autowired
	private ProductProducerService productProducerService;

	@Autowired
	private ProductPropertyImageDao productPropertyImageDao;

	@Autowired
	private ProductPropertyValueService productPropertyValueService;


	@Autowired
	private ProductPropertyImageConverter productPropertyImageConverter;

	@Autowired
	private MessageApi messageApi;

	@Autowired
	private AdminUserApi adminUserApi;

	@Override
	public ProductBO getProductBOByProductId(Long productId) {
		log.info("draftProductDao getby productID");
		DraftProduct draftProduct = draftProductDao.getByProductId(productId);
		if (null == draftProduct) {
			return null;
		}

		ProductBO productBO = draftProductConverter.convert2ProductBO(draftProduct);
		productBO.setStocks(skuDao.getProductStock(productId));

		// 预售信息
		//预售商品基本信息
		log.info("productBO.getPreSellFlag() is :{} ", productBO.getPreSellFlag());
		if (productBO.getPreSellFlag()) {
			PreSellProductBO preSellProductBO = JSONUtil.toBean(draftProduct.getPreSellProduct(), PreSellProductBO.class);
			if (ObjectUtil.isNotEmpty(preSellProductBO.getPreSaleStart())) {
				preSellProductBO.setSaleStart(preSellProductBO.getPreSaleStart().getTime());
			}
			if (ObjectUtil.isNotEmpty(preSellProductBO.getPreSaleEnd())) {
				preSellProductBO.setSaleEnd(preSellProductBO.getPreSaleEnd().getTime());
			}
			if (ObjectUtil.isNotEmpty(preSellProductBO.getPreDeliveryTime())) {
				preSellProductBO.setDeliveryTime(preSellProductBO.getPreDeliveryTime().getTime());
			}
			if (ObjectUtil.isNotEmpty(preSellProductBO.getPreDeliveryEndTime())) {
				preSellProductBO.setDeliveryEndTime(preSellProductBO.getPreDeliveryEndTime().getTime());
			}

			preSellProductBO.setStatus(0);
			if ((preSellProductBO.getPayPctType().equals(PreSellPayType.DEPOSIT.value()) || preSellProductBO.getPayPctType().equals(PreSellPayType.FULL_AMOUNT.value()))
					&& preSellProductBO.getPreSaleStart().before(new Date())
					&& preSellProductBO.getPreSaleEnd().after(new Date())) {
				preSellProductBO.setStatus(1);
			}


			//预售定金、尾款时间
			if (PreSellPayType.DEPOSIT.value().equals(preSellProductBO.getPayPctType())) {
				preSellProductBO.setDepositStarts(preSellProductBO.getDepositStart().getTime());
				preSellProductBO.setDepositEnds(preSellProductBO.getDepositEnd().getTime());
				preSellProductBO.setFinalMStarts(preSellProductBO.getFinalMStart().getTime());
				preSellProductBO.setFinalMEnds(preSellProductBO.getFinalMEnd().getTime());
			}
			productBO.setPreSellProductBO(preSellProductBO);
		}

		//解析参数
		ProductPropertyBO productPropertyBO = this.structureParameter(productBO.getParameter(), productBO.getUserParameter());
		//解析参数组
		List<ProductPropertyBO> productPropertyBOList = this.structureParamGroups(productBO.getParamGroup(), productBO.getUserParamGroup());
		//合并解析结果
		if (ObjectUtil.isNotEmpty(productPropertyBO)) {
			productPropertyBOList.add(productPropertyBO);
		}
		productBO.setParamGroupBOList(productPropertyBOList);
		log.info("解析完毕，置空不需要的字段");
		//解析完毕，置空不需要的字段
		productBO.setParameter(null);
		productBO.setUserParameter(null);
		productBO.setParamGroup(null);
		productBO.setUserParamGroup(null);

		//解析商品图片json字符串转化为list
		List<String> prodPics = JSONUtil.toList(JSONUtil.parseArray(draftProduct.getImgPath()), String.class);
		productBO.setProductPics(prodPics);
		log.info("解析商品图片json字符串转化为list，productBO:{}", productBO);
		return productBO;
	}

	@Override
	public ProductUpdateBO getProductUpdateBOByProductId(Long productId, Long shopId) {
		DraftProduct draftProduct = draftProductDao.getByProductId(productId);
		if (null == draftProduct) {
			return null;
		}

		if (!draftProduct.getShopId().equals(shopId)) {
			return null;
		}

		ProductUpdateBO productUpdateBO = draftProductConverter.convert2ProductUpdateBO(draftProduct);

		ProductBO productBO = productDao.getProductBO(productId);
		productUpdateBO.setStatus(productBO.getStatus());
		productUpdateBO.setDraftStatus(draftProduct.getStatus());

		// 获取预约上架
		if (StrUtil.isNotBlank(draftProduct.getAppoint())) {
			Appoint appoint = JSONUtil.toBean(draftProduct.getAppoint(), Appoint.class);
			productUpdateBO.setAppointTime(appoint.getOnSellDate());
		}

		// 解析参数
		productUpdateBO.setParamBOList(this.structureParameterList(productUpdateBO.getParameter(), productUpdateBO.getUserParameter()));

		// 解析参数组
		List<ProductPropertyBO> productPropertyBOList = JSONUtil.toList(JSONUtil.parseArray(productUpdateBO.getParamGroup()), ProductPropertyBO.class);
		productPropertyBOList.addAll(JSONUtil.toList(JSONUtil.parseArray(productUpdateBO.getUserParamGroup()), ProductPropertyBO.class));
		productUpdateBO.setParamGroupBOList(productPropertyBOList);

		// 解析完毕，置空不需要的字段
		productUpdateBO.setParameter(null);
		productUpdateBO.setUserParameter(null);
		productUpdateBO.setParamGroup(null);
		productUpdateBO.setUserParamGroup(null);

		//解析商品图片json字符串转化为list
		List<String> prodPics = JSONUtil.toList(JSONUtil.parseArray(draftProduct.getImgPath()), String.class);
		productUpdateBO.setProductPics(prodPics);

		// 获取sku
		productUpdateBO.setSku(draftSkuService.getByProductId(productId));
		productUpdateBO.setActualStocks(productUpdateBO.getSku().stream().map(SkuBO::getActualStocks).filter(ObjectUtil::isNotEmpty).reduce(0, Integer::sum));

		/* 回显运费模板名称 */
		Transport transport = transportDao.getById(productUpdateBO.getTransId());
		if (ObjectUtil.isNotNull(transport)) {
			productUpdateBO.setTransName(transport.getTransName());
		}

		return productUpdateBO;
	}

	@Override
	public DraftProductDTO getByProductId(Long productId) {
		return draftProductConverter.to(draftProductDao.getByProductId(productId));
	}

	@Override
	public R<Void> save(ProductDTO productDTO) {
		DraftProduct draftProduct = draftProductConverter.convert2DraftProduct(productDTO);
		if (OpStatusEnum.PASS.getValue().equals(productDTO.getOpStatus())) {
			draftProduct.setStatus(DraftProductStatusEnum.PASS.getValue());
		} else {
			draftProduct.setStatus(productDTO.getArraignment() ? DraftProductStatusEnum.WAIT.getValue() : DraftProductStatusEnum.UNPUBLISHED.getValue());
		}

		// 预约上架
		if (ProductOnSaleWayEnum.PRESALE.getValue().equals(productDTO.getOnSaleWay())) {
			Appoint appoint = new Appoint();
			appoint.setProductId(productDTO.getId());
			appoint.setOnSellDate(productDTO.getAppointTime());
			appoint.setOnSellFlag(false);
			appoint.setCreateTime(DateUtil.date());
			draftProduct.setAppoint(JSONUtil.toJsonStr(appoint));
		}

		// 保存限购信息
		ProductQuotaDTO productQuotaDTO = productDTO.getProductQuotaDTO();
		if (null != productQuotaDTO) {
			draftProduct.setQuotaTime(productQuotaDTO.getQuotaTime());
			draftProduct.setQuotaCount(productQuotaDTO.getQuotaCount());
			draftProduct.setQuotaType(productQuotaDTO.getQuotaType());
		}

		draftProduct.setCreateTime(DateUtil.date());
		draftProductDao.save(draftProduct);
		return R.ok();
	}

	@Override
	public R<Void> update(ProductDTO productDTO) {
		Long productId = productDTO.getId();
		Product originProduct = productDao.getById(productId);
		productDTO.setDeliveryType(originProduct.getDeliveryType());
		//获取店铺信息
		R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(productDTO.getShopId());
		ShopDetailDTO shopDetail = shopDetailResult.getData();

		messageApi.sendAdmin(shopDetail.getId(), -1L, "商品审核提醒" + originProduct.getName() + "已经重新编辑需要平台审核");


		return this.update(productDTO, originProduct, shopDetail);
	}

	@Override
	public R<Void> update(ProductDTO productDTO, Product originProduct, ShopDetailDTO shopDetail) {

		if (CollUtil.isEmpty(productDTO.getSku())) {
			log.warn("sku为空！");
			return R.fail("sku为空！");
		}

		//校验预购数量
		if (ObjectUtil.isNotNull(productDTO.getProductQuotaDTO())) {
			ProductQuotaDTO productQuotaDTO = productDTO.getProductQuotaDTO();
			if (StringUtil.isNotBlank(productQuotaDTO.getQuotaType())) {
				if (ObjectUtil.isEmpty(productQuotaDTO.getQuotaCount())) {
					log.warn("最多购买件数不能为空");
					return R.fail("最多购买件数不能为空");
				}

				if (productQuotaDTO.getQuotaCount() < 0) {
					log.warn("最多购买件数不能小于0");
					return R.fail("最多购买件数不能小于0");
				}

				if (ObjectUtil.isEmpty(productQuotaDTO.getQuotaTime())) {
					log.warn("开始生效时间不能为空");
					return R.fail("开始生效时间不能为空");
				}
			}
		}

		if (shopDetail == null
				|| !OpStatusEnum.PASS.getValue().equals(shopDetail.getOpStatus())
				|| !ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetail.getStatus())) {
			log.warn("店铺信息有误，请联系管理员");
			return R.fail("店铺信息有误，请联系管理员");
		}


		if (originProduct == null) {
			return R.fail("商品不存在，修改失败");
		}
		//更新配送类型
		productDao.updateProductDeliveryType(productDTO.getDeliveryType(), originProduct.getId());

		// 获取商品ID
		Long productId = originProduct.getId();

		if (!originProduct.getShopId().equals(productDTO.getShopId())) {
			log.error("修改商品失败，当前商家正在修改其它商家的商品，当前商家ID: {}，商品ID: {}", productDTO.getShopId(), productId);
			return R.fail("商品不存在，修改失败");
		}

		if (OpStatusEnum.PROD_ILLEGAL_LOCK.getValue().equals(originProduct.getOpStatus())) {
			log.warn("商品已被锁定，无法编辑");
			return R.fail("商品已被锁定，无法编辑");
		}

		if (!ProductDelStatusEnum.PROD_NORMAL.getValue().equals(originProduct.getDelStatus())) {
			log.warn("商品已被删除，无法编辑");
			return R.fail("商品已被删除，无法编辑");
		}

		if (CollUtil.isEmpty(productDTO.getImg()) && CollUtil.isEmpty(productDTO.getImageList())) {
			log.warn("商品图片不能为空,请上传图片");
			return R.fail("商品图片不能为空,请上传图片");
		}
		if (ProductOnSaleWayEnum.PRESALE.getValue().equals(productDTO.getOnSaleWay()) && productDTO.getAppointTime() == null) {
			log.warn("商品发布：预约上架时间不能为空");
			return R.fail("预约上架时间不能为空");
		}
		PreSellProductDTO preSell = productDTO.getPreSellProductDTO();
		if (productDTO.getPreSellFlag()) {
			if (ObjectUtil.isEmpty(preSell)) {
				log.warn("商品发布：预售信息不能为空");
				return R.fail("预售信息不能为空");
			}
			if (ProductOnSaleWayEnum.PRESALE.getValue().equals(productDTO.getOnSaleWay()) && productDTO.getAppointTime().after(preSell.getPreSaleStart())) {
				log.warn("商品发布：预约上架时间不能晚于预售时间");
				return R.fail("预约上架时间不能晚于预售时间");
			}
			if (preSell.getPayPctType() == 1 && preSell.getDepositStart().after(preSell.getDepositEnd())) {
				log.warn("定金支付开始时间不能晚于结束时间");
				return R.fail("定金支付开始时间不能晚于结束时间");
			}
			if (preSell.getPreSaleStart().after(preSell.getPreSaleEnd())) {
				log.warn("预售开始时间不能晚于结束时间");
				return R.fail("预售开始时间不能晚于结束时间");
			}
			if (preSell.getFinalMStart() != null && preSell.getFinalMStart().after(preSell.getFinalMEnd())) {
				log.warn("尾款支付开始时间不能晚于结束时间");
				return R.fail("尾款支付开始时间不能晚于结束时间");
			}
			if (preSell.getFinalMStart() != null && preSell.getPreDeliveryTime().before(preSell.getFinalMStart())) {
				log.warn("预售发货时间不能早于尾款支付开始时间");
				return R.fail("预售发货时间不能早于尾款支付开始时间");
			}
		}


		// 获取商品草稿
		DraftProduct draftProduct = draftProductDao.getByProductId(productId);
		if (null == draftProduct) {
			// 如果为空，则为旧数据，需要创建一个新的草稿
			draftProduct = draftProductConverter.convert2DraftProduct(productDTO);
			draftProduct.setCreateTime(DateUtil.date());
		} else {
			if (draftProduct.getStatus().equals(DraftProductStatusEnum.WAIT.getValue())) {
				return R.fail("更新失败，商品正在提审中！");
			}

			Long id = draftProduct.getId();
			Date createTime = draftProduct.getCreateTime();
			draftProduct = draftProductConverter.convert2DraftProduct(productDTO);
			draftProduct.setId(id);
			draftProduct.setCreateTime(createTime);
			draftProduct.setUpdateTime(DateUtil.date());
		}
		draftProduct.setVersion(originProduct.getVersion() + 1);

		Date date = new Date();

		//设置商品图片集合,如果有多个sku组图，取每个组图的第一张图片，否则是商品主图的所有图片
		List<String> imgList;
		if (CollUtil.isNotEmpty(productDTO.getImageList())) {
			imgList = productDTO.getImageList().stream().map(
					productPropertyImageDTO -> productPropertyImageDTO.getImgList().get(0)
			).collect(Collectors.toList());
		} else {
			imgList = productDTO.getImg();
		}


		// 如果选则多规格并且规格样式不是无图样式（是图文样式或者图文样式），设置商品主图为第一张sku图片
		if (productDTO.getMultipleSpecificationFlag() && !productDTO.getSpecificationStyle().equals(ProductSpecificationStyle.TXT.getValue())) {
			draftProduct.setPic(imgList.get(0));
		} else {
			// 设置商品搜索主图
			draftProduct.setPic(CollUtil.isNotEmpty(productDTO.getImg()) ? productDTO.getImg().get(0) : imgList.get(0));
		}
		// 设置商品搜索图片集合
		draftProduct.setImgPath(JSONUtil.toJsonStr(imgList));

		// sku组图
		if (ObjectUtil.isNotEmpty(productDTO.getImageList())) {
			productDTO.getImageList().forEach(productPropertyImageDTO -> {
				productPropertyImageDTO.setProductId(productId);
				productPropertyImageDTO.setCreateDate(date);
				List<String> imgList1 = productPropertyImageDTO.getImgList();
				productPropertyImageDTO.setUrl(JSONUtil.toJsonStr(imgList1));
			});
			List<ProductPropertyImage> propertyImageList = productPropertyImageConverter.from(productDTO.getImageList());
			draftProduct.setPropertyImage(JSONUtil.toJsonStr(propertyImageList));
		}

		// 更新sku
		List<DraftSkuDTO> actuallySkuList = draftSkuService.updateSku(productDTO, originProduct, date);

		//解析商品分类数组
		int size = productDTO.getGlobalCategoryId().size();
		draftProduct.setGlobalFirstCatId(productDTO.getGlobalCategoryId().get(0));
		draftProduct.setGlobalSecondCatId(size > 1 ? productDTO.getGlobalCategoryId().get(1) : null);
		draftProduct.setGlobalThirdCatId(size > 2 ? productDTO.getGlobalCategoryId().get(2) : null);

		//解析店铺商品分类数组
		if (ObjectUtil.isNotEmpty(productDTO.getShopCategoryId())) {
			int shopsize = productDTO.getShopCategoryId().size();
			draftProduct.setShopFirstCatId(shopsize > 0 ? productDTO.getShopCategoryId().get(0) : null);
			draftProduct.setShopSecondCatId(shopsize > 1 ? productDTO.getShopCategoryId().get(1) : null);
			draftProduct.setShopThirdCatId(shopsize > 2 ? productDTO.getShopCategoryId().get(2) : null);
		}

		// 整合sku
		BigDecimal max = new BigDecimal(0L);
		BigDecimal min = actuallySkuList.get(0).getPrice();
		for (DraftSkuDTO sku : actuallySkuList) {
			//计算销售价的范围
			max = max.compareTo(sku.getPrice()) >= 0 ? max : sku.getPrice();
			min = min.compareTo(sku.getPrice()) <= 0 ? min : sku.getPrice();
		}

		// 更新草稿商品价格
		draftProduct.setPrice(min.compareTo(max) == 0 ? formatPrice(min) : formatPrice(min) + " ~ " + formatPrice(max));
		draftProduct.setMinPrice(min);
		draftProduct.setMaxPrice(max);
		draftProduct.setUpdateTime(date);

		// 解析规格
		draftProduct.setSpecification(JSONUtil.toJsonStr(productDTO.getSpecificationList()));

		//解析参数组
		if (CollUtil.isNotEmpty(productDTO.getParamGroupList())) {
			draftProduct.setParamGroup(JSONUtil.toJsonStr(productDTO.getParamGroupList().stream()
					.filter(paramPropertyGroupDTO -> paramPropertyGroupDTO.getSource()
							.equals(ProductPropertySourceEnum.SYSTEM.getValue())).collect(Collectors.toList())));
			draftProduct.setUserParamGroup(JSONUtil.toJsonStr(productDTO.getParamGroupList().stream()
					.filter(paramPropertyGroupDTO -> paramPropertyGroupDTO.getSource()
							.equals(ProductPropertySourceEnum.USER.getValue())).collect(Collectors.toList())));
		}

		//判断该店铺是否需要审核商品，如果店铺是不需要审核且不是违规下架商品
		if (ObjectUtil.isNotEmpty(shopDetail.getProdRequireAudit())
				&& shopDetail.getProdRequireAudit() == 0
				&& productDTO.getArraignment()
				&& !OpStatusEnum.PROD_ILLEGAL_OFF.getValue().equals(originProduct.getOpStatus())
				&& !OpStatusEnum.PROD_ILLEGAL_LOCK.getValue().equals(originProduct.getOpStatus())) {
			draftProduct.setStatus(OpStatusEnum.PASS.getValue());
		} else {
			draftProduct.setStatus(productDTO.getArraignment() ? DraftProductStatusEnum.WAIT.getValue() : DraftProductStatusEnum.UNPUBLISHED.getValue());
		}

		//预约上架
		if (ProductOnSaleWayEnum.PRESALE.getValue().equals(productDTO.getOnSaleWay())) {
			Appoint appoint = new Appoint();
			appoint.setProductId(productId);
			appoint.setOnSellDate(productDTO.getAppointTime());
			appoint.setOnSellFlag(false);
			appoint.setCreateTime(date);
			draftProduct.setAppoint(JSONUtil.toJsonStr(appoint));
		}

		// 保存限购信息
		ProductQuotaDTO productQuotaDTO = productDTO.getProductQuotaDTO();
		if (null != productQuotaDTO) {
			draftProduct.setQuotaTime(productQuotaDTO.getQuotaTime());
			draftProduct.setQuotaCount(productQuotaDTO.getQuotaCount());
			draftProduct.setQuotaType(productQuotaDTO.getQuotaType());
		}

		List<ProductPropertyValueDTO> productPropertyValueList = new ArrayList<>();
		if (productDTO.getCustomPropertyValueList() != null && productDTO.getCustomPropertyValueList().size() > 0) {
			//收集用户自定义的规格、参数值
			productDTO.getCustomPropertyValueList().forEach(e -> {
				e.getProdPropList().forEach(i -> {
					ProductPropertyValueDTO productPropertyValue = new ProductPropertyValueDTO();
					productPropertyValue.setName(i.getName());
					productPropertyValue.setId(i.getId());
					productPropertyValue.setPropId(e.getId());
					productPropertyValue.setShopId(productDTO.getShopId());
					productPropertyValue.setSequence(0);//排序暂时取0
					productPropertyValue.setDeleteFlag(false);
					productPropertyValue.setCreateTime(date);
					productPropertyValueList.add(productPropertyValue);
				});
			});
		}

		//保存属性值
		if (CollUtil.isNotEmpty(productPropertyValueList)) {
			draftProduct.setCustomPropertyValue(JSONUtil.toJsonStr(productPropertyValueList));
		}

		draftProductDao.saveOrUpdate(draftProduct);

		// 如果店铺不需要审核，则直接发布
		if (DraftProductStatusEnum.PASS.getValue().equals(draftProduct.getStatus())) {
			return this.arraignment(productId);
		} else if (DraftProductStatusEnum.UNPUBLISHED.getValue().equals(draftProduct.getStatus())) {
			// 如果是未发布的，则直接更新
			return this.updateProduct(draftProduct, "");
		}
		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> release(Long productId, String auditOpinion) {
		DraftProduct draftProduct = draftProductDao.getByProductId(productId);

		// 如果为空，则为旧数据
		if (null == draftProduct) {
			log.info("当前商品无草稿，直接通过！");
			return R.ok();
		}

		if (!draftProduct.getStatus().equals(DraftProductStatusEnum.PASS.getValue())) {
			draftProduct.setStatus(DraftProductStatusEnum.PASS.getValue());
			draftProductDao.update(draftProduct);
		}

		return this.updateProduct(draftProduct, auditOpinion);
	}

	@Override
	public R audit(AuditDTO auditDTO) {
		//查询商品信息
		List<Product> productList = productDao.queryAllByIds(auditDTO.getIdList());
		if (CollUtil.isEmpty(productList)) {
			return R.fail("审核失败，需要审核的商品不存在！");
		}

		// 如果存在草稿，则是新的商品审核数据，走新的草稿审核流程
		List<DraftProduct> draftProducts = draftProductDao.queryByProductId(auditDTO.getIdList());
		if (draftProducts.stream().anyMatch(e -> !DraftProductStatusEnum.WAIT.getValue().equals(e.getStatus()))) {
			//创建索引
			amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_CREATE_ROUTING_KEY, auditDTO.getIdList(), true);
			return R.fail("审核失败，只有待审核商品可以被审核！");
		}

		// 新草稿审核流程
		if (CollUtil.isNotEmpty(draftProducts)) {
			for (DraftProduct draftProduct : draftProducts) {
				draftProduct.setStatus(OpStatusEnum.PASS.getValue().equals(auditDTO.getOpStatus()) ? DraftProductStatusEnum.PASS.getValue() : DraftProductStatusEnum.DENY.getValue());
				this.updateProduct(draftProduct, auditDTO.getAuditOpinion());
			}
			draftProductDao.update(draftProducts);
		}

		// 获取改造前的商品审核数据，走旧的审核流程
		List<Long> draftProductIds = draftProducts.stream().map(DraftProduct::getProductId).collect(Collectors.toList());
		List<Product> oldProductList = productList.stream().filter(e -> !draftProductIds.contains(e.getId())).collect(Collectors.toList());

		if (CollUtil.isNotEmpty(oldProductList)) {
			/*如果审核通过，更新商品状态，并创建商品索引*/
			int result = productDao.updateOpStatusAndAuditOpinion(oldProductList.stream().map(Product::getId).collect(Collectors.toList()), auditDTO.getOpStatus(), auditDTO.getAuditOpinion());
			if (OpStatusEnum.PASS.getValue().equals(auditDTO.getOpStatus())) {

				//获取店铺信息
				R<List<ShopDetailDTO>> shopDetailResult = shopDetailApi.queryByIds(oldProductList.stream().map(Product::getShopId).collect(Collectors.toList()));
				Map<Long, ShopDetailDTO> shopDetailMap = shopDetailResult.getData().stream().collect(Collectors.toMap(ShopDetailDTO::getId, e -> e));

				List<Long> products = new ArrayList<>();
				//获取实物商品、非预约上架、立马上架的商品id集合
				List<Long> productIds = oldProductList.stream().filter(product -> {
					ShopDetailDTO shopDetailDTO = shopDetailMap.get(product.getShopId());
					if (shopDetailDTO == null
							|| !OpStatusEnum.PASS.getValue().equals(shopDetailDTO.getOpStatus())
							|| !ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetailDTO.getStatus())) {
						products.add(product.getId());
						return false;
					}
					if (ProductTypeEnum.ENTITY.getValue().equals(product.getProdType()) && ObjectUtil.isEmpty(product.getAppointId())
							&& ProductOnSaleWayEnum.ONSALE.getValue().equals(product.getOnSaleWay())) {
						return true;
					}
					return false;
				}).map(product1 -> {
					return product1.getId();
				}).collect(Collectors.toList());
				if (CollUtil.isNotEmpty(productIds)) {
					this.productDao.updateStatus(ProductStatusEnum.PROD_ONLINE.value(), productIds);
				}

				// 店铺下线状态下审核商品时，下架商品
				if (CollUtil.isNotEmpty(products)) {
					this.productDao.updateStatus(ProductStatusEnum.PROD_OFFLINE.value(), productIds);
				}
			}

			//预约上架商品预约上架处理
			List<Long> appointIdList = oldProductList.stream()
					.filter(product -> ProductTypeEnum.ENTITY.getValue().equals(product.getProdType())
							&& ObjectUtil.isNotEmpty(product.getAppointId()))
					.map(Product::getId).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(appointIdList)) {
				log.info("商品审核通过，发现预约上架商品，开始发送延时队列，商品ID: {}", appointIdList);
				List<Appoint> appointList = appointDao.queryByProductId(appointIdList);
				/*每件商品的上架时间不一样，要一个个往队列里面放*/
				for (Appoint appoint : appointList) {
					productProducerService.appointOnLine(appoint.getProductId(), appoint.getOnSellDate());
				}
			}
		}
		//创建索引
		amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_CREATE_ROUTING_KEY, auditDTO.getIdList(), true);
		return R.ok("审核成功");
	}

	@Override
	public R<Void> arraignment(Long productId) {
		DraftProduct draftProduct = draftProductDao.getByProductId(productId);
		if (null == draftProduct) {
			log.info("草稿为空，不需要提审！");
			return R.fail("草稿为空，不需要提审！");
		}

		// 如果为待审核，则直接返回
		if (DraftProductStatusEnum.WAIT.getValue().equals(draftProduct.getStatus())) {
			return R.ok();
		}

		// 校验当前商品是否可以提审覆盖
		checkSkuActivity(productId);

		R<ShopDetailDTO> shopDetailDTOR = shopDetailApi.getById(draftProduct.getShopId());
		ShopDetailDTO shopDetail = shopDetailDTOR.getData();
		//判断该店铺是否需要审核商品
		if (ObjectUtil.isNotEmpty(shopDetail.getProdRequireAudit())
				&& shopDetail.getProdRequireAudit() == 0) {
			draftProduct.setStatus(DraftProductStatusEnum.PASS.getValue());
			draftProduct.setUpdateTime(DateUtil.date());
			draftProductDao.update(draftProduct);

			// 更新商品信息
			this.updateProduct(draftProduct, "");
		} else {
			// 更新成待审核状态
			draftProductDao.updateStatus(productId, draftProduct.getStatus(), DraftProductStatusEnum.WAIT);
		}

		return R.ok();
	}

	@Override
	public Integer deleteByProductId(List<Long> productIds) {
		Integer delete = draftProductDao.deleteByProductId(productIds);
		Integer skuDelete = draftSkuDao.deleteByProductId(productIds);
		return delete + skuDelete;
	}

	@Override
	public Integer deleteByProductId(Long productId) {
		Integer delete = draftProductDao.deleteByProductId(productId);
		Integer skuDelete = draftSkuDao.deleteByProductId(productId);
		return delete + skuDelete;
	}

	@Override
	public R revokeArraignment(Long productId) {
		DraftProduct draftProduct = draftProductDao.getByProductId(productId);
		if (null == draftProduct) {
			log.info("草稿为空，不需要提审！");
			return R.fail("草稿为空，不需要提审！");
		}

		// 如果不为待审核，则直接返回
		if (!DraftProductStatusEnum.WAIT.getValue().equals(draftProduct.getStatus())) {
			return R.ok();
		}

		// 更新成待审核状态
		draftProductDao.updateStatus(productId, draftProduct.getStatus(), DraftProductStatusEnum.UNPUBLISHED);
		return R.ok();
	}

	@Override
	public R updateProduct(DraftProduct draftProduct, String auditOpinion) {
		Long productId = draftProduct.getProductId();
		Integer productStatus = draftProduct.getStatus();

		Product originalProduct = productDao.getById(productId);

		// 如果商品不是未发布，或者草稿不是已通过审核，则不需要更新商品
		if (!(ProductStatusEnum.UNPUBLISHED.getValue().equals(originalProduct.getStatus()) || DraftProductStatusEnum.PASS.getValue().equals(draftProduct.getStatus()))) {
			log.info("当前草稿不需要更新商品，商品ID：{}", productId);
			return R.ok();
		}

		Product product = draftProductConverter.convert2Product(draftProduct);
		product.setUpdateTime(DateUtil.date());
		product.setAuditOpinion(auditOpinion);

		if (DraftProductStatusEnum.PASS.getValue().equals(productStatus)) {
			product.setOpStatus(OpStatusEnum.PASS.getValue());

			//上架方式
			if (ProductOnSaleWayEnum.ONSALE.getValue().equals(draftProduct.getOnSaleWay())) {
				product.setStatus(ProductStatusEnum.PROD_ONLINE.getValue());
			} else {
				product.setStatus(ProductStatusEnum.PROD_OFFLINE.getValue());
			}
		} else {
			product.setStatus(originalProduct.getStatus());
			product.setOpStatus(originalProduct.getOpStatus());
		}

		// 保存sku组图
		String propertyImage = draftProduct.getPropertyImage();
		productPropertyImageDao.deleteByProductId(productId);
		if (StrUtil.isNotBlank(propertyImage)) {
			List<ProductPropertyImage> productPropertyImages = JSONUtil.toList(JSONUtil.parseArray(propertyImage), ProductPropertyImage.class);
			productPropertyImageDao.save(productPropertyImages);
		}

		// 获取原sku
		List<Sku> skuList = skuDao.getSkuByProductId(productId);

		// 获取草稿sku
		List<DraftSku> draftSkuList = draftSkuDao.getSkuByProductId(productId);

		// 如果都是单规格，则直接赋值
		if (!draftProduct.getMultipleSpecificationFlag() && !originalProduct.getMultipleSpecificationFlag()) {
			// 如果原商品和新商品都是单sku，则直接获取原skuId
			draftSkuList.get(0).setSkuId(skuList.get(0).getId());
		} else {
			// 否则，需要匹配规格属性ID，获取原skuId
			for (Sku skuDTO : skuList) {
				// 按;分隔规格
				List<String> newProperties = StrUtil.split(skuDTO.getProperties(), ';');

				// 排序，方便后续比较
				Collections.sort(newProperties);

				// 获取与上面新的sku规格相同的旧sku
				Optional<DraftSku> optional = draftSkuList.stream().filter(e -> {
					List<String> originProperties = StrUtil.split(e.getProperties(), ';');
					Collections.sort(originProperties);
					return newProperties.equals(originProperties);
				}).findFirst();

				// 如果存在，则将旧skuId赋值到新sku上
				optional.ifPresent(draftSku -> {
					draftSku.setSkuId(skuDTO.getId());
				});
			}
		}

		Map<Long, DraftSku> draftSkuMap = draftSkuList.stream().collect(Collectors.toMap(DraftSku::getSkuId, e -> e));

		// 获取需要删除的sku
		List<Sku> deleteList = skuList.stream().filter(e -> !draftSkuMap.containsKey(e.getId())).collect(Collectors.toList());
		skuDao.delete(deleteList);

		// 获取需要更新的sku
		List<Sku> updateList = skuList.stream().filter(e -> draftSkuMap.containsKey(e.getId())).collect(Collectors.toList());
		for (Sku sku : updateList) {
			DraftSku draftSku = draftSkuMap.get(sku.getId());
			BeanUtil.copyProperties(draftSku, sku, "createTime", "updateTime", "id");
			sku.setUpdateTime(DateUtil.date());
			skuDao.updateProperties(sku, "cnProperties", "properties", "userProperties", "originalPrice",
					"price", "costPrice", "name", "partyCode", "modelId", "pic", "volume", "weight", "updateTime");
		}

		// 获取需要新增的sku
		List<Long> updateSkuIds = updateList.stream().map(Sku::getId).collect(Collectors.toList());
		List<Sku> saveList = draftSkuConverter.convert2Sku(draftSkuList.stream().filter(e -> !updateSkuIds.contains(e.getSkuId())).collect(Collectors.toList()));
		skuDao.saveWithId(saveList);

		// 更新商品SPU库存
		product.setStocks(updateList.stream().map(Sku::getStocks).reduce(0, Integer::sum));
		product.setActualStocks(updateList.stream().map(Sku::getActualStocks).reduce(0, Integer::sum));

		// 预约上架
		Appoint originAppoint = appointDao.queryByProductId(productId);
		if (ProductOnSaleWayEnum.PRESALE.getValue().equals(product.getOnSaleWay())) {
			Appoint appoint = JSONUtil.toBean(draftProduct.getAppoint(), Appoint.class);

			// 如果原来就有预约上架信息，就更新，没有就添加
			if (ObjectUtil.isNotEmpty(originAppoint)) {
				originAppoint.setOnSellDate(appoint.getOnSellDate());
				originAppoint.setUpdateTime(DateUtil.date());
				appointDao.update(originAppoint);
				product.setAppointId(originAppoint.getId());
			} else {
				product.setAppointId(appointDao.save(appoint));
			}

			// 发送到点上架队列
			if (DraftProductStatusEnum.PASS.getValue().equals(productStatus)) {
				productProducerService.appointOnLine(appoint.getProductId(), appoint.getOnSellDate());
			}

		} else {
			//删除预约信息
			product.setAppointId(null);
			appointDao.delete(originAppoint);
		}

		// 预售信息
		List<PreSellProduct> preSellProducts = preSellProductDao.queryByProductId(productId);
		if (draftProduct.getPreSellFlag()) {
			PreSellProduct preSell = JSONUtil.toBean(draftProduct.getPreSellProduct(), PreSellProduct.class);

			//如果全额支付，预售支付百分比为100%，尾款支付时间为空
			if (preSell.getPayPctType().compareTo(0) == 0) {
				preSell.setPayPct(BigDecimal.valueOf(100));
				preSell.setFinalMStart(null);
				preSell.setFinalMEnd(null);
			}

			//如果原来就有预售信息，就更新，没有就添加
			if (ObjectUtil.isNotEmpty(preSellProducts)) {
				PreSellProduct originPreSell = preSellProducts.get(0);
				originPreSell.setUpdateTime(DateUtil.date());
				BeanUtil.copyProperties(preSell, originPreSell);
				preSellProductDao.update(originPreSell);
			} else {
				preSell.setCreateTime(DateUtil.date());
				preSell.setProductId(productId);
				preSellProductDao.save(preSell);
			}

		} else {
			//删除预售信息
			preSellProductDao.delete(preSellProducts);
		}

		// 保存属性值
		String customPropertyValue = draftProduct.getCustomPropertyValue();
		if (StrUtil.isNotBlank(customPropertyValue)) {
			List<ProductPropertyValueDTO> productPropertyValueList = JSONUtil.toList(JSONUtil.parseArray(customPropertyValue), ProductPropertyValueDTO.class);
			if (CollUtil.isNotEmpty(productPropertyValueList) && productPropertyValueList.size() > 0) {
				productPropertyValueService.saveWithId(productPropertyValueList);
			}
			draftProduct.setCustomPropertyValue(null);
			draftProduct.setUpdateTime(DateUtil.date());
			draftProductDao.update(draftProduct);
		}

		// 更新商品
		productDao.updateProperties(product, "globalFirstCatId", "globalSecondCatId", "globalThirdCatId", "shopFirstCatId", "shopSecondCatId", "shopThirdCatId", "preSellFlag",
				"onSaleWay", "appointId", "specificationStyle", "video", "partyCode", "name", "price", "minPrice", "maxPrice",
				"brief", "contentM", "content", "status", "opStatus", "stocksArm", "prodType", "multipleSpecificationFlag", "keyWord",
				"parameter", "userParameter", "specification", "paramGroup", "userParamGroup", "brandId", "transId", "stockCounting", "updateTime",
				"wxACode", "shopWxCode", "imgPath", "pic", "mainSpecificationId", "version", "quotaType", "quotaCount", "quotaTime", "auditOpinion", "stocks", "actualStocks");

		//创建索引
		amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_CREATE_ROUTING_KEY, ImmutableList.of(productId), true);

		return R.ok();
	}


	/**
	 * 对价格保留2位小数，并转换成字符串
	 *
	 * @param bg
	 * @return
	 */
	private String formatPrice(BigDecimal bg) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(bg) + "";
	}

	/**
	 * 解析商品基本参数和自定义参数json数据构建其他参数组
	 * 步骤
	 * 1.创建参数列表
	 * 2.解析基本参数json字符串，过滤掉未选中（selectFlag=false）的参数值，过滤掉空的参数，存入参数列表
	 * 3.解析自定义参数json字符串，过滤掉未选中（selectFlag=false）的参数值，过滤掉空的参数，存入参数列表
	 * 4.创建其他参数组对象，将参数组名默认设为 “其他参数” ，将上面解析得到的参数列表存入其他参数组对象
	 *
	 * @param parameter     基本参数
	 * @param userParameter 自定义参数
	 * @return allProductPropertyBO  其他参数组
	 */
	public ProductPropertyBO structureParameter(String parameter, String userParameter) {

		List<ProductPropertyBO> productPropertyBOList = new ArrayList<>();

		if (StrUtil.isNotBlank(parameter)) {
			List<ProductPropertyBO> parameterList = JSONUtil.toList(JSONUtil.parseArray(parameter), ProductPropertyBO.class);
			//过滤掉selectFlag=false的参数值
			parameterList.forEach(productPropertyBO -> productPropertyBO.setProdPropList(productPropertyBO.getProdPropList().stream()
					.filter(ProductPropertyValueBO::getSelectFlag).collect(Collectors.toList())));
			//过滤掉prodPropList为空的参数对象
			parameterList = parameterList.stream().filter(productPropertyBO -> {
				return CollUtil.isNotEmpty(productPropertyBO.getProdPropList());
			}).collect(Collectors.toList());
			productPropertyBOList.addAll(parameterList);
		}

		if (StrUtil.isNotBlank(userParameter)) {
			List<ProductPropertyBO> userParameterList = JSONUtil.toList(JSONUtil.parseArray(userParameter), ProductPropertyBO.class);
			//过滤掉selectFlag=false的参数值
			userParameterList.forEach(productPropertyBO -> {
				productPropertyBO.setProdPropList(productPropertyBO.getProdPropList().stream()
						.filter(ProductPropertyValueBO::getSelectFlag).collect(Collectors.toList()));
			});
			//过滤掉prodPropList为空的参数对象
			userParameterList = userParameterList.stream().filter(productPropertyBO -> {
				return CollUtil.isNotEmpty(productPropertyBO.getProdPropList());
			}).collect(Collectors.toList());
			productPropertyBOList.addAll(userParameterList);
		}
		//创建其他参数组对象
		ProductPropertyBO allProductPropertyBO = null;
		if (CollUtil.isNotEmpty(productPropertyBOList)) {
			allProductPropertyBO = new ProductPropertyBO();
			allProductPropertyBO.setGroupName("其他参数");
			allProductPropertyBO.setProductPropertyBOList(productPropertyBOList);
		}
		return allProductPropertyBO;
	}

	/**
	 * 解析商品基本参数和自定义参数json数据构建其他参数组
	 *
	 * @param parameter     基本参数
	 * @param userParameter 自定义参数
	 * @return allProductPropertyBO  其他参数组
	 */
	public List<ProductPropertyBO> structureParameterList(String parameter, String userParameter) {

		List<ProductPropertyBO> productParameterList = new ArrayList<>();

		List<ProductPropertyDTO> systemParamList = JSONUtil.toList(JSONUtil.parseArray(parameter), ProductPropertyDTO.class);
		List<ProductPropertyDTO> userParamList = JSONUtil.toList(JSONUtil.parseArray(userParameter), ProductPropertyDTO.class);
		List<ProductPropertyDTO> productParamList = systemParamList.stream().map(e -> {
			e.setSource(ProductPropertySourceEnum.SYSTEM.value());
			return e;
		}).collect(Collectors.toList());
		productParamList.addAll(userParamList.stream().map(e -> {
			e.setSource(ProductPropertySourceEnum.USER.value());
			return e;
		}).collect(Collectors.toList()));
		/*将ProductParamDTO对象转换成ProductPropertyBO对象*/
		for (ProductPropertyDTO productParamDTO : productParamList) {
			ProductPropertyBO productPropertyBO = new ProductPropertyBO();
			productPropertyBO.setId(productParamDTO.getId());
			productPropertyBO.setPropName(productParamDTO.getPropName());
			productPropertyBO.setSource(productParamDTO.getSource());
			List<ProductPropertyValueDTO> valueList = productParamDTO.getProdPropList();
			if (valueList != null && valueList.size() > 0) {
				for (ProductPropertyValueDTO s : valueList) {
					ProductPropertyValueBO productPropertyValueBO = new ProductPropertyValueBO();
					productPropertyValueBO.setId(s.getId());
					productPropertyValueBO.setPropId(productParamDTO.getId());
					productPropertyValueBO.setName(s.getName());
					/*设置select为true*/
					productPropertyValueBO.setSelectFlag(s.getSelectFlag());
					if (productPropertyBO.getProdPropList() == null) {
						productPropertyBO.setProdPropList(new ArrayList<>());
					}
					productPropertyBO.addProductPropertyValueList(productPropertyValueBO);
				}
			}
			productParameterList.add(productPropertyBO);
		}
		return productParameterList;
	}

	/**
	 * 解析商品基本参数组和自定义参数组json数据构建参数组列表
	 * 步骤
	 * 1.创建参数列表
	 * 2.解析基本参数json字符串，过滤掉未选中（selectFlag=false）的参数值，过滤掉空的参数，存入参数列表
	 * 3.解析自定义参数json字符串，过滤掉未选中（selectFlag=false）的参数值，过滤掉空的参数，存入参数列表
	 * 4.遍历参数列表，创建参数组对象，参数组名为参数对象里的组名 ，参数对象存入参数列表，因为每个参数列表都是唯一的，以此不用根据参数组名合并参数对象，每个参数列表里只有一个对象
	 * 5.将参数组对象存入参数组列表中
	 *
	 * @param paramGroup     系统参数组
	 * @param userParamGroup 自定义参数组
	 * @return paramGroupBOList  参数组列表
	 */
	private List<ProductPropertyBO> structureParamGroups(String paramGroup, String userParamGroup) {
		List<ProductPropertyBO> productPropertyBOS = new ArrayList<>();
		//解析系统参数组
		if (StrUtil.isNotBlank(paramGroup)) {
			List<ProductPropertyBO> paramGroupList = JSONUtil.toList(JSONUtil.parseArray(paramGroup), ProductPropertyBO.class);
			//过滤掉selectFlag=false的参数值
			paramGroupList.forEach(productPropertyBO -> {
				productPropertyBO.setProdPropList(productPropertyBO.getProdPropList().stream()
						.filter(ProductPropertyValueBO::getSelectFlag).collect(Collectors.toList()));
			});
			//过滤掉prodPropList为空的参数组对象
			paramGroupList = paramGroupList.stream()
					.filter(productPropertyDTO -> CollUtil.isNotEmpty(productPropertyDTO.getProdPropList()))
					.collect(Collectors.toList());
			productPropertyBOS.addAll(paramGroupList);
		}
		//解析自定义参数组
		if (StrUtil.isNotBlank(userParamGroup)) {
			List<ProductPropertyBO> userParamGroupList = JSONUtil.toList(JSONUtil.parseArray(userParamGroup), ProductPropertyBO.class);
			//过滤掉selectFlag=false的参数值
			userParamGroupList.forEach(
					productPropertyBO -> productPropertyBO
							.setProdPropList(productPropertyBO.getProdPropList().stream()
									.filter(ProductPropertyValueBO::getSelectFlag).collect(Collectors.toList())));
			//过滤掉prodPropList为空的参数组对象
			userParamGroupList = userParamGroupList.stream().filter(productPropertyDTO -> {
				return CollUtil.isNotEmpty(productPropertyDTO.getProdPropList());
			}).collect(Collectors.toList());

			productPropertyBOS.addAll(userParamGroupList);
		}
		//合并相同的参数组
		Map<Long, List<ProductPropertyBO>> listMap = productPropertyBOS.stream().filter(e -> ObjectUtil.isNotEmpty(e.getGroupId())).collect(Collectors.groupingBy(ProductPropertyBO::getGroupId));
		//将参数对象和参数名存入到参数组列表中
		List<ProductPropertyBO> paramGroupBOList = new ArrayList<>();
		if (CollUtil.isNotEmpty(listMap.keySet())) {
			for (Long groupId : listMap.keySet()) {
				ProductPropertyBO propertyBO = new ProductPropertyBO();
				List<ProductPropertyBO> productPropertyBOList = listMap.get(groupId);
				ProductPropertyBO productPropertyBO = productPropertyBOList.get(0);
				propertyBO.setGroupName(productPropertyBO.getGroupName());
				propertyBO.setId(productPropertyBO.getGroupId());
				propertyBO.setProductPropertyBOList(productPropertyBOList);
				paramGroupBOList.add(propertyBO);
			}
		}
		return paramGroupBOList;
	}


	/**
	 * 校验sku是否参与活动
	 *
	 * @param productId
	 */
	private void checkSkuActivity(Long productId) {
		List<Sku> originSku = skuDao.getSkuByProductId(productId);
		List<Long> skuIds = originSku.stream().map(Sku::getId).collect(Collectors.toList());
		if (originSku.stream().anyMatch(e -> e.getIntegralDeductionFlag() || e.getIntegralFlag())) {
			throw new BusinessException("商品提审失败，该商品正在参与积分活动");
		}
	}
}
