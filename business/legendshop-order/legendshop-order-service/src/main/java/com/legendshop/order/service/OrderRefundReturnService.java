/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.logistics.kuaidi100.response.SubscribePushParamResp;
import com.legendshop.order.bo.OrderRefundReturnBO;
import com.legendshop.order.dto.*;
import com.legendshop.order.entity.OrderRefundReturn;
import com.legendshop.order.excel.OrderCancelExportDTO;
import com.legendshop.order.excel.OrderRefundExportDTO;
import com.legendshop.order.excel.ShopOrderBillRefundExportDTO;
import com.legendshop.order.query.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 退款服务
 *
 * @author legendshop
 */
public interface OrderRefundReturnService {


	/**
	 * 查询账单详情里的退款列表
	 *
	 * @param orderRefundReturnQuery
	 * @return
	 */
	PageSupport<OrderRefundReturnDTO> getByBillSnPage(OrderRefundReturnQuery orderRefundReturnQuery);

	/**
	 * 申请全额退款
	 *
	 * @param applyOrderRefundDTO
	 * @return
	 */
	boolean applyOrderRefund(ApplyOrderRefundDTO applyOrderRefundDTO);

	/**
	 * 申请顶单项退款
	 *
	 * @param applyRefundDTO
	 * @param refundAmount
	 * @return
	 */
	R<String> applyOrderItem(ApplyOrderItemRefundDTO applyRefundDTO, BigDecimal refundAmount);

	/**
	 * 申请顶单项退货
	 *
	 * @param applyRefundDTO
	 * @return
	 */
	R<String> applyOrderItem(ApplyOrderItemRefundDTO applyRefundDTO);

	/**
	 * 获取未结算而且已完成的退款/退货单
	 *
	 * @return
	 */
	List<OrderRefundReturnDTO> getListByStatusAndBillFlag(OrderRefundReturnBillQuery query);


	/**
	 * 更改退款/退货单为已结算，加上结算号
	 *
	 * @param ids
	 * @param billSn
	 */
	void updateBillStatusAndSn(List<Long> ids, String billSn);

	/**
	 * 撤销售后申请
	 *
	 * @param refundSn
	 * @param userId
	 * @return
	 */
	boolean cancelApply(String refundSn, Long userId);

	/**
	 * 审核退款订单
	 *
	 * @param refundId
	 * @param auditFlag
	 * @param sellerMessage
	 * @return
	 */
	boolean auditRefund(Long refundId, Boolean auditFlag, String sellerMessage, Long shopId);

	/**
	 * 审核退货订单
	 *
	 * @param refundId
	 * @param auditFlag
	 * @param abandonedGoodFlag
	 * @param sellerMessage
	 * @param shopId
	 * @return
	 */
	boolean auditRefundGood(Long refundId, Boolean auditFlag, Boolean abandonedGoodFlag, String sellerMessage, Long shopId);

	/**
	 * 用户发货
	 *
	 * @param userReturnReturnLogisticsDTO
	 * @return
	 */
	boolean confirmShip(UserReturnReturnLogisticsDTO userReturnReturnLogisticsDTO);

	/**
	 * 商家确认收货
	 *
	 * @param refundId
	 * @param shopId
	 * @return
	 */
	boolean confirmDeliver(Long refundId, Long shopId);


	/**
	 * 物流消息显示签收后、物流接口回调修改订单状态为已签收
	 *
	 * @param subscribePushParamResp
	 * @return
	 */
	boolean refundCallBack(SubscribePushParamResp subscribePushParamResp);

	/**
	 * 退款列表分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<OrderRefundReturnBO> page(OrderRefundReturnQuery query);

	/**
	 * 用户查看售后详情
	 *
	 * @param refundId
	 * @param userId
	 * @return
	 */
	OrderRefundReturnBO getUserRefundDetail(Long refundId, Long userId);

	/**
	 * 商家查看售后详情
	 *
	 * @param refundId
	 * @param shopId
	 * @return
	 */
	OrderRefundReturnBO getShopRefundDetail(Long refundId, Long shopId);

	/**
	 * 平台查看售后详情
	 *
	 * @param refundId
	 * @return
	 */
	OrderRefundReturnBO getAdminRefundDetail(Long refundId);

	/**
	 * 保存退款单
	 *
	 * @param orderRefundReturnDTO
	 * @return
	 */
	Long save(OrderRefundReturnDTO orderRefundReturnDTO);

	/**
	 * 平台批量确认退款
	 *
	 * @param confirmRefundDTOS
	 * @return
	 */
	R<Void> confirmRefund(List<ConfirmRefundDTO> confirmRefundDTOS);

	/**
	 * 平台确认退款
	 *
	 * @param confirmRefundDTO
	 * @return
	 */
	R confirmRefund(ConfirmRefundDTO confirmRefundDTO);

	/**
	 * 平台批量确认退款
	 *
	 * @param confirmRefundDTO
	 * @return
	 */
	R confirmRefundList(ConfirmRefundListDTO confirmRefundDTO);

	/**
	 * 获取账单档期内的退款订单数据
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	PageSupport<ShopOrderBillRefundDTO> getShopOrderBillRefundPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 获取账单档期内的退款订单数据导出
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	List<ShopOrderBillRefundExportDTO> exportShopOrderBillRefundPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	List<OrderRefundExportDTO> export(OrderRefundReturnQuery query);


	/**
	 * 获取用户退款退货数据
	 *
	 * @param orderId
	 * @param orderItemId
	 * @param userId
	 * @return
	 */
	R<ApplyRefundReturnDTO> getApplyRefundReturn(Long orderId, Long orderItemId, Long userId);


	/**
	 * 获取退款原因
	 *
	 * @return
	 */
	List<String> getRefundReason();

	R<String> applyOrder(ApplyOrderItemRefundDTO applyOrderItemRefundDTO);


	/**
	 * 通过订单状态判断该订单是否允许订单项退货退款
	 *
	 * @param completeTime
	 * @param productId
	 * @return
	 */
	boolean isAllowOrderReturn(Date completeTime, Long productId);

	/**
	 * 获取用户累计退款金额
	 *
	 * @param userId
	 * @return
	 */
	BigDecimal getRefundAmountByUserId(Long userId);

	/**
	 * 通过订单编号计算退款金额
	 *
	 * @param orderNumbers
	 * @return
	 */
	BigDecimal getRefundAmountByOrderNumbers(List<String> orderNumbers);

	/**
	 * 根据退款单号获取退款信息
	 *
	 * @param refundSn
	 * @return
	 */
	OrderRefundReturnDTO getByRefundSn(String refundSn);

	/**
	 * 处理退款
	 *
	 * @param payRefundSn
	 * @param refundStatus
	 * @return
	 */
	R<Void> refundHandler(String payRefundSn, Boolean refundStatus);

	/**
	 * 添加备注
	 *
	 * @param refundId
	 * @param remark
	 * @return
	 */
	boolean insertRemark(Long refundId, String remark);

	R<Void> refundCallback(String refundSn);

	/**
	 * 获取售后列表
	 *
	 * @param ids
	 * @return
	 */
	List<OrderRefundReturnDTO> queryById(List<Long> ids);

	/**
	 * 查询商家所有未确认的售后订单
	 *
	 * @return
	 */
	List<OrderRefundReturn> queryUntreatedOrderRefund();

	/**
	 * 商家申请列表
	 *
	 * @param query
	 * @return
	 */
	PageSupport<OrderCancelReasonDTO> pageCancelOrder(OrderCancelReasonQuery query);

	/**
	 * 商家申请列表 导出
	 *
	 * @param query
	 * @return
	 */
	List<OrderCancelExportDTO> getFlowExcelCancel(OrderCancelReasonQuery query);

	/**
	 * 订单的取消原因
	 *
	 * @param reason 取消原因
	 * @return
	 */
	List<SysParamItemDTO> queryCancelReason(String reason);

	/**
	 * 商家申请列表撤回
	 *
	 * @param refundIds 退款的id
	 * @param shopId    店铺的id
	 * @return
	 */
	R<String> auditWithdrawGood(List<Long> refundIds, Long shopId);

	/**
	 * 申请取消订单
	 *
	 * @param applyOrderCancelDTO
	 * @return
	 */
	R<String> applyCancelOrderRefund(ApplyOrderCancelDTO applyOrderCancelDTO);

	/**
	 * 用户退款列表分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<OrderRefundReturnBO> pageOrderRefundUser(OrderRefundReturnQuery query);

	/**
	 * 平台批量确认取消订单
	 *
	 * @param confirmRefundDTO
	 * @return
	 */
	R confirmRefundCanelList(ConfirmRefundCanelListDTO confirmRefundDTO);

	/**
	 * 批量退款订单
	 *
	 * @return
	 */
	boolean batchAuditRefund(OrderBulkRefundQuery query);

	/**
	 * 批量退货订单
	 *
	 * @return
	 */
	boolean batchAuditRefundGood(Long shopId, OrderBulkRefundQuery query);

	/**
	 * 售后原因
	 *
	 * @param shopId
	 * @return
	 */
	List<String> queryAfterSalesReason(Long shopId, Long applyType);
}
