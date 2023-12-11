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
import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.common.region.core.IpInfoDTO;
import com.legendshop.common.region.service.RegionSearcherService;
import com.legendshop.data.constants.AmqpConst;
import com.legendshop.data.dto.ProdViewMqDTO;
import com.legendshop.data.dto.ProductViewDTO;
import com.legendshop.data.dto.ShopViewDTO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dao.VitLogDao;
import com.legendshop.product.dto.CartVitLogDTO;
import com.legendshop.product.dto.VitLogDTO;
import com.legendshop.product.dto.VitLogRecordDTO;
import com.legendshop.product.dto.VitLogUserHistoryDTO;
import com.legendshop.product.entity.VitLog;
import com.legendshop.product.enums.VitLogPageEnum;
import com.legendshop.product.query.VitLogQuery;
import com.legendshop.product.service.VitLogService;
import com.legendshop.product.service.convert.VitLogConverter;
import com.legendshop.search.api.EsIndexApi;
import com.legendshop.search.enmus.IndexTargetMethodEnum;
import com.legendshop.search.enmus.IndexTypeEnum;
import com.legendshop.search.enmus.ProductTargetTypeEnum;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 浏览历史实现类
 *
 * @author legendshop
 */
@Slf4j
@Service
public class VitLogServiceImpl implements VitLogService {

	@Autowired
	private VitLogDao visitLogDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private EsIndexApi esIndexApi;

	@Autowired
	private VitLogConverter vitLogConverter;

	@Autowired
	private RegionSearcherService regionSearcherService;

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	@Autowired
	private ShopDetailApi shopDetailApi;


	/**
	 * 根据用户名获取浏览历史统计数量
	 */
	@Override
	public R<Integer> userVisitLogCount(Long userId) {
		return R.ok(Optional.ofNullable(this.visitLogDao.userVisitLogCount(userId)).orElse(0));
	}


	@Override
	public PageSupport<VitLogDTO> queryVitListPage(VitLogQuery vitLogQuery) {
		return this.vitLogConverter.page(visitLogDao.queryVitListPage(vitLogQuery));
	}

	/**
	 * 用户获取浏览历史
	 *
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	@Override
	public PageSupport<VitLogUserHistoryDTO> queryUserVitList(Integer pageSize, Integer curPage) {
		return this.vitLogConverter.convert2UserHistoryList(visitLogDao.queryUserVitList(pageSize, curPage));
	}

	@Override
	public Boolean deleteVitLog(Long userId, Long productId) {
		return visitLogDao.deleteUserVitLog(userId, productId);
	}

	@Override
	public void saveProdView(ProductBO bo, Long userId, String source) {

		ProdViewMqDTO dto = new ProdViewMqDTO();
		dto.setSource(source);
		dto.setUserId(userId);
		ProductViewDTO po = new ProductViewDTO();
		po.setProductId(bo.getId());
		po.setProductName(bo.getName());
		po.setShopId(bo.getShopId());
		po.setShopName(bo.getShopDetailBO().getShopName());
		po.setCreateTime(DateUtil.beginOfDay(new Date()));
		dto.setProductViewDTO(po);
		amqpSendMsgUtil.convertAndSend(AmqpConst.LEGENDSHOP_DATA_EXCHANGE, AmqpConst.LEGENDSHOP_DATA_PRODUCT_VIEW_LOG_ROUTING_KEY, dto);
	}

	/**
	 * 发送店铺浏览MQ
	 *
	 * @param shopId
	 * @param userId
	 * @param source
	 */
	@Override
	public void saveShopView(Long shopId, Long userId, String source) {
		ShopViewDTO shopViewDTO = new ShopViewDTO();
		shopViewDTO.setShopId(shopId);
		shopViewDTO.setUserId(userId);
		shopViewDTO.setSource(source);
		shopViewDTO.setCreateTime(DateUtil.beginOfDay(new Date()));
		amqpSendMsgUtil.convertAndSend(com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_EXCHANGE, com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_SHOP_VIEW_LOG_ROUTING_KEY, shopViewDTO);
	}

	@Override
	public void saveProdCartView(CartVitLogDTO cartVitLogDTO) {
		log.info("发送商品加入购物车队列~");
		ProdViewMqDTO dto = new ProdViewMqDTO();
		dto.setSource(cartVitLogDTO.getSource());
		dto.setUserId(cartVitLogDTO.getUserId());
		ProductViewDTO po = new ProductViewDTO();
		po.setProductId(cartVitLogDTO.getProductId());
		po.setProductName(cartVitLogDTO.getProductName());
		po.setShopId(cartVitLogDTO.getShopId());
		po.setCartNum(cartVitLogDTO.getCount());
		po.setSource(cartVitLogDTO.getSource());
		R<ShopDetailDTO> detailDTOR = shopDetailApi.getById(cartVitLogDTO.getShopId());
		if (detailDTOR.getSuccess()) {
			po.setShopName(detailDTOR.getData().getShopName());
		}
		po.setCreateTime(DateUtil.beginOfDay(new Date()));
		dto.setProductViewDTO(po);
		amqpSendMsgUtil.convertAndSend(AmqpConst.LEGENDSHOP_DATA_EXCHANGE, AmqpConst.LEGENDSHOP_DATA_PRODUCT_VIEW_CART_LOG_ROUTING_KEY, dto);
	}

	@Override
	public void batchSaveProdCartView(List<CartVitLogDTO> cartVitLogList) {
		if (CollUtil.isEmpty(cartVitLogList)) {
			return;
		}

		cartVitLogList.forEach(this::saveProdCartView);
	}

	@Override
	public R<List<VitLogDTO>> queryVitList(Long userId, Long shopId) {
		return R.ok(visitLogDao.queryVitList(userId, shopId));
	}

	@Override
	@Async
	public void recordVitLog(VitLogRecordDTO vitLogRecordDTO) {
		//每次访问记录访问记录
		VitLog vitLog = vitLogConverter.convert2Entity(vitLogRecordDTO);

		// 处理IP地址所属国家地区
		IpInfoDTO ipInfoDTO = regionSearcherService.binarySearch(vitLogRecordDTO.getIp());
		if (ipInfoDTO != null) {
			vitLog.setCountry(ipInfoDTO.getCountry());
			vitLog.setArea(ipInfoDTO.getAddress());
		}

		vitLog.setVisitNum(1);
		vitLog.setCreateTime(new Date());
		vitLog.setUpdateTime(new Date());
		visitLogDao.save(vitLog);

		//如果是商品访问，则商品的访问次数加1
		if (VitLogPageEnum.PRODUCT_PAGE.value().equals(vitLog.getPage())) {
			productDao.updateProductViews(vitLog.getProductId());

			// 更新es上的商品访问数
			esIndexApi.reIndex(IndexTypeEnum.PRODUCT_INDEX.name(), IndexTargetMethodEnum.UPDATE.getValue(), ProductTargetTypeEnum.STATISTICS.getValue(), String.valueOf(vitLog.getProductId()));
		}
	}

	@Override
	public PageSupport<VitLogDTO> queryVitListPageByUser(VitLogQuery vitLogQuery) {
		return this.vitLogConverter.page(visitLogDao.queryVitListPageByUser(vitLogQuery));
	}
}
