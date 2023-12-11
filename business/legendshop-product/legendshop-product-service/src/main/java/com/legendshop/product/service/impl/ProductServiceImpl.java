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
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.legendshop.activity.dto.ActivitySkuValidatorDTO;
import com.legendshop.activity.enums.ActivityEsTypeEnum;
import com.legendshop.basic.api.AuditApi;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.enums.*;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.CacheManagerUtil;
import com.legendshop.common.rabbitmq.constants.PayAmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.*;
import com.legendshop.product.dao.*;
import com.legendshop.product.dto.*;
import com.legendshop.product.entity.*;
import com.legendshop.product.enums.*;
import com.legendshop.product.excel.ProductExportDTO;
import com.legendshop.product.excel.ProductRecycleBinExportDTO;
import com.legendshop.product.excel.StockExportDTO;
import com.legendshop.product.mq.producer.ProductProducerService;
import com.legendshop.product.query.ProductConsultQuery;
import com.legendshop.product.query.ProductDetailQuery;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.query.SkuQuery;
import com.legendshop.product.service.*;
import com.legendshop.product.service.convert.*;
import com.legendshop.product.utils.ProductStatusChecker;
import com.legendshop.search.api.EsIndexApi;
import com.legendshop.search.api.SearchProductApi;
import com.legendshop.search.constants.AmqpConst;
import com.legendshop.search.dto.ProductDocumentDTO;
import com.legendshop.search.dto.Tag;
import com.legendshop.search.enmus.IndexTargetMethodEnum;
import com.legendshop.search.enmus.IndexTypeEnum;
import com.legendshop.search.enmus.ProductTargetTypeEnum;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.dto.ShopDecorateProductQuery;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import com.legendshop.user.api.AdminUserApi;
import com.legendshop.user.api.UserAddressApi;
import com.legendshop.user.bo.UserAddressBO;
import com.rabbitmq.client.Channel;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import io.seata.tm.api.transaction.TransactionHookAdapter;
import io.seata.tm.api.transaction.TransactionHookManager;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.legendshop.common.core.constant.CacheConstants.*;

/**
 * 产品服务.
 *
 * @author legendshop
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	final SkuDao skuDao;
	final BrandDao brandDao;
	final SkuService skuService;
	final AppointDao appointDao;
	final ProductDao productDao;
	final StockLogDao stockLogDao;
	final AuditApi auditApi;
	final SkuConverter skuConverter;
	final TransportDao transportDao;
	final TransportConverter transportConverter;
	final EsIndexApi esIndexApi;
	final AccusationDao accusationDao;
	final AmqpSendMsgUtil amqpSendMsgUtil;
	final StockLogService stockLogService;
	final CacheManagerUtil cacheManagerUtil;
	final ShopDetailApi shopDetailApi;
	final ProductConverter productConverter;
	final PreSellProductDao preSellProductDao;
	final ProductPropertyDao productPropertyDao;
	final ProductGroupService productGroupService;
	final DraftSkuService draftSkuService;
	final DraftProductService draftProductService;
	final ProductStatusChecker productStatusChecker;
	final ProductConsultService productConsultService;
	final ProductProducerService productProducerService;
	final FavoriteProductService favoriteProductService;
	final ProductPropertyImageDao productPropertyImageDao;
	final PreSellProductConverter preSellProductConverter;
	final ProductPropertyValueDao productPropertyValueDao;
	final ProductArrivalNoticeService arrivalNoticeService;
	final ProductGroupRelationService productGroupRelationService;
	final ProductArrivalNoticeService productArrivalNoticeService;
	final ProductPropertyValueService productPropertyValueService;
	final ProductPropertyParamGroupDao productPropertyParamGroupDao;
	final ProductPropertyImageConverter productPropertyImageConverter;
	final MessageApi messagePushClient;
	private final PreSellProductService preSellProductService;
	final SearchProductApi searchProductApi;
	private final UserAddressApi userAddressApi;
	private final TransRuleDao transRuleDao;
	final MessageApi messageApi;
	final AdminUserApi adminUserApi;
	final PriceLogService priceLogService;
	final DraftProductDao draftProductDao;
	final DraftProductConverter draftProductConverter;

	@Resource(name = "transportFeeHandler")
	private AbstractTransportHandler transportHandler;
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	@Cacheable(value = PRODUCT_DETAILS + PRODUCT_DTO, key = "#productId", unless = "#result == null ")
	public ProductDTO getDtoByProductId(Long productId) {
		ProductBO productBO = productConverter.convert2BO(productDao.getById(productId));
		return productConverter.convertToProductDTO(productBO);
	}

	@Override
	public ProductDTO getOnlineDtoByProductId(Long productId) {
		return productDao.getOnlineById(productId);
	}


	@Override
	public R<ProductBO> views(ProductDetailQuery query) {

		Long productId = query.getProductId();

		if (ObjectUtil.isNull(productId)) {
			throw new BusinessException("商品ID不能为空");
		}

		Long userId = query.getUserId();

		ProductBO productBO = null;

		// 如果是查看草稿，则直接查询商品草稿数据库信息
		if (query.getViewDraft() && ObjectUtil.isNotEmpty(query.getToken())) {
			productBO = draftProductService.getProductBOByProductId(productId);

			log.info("views productBO:{}", productBO);

			if (productBO != null && !productStatusChecker.isProductNormal(productBO, query.getToken())) {
				return R.fail("商品预览链接已失效~");
			}

			if (null != productBO) {
				List<SkuBO> skuBOList = draftSkuService.getByProductId(productId);

				log.info("views skuBOList:{}", skuBOList);

				productBO.setSkuBOList(skuBOList);
			}
		}

		log.info("views productBO:{}", productBO);

		// 如果商品BO为空，则：一、草稿不存在，当前商品是旧商品，没有草稿；二、当前不是预览草稿商品信息
		if (null == productBO) {
			// 获取商品基本信息，不再检查状态位，由下面的代码检查状态位
			R<ProductDocumentDTO> productDocumentDtoR = searchProductApi.searchProductById(query.getProductId());
			ProductDocumentDTO productDocumentDTO = productDocumentDtoR.getData();
			productBO = createProductBo(productDocumentDTO);
			log.info("views productBO:{}", productBO);
			// 如果查不到数据，则可能未重构或商品已被删除
			if (!productDocumentDtoR.success() || ObjectUtil.isEmpty(productBO)) {
				R<ProductBO> productBoResult = this.getBoByProductId(productId);
				productBO = productBoResult.getData();
			}

			log.info("views productBO:{}", productBO);
			// 如果状态不是正常状态，则需要带token才可以访问
			if (!ProductStatusEnum.PROD_ONLINE.getValue().equals(productBO.getStatus())
					|| !OpStatusEnum.PASS.getValue().equals(productBO.getOpStatus())
					|| !ProductDelStatusEnum.PROD_NORMAL.getValue().equals(productBO.getDelStatus())
					|| !ShopDetailStatusEnum.ONLINE.getStatus().equals(productBO.getShopStatus())) {
				if (ObjectUtil.isNotEmpty(query.getToken())) {
					if (!productStatusChecker.isProductNormal(productBO, query.getToken())) {
						return R.fail("商品预览链接已失效~");
					}
				} else {
					return R.fail("商品已被下架或被删除~");
				}
			}
		}
		log.info("判断当前是否是预售期间,{}", productBO.getPreSellFlag());
		// 判断当前是否是预售期间
		if (productBO.getPreSellFlag()) {
			PreSellProductBO preSellProductBO = productBO.getPreSellProductBO();
			preSellProductBO.setStatus(0);
			if (preSellProductBO.getPayPctType().equals(PreSellPayType.FULL_AMOUNT.value())
					&& preSellProductBO.getPreSaleStart().before(new Date())
					&& preSellProductBO.getPreSaleEnd().after(new Date())) {
				preSellProductBO.setStatus(1);
			} else if (preSellProductBO.getPayPctType().equals(PreSellPayType.DEPOSIT.value())
					&& preSellProductBO.getDepositStart().before(new Date())
					&& preSellProductBO.getDepositEnd().after(new Date())) {
				preSellProductBO.setStatus(1);
			}
		}

		log.info("获取店铺信息,{}", productBO.getShopId());
		//
		R<ShopDetailBO> shopDetailResult = shopDetailApi.getUserShopEs(userId, productBO.getShopId());
		if (!shopDetailResult.success() || ObjectUtil.isEmpty(shopDetailResult.getData())) {
			return R.fail(shopDetailResult.getMsg());
		}
		log.info("获取店铺信息,{}", shopDetailResult.getData());
		productBO.setShopDetailBO(shopDetailResult.getData());

		//收藏状态默认为未收藏
		productBO.setCollectionFlag(Boolean.FALSE);
		log.info("如果是登录状态，才判断是否收藏,{}", productId);
		//如果是登录状态，才判断是否收藏
		if (ObjectUtil.isNotNull(userId) && favoriteProductService.isExistsFavorite(productId, userId)) {
			productBO.setCollectionFlag(Boolean.TRUE);
		}

		log.info("views productBO:{}", productBO);

		//处理sku活动信息
		List<SkuBO> skuBOList = getSkuBoListEs(query, productBO);

		//按活动进行筛选，如果没有指明activityId，则返回所有的sku
		if (ObjectUtil.isNotEmpty(query.getActivityId())) {
			skuBOList = skuBOList.stream().filter(skuBo -> ObjectUtil.isNotNull(skuBo.getActivitySkuDTO())
					&& query.getActivityId().equals(skuBo.getActivitySkuDTO().getId())).collect(Collectors.toList());
		}
		// 计算运费 运费： 就是由运费模板计算一个运费一件的运费
		Transport transport = transportDao.getById(productBO.getTransId());
		if (!transport.getFreeFlag()) {
			for (SkuBO skuBO : skuBOList) {
				skuBO.setFreeFlag(Boolean.FALSE);
				if (ObjectUtil.isNotEmpty(transport)) {
					BigDecimal freight = calTransFreight(userId, productBO, skuBO);
					skuBO.setFreightPrice(freight);
				}
			}
		}

		productBO.setSkuBOList(skuBOList);
		//活动商品信息
		ActivityProductDTO activityProductDTO = new ActivityProductDTO();
		activityProductDTO.setActivityId(query.getActivityId());
		activityProductDTO.setSkuId(query.getSkuId());
		activityProductDTO.setSkuType(query.getSkuType());
		activityProductDTO.setSkuBOS(skuBOList);
		activityProductDTO.setSpecification(productBO.getSpecification());
		//获取商品规格集合
		List<ProductPropertyDTO> prodPropDtoList = skuService.getPropValListByProd(activityProductDTO);
		if (CollectionUtil.isNotEmpty(prodPropDtoList)) {
			//获取规格集合里的最后一个规格对象即位主规格
			ProductPropertyDTO propertyDTO = prodPropDtoList.get(prodPropDtoList.size() - 1);
			productBO.setProductPropertyDTO(propertyDTO);
		}
		productBO.setProdPropDtoList(prodPropDtoList);

		//根据活动id，判断是活动商品页面还是普通商品页面
		if (ObjectUtil.isNotNull(query.getSkuType())) {
			//活动商品页面则不显示普通限购
			productBO.setProductQuotaDTO(null);
		}

		//解析完毕，置空不需要的字段
		productBO.setSpecification(null);

		log.info("views productBO:{}", productBO);

		// 商品咨询信息
		ProductConsultQuery consultQuery = new ProductConsultQuery();
		consultQuery.setPageSize(2);
		consultQuery.setProductId(productId);
		PageSupport<ProductConsultDTO> pageSupport = productConsultService.getUserProductConsultPage(consultQuery);
		List<ProductConsultDTO> productConsultList = pageSupport.getResultList();
		productBO.setProductConsultList(CollUtil.isEmpty(productConsultList) ? null : productConsultList);

		return R.ok(productBO);
	}

	/**
	 * 计算运费 运费： 就是由运费模板计算一个运费一件的运费
	 *
	 * @param userId    用户id
	 * @param productBO 商品信息
	 * @return
	 */
	private BigDecimal calTransFreight(Long userId, ProductBO productBO, SkuBO sku) {
		R<UserAddressBO> commonAddress = userAddressApi.getCommonAddress(userId);
		if (!commonAddress.getSuccess() && ObjectUtil.isEmpty(commonAddress.getData())) {
			throw new BusinessException(commonAddress.getMsg());
		}
		UserAddressBO userAddressBO = commonAddress.getData();
		BigDecimal freight = BigDecimal.ZERO;
		//获取店铺设置的规则
		TransRule rule = transRuleDao.getByShopId(productBO.getShopId());
		//叠加 or 最高
		Integer type = ObjectUtil.isNotEmpty(rule) ? rule.getType() : 1;
		List<BigDecimal> feeList = new ArrayList<>();
		TransportBO transport = transportConverter.convert2BO(transportDao.getById(productBO.getTransId()));
		if (ObjectUtil.isEmpty(transport)) {
			return freight;
		}
		TransFeeCalProductDTO transFeeCalProduct = new TransFeeCalProductDTO();
		transFeeCalProduct.setProductId(productBO.getId());
		transFeeCalProduct.setTransId(productBO.getTransId());
		transFeeCalProduct.setTotalCount(1);
		transFeeCalProduct.setTotalWeight(sku.getWeight());
		transFeeCalProduct.setTotalVolume(sku.getVolume());
		//包邮处理类为责任链的首节点
		R r = transportHandler.calculateTransFee(CollUtil.newArrayList(transFeeCalProduct), transport, userAddressBO.getCityId());
		if (r.getCode() == 1) {
			BigDecimal transFee = (BigDecimal) r.getData();
			freight = freight.add(transFee);
			feeList.add(transFee);
		} else {
			return freight;
		}
		BigDecimal max = feeList.stream().max(BigDecimal::compareTo).get();
		if (type.equals(TransRuleTypeEnum.CAL_ADD.value())) {
			return freight;
		}
		return max;
	}


	/**
	 * 计算运费 运费： 就是由运费模板计算一个运费一件的运费
	 *
	 * @param userId    用户id
	 * @param productBO 商品信息
	 * @return
	 */
	private List<SkuBO> calTransFreight(Long userId, ProductBO productBO, List<SkuBO> skuBO) {
		R<UserAddressBO> commonAddress = userAddressApi.getCommonAddress(userId);
		if (!commonAddress.getSuccess() && ObjectUtil.isEmpty(commonAddress.getData())) {
			throw new BusinessException(commonAddress.getMsg());
		}
		UserAddressBO userAddressBO = commonAddress.getData();
		BigDecimal freight = BigDecimal.ZERO;
		//获取店铺设置的规则
		TransRule rule = transRuleDao.getByShopId(productBO.getShopId());
		//叠加 or 最高
		Integer type = ObjectUtil.isNotEmpty(rule) ? rule.getType() : 1;

		TransportBO transport = transportConverter.convert2BO(transportDao.getById(productBO.getTransId()));
		if (ObjectUtil.isEmpty(transport)) {
			return skuBO;
		}
		List<TransFeeCalProductDTO> transFeeCalProductList = skuBO.stream().map(sku -> {
			TransFeeCalProductDTO transFeeCalProduct = new TransFeeCalProductDTO();
			transFeeCalProduct.setProductId(productBO.getId());
			transFeeCalProduct.setTransId(productBO.getTransId());
			transFeeCalProduct.setTotalCount(1);
			transFeeCalProduct.setSkuId(sku.getSkuId());
			transFeeCalProduct.setTotalWeight(sku.getWeight());
			transFeeCalProduct.setTotalVolume(sku.getVolume());
			return transFeeCalProduct;
		}).collect(Collectors.toList());
		//包邮处理类为责任链的首节点
		R r = transportHandler.calculateTransFee(transFeeCalProductList, transport, userAddressBO.getCityId());
		if (r.getCode() != 1 && !r.getSuccess()) {
			return skuBO;
		}
		Map<Long, BigDecimal> skuAndTotalPriceMap = transFeeCalProductList.stream().collect(Collectors.toMap(TransFeeCalProductDTO::getSkuId, TransFeeCalProductDTO::getTotalPrice));
		skuBO.forEach(sku -> sku.setFreightPrice(skuAndTotalPriceMap.get(sku.getSkuId())));
		return skuBO;
	}

	private List<SkuBO> getSkuBoListEs(ProductDetailQuery query, ProductBO productBO) {
		Long activityId = query.getActivityId();
		Long productId = query.getProductId();
		Long userId = query.getUserId();
		if (productBO.getSkuBOList() == null) {
			List<SkuBO> skuList = skuService.getSkuBOByProduct(productId);
			productBO.setSkuBOList(skuList);
		}
		List<SkuBO> skuBOList = productBO.getSkuBOList();

		// 获取实际库存
		List<SkuBO> dbSkuList = skuService.getSkuByProduct(productId);
		Map<Long, SkuBO> dbSkuMap = dbSkuList.stream().collect(Collectors.toMap(SkuBO::getId, e -> e));
		skuBOList.forEach(e -> {
			if (dbSkuMap.containsKey(e.getId())) {
				e.setStocks(dbSkuMap.get(e.getId()).getStocks());
				e.setActualStocks(dbSkuMap.get(e.getId()).getActualStocks());
			}
		});

		// 活动没有限时折扣价
		if (activityId != null) {
			skuBOList.forEach(e -> e.setDiscountPrice(null));
		}

		List<ActivitySkuDTO> activitySkuDTOList = productBO.getActivitySkuList();
		List<ActivitySkuDTO> resultData;
		if (activityId == null) {
			resultData = activitySkuDTOList;
		} else {
			resultData = activitySkuDTOList.stream().filter(e -> e.getId().equals(activityId) && e.getSkuType().equals(query.getSkuType())).collect(Collectors.toList());
		}

		if (ObjectUtil.isEmpty(resultData)) {
			return skuBOList;
		}
		// 根据SkuID分组
		Map<Long, List<ActivitySkuDTO>> activitySkuMap = resultData.stream().collect(Collectors.groupingBy(ActivitySkuDTO::getSkuId));

		// 秒杀、拼图、团购
		skuBOList.forEach(skuBO -> {
			List<ActivitySkuDTO> activitySkuList = activitySkuMap.get(skuBO.getId());
			if (CollUtil.isEmpty(activitySkuList)) {
				return;
			}
			ActivitySkuDTO activitySkuDTO = activitySkuList.get(0);
			//组装营销活动信息
			activitySkuDTO.setStartTimeStamp(activitySkuDTO.getStartTime().getTime());
			activitySkuDTO.setEndTimeStamp(activitySkuDTO.getEndTime().getTime());
			skuBO.setActivitySkuDTO(activitySkuDTO);
			skuBO.setSkuType(activitySkuDTO.getSkuType());
			//库存需要设置为活动库存
			if (activityId != null) {
				skuBO.setStocks(activitySkuDTO.getStocks());
				skuBO.setActivityStocks(activitySkuDTO.getActivityStocks());
			}
		});

		// 将有活动的商品前置
		skuBOList.sort(Comparator.comparing(e -> ObjectUtil.isEmpty(e.getActivitySkuDTO())));

		// 传入了营销活动Id 不需要处理积分抵扣问题
		if (null != activityId) {
			return skuBOList;
		}

		return skuBOList;
	}


	private ProductBO createProductBo(ProductDocumentDTO data) {
		if (data == null) {
			return null;
		}
		ProductBO productBO = new ProductBO();
		productBO.setId(data.getProductId());
		productBO.setShopId(data.getShopId());
		productBO.setSpecification(data.getSpecification());
		productBO.setName(data.getProductName());
		productBO.setPrice(data.getPrice().toString());
		productBO.setBrief(data.getBrief());
		productBO.setPic(data.getProductPic());
		productBO.setStocks(skuService.getProductStock(data.getProductId()));

		productBO.setContent(data.getContent());
		productBO.setBrandId(data.getBrandId());
		productBO.setBrandName(data.getBrandName());
		productBO.setBrandPic(data.getBrandPic());
		productBO.setVideo(data.getVideo());
		productBO.setStatus(data.getStatus());
		productBO.setOpStatus(data.getOpStatus());
		productBO.setDelStatus(data.getDelStatus());
		productBO.setShopStatus(data.getShopStatus());
		productBO.setPreSellFlag(data.getPreSellFlag());
		productBO.setUserParamGroup(data.getUserParamGroup());
		productBO.setParameter(data.getParameter());
		productBO.setUserParameter(data.getUserParameter());
		productBO.setParamGroup(data.getParamGroup());
		productBO.setMainSpecificationId(data.getMainSpecificationId());
		productBO.setSpecificationStyle(data.getSpecificationStyle());
		productBO.setDeliveryType(data.getDeliveryType());
		productBO.setTransId(data.getTransId());


		// 预售信息
		//预售商品基本信息
		if (productBO.getPreSellFlag()) {
			PreSellProductBO preSellProductBO = preSellProductConverter.convert2BO(data.getPreSellProductMessage());
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
			// 检查是否为预付定金或全款支付类型
			boolean isDepositOrFullAmount = preSellProductBO.getPayPctType().equals(PreSellPayType.DEPOSIT.value()) ||
					preSellProductBO.getPayPctType().equals(PreSellPayType.FULL_AMOUNT.value());
			// 检查是否处于预售活动期间
			boolean isPreSaleActive = preSellProductBO.getPreSaleStart().before(new Date()) &&
					preSellProductBO.getPreSaleEnd().after(new Date());
			// 如果是预付定金或全款支付类型，并且处于预售活动期间，则将状态设置为1
			if (isDepositOrFullAmount && isPreSaleActive) {
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
		//解析参数信息
		//解析参数
		ProductPropertyBO productPropertyBO = this.structureParameter(productBO.getParameter(), productBO.getUserParameter());
		//解析参数组
		List<ProductPropertyBO> productPropertyBOList = this.structureParamGroups(productBO.getParamGroup(), productBO.getUserParamGroup());
		//合并解析结果
		if (ObjectUtil.isNotEmpty(productPropertyBO)) {
			productPropertyBOList.add(productPropertyBO);
		}
		productBO.setParamGroupBOList(productPropertyBOList);
		//解析完毕，置空不需要的字段
		productBO.setParameter(null);
		productBO.setUserParameter(null);
		productBO.setParamGroup(null);
		productBO.setUserParamGroup(null);
		productBO.setImgPath(data.getImgPath());

		//解析商品图片json字符串转化为list
		List<String> prodPics = JSONUtil.toList(JSONUtil.parseArray(data.getImgPath()), String.class);
		productBO.setProductPics(prodPics);

		return productBO;
	}

	/**
	 * 商品详情
	 *
	 * @param productId
	 * @return
	 */
	@Override
	@Cacheable(value = PRODUCT_DETAILS + PRODUCT_BO, key = "#productId", unless = "#result == null ")
	public R<ProductBO> getBoByProductId(Long productId) {
		//获取商品基本信息
		ProductBO productBO = productDao.getProductBO(productId);
		if (ObjectUtil.isEmpty(productBO)) {
			return R.fail("该商品不存在或已被下架");
		}

		ProductQuotaDTO productQuotaDTO = new ProductQuotaDTO();
		productQuotaDTO.setQuotaCount(productBO.getQuotaCount());
		productQuotaDTO.setQuotaTime(productBO.getQuotaTime());
		productQuotaDTO.setQuotaType(productBO.getQuotaType());
		productBO.setProductQuotaDTO(productQuotaDTO);

		//预售商品基本信息
		if (productBO.getPreSellFlag()) {
			PreSellProductBO preSellProductBO = preSellProductConverter.convert2BO(preSellProductService.getByProductId(productId));
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
			// 检查是否为预付定金或全款支付类型
			boolean isDepositOrFullAmount = preSellProductBO.getPayPctType().equals(PreSellPayType.DEPOSIT.value())
					|| preSellProductBO.getPayPctType().equals(PreSellPayType.FULL_AMOUNT.value());
			// 检查是否处于预售活动期间
			boolean isPreSaleActive = preSellProductBO.getPreSaleStart().before(new Date()) &&
					preSellProductBO.getPreSaleEnd().after(new Date());
			// 如果是预付定金或全款支付类型，并且处于预售活动期间，则将状态设置为1
			if (isDepositOrFullAmount && isPreSaleActive) {
				preSellProductBO.setStatus(1);
			}

			//预售定金、尾款时间
			if (PreSellPayType.DEPOSIT.value().equals(preSellProductBO.getPayPctType())) {
				preSellProductBO.setDepositStarts(preSellProductBO.getPreSaleStart().getTime());
				preSellProductBO.setDepositEnds(preSellProductBO.getPreSaleEnd().getTime());
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
		//解析完毕，置空不需要的字段
		productBO.setParameter(null);
		productBO.setUserParameter(null);
		productBO.setParamGroup(null);
		productBO.setUserParamGroup(null);


		//解析商品图片json字符串转化为list
		List<String> prodPics = JSONUtil.toList(JSONUtil.parseArray(productBO.getImgPath()), String.class);
		productBO.setProductPics(prodPics);

		return R.ok(productBO);
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
			parameterList = parameterList.stream().filter(productPropertyBO -> CollUtil.isNotEmpty(productPropertyBO.getProdPropList())).collect(Collectors.toList());
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
	public List<ProductPropertyBO> structureParamGroups(String paramGroup, String userParamGroup) {
		List<ProductPropertyBO> productPropertyBoList = new ArrayList<>();
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
			productPropertyBoList.addAll(paramGroupList);
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

			productPropertyBoList.addAll(userParamGroupList);
		}
		//合并相同的参数组
		Map<Long, List<ProductPropertyBO>> listMap = productPropertyBoList.stream().filter(e -> ObjectUtil.isNotEmpty(e.getGroupId())).collect(Collectors.groupingBy(ProductPropertyBO::getGroupId));
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

	@Override
	public List<ProductBO> getRencFavorProd(Integer amount, Long userId) {
		return productConverter.convert2BoList(productDao.getRencFavorProduct(amount, userId));
	}

	@Override
	public List<ProductExportDTO> findExportProd(ProductQuery query) {
		List<ProductBO> productList = productDao.getProductList(query);
		List<ProductExportDTO> resultList = productConverter.convert2ProductExportDTO(productList);
		resultList.forEach(e -> {

			e.setStatusStr(ProductStatusEnum.fromDesc(e.getStatus()));
			e.setDraftStatusStr(DraftProductStatusEnum.fromDesc(e.getDraftStatus()));

		});

		// 设置分销默认值
		resultList.forEach(e -> e.setCommission("￥0.00"));

		return resultList;
	}

	@Override
	public PageSupport<ProductBO> queryProductListPage(ProductQuery productQuery) {
		return productConverter.convert2BoPageList(productDao.queryProductListPage(productQuery));
	}


	@Override
	public PageSupport<ProductBO> getPage(ProductQuery productQuery) {
		PageSupport<ProductBO> pageSupport = this.productDao.getProductPage(productQuery);
		List<ProductBO> resultList = pageSupport.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			return pageSupport;
		}

		return pageSupport;
	}

	@Override
	public PageSupport<ProductBO> getProductPage(ProductQuery productQuery) {
		PageSupport<ProductBO> pageSupport = this.productDao.getPage(productQuery);
		List<ProductBO> resultList = pageSupport.getResultList();
		List<Long> productIdList = new ArrayList<>();
		List<Long> shopIdList = new ArrayList<>();
		for (ProductBO product : resultList) {
			productIdList.add(product.getId());
			shopIdList.add(product.getShopId());
		}
		//组装sku数量和商店名字
		List<SkuBO> skuBOList = skuService.querySkuByProductIdList(productIdList);
		R<List<ShopDetailDTO>> shopList = shopDetailApi.queryByIds(shopIdList);
		Map<Long, List<SkuBO>> skuMap = skuBOList.stream().collect(Collectors.groupingBy(SkuBO::getProductId));
		Map<Long, List<ShopDetailDTO>> shopMap = new HashMap<>(100);
		if (ObjectUtil.isNotNull(shopList)) {
			shopMap = shopList.getData().stream().collect(Collectors.groupingBy(ShopDetailDTO::getId));
		}

		// 获取商品标签
		R<List<ProductDocumentDTO>> searchProductResult = searchProductApi.searchProductByIds(productIdList);
		Map<Long, List<Tag>> tagMap = Optional.ofNullable(searchProductResult.getData())
				.orElse(Collections.emptyList())
				.stream()
				.filter(e -> CollUtil.isNotEmpty(e.getTagList()))
				.collect(Collectors.toMap(ProductDocumentDTO::getProductId, ProductDocumentDTO::getTagList));

		//product转productBO
		List<ProductBO> productBOList = new ArrayList<>();
		for (ProductBO productBO : resultList) {
			List<SkuBO> skuList = skuMap.get(productBO.getId());
			productBO.setSku(skuConverter.toDTOList(skuList));
			//整合商品分类数组
			List<Long> globalList = new ArrayList<>();
			globalList.add(productBO.getGlobalFirstCatId());
			if (ObjectUtil.isNotEmpty(productBO.getGlobalSecondCatId())) {
				globalList.add(productBO.getGlobalSecondCatId());
			}
			if (ObjectUtil.isNotEmpty(productBO.getGlobalThirdCatId())) {
				globalList.add(productBO.getGlobalThirdCatId());
			}
			productBO.setGlobalCategoryId(globalList);
			productBO.setSkuCount(Optional.ofNullable(skuList).map(List::size).orElse(0));
			if (shopMap.containsKey(productBO.getShopId())) {
				ShopDetailDTO shopDetailDTO = shopMap.get(productBO.getShopId()).get(0);
				productBO.setSiteName(shopDetailDTO.getShopName());
			}

			if (tagMap.containsKey(productBO.getId())) {
				List<String> tags = Tag.disposeTagList(tagMap.get(productBO.getId())).stream()
						.map(e -> ActivityEsTypeEnum.fromDesc(e.getType()))
						.collect(Collectors.toList());
				productBO.setTagList(tags);
			}
			productBOList.add(productBO);
		}

		//获取品牌
		if (CollUtil.isNotEmpty(resultList)) {
			List<Long> brandIdList = resultList.stream().map(ProductBO::getBrandId).collect(Collectors.toList());
			List<Brand> brandList = brandDao.queryAllByIds(brandIdList);
			if (CollUtil.isNotEmpty(brandIdList)) {
				Map<Long, Brand> brandMap = brandList.stream().collect(Collectors.toMap(Brand::getId, e -> e));
				for (ProductBO productBO : productBOList) {
					if (!brandMap.containsKey(productBO.getBrandId())) {
						continue;
					}
					productBO.setBrandName(brandMap.get(productBO.getBrandId()).getBrandName());
				}
			}
		}
		//重新设置分页
		PageSupport<ProductBO> productPageSupport = new PageSupport<>();
		BeanUtil.copyProperties(pageSupport, productPageSupport);
		productPageSupport.setResultList(productBOList);

		return productPageSupport;
	}

	@Override
	public PageSupport<ProductBO> queryActivityInfoPage(ProductQuery productQuery) {
		PageSupport<ProductBO> page = productDao.getProductPage(productQuery);
		List<ProductBO> productBoList = page.getResultList();
		if (CollUtil.isEmpty(productBoList)) {
			return page;
		}
		List<SkuBO> skuBOList = skuService.querySkuByProductIdList(productBoList.stream().map(ProductBO::getId).collect(Collectors.toList()));

		if (CollUtil.isEmpty(skuBOList)) {
			return page;
		}
		//通过sku和活动时间范围获取到冲突的sku集合
		ActivitySkuValidatorDTO skuValidatorDTO = new ActivitySkuValidatorDTO();
		skuValidatorDTO.setSkuIds(skuBOList.stream().map(SkuBO::getId).collect(Collectors.toList()));
		skuValidatorDTO.setBegTime(productQuery.getActivityBegTime());
		skuValidatorDTO.setEndTime(productQuery.getActivityEndTime());

		//通过商品id分组
		Map<Long, List<SkuBO>> legalSkuMap = skuBOList.stream().collect(Collectors.groupingBy(SkuBO::getProductId));
		productBoList.forEach(p -> {
			List<SkuBO> skuList = legalSkuMap.get(p.getId());
			if (CollUtil.isNotEmpty(skuList)) {
				skuList.forEach(sku -> {
					sku.setName(p.getName());
					if (StrUtil.isBlank(sku.getPic())) {
						sku.setPic(p.getPic());
					}
				});
			}
			p.setSkuBOList(skuList);
		});
		return page;
	}


	@Override
	public PageSupport<ProductBO> queryIntegralInfoPage(ProductQuery productQuery) {
		PageSupport<Product> page = productDao.queryProductOnline(productQuery);
		PageSupport<ProductBO> pageSupport = productConverter.convert2BoPageList(page);
		List<ProductBO> productBoList = pageSupport.getResultList();
		List<Long> productIds = productBoList.stream().map(ProductBO::getId).collect(Collectors.toList());
		List<SkuBO> skuBoList = skuService.querySkuByProductIdList(productIds);
		Map<Long, List<SkuBO>> map = skuBoList.stream().collect(Collectors.groupingBy(SkuBO::getProductId));
		productBoList.forEach(p -> {
			// 商品下添加sku,sku图片为商品图片
			List<SkuBO> skuList = map.get(p.getId());
			if (CollUtil.isNotEmpty(skuList)) {
				skuList.stream().filter(sku -> StrUtil.isBlank(sku.getPic())).forEach(sku -> sku.setPic(p.getPic()));
			}
			p.setSkuBOList(skuList);
		});
		return pageSupport;
	}

	@Override
	public void illegalOffProduct(List<Long> productIds, Integer illegalOff, AuditDTO auditDTO) {
		Integer opStatus = AccusationIllegalOffEnum.ILLEGAL.getValue().equals(illegalOff) ? OpStatusEnum.PROD_ILLEGAL_OFF.getValue() : OpStatusEnum.PROD_ILLEGAL_LOCK.getValue();
		productDao.illegalOffProduct(productIds, opStatus);

		for (Long id : productIds) {
			AuditDTO audit = new AuditDTO();
			audit.setAuditUsername(auditDTO.getAuditUsername());
			audit.setAuditTime(DateUtil.date());
			audit.setOpStatus(auditDTO.getOpStatus());
			auditDTO.setAuditOpinion(auditDTO.getAuditOpinion());
			audit.setAuditType(AuditTypeEnum.PRODUCT.getValue());
			audit.setIdList(Collections.singletonList(id));
			draftProductService.audit(audit);
		}

		//	this.productUpdateStatus(ProductStatusEnum.PROD_OFFLINE, productIds);//违规了之后，自动下架商品
//        //处理被举报的的商品并且下架发送站内信
//        Product prod = productService.getProductById(handleAccusation.getProductId());
//        sendSiteMessageProcessor.process(new SendSiteMsgEvent(prod.getUserName(), prod.getUserName(), "违规商品下架", "商品【" + prod.getName() + "】违规下架，违规意见：" + accusation.getHandleInfo()).getSource());
	}

	@Override
	public PageSupport<ProductDTO> queryProductOnLine(ProductQuery query) {
		return productConverter.page(productDao.queryProductOnline(query));
	}

	@Override
	public ProductDTO getProductOnLineEsByProductId(Long productId) {
		return productConverter.to(productDao.getProductOnLineEsByProductId(productId));
	}

	@Override
	public List<ProductDTO> queryProductOnLineEsByProductId(List<Long> productIds) {
		return productConverter.to(productDao.queryProductOnLineEsByProductId(productIds));
	}

	@Override
	public PageSupport<ProductDTO> queryProductOnLineEs(ProductQuery query) {
		return productDao.queryProductOnlineEs(query);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R batchUpdateStatus(ProductBranchDTO productBranchDTO) {
		List<ProductBO> productListByIds = productDao.getProductBO(productBranchDTO.getIds());
		//
		/*获取审核通过的商品id*/
		List<Long> productIds = productListByIds.stream()
				.filter(product -> OpStatusEnum.PASS.getValue().equals(product.getOpStatus()) || OpStatusEnum.DENY.getValue().equals(product.getOpStatus()))
				.map(product -> {
					clearProductCache(product.getId());
					return product.getId();
				})
				.collect(Collectors.toList());

		if (CollUtil.isNotEmpty(productIds)) {

			ProductStatusEnum status = ProductStatusEnum.codeValue(productBranchDTO.getStatus());
			if (ProductStatusEnum.PROD_ONLINE.equals(status)) {
				if (ObjectUtil.isNotEmpty(productBranchDTO.getShopId())) {
					//获取店铺信息
					R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(productBranchDTO.getShopId());
					ShopDetailDTO shopDetail = shopDetailResult.getData();
					if (shopDetail == null
							|| !OpStatusEnum.PASS.getValue().equals(shopDetail.getOpStatus())
							|| !ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetail.getStatus())) {
						log.warn("店铺信息有误，请联系管理员");
						throw new BusinessException("店铺信息有误，请联系管理员");
					}
				}

				List<ProductBO> preSellList = productListByIds.stream()
						.filter(ProductBO::getPreSellFlag)
						.filter(e -> productIds.contains(e.getId()) && DateUtil.date().after(e.getPreSaleEnd()))
						.collect(Collectors.toList());
				if (CollUtil.isEmpty(preSellList)) {
					this.productUpdateStatus(status, productIds);
					return R.ok();
				}

				// 已到结束时间的预售商品，如果上架，需要将预售商品变更为普通商品
				for (ProductBO preSellProduct : preSellList) {
					preSellProduct.setPreSellFlag(false);
				}
				productDao.update(productConverter.convert2Product(preSellList));
			}
			this.productUpdateStatus(status, productIds);
			return R.ok();
		}
		return R.fail();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R audit(AuditDTO auditDTO) {

		R audit = draftProductService.audit(auditDTO);
		if (!audit.getSuccess()) {
			return R.fail(audit.getMsg());
		}

		for (Long appointId : auditDTO.getIdList()) {
			this.clearProductCache(appointId);
		}

		/*保存审核日志*/
		List<AuditDTO> auditDTOList = new ArrayList<AuditDTO>();
		auditDTO.getIdList().forEach(id -> {
			AuditDTO auditItem = new AuditDTO();
			BeanUtil.copyProperties(auditDTO, auditItem);
			auditItem.setAuditTime(DateUtil.date());
			auditItem.setAuditType(AuditTypeEnum.PRODUCT.getValue());
			auditItem.setCommonId(id);
			auditDTOList.add(auditItem);
		});

		auditApi.audit(auditDTOList);

		//发送到货通知给用户
		log.info("########获取商品sku集合#######");
		List<SkuBO> skuBoList = skuService.querySkuBOByProductIdList(auditDTO.getIdList());
		/*发送到货通知*/
		if (CollUtil.isNotEmpty(skuBoList)) {
			log.info("########准备发送信息通知用户#######");
			arrivalNoticeService.noticeUser(skuBoList);
		}
		return R.ok("审核成功");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R save(ProductDTO productDTO) {
		if (ObjectUtil.isEmpty(productDTO.getDeliveryType())) {
			productDTO.setDeliveryType(ProductDeliveryTypeEnum.EXPRESS_DELIVERY.getCode());
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
		//获取店铺信息
		R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(productDTO.getShopId());
		ShopDetailDTO shopDetail = shopDetailResult.getData();
		if (shopDetail == null
				|| !OpStatusEnum.PASS.getValue().equals(shopDetail.getOpStatus())
				|| !ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetail.getStatus())) {
			log.warn("店铺信息有误，请联系管理员");
			return R.fail("店铺信息有误，请联系管理员");
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
		//设置商品图片集合,如果有多个sku组图，取每个组图的第一张图片，否则是商品主图的所有图片
		List<String> imgList;
		if (CollUtil.isNotEmpty(productDTO.getImageList())) {
			imgList = productDTO.getImageList().stream().map(
					productPropertyImageDTO -> productPropertyImageDTO.getImgList().get(0)
			).collect(Collectors.toList());
		} else {
			imgList = productDTO.getImg();
		}
		productDTO.setImgPath(cn.hutool.json.JSONUtil.toJsonStr(imgList));

		//设置商品搜索主图
		productDTO.setPic(CollUtil.isNotEmpty(productDTO.getImg()) ? productDTO.getImg().get(0) : imgList.get(0));

		//解析商品分类数组
		if (ObjectUtil.isNotEmpty(productDTO.getGlobalCategoryId())) {
			int size = productDTO.getGlobalCategoryId().size();
			productDTO.setGlobalFirstCatId(productDTO.getGlobalCategoryId().get(0));
			productDTO.setGlobalSecondCatId(size > 1 ? productDTO.getGlobalCategoryId().get(1) : null);
			productDTO.setGlobalThirdCatId(size > 2 ? productDTO.getGlobalCategoryId().get(2) : null);
		}


		//解析店铺商品分类数组
		if (ObjectUtil.isNotEmpty(productDTO.getShopCategoryId())) {
			int shopSize = productDTO.getShopCategoryId().size();
			productDTO.setShopFirstCatId(shopSize > 0 ? productDTO.getShopCategoryId().get(0) : null);
			productDTO.setShopSecondCatId(shopSize > 1 ? productDTO.getShopCategoryId().get(1) : null);
			productDTO.setShopThirdCatId(shopSize > 2 ? productDTO.getShopCategoryId().get(2) : null);
		}

		//整合sku
		List<SkuDTO> skuList = productDTO.getSku();
		BigDecimal max = new BigDecimal(0L);
		BigDecimal min = skuList.get(0).getPrice();
		productDTO.setStocks(0);

		for (SkuDTO skuDTO : skuList) {
			if (skuDTO.getActualStocks() == null) {
				skuDTO.setActualStocks(0);
			}

			//计算销售价的范围
			max = max.compareTo(skuDTO.getPrice()) >= 0 ? max : skuDTO.getPrice();
			min = min.compareTo(skuDTO.getPrice()) <= 0 ? min : skuDTO.getPrice();
			//计算库存量
			productDTO.setStocks(productDTO.getStocks() + skuDTO.getActualStocks());
		}

		productDTO.setPrice(min.compareTo(max) == 0 ? formatPrice(min) : formatPrice(min) + " ~ " + formatPrice(max));
		productDTO.setMinPrice(min);
		productDTO.setMaxPrice(max);
		productDTO.setActualStocks(productDTO.getStocks());
		productDTO.setBuys(0);
		productDTO.setViews(0);
		productDTO.setComments(0);
		productDTO.setReviewScores(0);
		Date date = new Date();
		productDTO.setCreateTime(date);
		productDTO.setUpdateTime(date);

		productDTO.setStatus(ProductStatusEnum.UNPUBLISHED.getValue());
		productDTO.setDelStatus(ProductDelStatusEnum.PROD_NORMAL.value());

		//判断该店铺是否需要审核商品
		if (ObjectUtil.isNotEmpty(shopDetail.getProdRequireAudit())
				&& shopDetail.getProdRequireAudit() == 0) {
			// 不需要审核商品的店铺，直接提审并通过
			productDTO.setArraignment(true);
			productDTO.setOpStatus(OpStatusEnum.PASS.getValue());

			//上架方式
			if (ProductOnSaleWayEnum.ONSALE.getValue().equals(productDTO.getOnSaleWay())) {
				productDTO.setStatus(ProductStatusEnum.PROD_ONLINE.getValue());
			} else {
				productDTO.setStatus(ProductStatusEnum.PROD_OFFLINE.getValue());
			}

		} else {
			productDTO.setOpStatus(OpStatusEnum.WAIT.getValue());
		}

		//预约上架id
		if (ProductOnSaleWayEnum.PRESALE.getValue().equals(productDTO.getOnSaleWay())) {
			productDTO.setAppointId(appointDao.createId());
		}

		//解析规格
		if (ObjectUtil.isEmpty(productDTO.getSpecification())) {
			productDTO.setSpecification(JSONUtil.toJsonStr(productDTO.getSpecificationList()));
		}

		log.info("1111111111111111{}", productDTO.getSpecification());
		//解析参数组
		if (CollUtil.isNotEmpty(productDTO.getParamGroupList())) {
			productDTO.setParamGroup(JSONUtil.toJsonStr(productDTO.getParamGroupList().stream().filter(
					paramPropertyGroupDTO -> paramPropertyGroupDTO.getSource().equals(ProductPropertySourceEnum.SYSTEM.getValue())
			).collect(Collectors.toList())));
			productDTO.setUserParamGroup(JSONUtil.toJsonStr(productDTO.getParamGroupList().stream().filter(
					paramPropertyGroupDTO -> paramPropertyGroupDTO.getSource().equals(ProductPropertySourceEnum.USER.getValue())
			).collect(Collectors.toList())));
		}
		//设置版本号
		productDTO.setVersion(1);
		Product product = productConverter.from(productDTO);
		if (ObjectUtil.isNotEmpty(productDTO.getProductQuotaDTO())) {
			ProductQuotaDTO productQuotaDTO = productDTO.getProductQuotaDTO();
			product.setQuotaType(productQuotaDTO.getQuotaType());
			product.setQuotaCount(productQuotaDTO.getQuotaCount());
			product.setQuotaTime(productQuotaDTO.getQuotaTime());
		}

		Long productId = productDao.save(product);

		// 预约上架
		if (ProductOnSaleWayEnum.PRESALE.getValue().equals(productDTO.getOnSaleWay())) {
			Appoint appoint = new Appoint();
			appoint.setProductId(productId);
			appoint.setOnSellDate(productDTO.getAppointTime());
			appoint.setOnSellFlag(false);
			appoint.setCreateTime(date);
			appointDao.save(appoint, productDTO.getAppointId());

			if (ObjectUtil.isNotEmpty(shopDetail.getProdRequireAudit()) && shopDetail.getProdRequireAudit() == 0) {
				productProducerService.appointOnLine(appoint.getProductId(), appoint.getOnSellDate());
			}
		}

		//保存预售信息
		if (productDTO.getPreSellFlag()) {
			preSell.setCreateTime(date);
			preSell.setProductId(productId);
			//如果是全款，尾款支付时间都为null
			if (preSell.getPayPctType().compareTo(0) == 0) {
				preSell.setPayPct(BigDecimal.valueOf(100));
				preSell.setFinalMStart(null);
				preSell.setFinalMEnd(null);
			}
			preSellProductDao.save(preSellProductConverter.from(preSell));
			productProducerService.preSellFinish(productId, preSell.getPreSaleEnd());
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
			productDTO.setCustomPropertyValueList(null);
		}
		//保存属性值
		if (!CollectionUtils.isEmpty(productPropertyValueList)) {
			if (!productDTO.getBatchFlag()) {
				productPropertyValueService.saveWithId(productPropertyValueList);
			}

		}

		//保存sku组图
		if (ObjectUtil.isNotEmpty(productDTO.getImageList())) {
			productDTO.getImageList().forEach(productPropertyImageDTO -> {
				productPropertyImageDTO.setProductId(productId);
				productPropertyImageDTO.setCreateDate(date);
				productPropertyImageDTO.setUrl(JSONUtil.toJsonStr(productPropertyImageDTO.getImgList()));
			});
			productPropertyImageDao.save(productPropertyImageConverter.from(productDTO.getImageList()));
		}

		//保存sku
		skuList = productDTO.getSku().stream().peek(e -> {
			e.setId(skuDao.createId());
			e.setProductId(productId);
			e.setCreateTime(date);
			e.setSkuType(SkuActiveTypeEnum.PRODUCT.value());
			e.setStocks(e.getActualStocks());
			e.setBuys(0);
			e.setIntegralDeductionFlag(Boolean.FALSE);
			e.setIntegralFlag(Boolean.FALSE);
			/*设置图片*/
			/*切割规格*/
			if (ObjectUtil.isNotEmpty(e.getProperties()) && ObjectUtil.isNotEmpty(productDTO.getMainSpecificationId())) {
				String[] specificationMap = e.getProperties().split(";");
				/*在规格列表里找出主规格对应的规格id：规格值id*/
				List<String> value = Arrays.stream(specificationMap).filter(s -> {
					String[] strings = s.split(":");
					return strings[0].compareTo(productDTO.getMainSpecificationId().toString()) == 0;
				}).collect(Collectors.toList());
				/*根据规格值id、规格Id、查找图片，并设置进去*/
				if (CollUtil.isNotEmpty(value)) {
					List<String> imageSrc = productDTO.getImageList().stream().filter(productPropertyImageDTO -> {
						String[] split = value.get(0).split(":");
						return productPropertyImageDTO.getPropId().compareTo(productDTO.getMainSpecificationId()) == 0
								&& productPropertyImageDTO.getValueId().compareTo(Long.parseLong(split[1])) == 0;
					}).map(productPropertyImageDTO -> productPropertyImageDTO.getImgList().get(0))
							.collect(Collectors.toList());
					if (CollUtil.isNotEmpty(imageSrc)) {
						e.setPic(imageSrc.get(0));
					}
				}
			} else {
				e.setPic(productDTO.getPic());
			}
		}).collect(Collectors.toList());

		skuDao.saveWithId(skuConverter.from(skuList));

		// 保存分销数据
		productDTO.setId(productId);

		draftSkuService.save(skuList);
		draftProductService.save(productDTO);
		if (!productDTO.getArraignment()) {
			messageApi.sendAdmin(product.getShopId(), -1L, "商品审核提醒" + product.getName() + "发布需要平台审核");
		}


		//创建索引
		amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_CREATE_ROUTING_KEY, ImmutableList.of(productId), true);
		return R.ok(productId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@Caching(evict = {
			@CacheEvict(value = PRODUCT_DETAILS + PRODUCT_DTO, key = "#productDTO.id"),
			@CacheEvict(value = PRODUCT_DETAILS + PRODUCT_BO, key = "#productDTO.id")
	}
	)
	public R update(ProductDTO productDTO) {

		if (CollUtil.isEmpty(productDTO.getSku())) {
			log.warn("sku为空！");
			return R.fail("sku为空！");
		}

		//校验预购数量
		if (ObjectUtil.isNotNull(productDTO.getProductQuotaDTO())) {
			ProductQuotaDTO productQuotaDTO = productDTO.getProductQuotaDTO();
			if (StringUtil.isNotBlank(productQuotaDTO.getQuotaType())) {
				int skuStocks = productDTO.getSku().stream().mapToInt(SkuDTO::getActualStocks).sum();
				if (ObjectUtil.isEmpty(productQuotaDTO.getQuotaCount())) {
					log.warn("最多购买件数不能为空");
					return R.fail("最多购买件数不能为空");
				}

				if (productQuotaDTO.getQuotaCount() > skuStocks) {
					log.warn("最多购买件数不能大于实际库存");
					return R.fail("最多购买件数大于实际库存");
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

		//获取店铺信息
		R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(productDTO.getShopId());
		ShopDetailDTO shopDetail = shopDetailResult.getData();
		if (shopDetail == null
				|| !OpStatusEnum.PASS.getValue().equals(shopDetail.getOpStatus())
				|| !ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetail.getStatus())) {
			log.warn("店铺信息有误，请联系管理员");
			return R.fail("店铺信息有误，请联系管理员");
		}

		Long productId = productDTO.getId();

		Product originProduct = productDao.getById(productId);
		if (originProduct == null) {
			return R.fail("商品不存在，修改失败");
		}

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
			productDTO.setPic(imgList.get(0));
		} else {
			// 设置商品搜索主图
			productDTO.setPic(CollUtil.isNotEmpty(productDTO.getImg()) ? productDTO.getImg().get(0) : imgList.get(0));
		}
		//设置商品搜索图片集合
		productDTO.setImgPath(JSONUtil.toJsonStr(imgList));

		//sku组图，先删除原有的记录，重新保存
		productPropertyImageDao.deleteByProductId(productId);
		if (ObjectUtil.isNotEmpty(productDTO.getImageList())) {
			productDTO.getImageList().forEach(productPropertyImageDTO -> {
				productPropertyImageDTO.setProductId(productId);
				productPropertyImageDTO.setCreateDate(date);
				List<String> imgList1 = productPropertyImageDTO.getImgList();
				productPropertyImageDTO.setUrl(JSONUtil.toJsonStr(imgList1));
			});
			productPropertyImageDao.save(productPropertyImageConverter.from(productDTO.getImageList()));
		}

		/*更新sku*/
		this.updateSku(productDTO, date);

		//解析商品分类数组
		int size = productDTO.getGlobalCategoryId().size();
		productDTO.setGlobalFirstCatId(productDTO.getGlobalCategoryId().get(0));
		productDTO.setGlobalSecondCatId(size > 1 ? productDTO.getGlobalCategoryId().get(1) : null);
		productDTO.setGlobalThirdCatId(size > 2 ? productDTO.getGlobalCategoryId().get(2) : null);

		//解析店铺商品分类数组
		if (ObjectUtil.isNotEmpty(productDTO.getShopCategoryId())) {
			int shopsize = productDTO.getShopCategoryId().size();
			productDTO.setShopFirstCatId(shopsize > 0 ? productDTO.getShopCategoryId().get(0) : null);
			productDTO.setShopSecondCatId(shopsize > 1 ? productDTO.getShopCategoryId().get(1) : null);
			productDTO.setShopThirdCatId(shopsize > 2 ? productDTO.getShopCategoryId().get(2) : null);
		}
		List<Sku> actuallySkuList = skuDao.getSkuByProductId(productId);
		//整合sku
		BigDecimal max = new BigDecimal(0L);
		BigDecimal min = actuallySkuList.get(0).getPrice();
		productDTO.setStocks(0);
		productDTO.setActualStocks(0);
		Integer stocksArm = productDTO.getStocksArm();
		for (Sku sku : actuallySkuList) {
			//计算销售价的范围
			max = max.compareTo(sku.getPrice()) >= 0 ? max : sku.getPrice();
			min = min.compareTo(sku.getPrice()) <= 0 ? min : sku.getPrice();
			//sku销售库存小于库存预警值预警
			if (ObjectUtil.isNotEmpty(stocksArm) && sku.getStocks() <= stocksArm) {
				// 发送库存预警站内信给商家
				List<MsgSendParamDTO> msgSendParamDtoList = new ArrayList<>();
				//替换参数内容
				MsgSendParamDTO refundSnDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, ObjectUtil.isNotEmpty(sku.getName()) ? sku.getName() : productDTO.getName(), "black");
				msgSendParamDtoList.add(refundSnDTO);
				messagePushClient.push(new MessagePushDTO()
						.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
						.setReceiveIdArr(new Long[]{SecurityUtils.getShopUser().getShopId()})
						.setMsgSendType(MsgSendTypeEnum.PROD_REMIND_STOCKS)
						.setSysParamNameEnum(SysParamNameEnum.PROD_STOCKS)
						.setMsgSendParamDTOList(msgSendParamDtoList)
						.setDetailId(productDTO.getId())
				);
			}
			//计算库存量
			productDTO.setStocks(productDTO.getStocks() + sku.getStocks());
			productDTO.setActualStocks(productDTO.getActualStocks() + sku.getActualStocks());
		}

		productDTO.setPrice(min.compareTo(max) == 0 ? formatPrice(min) : formatPrice(min) + " ~ " + formatPrice(max));
		productDTO.setMinPrice(min);
		productDTO.setMaxPrice(max);
		productDTO.setUpdateTime(date);

		//解析规格
		productDTO.setSpecification(JSONUtil.toJsonStr(productDTO.getSpecificationList()));

		//解析参数组
		if (CollUtil.isNotEmpty(productDTO.getParamGroupList())) {
			productDTO.setParamGroup(JSONUtil.toJsonStr(productDTO.getParamGroupList().stream().filter(
					paramPropertyGroupDTO -> paramPropertyGroupDTO.getSource().equals(ProductPropertySourceEnum.SYSTEM.getValue())
			).collect(Collectors.toList())));
			productDTO.setUserParamGroup(JSONUtil.toJsonStr(productDTO.getParamGroupList().stream().filter(
					paramPropertyGroupDTO -> paramPropertyGroupDTO.getSource().equals(ProductPropertySourceEnum.USER.getValue())
			).collect(Collectors.toList())));
		}


		// 判断该店铺是否需要审核商品，如果店铺是不需要审核且不是违规下架商品
		if (!OpStatusEnum.PROD_ILLEGAL_OFF.getValue().equals(originProduct.getOpStatus())) {
			productDTO.setOpStatus(OpStatusEnum.PASS.getValue());
		} else {
			productDTO.setOpStatus(OpStatusEnum.WAIT.getValue());
		}

		// 旧的商品信息
		BeanUtil.copyProperties(productDTO, originProduct, new CopyOptions().ignoreNullValue());


		//预约上架
		Appoint originAppoint = appointDao.queryByProductId(productId);
		if (ProductOnSaleWayEnum.PRESALE.getValue().equals(productDTO.getOnSaleWay())) {
			originProduct.setStatus(ProductStatusEnum.PROD_OFFLINE.getValue());

			//如果原来就有预约上架信息，就更新，没有就添加
			if (ObjectUtil.isNotEmpty(originAppoint)) {
				originAppoint.setOnSellDate(productDTO.getAppointTime());
				originAppoint.setUpdateTime(date);
				appointDao.update(originAppoint);
				originProduct.setAppointId(originAppoint.getId());
			} else {
				Appoint appoint = new Appoint();
				appoint.setProductId(productId);
				appoint.setOnSellDate(productDTO.getAppointTime());
				appoint.setOnSellFlag(false);
				appoint.setCreateTime(date);
				originProduct.setAppointId(appointDao.save(appoint));
			}
		} else {
			//删除预约信息
			originProduct.setAppointId(null);
			appointDao.delete(originAppoint);
		}
		//保存预售信息
		List<PreSellProduct> preSellProducts = preSellProductDao.queryByProductId(productId);
		if (productDTO.getPreSellFlag()) {
			//如果全额支付，预售支付百分比为100%，尾款支付时间为空
			if (preSell.getPayPctType().compareTo(0) == 0) {
				preSell.setPayPct(BigDecimal.valueOf(100));
				preSell.setFinalMStart(null);
				preSell.setFinalMEnd(null);
			}
			//如果原来就有预售信息，就更新，没有就添加
			if (ObjectUtil.isNotEmpty(preSellProducts)) {
				PreSellProduct originPreSell = preSellProducts.get(0);
				originPreSell.setUpdateTime(date);
				BeanUtil.copyProperties(preSell, originPreSell);
				preSellProductDao.update(originPreSell);
			} else {
				preSell.setCreateTime(date);
				preSell.setProductId(productId);
				preSellProductDao.save(preSellProductConverter.from(preSell));
			}

			productProducerService.preSellFinish(productId, preSell.getPreSaleEnd());
		} else {
			//删除预售信息
			preSellProductDao.delete(preSellProducts);
		}
		originProduct.setVersion(originProduct.getVersion() + 1);
		ProductQuotaDTO productQuotaDTO = productDTO.getProductQuotaDTO();
		originProduct.setQuotaTime(productQuotaDTO.getQuotaTime());
		originProduct.setQuotaCount(productQuotaDTO.getQuotaCount());
		originProduct.setQuotaType(productQuotaDTO.getQuotaType());
		productDao.update(originProduct);

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
			productDTO.setCustomPropertyValueList(null);
		}
		//保存属性值
		if (CollUtil.isNotEmpty(productPropertyValueList) && productPropertyValueList.size() > 0) {
			productPropertyValueService.saveWithId(productPropertyValueList);
		}


		//判断该店铺是否需要审核商品并且立即上线
		if (ObjectUtil.isNotEmpty(shopDetail.getProdRequireAudit()) && shopDetail.getProdRequireAudit() == 0 && ProductOnSaleWayEnum.ONSALE.getValue().equals(productDTO.getOnSaleWay())) {

			//发送到货通知给用户
			log.info("########获取商品sku集合#######");
			List<SkuBO> skuBoList = skuService.getSkuBOByProduct(productId);
			/*发送到货通知*/
			if (CollUtil.isNotEmpty(skuBoList)) {
				log.info("########准备发送信息通知用户#######");
				arrivalNoticeService.noticeUser(skuBoList);
			}
		}

		// 保存草稿
		draftProductService.update(productDTO);
		draftSkuService.deleteByProductId(productId);
		draftSkuService.save(skuConverter.to(skuDao.getSkuByProductId(productId)));

		//创建索引
		amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_CREATE_ROUTING_KEY, ImmutableList.of(productId), true);

		return R.ok(productId);
	}

	/**
	 * 更新sku
	 *
	 * @param productDTO
	 * @param date
	 */
	private void updateSku(ProductDTO productDTO, Date date) {
		//清空sku缓存
		skuService.clearSkuCache(productDTO.getSku().stream().map(SkuDTO::getId).collect(Collectors.toList()));

		List<SkuDTO> skuList = productDTO.getSku();
		Long productId = productDTO.getId();

		//对于删除的sku，删除sku
		List<SkuDTO> originSku = skuConverter.to(skuDao.getSkuByProductId(productId));

		// 预售商品不能参与营销活动
		if (productDTO.getPreSellFlag()) {
			checkSkuActivity(originSku);
		}

		/*removeAll后，剩下的就是要删除的*/
		originSku.removeAll(skuList);
		if (!productDTO.getPreSellFlag()) {
			checkSkuActivity(originSku);
		}

		skuDao.delete(skuConverter.from(originSku));
		//对于新增的sku，保存sku
		List<Sku> skus = skuConverter.from(skuList.stream().filter(skuDTO -> ObjectUtil.isEmpty(skuDTO.getId())).map(e -> {
			e.setProductId(productId);
			e.setCreateTime(date);
			e.setStocks(e.getActualStocks());
			e.setBuys(0);
			e.setIntegralDeductionFlag(Boolean.FALSE);
			e.setIntegralFlag(Boolean.FALSE);
			/*设置图片*/
			/*切割规格*/
			if (ObjectUtil.isNotEmpty(e.getProperties()) && ObjectUtil.isNotEmpty(productDTO.getMainSpecificationId())) {
				String[] specificationMap = e.getProperties().split(";");
				/*在规格列表里找出主规格对应的规格id：规格值id*/
				List<String> value = Arrays.stream(specificationMap).filter(s -> {
					String[] strings = s.split(":");
					return strings[0].compareTo(productDTO.getMainSpecificationId().toString()) == 0;
				}).collect(Collectors.toList());
				/*根据规格值id、规格Id、查找图片，并设置进去*/
				if (CollUtil.isNotEmpty(value)) {
					List<String> imageSrc = productDTO.getImageList().stream().filter(productPropertyImageDTO -> {
						String[] split = value.get(0).split(":");
						return productPropertyImageDTO.getPropId().compareTo(productDTO.getMainSpecificationId()) == 0
								&& productPropertyImageDTO.getValueId().compareTo(Long.parseLong(split[1])) == 0;
					}).map(productPropertyImageDTO -> productPropertyImageDTO.getImgList().get(0))
							.collect(Collectors.toList());
					if (CollUtil.isNotEmpty(imageSrc)) {
						e.setPic(imageSrc.get(0));
					}
				}
			} else {
				e.setPic(productDTO.getPic());
			}
			return e;
		}).collect(Collectors.toList()));
		skuDao.save(skus);

		//对于修改的sku，修改sku
		skuList = skuList.stream().filter(skuDTO -> ObjectUtil.isNotEmpty(skuDTO.getId())).collect(Collectors.toList());
		originSku = skuConverter.to(skuDao.queryAllByIds(skuList.stream().map(SkuDTO::getId).collect(Collectors.toList())));
		for (SkuDTO skuDTO : skuList) {
			for (SkuDTO origin : originSku) {
				if (origin.getId().compareTo(skuDTO.getId()) == 0) {
					//实际库存
					Integer actualStocks = origin.getActualStocks();
					//销售库存
					Integer stocks = origin.getStocks();
					BeanUtil.copyProperties(skuDTO, origin, new CopyOptions().ignoreNullValue());
					//实际库存修改 实际库存根据销售库存的变化量做对应的调整 ， 销售库存一定会少于实际库存，如果数据不对，则恢复实际库存和销售库存同样的数量。
					// 但是销售库存一旦混乱则无法从页面上恢复，需要额外做功能同时编辑商品实际库存和销售库存。
					int i = actualStocks - stocks;
					if (i < 0) {
						i = 0;
					}
					origin.setActualStocks(skuDTO.getActualStocks());

					origin.setStocks(skuDTO.getActualStocks() - i < 0 ? skuDTO.getActualStocks() : skuDTO.getActualStocks() - i);

					origin.setUpdateTime(date);
					/*设置图片*/
					if (ObjectUtil.isNotEmpty(origin.getProperties()) && ObjectUtil.isNotEmpty(productDTO.getMainSpecificationId())) {
						/*切割规格*/
						String[] specificationMap = origin.getProperties().split(";");
						/*在规格列表里找出主规格对应的规格id：规格值id*/
						List<String> value = Arrays.stream(specificationMap).filter(s -> {
							String[] strings = s.split(":");
							return strings[0].compareTo(productDTO.getMainSpecificationId().toString()) == 0;
						}).collect(Collectors.toList());
						/*根据规格值id、规格Id、查找图片，并设置进去*/
						if (CollUtil.isNotEmpty(value)) {
							List<String> imageSrc = productDTO.getImageList().stream().filter(productPropertyImageDTO -> {
								String[] split = value.get(0).split(":");
								return productPropertyImageDTO.getPropId().compareTo(productDTO.getMainSpecificationId()) == 0
										&& productPropertyImageDTO.getValueId().compareTo(Long.parseLong(split[1])) == 0;
							}).map(productPropertyImageDTO -> productPropertyImageDTO.getImgList().get(0))
									.collect(Collectors.toList());
							if (CollUtil.isNotEmpty(imageSrc)) {
								origin.setPic(imageSrc.get(0));
							}
						}
					} else {
						origin.setPic(productDTO.getPic());
					}
				}
			}
		}
		skuDao.update(skuConverter.from(originSku));
	}

	@Override
	public List<HotSellProductBO> queryHotSellProdByShopId(Long shopId) {
		return productDao.queryHotSellProductByShopId(shopId);
	}


	@Override
	public ProductDTO getShopProductById(Long id, Long shopId) {
		ProductBO productBO = productConverter.convert2BO(productDao.getById(id));
		if (productBO == null) {
			return null;
		}

		if (!productBO.getShopId().equals(shopId)) {
			return null;
		}
		ProductDTO productDTO = productConverter.convertToProductDTO(productBO);

		// 获取草稿状态
		DraftProductDTO draftProductDTO = draftProductService.getByProductId(id);
		productDTO.setDraftStatus(Optional.ofNullable(draftProductDTO).map(DraftProductDTO::getStatus).orElse(null));

		//整合商品分类数组
		List<Long> globalList = new ArrayList<>();
		globalList.add(productDTO.getGlobalFirstCatId());
		if (ObjectUtil.isNotEmpty(productDTO.getGlobalSecondCatId())) {
			globalList.add(productDTO.getGlobalSecondCatId());
		}
		if (ObjectUtil.isNotEmpty(productDTO.getGlobalThirdCatId())) {
			globalList.add(productDTO.getGlobalThirdCatId());
		}
		productDTO.setGlobalCategoryId(globalList);

		//整合店铺商品分类数组
		List<Long> shopList = new ArrayList<>();
		if (ObjectUtil.isNotEmpty(productDTO.getShopFirstCatId())) {
			shopList.add(productDTO.getShopFirstCatId());
		}
		if (ObjectUtil.isNotEmpty(productDTO.getShopSecondCatId())) {
			shopList.add(productDTO.getShopSecondCatId());
		}
		if (ObjectUtil.isNotEmpty(productDTO.getShopThirdCatId())) {
			shopList.add(productDTO.getShopThirdCatId());
		}
		productDTO.setShopCategoryId(shopList);

		//预约上架信息
		if (productDTO.getAppointId() != null && ProductOnSaleWayEnum.PRESALE.getValue().equals(productDTO.getOnSaleWay())) {
			Appoint appoint = appointDao.getById(productDTO.getAppointId());
			productDTO.setAppointTime(appoint.getOnSellDate());
		}

		//预售信息
		if (productDTO.getPreSellFlag()) {
			PreSellProduct preSellProduct = preSellProductDao.getByProductId(productDTO.getId());
			productDTO.setPreSellProductDTO(preSellProductConverter.to(preSellProduct));
		} else {
			PreSellProductDTO preSellProduct = new PreSellProductDTO();
			preSellProduct.setPayPctType(0);
			productDTO.setPreSellProductDTO(preSellProduct);
		}

		//sku
		productDTO.setSku(skuConverter.to(skuDao.getSkuByProductId(id)));

		/*spu主图*/
		productDTO.setImg(JSONUtil.toList(JSONUtil.parseArray(productDTO.getImgPath()), String.class));

		/* 回显运费模板名称 */
		Transport transport = transportDao.getById(productDTO.getTransId());
		if (ObjectUtil.isNotNull(transport)) {
			productDTO.setTransName(transport.getTransName());
		}

		productDTO.setOpenDistribution(Boolean.FALSE);
		productDTO.setCommissionRatio(BigDecimal.ZERO);


		return productDTO;
	}

	@Override
	public PageSupport<DecorateProductBO> queryDecorateProductList(ProductQuery productQuery) {

		PageSupport<ProductBO> ps = this.queryProductListPage(productQuery);
		PageSupport<DecorateProductBO> result = productConverter.convert2DecorateProductBOPageList(ps);
		return result;
	}

	@Override
	public PageSupport<DecorateProductBO> queryDecorateProductList(ShopDecorateProductQuery shopDecorateProductQuery) {
		PageSupport<ProductBO> ps = productConverter.convert2BoPageList(productDao.queryProductListPage(shopDecorateProductQuery));
		PageSupport<DecorateProductBO> result = productConverter.convert2DecorateProductBOPageList(ps);
		result.getResultList().forEach(p -> {
			R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(p.getShopId());
			if (shopDetailResult.success() && ObjectUtil.isNotEmpty(shopDetailResult.getData())) {
				p.setSiteName(shopDetailResult.getData().getShopName());
			}
		});
		return result;
	}

	@Override
	public PageSupport<SkuBO> stocksPage(ProductQuery query) {
		PageSupport<SkuBO> pageSupport = skuDao.stocksPage(query);
		List<SkuBO> resultList = pageSupport.getResultList();
		return pageSupport;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional
	public R batchUpdateStock(List<Long> ids, Integer stock) {
		Date date = new Date();
		List<Long> idList = productDao.batchCheckStock(ids, stock);
		if (ObjectUtil.isNotEmpty(idList)) {
			log.error("库存数据出错了，实际库存小于销售库存，skuId为" + JSONUtil.parse(idList));
			return R.fail("库存数据出错了，实际库存小于销售库存，skuId为" + JSONUtil.parse(idList));
		}
		List<StockLogDTO> list = new ArrayList<>();

		/**
		 * 保存编辑库存的日志
		 * 获取sku
		 */
		List<Sku> skuList = skuDao.getSku(ArrayUtil.toArray(ids, Long.class));
		ids.forEach(e -> skuList.forEach(i -> {
			if (i.getId().compareTo(e) == 0) {
				StockLogDTO stockLogDTO = new StockLogDTO();
				stockLogDTO.setProductId(i.getProductId());
				stockLogDTO.setSkuId(i.getId());
				stockLogDTO.setName(i.getName());
				stockLogDTO.setBeforeStock(i.getStocks());
				stockLogDTO.setAfterStock(stock);
				stockLogDTO.setUpdateTime(date);
				stockLogDTO.setUpdateRemark("商家修改【" + i.getName() + i.getCnProperties() + "】库存为：" + stock + "");
				list.add(stockLogDTO);
			}
		}));
		stockLogService.saveStockLog(list);
		productDao.batchUpdateStock(ids, stock);
		//更新spu库存
		List<Long> productIdList = skuList.stream().map(Sku::getProductId).distinct().collect(Collectors.toList());
		updateProductStock(productIdList);
		return R.ok();
	}

	@Override
	public List<StockExportDTO> stocksExport(ProductQuery query) {
		List<StockExportDTO> stocksExport = skuDao.stocksExport(query);
		stocksExport.forEach(e -> {
			if (StrUtil.isNotBlank(e.getStatusStr())) {
				switch (e.getStatusStr()) {
					case "1":
						e.setStatusStr("上架");
						break;
					case "0":
						e.setStatusStr("下架");
						break;
					default:
						break;
				}
			}
		});
		return stocksExport;
	}

	@Override
	public List<SkuBO> stocksList(SkuQuery query) {
		return skuDao.stocksList(query);
	}

	@Override
	public List<SkuBO> queryStocksListBySkuIdList(List<Long> skuIdList) {
		List<SkuBO> skuBoList = skuDao.queryStocksListBySkuIdList(skuIdList);
		if (CollUtil.isEmpty(skuBoList)) {
			return skuBoList;
		}
		Map<Long, SkuBO> collect = skuBoList.stream().collect(Collectors.toMap(SkuBO::getId, e -> e));
		List<SkuBO> noPicSku = skuBoList.stream().filter(e -> StrUtil.isBlank(e.getPic())).collect(Collectors.toList());
		if (CollUtil.isEmpty(noPicSku)) {
			return skuBoList;
		}
		List<Long> productIdList = noPicSku.stream().map(SkuBO::getProductId).collect(Collectors.toList());
		if (CollUtil.isEmpty(productIdList)) {
			return skuBoList;
		}
		List<Product> products = productDao.queryAllByIds(productIdList);
		if (CollUtil.isEmpty(products)) {
			return skuBoList;
		}
		Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, e -> e));
		for (SkuBO skuBO : noPicSku) {
			if (!productMap.containsKey(skuBO.getProductId()) || !collect.containsKey(skuBO.getId())) {
				continue;
			}
			String pic = productMap.get((skuBO.getProductId())).getPic();
			collect.get(skuBO.getId()).setPic(pic);
		}

		return skuBoList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R batchUpdateSku(List<SkuUpdateSkuDTO> skuDTOList) {
		BigDecimal max = new BigDecimal(0L);
		BigDecimal min = new BigDecimal(0L);
		List<SkuBO> originskuList = skuDao.queryStocksListBySkuIdList(skuDTOList.stream().map(SkuUpdateSkuDTO::getId).collect(Collectors.toList()));
		Date date = new Date();
		List<StockLogDTO> list = new ArrayList<>();
		List<PriceLogDTO> priceList = new ArrayList<>();
		/*记录原sku库存为0的skuId，用于到货通知*/
		List<SkuBO> arricalSkuIdList = new ArrayList<>();
		Long productId = null;
		/*遍历两个skulist，修改库存，*/
		for (SkuBO originsku : originskuList) {
			for (SkuUpdateSkuDTO skuDTO : skuDTOList) {
				if (originsku.getId().compareTo(skuDTO.getId()) == 0) {
					int editstock = skuDTO.getEditStocks();
					if (editstock == 0) {
						continue;
					}
					if (originsku.getActualStocks().compareTo(originsku.getStocks()) < 0) {
						log.error("库存数据出错了，实际库存小于销售库存，skuId为" + originsku.getId() + ":" + originsku.getCnProperties());
						return R.fail("库存数据出错了，实际库存小于销售库存，skuId为" + originsku.getId() + ":" + originsku.getCnProperties());
					}
					productId = originsku.getProductId();
					/*原库存数*/
					Integer stocks = originsku.getStocks();

					/*如果入库标识为true，销售库存、实际库存都加上操作库存数editStocks，反之*/
					if (skuDTO.getPutStorageFlag()) {
						originsku.setActualStocks(originsku.getActualStocks() + editstock);
						originsku.setStocks(originsku.getStocks() + editstock);
					} else {
						originsku.setActualStocks(originsku.getActualStocks() - editstock);
						originsku.setStocks(originsku.getStocks() - editstock);
						if (originsku.getStocks() < 0 || originsku.getActualStocks() < 0) {
							return R.fail("修改后的库存不能小于0");
						}
					}

					if (ObjectUtil.isEmpty(originsku.getIntegralFlag())) {
						originsku.setIntegralFlag(false);
					}

					/*如果原库存为0，添加到arricalSkuIdList，发送到货通知*/
					if (stocks == 0) {
						arricalSkuIdList.add(originsku);
					}

					StockLogDTO stockLogDTO = new StockLogDTO();
					stockLogDTO.setProductId(originsku.getProductId());
					stockLogDTO.setSkuId(originsku.getId());
					stockLogDTO.setName(originsku.getName());
					stockLogDTO.setBeforeStock(stocks);
					stockLogDTO.setAfterStock(originsku.getStocks());
					stockLogDTO.setUpdateTime(date);
					stockLogDTO.setUpdateRemark("商家修改" + originsku.getProductId() + "【" + originsku.getProductName() + originsku.getCnProperties() + "】库存为：" + originsku.getStocks() + "");
					list.add(stockLogDTO);
					if (ObjectUtil.isNotEmpty(skuDTO.getEditPrice())) {
						//计算销售价的范围
						max = max.compareTo(skuDTO.getEditPrice()) >= 0 ? max : skuDTO.getEditPrice();
						min = min.compareTo(skuDTO.getEditPrice()) <= 0 ? min : skuDTO.getEditPrice();
						BigDecimal editPrice = skuDTO.getEditPrice();
						BigDecimal price = originsku.getPrice();
						originsku.setPrice(editPrice);
						PriceLogDTO priceLogDTO = new PriceLogDTO();
						priceLogDTO.setProductId(originsku.getProductId());
						priceLogDTO.setSkuId(originsku.getId());
						priceLogDTO.setName(originsku.getName());
						priceLogDTO.setBeforePrice(price);
						priceLogDTO.setAfterPrice(skuDTO.getEditPrice());
						priceLogDTO.setUpdateTime(date);
						priceLogDTO.setUpdateRemark("商家修改" + originsku.getProductId() + "【" + originsku.getProductName() + originsku.getCnProperties() + "】价格为：" + skuDTO.getEditPrice() + "");
						priceList.add(priceLogDTO);
					}

					// 缓存清理
					skuDao.cleanCache(skuDTO);
				}
			}
		}
		if (CollUtil.isNotEmpty(priceList)) {
			//更新商品价格
			Product product = productDao.getById(productId);
			product.setPrice(min.compareTo(max) == 0 ? formatPrice(min) : formatPrice(min) + " ~ " + formatPrice(max));
			product.setMinPrice(min);
			product.setMaxPrice(max);
			productDao.update(product);
			/*保存价格日志*/
			priceLogService.savePriceLog(priceList);
		}
		/*发送到货通知*/
		if (CollUtil.isNotEmpty(arricalSkuIdList)) {
			arrivalNoticeService.noticeUser(arricalSkuIdList);
		}
		if (CollUtil.isNotEmpty(list)) {
			/*保存库存日志*/
			stockLogService.saveStockLog(list);


			/*更新商品sku的销售库存和实际库存*/
			List<Sku> skus = skuConverter.toEntityList(originskuList);
			skus.forEach(sku -> {
				sku.setUpdateTime(date);
			});
			skuDao.update(skus);
			/*更新商品spu的销售库存和实际库存*/
			List<Long> productIds = originskuList.stream().map(SkuBO::getProductId).distinct().collect(Collectors.toList());
			updateProductStock(productIds);
		}
		return R.ok();
	}

	/**
	 * 对价格保留2位小数，并转换成字符串
	 */
	private String formatPrice(BigDecimal bg) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(bg) + "";
	}

	/**
	 * 根据商品idList刷新spu库存
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateProductStock(List<Long> productIdList) {
		List<Product> originProductList = productDao.queryAllByIds(productIdList);
		List<Product> productList = new ArrayList<>();
		List<Sku> sku = skuDao.queryByProductId(productIdList);
		/*按照productId对sku进行分类*/
		Map<Long, List<Sku>> skuMap = sku.stream().collect(Collectors.groupingBy(Sku::getProductId));
		Map<Long, List<Product>> productMap = originProductList.stream().collect(Collectors.groupingBy(Product::getId));

		productIdList.forEach(productId -> {
			clearProductCache(productId);
			List<Sku> skus = skuMap.get(productId);
			Product product = productMap.get(productId).get(0);
			/*设置实际库存*/
			product.setActualStocks(skus.stream().mapToInt(Sku::getActualStocks).sum());
			/*设置虚拟库存*/
			product.setStocks(skus.stream().mapToInt(Sku::getStocks).sum());
			productList.add(product);
		});
		return productDao.update(productList);
	}

	@Override
	public List<ProductDTO> queryAllByIds(List<Long> productIds) {
		List<Product> products = productDao.queryAllByIds(productIds);
		List<ProductDTO> productDTOList = new ArrayList<>();
		for (Product product : products) {
			ProductBO productBO = productConverter.convert2BO(product);
			productDTOList.add(productConverter.convertToProductDTO(productBO));
		}
		if (CollectionUtils.isEmpty(productDTOList)) {
			return new ArrayList<>();
		}
		//需要按原来的顺序输出
		productDTOList.sort(Comparator.comparingInt(prod -> productIds.indexOf(prod.getId())));
		return productDTOList;
	}


	/**
	 * 根据productId清除缓存
	 */
	public void clearProductCache(Long productId) {
		cacheManagerUtil.evictCache(PRODUCT_DETAILS + PRODUCT_DTO, productId);
		cacheManagerUtil.evictCache(PRODUCT_DETAILS + PRODUCT_BO, productId);
	}

	@Override
	public Long getCountByTransId(Long transId) {
		return productDao.getCountByTransId(transId);
	}

	@Override
	public void appointOnline(Long productId) {
		Appoint appoint = appointDao.queryByProductId(productId);
		if (DateUtil.date().before(appoint.getOnSellDate())) {
			log.info("未到预约时间，继续发送队列~ 商品ID: {}", productId);
			productProducerService.appointOnLine(appoint.getProductId(), appoint.getOnSellDate());
			return;
		}

		Product product = productDao.getById(productId);
		ShopDetailDTO shopDetailDTO = null;
		R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(product.getShopId());
		if (shopDetailResult.success()) {
			shopDetailDTO = shopDetailResult.getData();
		}

		/**
		 * 上架必须满足四个条件：
		 * 店铺是正常的运营状态
		 * 商品是下架状态
		 * 预约上架表有未处理的上架信息
		 * 上架时间在过去的合理范围内
		 */
		if (ObjectUtil.isNotNull(shopDetailDTO)
				&& ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetailDTO.getStatus())
				&& ProductStatusEnum.PROD_OFFLINE.getValue().equals(product.getStatus())
				&& OpStatusEnum.PASS.getValue().equals(product.getOpStatus())
				&& ProductDelStatusEnum.PROD_NORMAL.getValue().equals(product.getDelStatus())) {
			if (ObjectUtil.isNotEmpty(appoint) && !appoint.getOnSellFlag()) {
				List<Long> idList = new ArrayList<>();
				idList.add(productId);
				this.productUpdateStatus(ProductStatusEnum.PROD_ONLINE, idList);
				clearProductCache(productId);
			}
		}
	}

	@Override
	public List<ProductDTO> getProductListByGroupId(Long productGroupId, Integer pageSize) {

		ProductGroupBO productGroup = productGroupService.getProductGroup(productGroupId);
		if (ObjectUtil.isNull(productGroup)) {
			return null;
		}
		// 获取分组关联商品ID集合
		List<Long> productIds = productGroupRelationService.getProductIdListByGroupId(productGroupId);
		if (ObjectUtil.isEmpty(productIds)) {
			return null;
		}
		// 获取分组排序条件
		String orderBy;
		if (ProductGroupTypeEnum.SYSTEM.getValue().equals(productGroup.getType())) {
			//获取系统定义分组的排序条件
			String conditional = productGroup.getConditional();
			ProductGroupConditionalBO productGroupConditionalBO = JSONUtil.toBean(conditional, ProductGroupConditionalBO.class);
			orderBy = productGroupConditionalBO.getSortBy();
		} else {
			orderBy = productGroup.getSort();
		}
		List<Product> productList = productDao.getProductListByIds(productIds, pageSize, orderBy);
		return productConverter.to(productList);
	}

	@Override
	public void offLineByShopIds(List<Long> shopIds) {
		List<Long> productIds = productDao.queryIdByShopId(shopIds);
		this.productUpdateStatus(ProductStatusEnum.PROD_OFFLINE, productIds);
		//清除缓存
		for (Long id : productIds) {
			clearProductCache(id);
		}
	}

	@Override
	public Long getProductCountByShopId(Long shopId, OpStatusEnum opStatusEnum) {
		return productDao.getProductCountByShopId(shopId, opStatusEnum);
	}

	@Override
	public R batchUpdateDelStatus(ProductBatchDelDTO batchDelDTO, Long shopId) {
		List<Product> productList = productDao.queryAllByIds(batchDelDTO.getIds());
		if (CollUtil.isEmpty(productList)) {
			return R.fail("删除失败，找不到删除商品信息！");
		}

		Integer status = batchDelDTO.getStatus();

		List<Product> effectiveProductList = productList.stream()
				// 只放行平台或者本店铺的商品
				.filter(product -> ObjectUtil.isEmpty(shopId) || product.getShopId().equals(shopId))
				// 待审核状态的商品不能删除（未发布的可以删除）
				.filter(product -> !OpStatusEnum.WAIT.getValue().equals(product.getOpStatus())
						|| ProductStatusEnum.UNPUBLISHED.getValue().equals(product.getStatus()))
				.collect(Collectors.toList());

		if (CollUtil.isEmpty(effectiveProductList)) {
			return R.fail("删除失败，找不到删除商品信息！");
		}

		/*获取审核通过的商品id*/
		List<Long> productIds = effectiveProductList.stream()
				.map(Product::getId)
				.collect(Collectors.toList());

		if (CollUtil.isNotEmpty(productIds)) {

			//如果是删除  要先下架，再删除
			if (!ProductDelStatusEnum.PROD_NORMAL.getValue().equals(status)) {
				long count = effectiveProductList.stream()
						.filter(product -> ProductStatusEnum.PROD_ONLINE.getValue().equals(product.getStatus()) && OpStatusEnum.PASS.getValue().equals(product.getOpStatus()))
						.count();
				if (count > 0) {
					return R.fail("该商品列表中有上架的商品，请先下架");
				}

				// 锋哥：商品删除时，把草稿也一起删除
				draftProductService.deleteByProductId(productIds);
			}

			// 如果是商家
			if (ObjectUtil.isNotEmpty(shopId)) {
				// 则不能物理删除
				if (ProductDelStatusEnum.PROD_ADMIN_DELETE.value().equals(status)) {
					return R.fail("状态标识不匹配");
				}
			} else {
				// 如果是平台
				if (ProductDelStatusEnum.PROD_ADMIN_DELETE.getValue().equals(status)) {
					// 则只能删除商家永久删除的商品
					long count = effectiveProductList.stream()
							.filter(product -> !ProductDelStatusEnum.PROD_SHOP_DELETE.getValue().equals(product.getDelStatus()))
							.count();
					if (count > 0) {
						return R.fail("该商品列表中有商家未永久删除的商品");
					}
				} else {
					// 平台不能帮助商家恢复商品
					return R.fail("状态标识不匹配");
				}
			}

			productDao.updateDelStatus(batchDelDTO.getStatus(), effectiveProductList);
			if (ProductDelStatusEnum.PROD_ADMIN_DELETE.getValue().equals(batchDelDTO.getStatus())) {
				//删除索引
				amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_DELETE_ROUTING_KEY, productIds, true);
			} else {
				//创建索引
				amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_CREATE_ROUTING_KEY, productIds, true);
			}
			effectiveProductList.stream()
					.map(product -> {
						//  更新后清楚缓存
						clearProductCache(product.getId());
						return product.getId();
					})
					.collect(Collectors.toList());
			return R.ok();
		}
		return R.fail("删除失败，需要删除的商品状");
	}

	@Override
	public PageSupport<ProductAccusationBO> accusationPage(ProductQuery query) {
		PageSupport<ProductBO> productPage = productDao.getProductPage(query);
		PageSupport<ProductAccusationBO> pageSupport = productConverter.convert2ProductAccusationBO(productPage);
		List<ProductAccusationBO> resultList = pageSupport.getResultList();
		if (CollUtil.isNotEmpty(resultList)) {
			// 获取违规商品Id
			List<Long> prodIds = resultList.stream()
					.filter(product -> OpStatusEnum.PROD_ILLEGAL_OFF.getValue().equals(product.getOpStatus())
							|| OpStatusEnum.PROD_ILLEGAL_LOCK.getValue().equals(product.getOpStatus()))
					.map(ProductAccusationBO::getId)
					.collect(Collectors.toList());
			// 查询违规商品举报信息
			List<AccusationBO> accusationBoList = accusationDao.queryLatestAccusation(prodIds);
			if (CollUtil.isNotEmpty(accusationBoList)) {
				Map<Long, List<AccusationBO>> accusationMap = accusationBoList.stream().collect(Collectors.groupingBy(AccusationBO::getProductId));
				resultList.forEach(result -> {
					if (accusationMap.containsKey(result.getId())) {
						List<AccusationBO> accusationList = accusationMap.get(result.getId());
						for (AccusationBO accusationBO : accusationList) {
							result.setAccusationContent(accusationBO.getContent());
							result.setAccusationTime(accusationBO.getCreateTime());
							result.setAccusationTypeName(accusationBO.getTypeName());
						}
					}
				});
			}
		}
		return pageSupport;
	}


	@Override
	public Integer updateStocksByProductId(Long id, Integer basketCount, Long shopId) {
		if (basketCount == 0) {
			throw new BusinessException("变更库存不能为0！");
		}
		Integer result = productDao.updateStocksByProductId(id, basketCount, shopId);
		if (result == 0) {
			throw new BusinessException("变更库存失败！");
		}
		return result;
	}


	/**
	 * 商品状态变更处理
	 */
	public void productUpdateStatus(ProductStatusEnum status, List<Long> productIds) {
		this.productDao.updateStatus(status.value(), productIds);
		//创建索引
		amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_CREATE_ROUTING_KEY, productIds, true);
	}

	/**
	 * 根据商品ID获取商品信息分页列表
	 */
	private PageSupport<ProductBO> pageListByProdIds(List<Long> prodIds) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageSize(prodIds.size());
		productQuery.setCurPage(1);
		productQuery.setProductIdList(prodIds);
		return this.productDao.getProductPage(productQuery);
	}

	@GlobalTransactional(rollbackFor = Exception.class)
	@Transactional(rollbackFor = Exception.class)
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = PayAmqpConst.UPDATE_BUYS_QUEUE, durable = "true"),
			exchange = @Exchange(value = PayAmqpConst.PAY_EXCHANGE), key = {PayAmqpConst.UPDATE_BUYS_ROUTING_KEY}
	))
	public void updateBuys(String msg, Message message, Channel channel) throws IOException {
		log.info("mq消费支付完成更新商品购买数 接收到消息：" + msg);
		log.info("消息体：" + new String(message.getBody()));
		try {
			if (JSONUtil.isJsonArray(msg)) {
				JSONArray jsonArray = JSONUtil.parseArray(msg);
				List<String> numberList = JSONUtil.toList(jsonArray, String.class);

				List<SkuBO> skuList = skuDao.calculateBuys(numberList);
				for (SkuBO sku : skuList) {
					//更新商品购买数
					boolean result = skuDao.updateBuys(sku.getId(), sku.getBuys());
					if (!result) {
						log.error("mq更新商品购买数失败");
						//重新推送
						channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
						throw new BusinessException("mq更新商品购买数失败");
					}
				}

				//计算每个商品的购买数
				List<ProductDTO> productDTOList = productDao.calculateBuys(numberList);
				for (ProductDTO productDTO : productDTOList) {
					//更新商品购买数
					boolean result = productDao.updateBuys(productDTO.getId(), productDTO.getBuys());
					if (!result) {
						log.error("mq更新商品购买数失败");
						//重新推送
						throw new BusinessException("mq更新商品购买数失败,订单编号" + numberList);
					}

					// 更新购买数
					esIndexApi.reIndex(IndexTypeEnum.PRODUCT_INDEX.name(), IndexTargetMethodEnum.UPDATE.getValue(), ProductTargetTypeEnum.STATISTICS.getValue(), String.valueOf(productDTO.getId()));
				}
				log.info("更新商品购买数并重建索引");
				//分组统计店铺购买数
				Map<Long, List<ProductDTO>> shopBuysMap = productDTOList.stream().collect(Collectors.groupingBy(ProductDTO::getShopId));
				for (Long shopId : shopBuysMap.keySet()) {
					List<ProductDTO> productDtoList = shopBuysMap.get(shopId);
					Integer buys = productDtoList.stream().mapToInt(ProductDTO::getBuys).sum();
					R<Boolean> result = shopDetailApi.updateBuys(shopId, buys);
					if (!result.getSuccess()) {
						log.error("mq更新店铺销售数失败");
						//重新推送
						throw new BusinessException("mq更新店铺销售数失败" + numberList);
					}
				}
				//重建索引
				log.info("###### 更新购买数成功 ##### ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新商品购买数失败，原因：{}", e.toString());
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}

	private void checkSkuActivity(List<SkuDTO> originSku) {
		List<Long> skuIds = originSku.stream().map(SkuDTO::getId).collect(Collectors.toList());
		if (originSku.stream().anyMatch(e -> e.getIntegralDeductionFlag() || e.getIntegralFlag())) {
			throw new BusinessException("商品编辑失败，该商品正在参与积分活动");
		}
	}

	@Override
	public PageSupport<SkuBO> stocksArmPage(ProductQuery query) {
		return skuDao.stocksArmPage(query);
	}

	@Override
	public ProductNewCountBO getIndexInfo() {
		Date now = DateUtil.parse(DateUtil.now());
		Date beginOfDay = DateUtil.beginOfDay(now);
		Integer newProductCount = productDao.getNewProduct(beginOfDay, now);
		Integer newOrderCount = productDao.getNewOrder(beginOfDay, now);
		Integer newAfterSaleOrderCount = productDao.getNewRefundOrder(beginOfDay, now);
		Integer newReportProductCount = productDao.getNewAccusationProduct(beginOfDay, now);
		Integer newReferProductCount = productDao.getNewReferProduct(beginOfDay, now);
		return ProductNewCountBO
				.builder()
				.newProductCount(newProductCount)
				.newOrderCount(newOrderCount)
				.newAfterSaleOrderCount(newAfterSaleOrderCount)
				.newReportProductCount(newReportProductCount)
				.newReferProductCount(newReferProductCount)
				.build();
	}

	@Override
	@GlobalTransactional
	public R productOnline(AuditDTO auditDTO) {
		if (CollUtil.isEmpty(auditDTO.getIdList())) {
			return R.fail("请选择商品~");
		}

		if (StrUtil.isBlank(auditDTO.getAuditUsername())) {
			return R.fail("请登录~");
		}

		List<Product> products = productDao.queryAllByIds(auditDTO.getIdList());
		if (CollUtil.isEmpty(products)) {
			return R.fail("请选择商品~");
		}

		/*保存审核日志*/
		List<AuditDTO> auditDTOList = new ArrayList<AuditDTO>();

		products = products.stream()
				.filter(e -> OpStatusEnum.PROD_ILLEGAL_OFF.getValue().equals(e.getOpStatus())
						|| OpStatusEnum.PROD_ILLEGAL_LOCK.getValue().equals(e.getOpStatus()))
				.collect(Collectors.toList());
		for (Product product : products) {
			product.setOpStatus(OpStatusEnum.PASS.getValue());
			product.setUpdateTime(DateUtil.date());

			AuditDTO auditItem = new AuditDTO();
			auditItem.setAuditTime(DateUtil.date());
			auditItem.setAuditType(AuditTypeEnum.PRODUCT.getValue());
			auditItem.setCommonId(product.getId());
			auditItem.setAuditUsername(auditDTO.getAuditUsername());
			auditItem.setOpStatus(OpStatusEnum.PASS.getValue());
			auditItem.setAuditOpinion("违规商品上架~");
			auditDTOList.add(auditItem);
		}
		productDao.update(products);
		auditApi.audit(auditDTOList);

		//创建索引
		GlobalTransaction currentTransaction = GlobalTransactionContext.getCurrent();
		if (null != currentTransaction) {
			TransactionHookManager.registerHook(new TransactionHookAdapter() {
				@Override
				public void afterCommit() {
					esIndexApi.reIndex(IndexTypeEnum.PRODUCT_INDEX.name(), IndexTargetMethodEnum.CREATE.getValue(), null, StrUtil.join(",", auditDTO.getIdList()));
				}
			});
		} else {
			esIndexApi.reIndex(IndexTypeEnum.PRODUCT_INDEX.name(), IndexTargetMethodEnum.CREATE.getValue(), null, StrUtil.join(",", auditDTO.getIdList()));
		}
		return R.ok();
	}

	@Override
	public List<ProductRecycleBinExportDTO> findExportRecycleBinProd(ProductQuery query) {
		List<ProductBO> productList = productDao.getProductList(query);
		List<ProductRecycleBinExportDTO> resultList = productConverter.convert2ProductRecycleBinExportDTO(productList);
		resultList.forEach(e -> {
			switch (e.getDelStatus()) {
				case -2 -> e.setDelStatusStr("商家永久删除");
				case -1 -> e.setDelStatusStr("商家删除");
				case 1 -> e.setDelStatusStr("正常");
				default -> {
				}
			}
		});
		return resultList;
	}

	@Override
	public void preSellFinish(Long productId) {
		PreSellProduct preSell = preSellProductDao.getByProductId(productId);
		if (null == preSell) {
			return;
		}
		// 如果当前时间小于预售结束时间，则继续进队列
		if (DateUtil.date().before(preSell.getPreSaleEnd())) {
			productProducerService.preSellFinish(productId, preSell.getPreSaleEnd());
			return;
		}

		Product product = productDao.getById(productId);
		if (null == product) {
			return;
		}

		// 预售商品下线
		product.setStatus(ProductStatusEnum.PROD_OFFLINE.getValue());
		product.setUpdateTime(DateUtil.date());
		productDao.update(product);
		//删除索引
		amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_DELETE_ROUTING_KEY, Collections.singleton(productId), true);
	}

	@Override
	public PageSupport<ProductAuditBO> auditPage(ProductQuery query) {
		PageSupport<ProductBO> productPage = productDao.auditPage(query);
		PageSupport<ProductAuditBO> pageSupport = productConverter.convert2ProductAuditBO(productPage);
		List<ProductAuditBO> resultList = pageSupport.getResultList();
		if (CollUtil.isNotEmpty(resultList)) {

			List<Long> productIdList = resultList.stream().map(ProductAuditBO::getId).collect(Collectors.toList());
			List<Long> shopIdList = resultList.stream().map(ProductAuditBO::getShopId).collect(Collectors.toList());

			//组装sku数量和商店名字
			List<SkuBO> skuBOList = skuService.querySkuByProductIdList(productIdList);
			R<List<ShopDetailDTO>> shopList = shopDetailApi.queryByIds(shopIdList);
			Map<Long, List<SkuBO>> skuMap = skuBOList.stream().collect(Collectors.groupingBy(SkuBO::getProductId));
			Map<Long, List<ShopDetailDTO>> shopMap = Optional.ofNullable(shopList.getData()).orElse(Collections.emptyList()).stream().collect(Collectors.groupingBy(ShopDetailDTO::getId));

			//获取品牌
			List<Long> brandIdList = resultList.stream().map(ProductBO::getBrandId).collect(Collectors.toList());
			List<Brand> brandList = brandDao.queryAllByIds(brandIdList);
			Map<Long, Brand> brandMap = Optional.ofNullable(brandList).orElse(Collections.emptyList()).stream().collect(Collectors.toMap(Brand::getId, e -> e));

			resultList.forEach(result -> {

				if (brandMap.containsKey(result.getBrandId())) {
					result.setBrandName(brandMap.get(result.getBrandId()).getBrandName());
				}

				List<SkuBO> skuList = skuMap.get(result.getId());
				result.setSkuCount(Optional.ofNullable(skuList).map(List::size).orElse(0));
				if (shopMap.containsKey(result.getShopId())) {
					ShopDetailDTO shopDetailDTO = shopMap.get(result.getShopId()).get(0);
					result.setSiteName(shopDetailDTO.getShopName());
				}
			});
		}
		return pageSupport;
	}

	@Override
	public R<List<ProductBO>> queryProductBySkuIdList(List<Long> skuIdList) {
		List<Product> products = productDao.queryProductBySkuIdList(skuIdList);
		List<ProductBO> productBOList = new ArrayList<>();
		for (Product product : products) {
			productBOList.add(productConverter.convert2BO(product));
		}
		return R.ok(productBOList);
	}

	@Override
	public List<ProductPlatformExportDTO> findExportPlatformProd(ProductQuery query) {
		List<ProductPlatformBO> productList = productDao.getProductPlatformList(query);
		List<ProductPlatformExportDTO> resultList = productConverter.convert2ProductPlatformExportDTO(productList);
		resultList.forEach(e -> {
			switch (e.getStatus()) {
				case -2 -> e.setStatusStr("永久删除");
				case -1 -> e.setStatusStr("已删除");
				case 0 -> e.setStatusStr("下线");
				case 1 -> e.setStatusStr("上线");
				case 2 -> e.setStatusStr("违规下线");
				case 3 -> e.setStatusStr("待审核");
				case 4 -> e.setStatusStr("审核失败");
				default -> {
				}
			}
		});

		// 设置分销默认值
		resultList.forEach(e -> {
			e.setCommission("￥0.00");
		});

		return resultList;
	}

	@Override
	public Long getIndexCount() {
		return productDao.getIndexCount();
	}

	@Override
	public Long getBrandId(Long id) {
		List<Product> productList = productDao.getBrandId(id);
		if (CollUtil.isEmpty(productList)) {
			return 0L;
		}
		//过滤出品牌已上架 的商品数量
		return productList.stream().filter(e -> ProductStatusEnum.PROD_ONLINE.getValue().equals(e.getStatus())).count();
	}

	@Override
	public void updateProductList(List<ProductDTO> productDTOList) {

		for (ProductDTO productDTO : productDTOList) {
			// 把已提交审核更新成待审核状态
			if (DraftProductStatusEnum.WAIT.getValue().equals(productDTO.getDraftStatus())) {
				draftProductDao.updateStatus(productDTO.getId(), productDTO.getDraftStatus(), DraftProductStatusEnum.UNPUBLISHED);
			}
			productDTO.setArraignment(true);
			draftProductService.update(productDTO);
		}
	}

	@Override
	public R batchEditProduct(List<ProductDTO> productDTOList) {
		StringBuilder stringBuilder = new StringBuilder();

		for (ProductDTO productDTO : productDTOList) {


			productDTO.setArraignment(true);

			Long productId = productDTO.getId();
			Product originProduct = productDao.getById(productId);

			if (DraftProductStatusEnum.WAIT.getValue().equals(originProduct.getStatus())) {
				return R.fail("商品：" + originProduct.getName() + "正在提审中！");
			}

			//获取店铺信息
			R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(productDTO.getShopId());
			ShopDetailDTO shopDetail = shopDetailResult.getData();


			if (shopDetail == null
					|| !OpStatusEnum.PASS.getValue().equals(shopDetail.getOpStatus())
					|| !ShopDetailStatusEnum.ONLINE.getStatus().equals(shopDetail.getStatus())) {
				log.warn("店铺信息有误，请联系管理员");
				return R.fail("商品：" + originProduct.getName() + "店铺信息有误，请联系管理员！");
			}

			if (originProduct == null) {
				return R.fail("商品：" + originProduct.getName() + "不存在，修改失败！");
			}

			// 获取商品ID
			Long originProductId = originProduct.getId();

			if (!originProduct.getShopId().equals(productDTO.getShopId())) {
				log.error("修改商品失败，当前商家正在修改其它商家的商品，当前商家ID: {}，商品ID: {}", productDTO.getShopId(), originProductId);
				return R.fail("商品：" + originProduct.getName() + "不存在，修改失败！");
			}

			if (OpStatusEnum.PROD_ILLEGAL_LOCK.getValue().equals(originProduct.getOpStatus())) {
				log.warn("商品已被锁定，无法编辑");
				return R.fail("商品：" + originProduct.getName() + "已被锁定，无法编辑！");
			}

			if (!ProductDelStatusEnum.PROD_NORMAL.getValue().equals(originProduct.getDelStatus())) {
				log.warn("商品已被删除，无法编辑");
				return R.fail("商品：" + originProduct.getName() + "已被删除，无法编辑！");
			}

			if (ObjectUtil.isNotNull(productDTO.getDeliveryType())) {
				//更新配送类型
				productDao.updateProductDeliveryType(productDTO.getDeliveryType(), originProduct.getId());
			}

			// 获取原sku列表
			List<DraftSku> originSkuList = draftSkuService.getSkuByProductId(productId);
			if (CollUtil.isNotEmpty(originSkuList)) {
				// 整合sku
				BigDecimal max = new BigDecimal(0L);
				BigDecimal min = originSkuList.get(0).getPrice();
				for (DraftSku sku : originSkuList) {
					//计算销售价的范围
					max = max.compareTo(sku.getPrice()) >= 0 ? max : sku.getPrice();
					min = min.compareTo(sku.getPrice()) <= 0 ? min : sku.getPrice();
				}
				productDTO.setMaxPrice(max);
				productDTO.setMinPrice(min);
			} else {
				productDTO.setMaxPrice(new BigDecimal(productDTO.getPrice()));
				productDTO.setMinPrice(new BigDecimal(productDTO.getPrice()));
			}


			// 获取商品草稿
			DraftProduct draftProduct = draftProductDao.getByProductId(originProductId);
			if (null == draftProduct) {
				// 如果为空，则为旧数据，需要创建一个新的草稿
				draftProduct = draftProductConverter.convert2DraftProduct(productDTO);
				draftProduct.setCreateTime(DateUtil.date());
			} else {
				if (draftProduct.getStatus().equals(DraftProductStatusEnum.WAIT.getValue())) {
					return R.fail("商品：" + originProduct.getName() + "更新失败，商品正在提审中！");
				}

				Long id = draftProduct.getId();
				Date createTime = draftProduct.getCreateTime();
				draftProduct = draftProductConverter.convert2DraftProduct(productDTO);
				draftProduct.setId(id);
				draftProduct.setCreateTime(createTime);
				draftProduct.setUpdateTime(DateUtil.date());
			}
			draftProduct.setVersion(originProduct.getVersion() + 1);

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

			/*// 更新sku
			draftSkuService.updateSku(productDTO, originProduct, date);*/
			// 更新商品草稿
			draftProductDao.saveOrUpdate(draftProduct);

			// 如果店铺不需要审核，则直接发布
			if (DraftProductStatusEnum.PASS.getValue().equals(draftProduct.getStatus())) {
				return draftProductService.arraignment(originProductId);
			} else if (DraftProductStatusEnum.UNPUBLISHED.getValue().equals(draftProduct.getStatus())) {
				// 如果是未发布的，则直接更新
				return draftProductService.updateProduct(draftProduct, "");
			}

		}
		return R.ok();
	}
}


