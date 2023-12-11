/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.DefaultPagerProvider;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.order.dao.OrderDao;
import com.legendshop.order.dao.OrderItemDao;
import com.legendshop.order.dao.PreSellOrderDao;
import com.legendshop.order.dao.ShopOrderBillDao;
import com.legendshop.order.dto.*;
import com.legendshop.order.entity.ShopOrderBill;
import com.legendshop.order.enums.*;
import com.legendshop.order.excel.*;
import com.legendshop.order.query.OrderRefundReturnBillQuery;
import com.legendshop.order.query.ShopOrderBillOrderQuery;
import com.legendshop.order.query.ShopOrderBillQuery;
import com.legendshop.order.service.*;
import com.legendshop.order.service.convert.ShopOrderBillConverter;
import com.legendshop.shop.api.PlateCapitalFlowApi;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.PlateCapitalFlowDTO;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.enums.DealTypeEnum;
import com.legendshop.shop.enums.FlowTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商家结算服务
 *
 * @author legendshop
 */
@Service
@Slf4j
public class ShopOrderBillServiceImpl implements ShopOrderBillService {

	@Autowired
	private ShopDetailApi shopDetailApi;

	@Autowired
	private PlateCapitalFlowApi plateCapitalFlowApi;

	@Autowired
	private ShopOrderBillDao shopOrderBillDao;

	@Autowired
	private ShopOrderBillConverter shopOrderBillConverter;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderItemDao orderItemDao;

	@Autowired
	private PreSellOrderDao preSellOrderDao;

	@Autowired
	private OrderRefundReturnService orderRefundReturnService;

	@Autowired
	private ShopBillPeriodService shopBillPeriodService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private PreSellOrderService preSellOrderService;

	@Autowired
	private SysParamsApi sysParamsApi;


	@Override
	public ShopOrderBillDTO getById(Long id) {
		ShopOrderBillDTO billDTO = shopOrderBillConverter.to(shopOrderBillDao.getById(id));
		if (null != billDTO) {
			billDTO.setOrderAmount(billDTO.getOrderAmount().add(billDTO.getShippingTotals()));
		}
		return billDTO;
	}

	public void deleteById(Long id) {
		shopOrderBillDao.deleteById(id);
	}

	@Override
	public Long saveShopOrderBill(ShopOrderBillDTO shopOrderBill) {
		if (ObjectUtil.isNotNull(shopOrderBill.getId())) {
			updateShopOrderBill(shopOrderBill);
			return shopOrderBill.getId();
		}
		return shopOrderBillDao.save(shopOrderBillConverter.from(shopOrderBill));
	}

	@Override
	public void updateShopOrderBill(ShopOrderBillDTO shopOrderBill) {
		shopOrderBillDao.update(shopOrderBillConverter.from(shopOrderBill));
	}

	/**
	 * 获取当前期结算的月份
	 */
	@Override
	public String getShopBillMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		return sdf.format(c.getTime());
	}


	/**
	 * 订单开始时间
	 *
	 * @param endDate
	 * @return
	 */
	private Date parseStartDate(Date endDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(endDate);
		c.add(Calendar.MONTH, -1);
		return c.getTime();
	}

	/**
	 * 订单结束时间  这个月的开始日期  例如： 2015-12-01 00:00:00
	 *
	 * @return
	 */
	private Date parseEndDate() {
		Calendar c = Calendar.getInstance();
		Date nowDate = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		Date endDate = null;
		try {
			endDate = df.parse(df.format(nowDate));
			c.setTime(endDate);
			//c.add(Calendar.MONTH, 1);//测试
			endDate = c.getTime();
		} catch (ParseException e) {
			throw new BusinessException("date format exception");
		}
		return endDate;
	}

	/**
	 * 上个订单结束时间，本账单的开始时间
	 *
	 * @param endDate
	 * @return
	 */
	private Date parseBillStartDate(Date endDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(endDate);
		c.add(Calendar.SECOND, 1);
		return c.getTime();
	}

	@Override
	public ShopOrderBillDTO getLastShopOrderBillByShopId(Long shopId) {
		return shopOrderBillConverter.to(shopOrderBillDao.getLastShopOrderBillByShopId(shopId));
	}

	/**
	 * 保存结算单并更新订单状态和退货单为结束状态
	 */
	@Override
	public void generateShopBill(ShopOrderBillDTO shopOrderBill, List<Long> billSubIds, List<Long> billRefundIds) {
		Long number = shopOrderBillDao.save(shopOrderBillConverter.from(shopOrderBill));
		if (number > 0) {
			if (CollUtil.isNotEmpty(billSubIds)) {
				orderDao.updateBillStatusAndSn(billSubIds, shopOrderBill.getSn());
			}
			if (CollUtil.isNotEmpty(billRefundIds)) {
				orderRefundReturnService.updateBillStatusAndSn(billRefundIds, shopOrderBill.getSn());
			}
		}
	}

	@Override
	public PageSupport<ShopOrderBillDTO> getShopOrderBillPage(ShopOrderBillQuery query) {
		return shopOrderBillConverter.page(shopOrderBillDao.getShopOrderBillPage(query));
	}

	@Override
	public boolean getShopOrderBill(Long shopId, Date startDate, Date endDate) {
		return shopOrderBillDao.getShopOrderBill(shopId, startDate, endDate);
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public R<Void> payBill(ShopOrderBillStatusEnum orderBillStatusEnum, Long id, String payDate, String payContent, PayTypeEnum payTypeEnum) {

		ShopOrderBill shopOrderBill = shopOrderBillDao.getById(id);
		if (ObjectUtil.isNull(shopOrderBill)) {
			return R.fail("账单不存在，清刷新后重试");
		}

		ShopOrderBillDTO billDTO = getById(id);
		PlateCapitalFlowDTO f = new PlateCapitalFlowDTO();
		f.setFlowType(FlowTypeEnum.SPEND.getValue());
		f.setDealTypeEnum(DealTypeEnum.SHOP_BILL);
		f.setAmount(billDTO.getResultTotalAmount());
		f.setRecDate(billDTO.getCreateDate());
		f.setPayTime(DateUtil.parseDate(payDate));
		f.setFlag(billDTO.getFlag());
		R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(billDTO.getShopId());
		if (shopDetailResult.success()) {
			ShopDetailDTO shopDetailDTO = shopDetailResult.getData();
			f.setUserId(shopDetailDTO.getShopUserId());
		}
		f.setPayType(payTypeEnum.value());
		f.setPayTypeName(payTypeEnum.getValueName());
		f.setDetailId(billDTO.getId());
		// 添加店铺ID、店铺名称
		f.setShopId(billDTO.getShopId());
		f.setShopName(billDTO.getShopName());
		plateCapitalFlowApi.save(f);
		int result = shopOrderBillDao.payBill(orderBillStatusEnum.value(), id, payDate, payContent);
		if (result <= 0) {
			return R.fail("账单结算状态更新失败");
		}
		return R.ok();
	}

	@Override
	public PageSupport getShopOrderBillOrderPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		// 先获取结算记录获取佣金比例
		ShopOrderBill shopOrderBill = shopOrderBillDao.getShopOrderBillById(shopOrderBillOrderQuery.getId());
		if (null == shopOrderBill) {
			DefaultPagerProvider defaultPagerProvider = new DefaultPagerProvider(0, shopOrderBillOrderQuery.getCurPage(), shopOrderBillOrderQuery.getPageSize());
			return new PageSupport<>(Collections.emptyList(), defaultPagerProvider);
		}

		// 获取结算档期内的订单
		shopOrderBillOrderQuery.setSn(shopOrderBill.getSn());

		ShopOrderBillOrderTypeEnum fromCode = ShopOrderBillOrderTypeEnum.fromCode(shopOrderBillOrderQuery.getOrderType());
		switch (fromCode) {
			// 订单金额
			case ORDER_AMOUNT:
				Double commisRate = shopOrderBill.getCommisRate();
				shopOrderBillOrderQuery.setOType(OrderTypeEnum.ORDINARY.getValue());
				PageSupport<ShopOrderBillOrderDTO> result = orderDao.getShopBillOrderPage(shopOrderBillOrderQuery);
				if (CollUtil.isNotEmpty(result.getResultList())) {
					for (ShopOrderBillOrderDTO shopOrderBillOrderDTO : result.getResultList()) {
						// 佣金计算，不包含运费
						BigDecimal orderAmount = shopOrderBillOrderDTO.getOrderAmount().subtract(shopOrderBillOrderDTO.getFreightAmount());
						shopOrderBillOrderDTO.setCommissionAmount(orderAmount.multiply(BigDecimal.valueOf(commisRate)).setScale(2, RoundingMode.DOWN));
					}
				}
				return result;
			// 退款金额
			case REFUND_AMOUNT:
				return orderRefundReturnService.getShopOrderBillRefundPage(shopOrderBillOrderQuery);
			// 分销佣金
			case DISTRIBUTION_AMOUNT:
				return orderItemDao.getShopOrderBillDistributionPage(shopOrderBillOrderQuery);
			// 预售定金
			case PRE_SELL_DEPOSIT:
				return preSellOrderDao.getShopOrderBillPreSellPage(shopOrderBillOrderQuery);
			//积分订单
			case INTEGRAL:
				shopOrderBillOrderQuery.setOType(OrderTypeEnum.INTEGRAL.getValue());
				return orderDao.getShopBillOrderPage(shopOrderBillOrderQuery);
			// 拍卖保证金
			case AUCTION_MARGIN:
			default:
				DefaultPagerProvider defaultPagerProvider = new DefaultPagerProvider(0, shopOrderBillOrderQuery.getCurPage(), shopOrderBillOrderQuery.getPageSize());
				return new PageSupport<>(Collections.emptyList(), defaultPagerProvider);
		}
	}

	@Override
	public List exportOrderPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		// 先获取结算记录获取佣金比例
		ShopOrderBill shopOrderBill = shopOrderBillDao.getShopOrderBillById(shopOrderBillOrderQuery.getId());
		if (null == shopOrderBill) {
			return Collections.emptyList();
		}

		// 获取结算档期内的订单
		shopOrderBillOrderQuery.setSn(shopOrderBill.getSn());

		ShopOrderBillOrderTypeEnum fromCode = ShopOrderBillOrderTypeEnum.fromCode(shopOrderBillOrderQuery.getOrderType());
		switch (fromCode) {
			// 订单金额
			case ORDER_AMOUNT:
				Double commisRate = shopOrderBill.getCommisRate();
				shopOrderBillOrderQuery.setOType(OrderTypeEnum.ORDINARY.getValue());
				// 获取结算档期内的订单
				shopOrderBillOrderQuery.setSn(shopOrderBill.getSn());
				List<ShopOrderBillOrderExportDTO> result = orderDao.exportShopOrderBillOrderPage(shopOrderBillOrderQuery);
				if (CollUtil.isNotEmpty(result)) {
					for (ShopOrderBillOrderExportDTO shopOrderBillOrderExportDTO : result) {
						// 佣金计算，不包含运费
						BigDecimal orderAmount = shopOrderBillOrderExportDTO.getOrderAmount().subtract(shopOrderBillOrderExportDTO.getFreightAmount());
						shopOrderBillOrderExportDTO.setCommissionAmount(orderAmount.multiply(BigDecimal.valueOf(commisRate)).setScale(2, RoundingMode.DOWN));
					}
				}
				return CollUtil.defaultIfEmpty(result, Collections.singletonList(new ShopOrderBillOrderExportDTO()));
			// 退款金额
			case REFUND_AMOUNT:
				List<ShopOrderBillRefundExportDTO> refundPage = orderRefundReturnService.exportShopOrderBillRefundPage(shopOrderBillOrderQuery);
				return CollUtil.defaultIfEmpty(refundPage, Collections.singletonList(new ShopOrderBillRefundExportDTO()));
			// 预售定金
			case PRE_SELL_DEPOSIT:
				List<ShopOrderBillPreSellExportDTO> preSellPage = preSellOrderDao.exportShopOrderBillPreSellPage(shopOrderBillOrderQuery);
				return CollUtil.defaultIfEmpty(preSellPage, Collections.singletonList(new ShopOrderBillPreSellExportDTO()));
			// 拍卖保证金
			case AUCTION_MARGIN:
				return Collections.singletonList(new ShopOrderBillAuctionDepositExportDTO());
			//积分订单
			case INTEGRAL:
				// 获取结算档期内的订单
				shopOrderBillOrderQuery.setSn(shopOrderBill.getSn());
				shopOrderBillOrderQuery.setOType(OrderTypeEnum.INTEGRAL.getValue());
				List<ShopOrderBillOrderIntegralExportDTO> integralPage = orderDao.exportShopOrderBillIntegralOrderPage(shopOrderBillOrderQuery);
				if (CollUtil.isNotEmpty(integralPage)) {
					for (ShopOrderBillOrderIntegralExportDTO shopOrderBillOrderIntegralExportDTO : integralPage) {
						shopOrderBillOrderIntegralExportDTO.setProportion(shopOrderBillOrderIntegralExportDTO.getProportion() + "：1");
					}
					return integralPage;
				}
				return Collections.singletonList(new ShopOrderBillOrderIntegralExportDTO());
			default:
				return Collections.emptyList();
		}

	}

	@Override
	public List<ShopOrderBillExportDTO> exportShopBillPeriod(ShopOrderBillQuery query) {
		List<ShopOrderBillExportDTO> list = shopOrderBillDao.exportShopBillPeriod(query);

		//转换成状态名字
		if (CollUtil.isNotEmpty(list)) {
			list.forEach(bill -> {
				bill.setStatus(ShopOrderBillStatusEnum.getStatusName(Integer.valueOf(bill.getStatus())));
			});
		}

		return list;
	}

	@Override
	public R<Void> shopConfirm(Long id, Long shopId) {
		ShopOrderBill shopOrderBill = shopOrderBillDao.getById(id);
		if (ObjectUtil.isNull(shopOrderBill)) {
			return R.fail("账单不存在，清刷新后重试");
		}
		int result = shopOrderBillDao.shopConfirm(id, shopId);
		if (result <= 0) {
			return R.fail("账单确认失败，请重试");
		}
		return R.ok();
	}

	@Override
	public R<Void> adminCheck(Long id) {

		ShopOrderBill shopOrderBill = shopOrderBillDao.getById(id);
		if (ObjectUtil.isNull(shopOrderBill)) {
			return R.fail("账单不存在，清刷新后重试");
		}
		int result = shopOrderBillDao.adminCheck(id);
		if (result <= 0) {
			return R.fail("账单状态更新失败，请重试");
		}
		return R.ok();
	}

	@Override
	public ShopOrderBillDTO getShopOrderBillCount(Long shopId) {
		ShopOrderBillDTO result = shopOrderBillDao.getshopOrderBillCount(shopId);
		if (result == null) {
			return null;
		}
		result.setOrderCommissionTotals(Optional.ofNullable(result.getCommisTotals()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(result.getDistCommisTotals()).orElse(BigDecimal.ZERO)));
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> calculateBillJobHandle(Date startDate, Date endDate) {

		ShopBillPeriodDTO shopBillPeriodDTO = new ShopBillPeriodDTO();
		String flag = null;
		Long shopBillPeriodId = shopBillPeriodService.getShopBillPeriodId();
		shopBillPeriodDTO.setId(shopBillPeriodId);
		flag = shopBillPeriodService.getShopBillMonth() + "-" + shopBillPeriodId;
		shopBillPeriodDTO.setFlag(flag);
		List<ShopOrderBillDTO> shopOrderBillDTOList = new ArrayList<>();
		// 已完成的订单，并且没有结算过，并且超过售后期  订单最终售后截止时间在档期结算时间内 TODO 售后时间
		List<OrderDTO> orderDTOS = orderService.getListByStatusAndBillFlag(OrderStatusEnum.SUCCESS.getValue(), 0, endDate);
		// 普通订单
		Map<Long, List<OrderDTO>> orderMap = orderDTOS.stream().collect(Collectors.groupingBy(OrderDTO::getShopId));

		// 获取订单项
		R<List<OrderItemDTO>> orderItemResult = orderItemService.queryByNumberList(orderDTOS.stream().map(OrderDTO::getOrderNumber).collect(Collectors.toList()));
		List<OrderItemDTO> orderItemList = orderItemResult.getData();

		// 退款单
		List<OrderRefundReturnDTO> refundReturnDTOS = orderRefundReturnService.getListByStatusAndBillFlag(new OrderRefundReturnBillQuery(endDate, orderDTOS.stream().map(OrderDTO::getId).collect(Collectors.toList())));
		//32104 32154
		Map<Long, List<OrderRefundReturnDTO>> refundMap = refundReturnDTOS.stream().collect(Collectors.groupingBy(OrderRefundReturnDTO::getShopId));

		// 已取消的预售订单，并且没有结算，并且只付了定金
		List<PreSellOrderBillDTO> preSellOrderDTOList = preSellOrderService.getBillUnPayFinalPreSellOrder(endDate, OrderStatusEnum.CLOSE.getValue());
		//预售订单
		Map<Long, List<PreSellOrderBillDTO>> preSellMap = preSellOrderDTOList.stream().collect(Collectors.groupingBy(PreSellOrderBillDTO::getShopId));

		//按店铺分别结算
		for (Map.Entry<Long, List<OrderDTO>> entry : orderMap.entrySet()) {
			ShopOrderBillDTO shopOrderBillDTO = new ShopOrderBillDTO();
			Long shopId = entry.getKey();
			R<ShopDetailDTO> shopDetailResult = shopDetailApi.getById(shopId);
			//平台佣金比例
			double rate = getSysCommisRate();
			String shopName = null;
			if (shopDetailResult.success()) {
				ShopDetailDTO shopDetailDTO = shopDetailResult.getData();
				shopName = shopDetailDTO.getShopName();
				if (ObjectUtil.isNotNull(shopDetailDTO.getCommissionRate())) {
					rate = NumberUtil.mul(shopDetailDTO.getCommissionRate().doubleValue(), 0.01);
				}
			}

			// 如果大于100%，则设置成100%
			if (rate > 1D) {
				rate = 1D;
			}

			log.info("佣金比例，{}", rate);
			List<OrderDTO> orderDTOList = entry.getValue();
			List<OrderRefundReturnDTO> orderRefundReturnDTOS = refundMap.get(shopId);
			List<PreSellOrderBillDTO> preSellOrderDTOS = preSellMap.get(shopId);
			// 总运费
			BigDecimal totalFreightPrice = BigDecimal.ZERO;
			// 应结金额
			BigDecimal shouldPay = BigDecimal.ZERO;
			// 订单总金额
			BigDecimal totalOrderAmount = BigDecimal.ZERO;
			// 红包总金额
			BigDecimal totalRedPackAmount = BigDecimal.ZERO;
			// 平台总佣金
			BigDecimal totalCommission = BigDecimal.ZERO;
			// 分销佣金 todo
			BigDecimal totalPromote = BigDecimal.ZERO;
			// 预售定金
			BigDecimal totalPreSellDeposit = BigDecimal.ZERO;
			// 拍卖保证金 todo
			BigDecimal totalAuction = BigDecimal.ZERO;
			// 积分兑换结算金额
			BigDecimal totalSettlementPrice = BigDecimal.ZERO;
			// 积分商品抵扣金额
			BigDecimal totalDeductionAmount = BigDecimal.ZERO;
			// 退款总金额
			BigDecimal totalRefundAmount = BigDecimal.ZERO;
			// 退单红包金额
			BigDecimal totalOrderReturnRedPackOffPrice = BigDecimal.ZERO;
			// 退款ids
			List<Long> refundIds = null;
			List<Long> orderIds = new ArrayList<>();

			// 退款订单Map
			Map<String, BigDecimal> refundAmountMap = new HashMap<>(16);
			if (CollUtil.isNotEmpty(orderRefundReturnDTOS)) {
				//退款总金额
				totalRefundAmount = orderRefundReturnDTOS.stream().map(OrderRefundReturnDTO::getRefundAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
				// 退款订单Map
				List<String> orderNumbers = orderRefundReturnDTOS.stream().map(OrderRefundReturnDTO::getOrderNumber).collect(Collectors.toList());
				refundIds = orderRefundReturnDTOS.stream().map(OrderRefundReturnDTO::getId).collect(Collectors.toList());
				List<String> collect = orderNumbers.stream().distinct().collect(Collectors.toList());
				//退款订单红包金额
				totalOrderReturnRedPackOffPrice = orderService.getReturnRedPackOffPrice(collect);
				if (ObjectUtil.isEmpty(totalOrderReturnRedPackOffPrice)) {
					totalOrderReturnRedPackOffPrice = BigDecimal.ZERO;
				}
			}

			if (CollUtil.isNotEmpty(preSellOrderDTOS)) {
				totalPreSellDeposit = preSellOrderDTOS.stream().map(PreSellOrderBillDTO::getPreDepositPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
				List<Long> orderIdList = preSellOrderDTOS.stream().map(PreSellOrderBillDTO::getOrderId).collect(Collectors.toList());
				orderIds.addAll(orderIdList);
			}
			//结算该店铺的所有订单
			for (int i = 0; i < orderDTOList.size(); i++) {
				orderIds.add(orderDTOList.get(i).getId());
				OrderDTO order = orderDTOList.get(i);
				Optional<OrderDTO> optionalOrderDTO = Optional.ofNullable(order);
				// 运费
				BigDecimal freightPrice = optionalOrderDTO.map(OrderDTO::getFreightPrice).orElse(BigDecimal.ZERO);
				totalFreightPrice = totalFreightPrice.add(freightPrice);
				// 订单总金额（加上运费）=> 2020-11-30 actualTotalPrice已经包含了运费, 并且佣金的计算不包含运费 (candy已确认)
				BigDecimal orderAmount = order.getActualTotalPrice().subtract(freightPrice);
				totalOrderAmount = totalOrderAmount.add(orderAmount);
				// 平台优惠券
				totalRedPackAmount = totalRedPackAmount.add(optionalOrderDTO.map(OrderDTO::getPlatformCouponAmount).orElse(BigDecimal.ZERO));
				// 退款金额
				BigDecimal refundAmount = BigDecimal.ZERO;
				if (CollUtil.isNotEmpty(orderRefundReturnDTOS)) {
					for (OrderRefundReturnDTO orderRefundReturnDTO : orderRefundReturnDTOS) {
						if (!orderRefundReturnDTO.getOrderNumber().equals(order.getOrderNumber())) {
							continue;
						}
						refundAmount = refundAmount.add(orderRefundReturnDTO.getRefundAmount());
					}
				}
				// 积分兑换总结算金额
				if (ObjectUtil.isNotEmpty(order.getSettlementPrice())) {
					totalSettlementPrice = totalSettlementPrice.add(order.getSettlementPrice());
				}
				// 积分商品总抵扣金额
				if (ObjectUtil.isNotEmpty(order.getTotalDeductionAmount())) {
					totalDeductionAmount = totalDeductionAmount.add(order.getTotalDeductionAmount());
				}
				// 平台佣金:(订单金额-退单金额)*平台结算佣金比例。
				BigDecimal commission = NumberUtil.mul(orderAmount.subtract(refundAmount), rate).setScale(2, RoundingMode.DOWN);
				totalCommission = totalCommission.add(commission);


			}
			// 账单计算公式：订单金额 + 运费 - 退单金额 - 平台佣金 - 分销佣金 + 红包总额 - 退单红包金额 + 预售定金 + 拍卖保证金+ 积分商品结算
			BigDecimal totalAdd = totalOrderAmount.add(totalFreightPrice).add(totalRedPackAmount).add(totalPreSellDeposit).add(totalAuction).add(totalSettlementPrice);
			BigDecimal totalSub = totalRefundAmount.add(totalCommission).add(totalPromote).add(totalOrderReturnRedPackOffPrice);
			shouldPay = NumberUtil.sub(totalAdd, totalSub);
			log.info("#####应结金额，{}", shouldPay);
			shopOrderBillDTO.setFlag(flag);
			shopOrderBillDTO.setShopId(shopId);
			shopOrderBillDTO.setStartDate(startDate);
			shopOrderBillDTO.setEndDate(endDate);
			shopOrderBillDTO.setShopName(shopName);
			shopOrderBillDTO.setSn(generateShopBillSN(shopId));
			shopOrderBillDTO.setOrderAmount(totalOrderAmount);
			shopOrderBillDTO.setShippingTotals(totalFreightPrice);
			shopOrderBillDTO.setOrderReturnTotals(totalRefundAmount);
			shopOrderBillDTO.setCommisTotals(totalCommission);
			shopOrderBillDTO.setDistCommisTotals(totalPromote);
			shopOrderBillDTO.setTotalDeductionAmount(totalDeductionAmount);
			shopOrderBillDTO.setTotalSettlementPrice(totalSettlementPrice);
			//5.5设置的0 todo
			shopOrderBillDTO.setCommisReturnTotals(BigDecimal.ZERO);
			shopOrderBillDTO.setRedpackTotals(totalRedPackAmount);
			shopOrderBillDTO.setResultTotalAmount(shouldPay);
			shopOrderBillDTO.setCreateDate(DateUtil.date());
			shopOrderBillDTO.setMonth(getShopBillMonth());
			shopOrderBillDTO.setStatus(String.valueOf(ShopOrderBillStatusEnum.INIT.value()));
			shopOrderBillDTO.setCommisRate(rate);
			shopOrderBillDTO.setOrderReturnRedpackTotals(totalOrderReturnRedPackOffPrice);
			shopOrderBillDTO.setPreDepositPriceTotal(totalPreSellDeposit);
			shopOrderBillDTO.setAuctionDepositTotal(totalAuction);
			shopOrderBillDTOList.add(shopOrderBillDTO);
			//修改订单结算状态
			this.generateShopBill(shopOrderBillDTO, orderIds, refundIds);
			log.info("######修改订单结算状态#########");
		}
		saveShopBillPeriod(shopBillPeriodDTO, shopOrderBillDTOList);
		return R.ok();
	}

	/**
	 * 账单档期合计
	 *
	 * @param shopBillPeriodDTO
	 * @param shopOrderBillDTOList
	 */
	private void saveShopBillPeriod(ShopBillPeriodDTO shopBillPeriodDTO, List<ShopOrderBillDTO> shopOrderBillDTOList) {
		log.info("####账单档期合计开始######");
		//该周期订单总金额
		BigDecimal sumOrderAmount = BigDecimal.ZERO;
		//该周期订单总金额
		BigDecimal resultTotalAmount = BigDecimal.ZERO;
		//该周期平台佣金总金额
		BigDecimal commisTotals = BigDecimal.ZERO;
		//该周期分销佣金总金额
		BigDecimal distCommisTotals = BigDecimal.ZERO;
		//该周期退单总金额
		BigDecimal orderReturnTotals = BigDecimal.ZERO;
		//该周期订单红包总金额
		BigDecimal redPackTotals = BigDecimal.ZERO;
		//积分兑换结算金额
		BigDecimal totalSettlementPrice = BigDecimal.ZERO;
		//积分商品抵扣金额
		BigDecimal totalDeductionAmount = BigDecimal.ZERO;
		for (int i = 0; i < shopOrderBillDTOList.size(); i++) {
			ShopOrderBillDTO billDTO = shopOrderBillDTOList.get(i);
			sumOrderAmount = sumOrderAmount.add(billDTO.getOrderAmount());
			resultTotalAmount = resultTotalAmount.add(billDTO.getResultTotalAmount());
			commisTotals = commisTotals.add(billDTO.getCommisTotals());
			distCommisTotals = distCommisTotals.add(billDTO.getDistCommisTotals());
			orderReturnTotals = orderReturnTotals.add(billDTO.getOrderReturnTotals());
			redPackTotals = redPackTotals.add(billDTO.getRedpackTotals());
			totalSettlementPrice = totalSettlementPrice.add(billDTO.getTotalSettlementPrice());
			totalDeductionAmount = totalDeductionAmount.add(billDTO.getTotalDeductionAmount());
		}
		shopBillPeriodDTO.setOrderAmount(sumOrderAmount);
		shopBillPeriodDTO.setResultTotalAmount(resultTotalAmount);
		shopBillPeriodDTO.setCommisTotals(commisTotals);
		shopBillPeriodDTO.setDistCommisTotals(distCommisTotals);
		shopBillPeriodDTO.setOrderReturnTotals(orderReturnTotals);
		shopBillPeriodDTO.setRedpackTotals(redPackTotals);
		shopBillPeriodDTO.setTotalSettlementPrice(totalSettlementPrice);
		shopBillPeriodDTO.setTotalDeductionAmount(totalDeductionAmount);
		shopBillPeriodService.saveShopBillPeriodWithId(shopBillPeriodDTO, shopBillPeriodDTO.getId());
		log.info("####账单档期合计完成######");
	}

	/**
	 * 生成结算单号
	 */
	private synchronized String generateShopBillSN(Long shopId) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyMMddHHmmssSSS");
		String billSN = simpledateformat.format(new Date());
		String shopIds = shopId.toString();
		int length = shopIds.length();
		if (length > 2) {
			shopIds = shopIds.substring(length - 2, length);
		}
		billSN = billSN + shopIds + RandomUtil.random(3);
		return billSN;
	}

	private Double getSysCommisRate() {
		try {
			List<SysParamItemDTO> paramItemDTOS = sysParamsApi.getSysParamItemsByParamName(SysParamNameEnum.BILL_COMMISSION_RATE.name()).getData();
			SysParamItemDTO itemDTO = paramItemDTOS.get(0);
			return Double.valueOf(itemDTO.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			return 0.01;
		}
	}
}
