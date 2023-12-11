/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendParamEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.CacheManagerUtil;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dao.SkuDao;
import com.legendshop.product.dao.StockLogDao;
import com.legendshop.product.dto.BatchUpdateStockDTO;
import com.legendshop.product.dto.InventoryOperationsDTO;
import com.legendshop.product.entity.Product;
import com.legendshop.product.entity.Sku;
import com.legendshop.product.entity.StockLog;
import com.legendshop.product.enums.InventoryOperationsTypeEnum;
import com.legendshop.product.service.StockService;
import com.legendshop.shop.api.ShopDetailApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存管理服务
 *
 * @author legendshop
 */
@Slf4j
@Service
public class StockServiceImpl implements StockService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private SkuDao skuDao;


	@Autowired
	private StockLogDao stockLogDao;

	@Autowired
	private MessageApi messagePushClient;

	@Autowired
	private CacheManagerUtil cacheManagerUtil;

	@Autowired
	private ShopDetailApi shopDetailApi;

	/**
	 * hold住库存，客户下单时调用，使得后面的人不能继续下单 如果库存不足， 则不能hold成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean reduceHold(Long productId, Long skuId, Integer basketCount) {
		Integer stocks = skuDao.getStocksByLockMode(skuId);
		// sku库存
		if (stocks == null) {
			stocks = 0;
		}
		if (stocks - basketCount < 0) {
			return false;
		} else {
			Integer result = skuDao.updateSkuStocks(skuId, -basketCount);
			if (result == 0) {
				return false;
			}
			Sku sku = skuDao.getByProductIdSkuId(productId, skuId);
			StockLog stockLog = new StockLog();
			stockLog.setProductId(productId);
			stockLog.setSkuId(skuId);

			if (StrUtil.isEmpty(sku.getName())) {
				Product product = productDao.getById(productId);
				stockLog.setName(product.getName());
			} else {
				stockLog.setName(sku.getName());
			}
			Integer after = sku.getStocks() + basketCount;
			stockLog.setBeforeStock(after);
			stockLog.setAfterStock(sku.getStocks());
			stockLog.setUpdateTime(new Date());
			stockLog.setUpdateRemark("用戶购买商品'" + stockLog.getName() + "'更新库存，商品库存为:'" + stockLog.getAfterStock() + "'");
			stockLogDao.save(stockLog);
			//更新商品库存：等于所有SKU的库存
			productDao.updateProdStocks(productId);
			// 发送库存预警
			ProductBO productBO = productDao.getProductBO(productId);
			if (ObjectUtil.isNotEmpty(productBO.getStocksArm()) && NumberUtil.compare(sku.getStocks(), productBO.getStocksArm()) <= 0) {
				//发送提醒发货站内信给商家
				List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
				//替换参数内容
				String s = ObjectUtil.isNotEmpty(sku.getCnProperties()) ? sku.getCnProperties() : "";
				MsgSendParamDTO productNameDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, sku.getName() + " " + s, "black");
				msgSendParamDTOS.add(productNameDTO);
				messagePushClient.push(new MessagePushDTO()
						.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
						.setReceiveIdArr(new Long[]{shopDetailApi.getById(productBO.getShopId()).getData().getShopUserId()})
						.setMsgSendType(MsgSendTypeEnum.PROD_REMIND_STOCKS)
						.setSysParamNameEnum(SysParamNameEnum.PROD_STOCKS)
						.setMsgSendParamDTOList(msgSendParamDTOS)
						.setDetailId(productId));
			}
			return true;
		}
	}


	/**
	 * 扣库存,允许为负
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean batchDeductionInventory(List<BatchUpdateStockDTO> batchUpdateStockDTO) {
		if (CollUtil.isEmpty(batchUpdateStockDTO)) {
			throw new BusinessException("库存扣减异常");
		}

		List<StockLog> stockLogs = new ArrayList<>();
		for (BatchUpdateStockDTO updateStockDTO : batchUpdateStockDTO) {
			if (updateStockDTO.getBasketCount() == 0) {
				continue;
			}
			cacheManagerUtil.evictCache("SkuBO", updateStockDTO.getSkuId());
			Sku sku = skuDao.getById(updateStockDTO.getSkuId());
			if (sku == null) {
				continue;
			}

			Product product = productDao.getById(updateStockDTO.getProductId());
			if (product == null) {
				continue;
			}

			Integer updateCount = 0;
			if (updateStockDTO.getBasketCount() > 0) {
				updateCount = skuDao.directDeductionSkuStocks(updateStockDTO.getSkuId(), updateStockDTO.getBasketCount());
			} else {
				// 如果加库存，则where条件不需要判断库存是否大于0
				updateCount = skuDao.directAddSkuStocks(updateStockDTO.getSkuId(), -updateStockDTO.getBasketCount());
			}
			if (updateCount == 0) {
				throw new BusinessException("库存不足，扣减商品库存失败");
			}

			StockLog stockLog = new StockLog();
			stockLog.setProductId(updateStockDTO.getProductId());
			stockLog.setSkuId(updateStockDTO.getSkuId());

			if (StrUtil.isEmpty(sku.getName())) {
				stockLog.setName(product.getName());
			} else {
				stockLog.setName(sku.getName());
			}
			//上边已经修改过库存
			stockLog.setBeforeStock(sku.getStocks() + updateStockDTO.getBasketCount());
			stockLog.setAfterStock(sku.getStocks());

			stockLog.setUpdateTime(new Date());
			String orderTypeEnum = updateStockDTO.getOrderType();
			String nameByValue = OrderTypeEnum.getDes(orderTypeEnum);
			nameByValue = updateStockDTO.getOperationType() + nameByValue;
			stockLog.setUpdateRemark(nameByValue + ",商品'" + stockLog.getName() + "'更新库存,商品库存为:'" + stockLog.getAfterStock() + "'");
			stockLogs.add(stockLog);
		}
		//批量插入库存记录表
		stockLogDao.save(stockLogs);
		List<Long> productIdList = batchUpdateStockDTO.stream().map(BatchUpdateStockDTO::getProductId).distinct().collect(Collectors.toList());
		//更新商品库存：等于所有SKU的库存
		productDao.batchUpdateProdStocks(productIdList);
		return true;
	}


	/**
	 * 取消订单补库存
	 *
	 * @param skuId
	 * @param basketCount
	 */
	@Override
	public void makeUpSalesStocks(Long skuId, Integer basketCount) {
		//更新库存记录
		Sku sku = skuDao.getById(skuId);
		if (ObjectUtil.isNotNull(sku)) {
			this.skuDao.updateSkuStocks(skuId, basketCount);
			sku = skuDao.getById(skuId);
			StockLog stockLog = new StockLog();
			stockLog.setProductId(sku.getProductId());
			stockLog.setSkuId(sku.getId());

			if (StrUtil.isEmpty(sku.getName())) {
				Product product = productDao.getById(sku.getProductId());
				stockLog.setName(product.getName());
			} else {
				stockLog.setName(sku.getName());
			}
			stockLog.setBeforeStock(sku.getStocks() - basketCount);
			stockLog.setAfterStock(sku.getStocks());
			stockLog.setUpdateTime(new Date());
			stockLog.setUpdateRemark("用户订单取消，更新商品'" + stockLog.getName() + "'库存，商品库存为:'" + stockLog.getAfterStock() + "'");
			stockLogDao.save(stockLog);
			//更新商品库存：等于所有SKU的库存
			productDao.updateProdStocks(sku.getProductId());
		}
	}

	/**
	 * 退款退货补库存
	 */
	@Override
	public void makeUpStocks(Long skuId, Integer basketCount) {
		//更新库存记录
		Sku sku = skuDao.getById(skuId);
		if (ObjectUtil.isNotNull(sku)) {
			// 退货退款处理库存  退货退款需商家手动确认商品是否能够再次销售 在添加实际库存（添加实际库存时自动添加销售库存）
			// 退款处理库存   只退销售库存
			this.skuDao.updateSkuStocks(skuId, basketCount);

			StockLog stockLog = new StockLog();
			stockLog.setProductId(sku.getProductId());
			stockLog.setSkuId(sku.getId());

			if (StrUtil.isEmpty(sku.getName())) {
				Product product = productDao.getById(sku.getProductId());
				stockLog.setName(product.getName());
			} else {
				stockLog.setName(sku.getName());
			}
			stockLog.setBeforeStock(sku.getStocks() - basketCount);
			stockLog.setAfterStock(sku.getStocks());
			stockLog.setUpdateTime(new Date());
			stockLog.setUpdateRemark("用户退货，更新商品'" + stockLog.getName() + "'库存，商品库存为:'" + stockLog.getAfterStock() + "'");
			stockLogDao.save(stockLog);
			//更新商品库存：等于所有SKU的库存
			productDao.updateProductAllStocks(sku.getProductId());
		}
	}

	/**
	 * hold住库存，后台发货时调用  如果库存不足， 则不能hold成功
	 *
	 * @param productId   商品Id
	 * @param skuId       单品Id
	 * @param basketCount 订购数
	 * @return 成功与否
	 */

	@Override
	public boolean addActualHold(Long productId, Long skuId, Integer basketCount) {
		Integer stocks = skuDao.getActualStocksByLockMode(skuId);
		Sku sku = skuDao.getById(skuId);
		//校验sku是否存在，不存在则直接发货
		if (ObjectUtil.isEmpty(sku)) {
			return true;
		}

		// sku库存
		if (stocks == null) {

			stocks = 0;
		}
		if (stocks - basketCount < 0) {
			return false;
		} else {
			int result = skuDao.updateSkuActualStocks(skuId, -basketCount);
			if (result == 0) {
				return false;
			}
			//更新商品销售库存：等于所有SKU的库存
			productDao.updateProdActualStocks(productId);
			return true;
		}
	}

	@Override
	public void makeUpSalesStocksByOrderType(Long skuId, Integer basketCount, String orderType) {
		//更新库存记录
		Sku sku = skuDao.getById(skuId);
		if (ObjectUtil.isNotNull(sku)) {
			this.skuDao.updateSkuStocks(skuId, basketCount);
			sku = skuDao.getById(skuId);
			StockLog stockLog = new StockLog();
			stockLog.setProductId(sku.getProductId());
			stockLog.setSkuId(sku.getId());

			if (StrUtil.isEmpty(sku.getName())) {
				Product product = productDao.getById(sku.getProductId());
				stockLog.setName(product.getName());
			} else {
				stockLog.setName(sku.getName());
			}
			stockLog.setBeforeStock(sku.getStocks() - basketCount);
			stockLog.setAfterStock(sku.getStocks());
			stockLog.setUpdateTime(new Date());
			stockLog.setUpdateRemark("活动结束,用户" + orderType + "取消,更新商品'" + stockLog.getName() + "'库存，商品库存为:'" + stockLog.getAfterStock() + "'");
			stockLogDao.save(stockLog);
			//更新商品库存：等于所有SKU的库存
			productDao.updateProdStocks(sku.getProductId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> inventoryOperations(List<InventoryOperationsDTO> operationsList) {
		if (CollectionUtils.isEmpty(operationsList)) {
			return R.fail("没有可用的库存操作");
		}
		// 根据操作类型进行分组
		Map<InventoryOperationsTypeEnum, List<InventoryOperationsDTO>> operationsMap = operationsList.stream().collect(Collectors.groupingBy(InventoryOperationsDTO::getType));

		List<InventoryOperationsDTO> allList = operationsMap.get(InventoryOperationsTypeEnum.ALL);
		List<InventoryOperationsDTO> salesList = operationsMap.get(InventoryOperationsTypeEnum.SALES);
		List<InventoryOperationsDTO> actualList = operationsMap.get(InventoryOperationsTypeEnum.ACTUAL);

		//批量修改实际库存
		int[] allResult = this.skuDao.updateAllSkuStocks(allList);
		//批量修改销售库存
		int[] salesResult = this.skuDao.updateSalesSkuStocks(salesList);
		//批量修改实际库存
		int[] actualResult = this.skuDao.updateActualSkuStocks(actualList);
		//批量修改实际库存
		int[] productResult = this.productDao.updateActualProductStocks(actualList);

		if (ObjectUtil.isNotEmpty(allList)) {
			if (allResult.length != allList.size() || Arrays.stream(allResult).anyMatch(e -> e <= 0)) {
				throw new BusinessException("库存操作失败！");
			}
		}
		if (ObjectUtil.isNotEmpty(allList)) {
			if (salesResult.length != salesList.size() || Arrays.stream(salesResult).anyMatch(e -> e <= 0)) {
				throw new BusinessException("库存操作失败！");
			}
		}
		if (ObjectUtil.isNotEmpty(allList)) {
			if (actualResult.length != actualList.size() || Arrays.stream(actualResult).anyMatch(e -> e <= 0)) {
				throw new BusinessException("库存操作失败！");
			}
		}
		if (ObjectUtil.isNotEmpty(allList)) {
			if (productResult.length != actualList.size() || Arrays.stream(productResult).anyMatch(e -> e <= 0)) {
				throw new BusinessException("库存操作失败！");
			}

		}
		return R.ok();
	}

	/**
	 * 扣库存,允许为负
	 */
	@Override
	public boolean deductionInventory(Long productId, Long skuId, Integer basketCount) {
		log.info("sku处理库存, skuId:{}, 扣减库存:{}", skuId, basketCount);
		Integer integer = skuDao.directDeductionSkuStocks(skuId, basketCount);
		if (integer == 0) {
			throw new BusinessException("库存不足，扣减商品库存失败");
		}
		//更新商品库存：等于所有SKU的库存
		log.info("商品处理库存, productId:{}", productId);
		productDao.updateProdStocks(productId);
		return true;
	}
}
