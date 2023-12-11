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
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.OrderSettingDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.common.data.cache.util.CacheManagerUtil;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.order.api.OrderApi;
import com.legendshop.order.api.OrderItemApi;
import com.legendshop.order.api.OrderLogisticsApi;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.dto.OrderLogisticsDTO;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.product.bo.ProductCommentAnalysisBO;
import com.legendshop.product.bo.ProductCommentBO;
import com.legendshop.product.bo.ProductCommentInfoBO;
import com.legendshop.product.dao.ProductAddCommentDao;
import com.legendshop.product.dao.ProductCommentDao;
import com.legendshop.product.dao.ProductCommentStatisticsDao;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dto.ProductCommentDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.entity.Product;
import com.legendshop.product.entity.ProductAddComment;
import com.legendshop.product.entity.ProductComment;
import com.legendshop.product.entity.ProductCommentStatistics;
import com.legendshop.product.enums.ProductCommStatusEnum;
import com.legendshop.product.query.MyProductCommentQuery;
import com.legendshop.product.query.ProductCommentQuery;
import com.legendshop.product.service.ProductCommentService;
import com.legendshop.product.service.convert.ProductCommentConverter;
import com.legendshop.search.api.EsIndexApi;
import com.legendshop.search.enmus.IndexTargetMethodEnum;
import com.legendshop.search.enmus.IndexTypeEnum;
import com.legendshop.search.enmus.ProductTargetTypeEnum;
import com.legendshop.shop.api.LogisticsCommentStatisticsApi;
import com.legendshop.shop.api.ShopCommentStatisticsApi;
import com.legendshop.shop.dto.LogisticsCommentStatisticsDTO;
import com.legendshop.shop.dto.ShopCommentStatisticsDTO;
import com.legendshop.user.api.UserDetailApi;
import com.legendshop.user.dto.UserDetailDTO;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.transaction.TransactionHookAdapter;
import io.seata.tm.api.transaction.TransactionHookManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.legendshop.common.core.constant.CacheConstants.*;

/**
 * 产品评论服务实现
 *
 * @author legendshop
 */
@Service
@Slf4j
public class ProductCommentServiceImpl extends BaseServiceImpl<ProductCommentDTO, ProductCommentDao, ProductCommentConverter> implements ProductCommentService {

	@Autowired
	private ProductCommentDao productCommentDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductCommentConverter productCommentConverter;

	@Autowired
	private MessageApi messagePushClient;

	/**** domain 方法搬迁 ***/

	@Autowired
	private OrderItemApi orderItemApi;

	@Autowired
	private OrderApi orderApi;

	@Autowired
	private SysParamsApi sysParamsApi;

	@Autowired
	private ProductCommentStatisticsDao productCommentStatisticsDao;

	@Autowired
	private ShopCommentStatisticsApi shopCommentStatisticsApi;

	@Autowired
	private LogisticsCommentStatisticsApi logisticsCommentStatisticsApi;

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	@Autowired
	private CacheManagerUtil cacheManagerUtil;

	@Autowired
	private ProductAddCommentDao productAddCommentDao;

	@Autowired
	private OrderLogisticsApi orderLogisticsApi;

	@Autowired
	EsIndexApi esIndexApi;

	@Resource
	private UserDetailApi userDetailApi;


	@Override
	public ProductCommentBO getProductCommentById(Long id) {
		ProductCommentBO productCommentBO = productCommentConverter.convert2BO(productCommentDao.getById(id));
		if (ObjectUtil.isNotNull(productCommentBO)) {
			Double compositeScore = NumberUtil.div((productCommentBO.getScore() + productCommentBO.getShopScore() + productCommentBO.getLogisticsScore()), 3, 1);
			productCommentBO.setCompositeScore(compositeScore);
		}
		return productCommentBO;
	}


	@Override
	public PageSupport<ProductCommentInfoBO> queryProductComment(ProductCommentQuery params) {
		if (ObjectUtil.isEmpty(params.getProductId())) {
			return new PageSupport<>();
		}
		PageSupport<ProductCommentInfoBO> pageSupport = productCommentDao.queryProductComment(params);
		pageSupport.getResultList().forEach(e -> {
			if (e.getAnonymousFlag()) {
				String userName = e.getNickName();
				if (StrUtil.isNotBlank(userName)) {
					e.setNickName(userNameSubstitution(userName));
				}
				//匿名用户头像置空
				e.setPortrait(null);

			}
		});
		return pageSupport;
	}


	/**
	 * 是否是表情符号
	 *
	 * @param codePoint
	 * @return
	 */
	public boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}


	@Override
	public ProductDTO getComScore(Long productId) {
		Double comScore = productCommentDao.getComScore(productId);
		DecimalFormat df = new DecimalFormat("0.0");
		comScore = Double.parseDouble(df.format(comScore));
		ProductDTO productDTO = new ProductDTO();
		productDTO.setComScore(comScore);
		return productDTO;
	}

	@Override
	public PageSupport<ProductCommentInfoBO> queryMyProductComment(MyProductCommentQuery productCommentQuery) {
		PageSupport<ProductCommentInfoBO> pageSupport = productCommentDao.queryMyProductComment(productCommentQuery);
		if (CollUtil.isEmpty(pageSupport.getResultList())) {
			return pageSupport;
		}
		List<ProductCommentInfoBO> resultList = pageSupport.getResultList();
		List<Long> userIdList = resultList.stream().map(ProductCommentInfoBO::getUserId).collect(Collectors.toList());
		R<List<UserDetailDTO>> userDetails = userDetailApi.getUserDetailByIds(userIdList);
		if (CollUtil.isEmpty(userDetails.getData())) {
			return pageSupport;
		}
		Map<Long, UserDetailDTO> userDetailMap = userDetails.getData().stream().collect(Collectors.toMap(UserDetailDTO::getUserId, e -> e));

		for (ProductCommentInfoBO productCommentInfoBO : resultList) {
			if (!userDetailMap.containsKey(productCommentInfoBO.getUserId())) {
				continue;
			}

			if (productCommentInfoBO.getAnonymousFlag()) {
				productCommentInfoBO.setNickName("已匿名");
			} else {
				productCommentInfoBO.setNickName(userDetailMap.get(productCommentInfoBO.getUserId()).getNickName());
			}
		}
		return pageSupport;
	}

	@Override
	public PageSupport<ProductCommentDTO> querySpuComment(ProductCommentQuery productCommentQuery) {
		PageSupport<ProductCommentDTO> pageSupport = productCommentDao.querySpuComment(productCommentQuery);
		//计算综合分数
		pageSupport.getResultList().forEach(productCommentDTO -> {
			Double comScore = productCommentDao.getComScore(productCommentDTO.getProductId());
			double v = new BigDecimal(comScore).setScale(1, RoundingMode.HALF_UP).doubleValue();
			productCommentDTO.setCompositeScore(v);
		});
		return pageSupport;
	}

	@Override
	public PageSupport<ProductCommentInfoBO> querySkuComment(ProductCommentQuery productCommentQuery) {
		PageSupport<ProductCommentInfoBO> productCommentInfoBOPageSupport = productCommentDao.querySkuComment(productCommentQuery);
		List<ProductCommentInfoBO> resultList = productCommentInfoBOPageSupport.getResultList();
		resultList.forEach(
				e -> {
					if (StrUtil.isNotEmpty(e.getPhotos())) {
						String photos = e.getPhotos();
						List<String> imgList = Arrays.asList(StringUtils.split(photos, ","));
						e.setPhotoList(imgList);
					}
				}
		);
		productCommentInfoBOPageSupport.setResultList(resultList);
		return productCommentInfoBOPageSupport;
	}

	@Override
	public boolean replyFirst(Long id, String content, Long shopId) {
		ProductComment productComment = productCommentDao.getByProperties(new EntityCriterion().eq("id", id).eq("shopId", shopId));
		if (ObjectUtil.isEmpty(productComment)) {
			throw new BusinessException("回复失败，该评论已不存在");
		}
		if (productComment.getReplyFlag()) {
			throw new BusinessException("该评论已回复，不能重复回复");
		}
		// 处理评论内容
		String html = content.trim();
		StringBuilder nicksb = new StringBuilder();
		int l = html.length();
		for (int i = 0; i < l; i++) {
			char s = html.charAt(i);
			if (isEmojiCharacter(s)) {
				nicksb.append(s);
			}
		}
		productComment.setShopReplyContent(content);
		productComment.setReplyFlag(Boolean.TRUE);
		productComment.setShopReplyTime(DateUtil.date());
		boolean flag = productCommentDao.update(productComment) > 0;
		if (flag) {
			R<OrderItemDTO> itemDTOR = orderItemApi.getById(productComment.getOrderItemId());
			// 发送评论回复通知站内信给用户
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
					.setReceiveIdArr(new Long[]{productComment.getUserId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_COMMENT)
					.setDetailId(itemDTOR.getData().getOrderId())
					.setSysParamNameEnum(SysParamNameEnum.ORDER_COMMENT_REPLY)
			);
		}
		return flag;
	}


	/**
	 * 匿名处理
	 *
	 * @param userName
	 * @return
	 */
	private String userNameSubstitution(String userName) {
		int userNameLength = userName.length();

		StringBuilder sb = new StringBuilder();
		if (userNameLength > 2) {
			String prefix = userName.substring(0, 1);
			sb.append(prefix);
			for (int i = 1; i < userNameLength - 1; i++) {
				sb.append("*");
			}
			String suffix = userName.substring(userNameLength - 1, userNameLength);
			sb.append(suffix);
		} else if (userNameLength == 2) {
			String prefix = userName.substring(0, 1);
			sb.append(prefix);
			sb.append("*******");
		} else {
			sb.append("********");
		}

		return sb.toString();
	}

	@Override
	public boolean isCanAddComment(Long prodCommId, Long userId) {

		ProductComment productComment = productCommentDao.getById(prodCommId);

		if (ObjectUtil.isEmpty(productComment) || productComment.getAddCommFlag()) {
			return false;
		}

		Product prod = productDao.getById(productComment.getProductId());
		if (ObjectUtil.isEmpty(prod)) {
			return false;
		}

		if (productComment.getDeleteType() == 1) {
			throw new BusinessException("商品评论已被删除~");
		}
		return true;
	}

	@Override
	public ProductCommentInfoBO getProductCommentDetail(Long prodComId) {
		ProductCommentInfoBO productCommentInfoBO = productCommentDao.getProductCommentDetail(prodComId);
		//计算平均分数
		Double averageScore = NumberUtil.div((productCommentInfoBO.getScore() + productCommentInfoBO.getShopScore() + productCommentInfoBO.getLogisticsScore()), 3, 1);
		productCommentInfoBO.setAverageScore(averageScore);
		log.info("productCommentInfoBO{}", productCommentInfoBO);
		return productCommentInfoBO;
	}

	@Override
	public PageSupport<ProductCommentInfoBO> getAdminProductCommentList(ProductCommentQuery productCommentQuery) {
		PageSupport<ProductCommentInfoBO> pageSupport = productCommentDao.getAdminProductCommentList(productCommentQuery);
		//计算平均分数
		pageSupport.getResultList().forEach(productCommentInfoBO -> {
			Double averageScore = NumberUtil.div((productCommentInfoBO.getScore() + productCommentInfoBO.getShopScore() + productCommentInfoBO.getLogisticsScore()), 3, 1);
			productCommentInfoBO.setAverageScore(averageScore);
		});
		return pageSupport;
	}

	/******  domain搬迁方法 ***/

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> saveProductComment(ProductCommentDTO productCommentDTO) {
		R<OrderItemDTO> orderItemResult = orderItemApi.getById(productCommentDTO.getOrderItemId());
		OrderItemDTO orderItem = orderItemResult.getData();
		if (ObjectUtil.isEmpty(orderItem)) {
			throw new BusinessException("评论的订单不存在");
		}

		if (!orderItem.getUserId().equals(productCommentDTO.getUserId())) {
			throw new BusinessException("您没有权限评价该订单");
		}

		R<OrderDTO> orderResult = orderApi.getOrderById(orderItem.getOrderId());
		OrderDTO order = orderResult.getData();
		if (!orderResult.getSuccess() || ObjectUtil.isEmpty(order)) {
			throw new BusinessException("评论的订单不存在");
		}

		if (!OrderStatusEnum.SUCCESS.getValue().equals(order.getStatus())) {
			throw new BusinessException("请收货后再进行评论~");
		}

		Integer photoSize = 0;
		if (ObjectUtil.isNotEmpty(productCommentDTO.getPhotos())) {
			List<String> photoList = JSONUtil.toList(JSONUtil.parseArray(productCommentDTO.getPhotos()), String.class);
			photoSize = photoList.size();
			if (photoList.size() > 5) {
				throw new BusinessException("评论图片不能超过5张");
			}
		}

		Integer videoSize = 0;
		if (ObjectUtil.isNotEmpty(productCommentDTO.getPhotos())) {
			List<String> videoList = JSONUtil.toList(JSONUtil.parseArray(productCommentDTO.getVideo()), String.class);
			videoSize = videoList.size();
			if (videoSize > 5) {
				throw new BusinessException("评论视频不能超过5个");
			}
		}
		Long userId = productCommentDTO.getUserId();
		Long productId = orderItem.getProductId();
		productCommentDTO.setProductId(productId);
		// 获取订单评论的系统配置
		ObjectMapper mapper = new ObjectMapper();
		OrderSettingDTO orderSetting = mapper.convertValue(
				sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.ORDER_SETTING.name(), OrderSettingDTO.class).getData(), OrderSettingDTO.class);
		Boolean commentNeedReview = Optional.ofNullable(orderSetting).map(OrderSettingDTO::getCommentNeedReview).orElse(true);
		Integer status = ProductCommStatusEnum.WAIT_AUDIT.value();

		// 有效评论时间
		Integer commentValidDay = ObjectUtil.isNotEmpty(order.getCommentValidDay()) ? order.getCommentValidDay() : Optional.ofNullable(orderSetting).map(OrderSettingDTO::getOrderCommentValidDay).orElse(7);
		if (ObjectUtil.isNotEmpty(order.getCompleteTime())) {
			Date validDay = DateUtil.offsetDay(order.getCompleteTime(), order.getCommentValidDay());
			if (validDay.before(DateUtil.date())) {
				throw new BusinessException("评论失败，已超出评论有效时间！");
			}
		} else {
			// 确认收货时候STATUS=20生成时间
			throw new BusinessException("订单未生成完成时间！");
		}

		// 更新订单项 的评论状态
		Long orderItemId = productCommentDTO.getOrderItemId();
		R<Boolean> commFlag = orderItemApi.updateOrderItemCommFlag(1, orderItemId, userId);
		OrderItemDTO orderItemDTO = orderItemApi.getById(orderItemId).getData();

		if (!commFlag.getSuccess()) {
			throw new BusinessException("更新订单评论状态失败！");
		}
		//审核状态为-1拒绝可以重复提交评论
      /*  if( !commFlag.getData() && !productCommentDTO.getStatus().equals(-1)){
			return R.fail("请勿重复评论！");
		}*/
		if (!commFlag.getData() && orderItemDTO.getCommFlag()) {
			throw new BusinessException("您已评论，请勿重复评论！");
		}


		// 处理评论内容
		String html = productCommentDTO.getContent().trim();
		StringBuilder nickSb = new StringBuilder();
		int l = html.length();
		for (int i = 0; i < l; i++) {
			char s = html.charAt(i);
			if (isEmojiCharacter(s)) {
				nickSb.append(s);
			}
		}
		productCommentDTO.setContent(nickSb.toString());


		R<OrderLogisticsDTO> orderLogisticsResult = orderLogisticsApi.getByOrderId(order.getId());

		if (ObjectUtil.isNotEmpty(orderLogisticsResult.getData())) {
			OrderLogisticsDTO orderLogistics = orderLogisticsResult.getData();
			productCommentDTO.setLogisticsCompanyId(orderLogistics.getLogisticsCompanyId());
		}

		productCommentDTO.setShopId(order.getShopId());
		productCommentDTO.setStatus(status);
		productCommentDTO.setCreateTime(DateUtil.date());
		productCommentDTO.setReplyFlag(Boolean.FALSE);
		productCommentDTO.setAddCommFlag(Boolean.FALSE);
		productCommentDTO.setDeleteType(0);
		if (!commentNeedReview) {
			productCommentDTO.setAuditTime(DateUtil.date());
		}
		if (ObjectUtil.isEmpty(productCommentDTO.getId())) {
			Long commId = productCommentDao.save(productCommentConverter.from(productCommentDTO));
			if (commId > 0) {
				if (!commentNeedReview) {
					this.auditFirstComment(commId, ProductCommStatusEnum.SUCCESS.value());
				}
				return R.ok();
			}
		} else {
			int update = productCommentDao.update(productCommentConverter.from(productCommentDTO));
			if (update > 0) {
				return R.ok();
			}
		}

		throw new BusinessException("评论保存失败");
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	public boolean auditFirstComment(Long commentId, Integer status) {

		ProductComment productComment = productCommentDao.getById(commentId);
		if (ObjectUtil.isEmpty(productComment)) {
			throw new BusinessException("审核失败，该评论已不存在");
		}
		if (!ProductCommStatusEnum.WAIT_AUDIT.value().equals(productComment.getStatus())) {
			throw new BusinessException("请匆重复审核！");
		}
		productComment.setStatus(status);
		productComment.setAuditTime(new Date());
		if (productCommentDao.update(productComment) > 0) {
			//发送审核站内信给用户
			sendAuditToUser(new ArrayList<>(Collections.singleton(productComment)));
			if (ProductCommStatusEnum.SUCCESS.value().equals(status)) {
				// 更新统计表
				updateCommentStatInfo(productCommentConverter.to(productComment));
				//发送审核站内信给商家
				sendAuditToShop(new ArrayList<>(Collections.singleton(productComment)));

			}
			return true;
		}
		return false;
	}

	/**
	 * 发送审核站内信给用户
	 *
	 * @param
	 */
	private void sendAuditToUser(List<ProductComment> productCommentList) {
		R<List<OrderItemDTO>> itemListR = orderItemApi.queryById(productCommentList.stream().map(ProductComment::getOrderItemId).collect(Collectors.toList()));
		Map<Long, Long> itemMap = itemListR.getData().stream().collect(Collectors.toMap(OrderItemDTO::getId, OrderItemDTO::getOrderId));

		for (ProductComment productComment : productCommentList) {
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
					.setReceiveIdArr(new Long[]{productComment.getUserId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_COMMENT)
					.setDetailId(itemMap.get(productComment.getOrderItemId()))
					.setSysParamNameEnum(SysParamNameEnum.ORDER_COMMENT_AUDIT_USER)
			);
		}
	}


	/**
	 * 发送审核站内信给商家
	 */
	private void sendAuditToShop(List<ProductComment> productCommentList) {
		R<List<OrderItemDTO>> itemListR = orderItemApi.queryById(productCommentList.stream().map(ProductComment::getOrderItemId).collect(Collectors.toList()));
		Map<Long, Long> itemMap = itemListR.getData().stream().collect(Collectors.toMap(OrderItemDTO::getId, OrderItemDTO::getOrderId));

		for (ProductComment productComment : productCommentList) {
			messagePushClient.push(new MessagePushDTO()
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.SHOP_USER)
					.setReceiveIdArr(new Long[]{productComment.getShopId()})
					.setMsgSendType(MsgSendTypeEnum.ORDER_COMMENT)
					.setDetailId(itemMap.get(productComment.getOrderItemId()))
					.setSysParamNameEnum(SysParamNameEnum.ORDER_COMMENT_AUDIT_SHOP)
			);
		}
	}

	@Override
	@GlobalTransactional(rollbackFor = Exception.class)
	public boolean batchAuditComment(List<Long> ids, List<Long> addIds, Integer status) {
		if (ids.size() > 0) {
			List<ProductComment> productCommentList = productCommentDao.getWaitAuditByIdList(ids);
			if (CollUtil.isNotEmpty(productCommentList)) {
				productCommentList.forEach(productComment -> {
					productComment.setStatus(status);
					productComment.setAuditTime(new Date());
				});
				if (productCommentDao.update(productCommentList) > 0) {
					//发送审核站内信给用户
					sendAuditToUser(productCommentList);
					//审核通过才需要通知商家
					if (ProductCommStatusEnum.SUCCESS.value().equals(status)) {
						batchUpdateCommentStatInfo(productCommentList);
						//发送审核站内信给商家
						sendAuditToShop(productCommentList);
					}
				}
			}
		}
		if (addIds.size() > 0) {
			List<ProductAddComment> productAddCommentList = productAddCommentDao.getWaitAuditByIdList(addIds);
			if (CollUtil.isNotEmpty(productAddCommentList)) {
				productAddCommentList.forEach(productAddComment -> {
					productAddComment.setStatus(status);
					productAddComment.setAuditTime(new Date());
				});

				if (productAddCommentDao.update(productAddCommentList) > 0) {
					//获取评论id集合
					List<Long> commentIdList = productAddCommentList.stream().map(ProductAddComment::getProductCommentId).collect(Collectors.toList());
					//获取评论集合
					List<ProductComment> productCommentList = productCommentDao.getByIdList(commentIdList);
					//获取用户id集合
					List<Long> userIdList = productCommentList.stream().map(ProductComment::getUserId).collect(Collectors.toList());
					//发送审核站内信给用户
					Long[] userIdAtr = userIdList.toArray(new Long[userIdList.size()]);
					sendAuditToUser(productCommentList);
					//审核通过才需要通知商家
					if (ProductCommStatusEnum.SUCCESS.value().equals(status)) {
						//发送审核站内信给商家
						sendAuditToShop(productCommentList);
					}
				}
			}
		}
		return true;
	}

	@Override
	public R<ProductCommentInfoBO> getProductDetailByComment(Long orderItemId) {

		R<OrderItemDTO> result = orderItemApi.getById(orderItemId);
		if (!result.success()) {
			return R.fail("调用订单服务失败");
		}
		OrderItemDTO orderItemDTO = result.getData();
		if (ObjectUtil.isNull(orderItemDTO)) {
			return R.fail("订单信息不存在，请重试");
		}
		ProductCommentInfoBO productCommentInfoBO = new ProductCommentInfoBO();
		productCommentInfoBO.setProductId(orderItemDTO.getProductId());
		productCommentInfoBO.setProductName(orderItemDTO.getProductName());
		productCommentInfoBO.setProdPic(orderItemDTO.getPic());
		productCommentInfoBO.setAttribute(orderItemDTO.getAttribute());
		productCommentInfoBO.setOrderItemId(orderItemDTO.getId());

		return R.ok(productCommentInfoBO);
	}

	@Override
	public R<ProductCommentAnalysisBO> getAdminProductCommentAnalysis() {

		ProductCommentAnalysisBO productCommentAnalysisBO = new ProductCommentAnalysisBO();
		List<ProductComment> productComments = this.productCommentDao.queryAll();
		List<ProductCommentDTO> productCommentDTOList = this.productCommentConverter.to(productComments);
		//计算平均分数
		productCommentDTOList.forEach(e -> {
			Double averageScore = NumberUtil.div((e.getScore() + e.getShopScore() + e.getLogisticsScore()), 3, 1);
			e.setCompositeScore(averageScore);
		});
		// 统计好评数量
		Long goodCommentCount = productCommentDTOList.stream().
				filter(productComment -> productComment.getCompositeScore() >= 4)
				.count();

		// 统计中评数量
		Long mediumCommentCount = productCommentDTOList.stream().
				filter(productComment -> productComment.getCompositeScore() >= 3 && productComment.getCompositeScore() < 4)
				.count();

		// 统计差评数量
		Long poorCommentCount = productCommentDTOList.stream().
				filter(productComment -> productComment.getCompositeScore() < 3)
				.count();
		productCommentAnalysisBO.setGood(goodCommentCount);
		productCommentAnalysisBO.setMedium(mediumCommentCount);
		productCommentAnalysisBO.setPoor(poorCommentCount);
		return R.ok(productCommentAnalysisBO);
	}

	@Override
	public ProductCommentAnalysisBO getProductProductCommentAnalysis(Long productId) {

		ProductCommentAnalysisBO productCommentAnalysisBO = new ProductCommentAnalysisBO();
		List<ProductComment> productComments = productCommentDao.queryCommentByProductId(productId);
		List<ProductCommentDTO> productCommentDTOList = this.productCommentConverter.to(productComments);

		productCommentDTOList.forEach(e -> {
			// 计算平均分数
			Double averageScore = NumberUtil.div((e.getScore() + e.getShopScore() + e.getLogisticsScore()), 3, 1);
			e.setCompositeScore(averageScore);
			// 查询追评图片
			if (e.getAddCommFlag()) {
				ProductAddComment productAddComment = productAddCommentDao.getByProductCommentId(e.getId());
				e.setAddPhotos(productAddComment.getPhotos());
			}
		});
		// 统计全部评论数量
		Long allCommentCount = productCommentDTOList.stream().count();

		// 统计好评数量
		Long goodCommentCount = productCommentDTOList.stream().
				filter(productComment -> productComment.getCompositeScore() >= 4)
				.count();

		// 统计中评数量
		Long mediumCommentCount = productCommentDTOList.stream().
				filter(productComment -> productComment.getCompositeScore() >= 3 && productComment.getCompositeScore() < 4)
				.count();

		// 统计差评数量
		Long poorCommentCount = productCommentDTOList.stream().
				filter(productComment -> productComment.getCompositeScore() < 3)
				.count();

		// 统计有图
		Long hasPhotoCommentCount = productCommentDTOList.stream().
				filter(productComment -> ObjectUtil.isNotNull(productComment.getPhotos()) || ObjectUtil.isNotNull(productComment.getAddPhotos()))
				.count();

		// 统计有追评
		Long appendCommentCount = productCommentDTOList.stream().
				filter(productComment -> productComment.getAddCommFlag())
				.count();
		productCommentAnalysisBO.setAll(allCommentCount);
		productCommentAnalysisBO.setGood(goodCommentCount);
		productCommentAnalysisBO.setMedium(mediumCommentCount);
		productCommentAnalysisBO.setPoor(poorCommentCount);
		productCommentAnalysisBO.setPhoto(hasPhotoCommentCount);
		productCommentAnalysisBO.setAppend(appendCommentCount);
		return productCommentAnalysisBO;
	}

	@Override
	@Transient
	public Integer deleteByProductCommentId(Long id) {
		ProductComment productComment = productCommentDao.getById(id);
		if (null == productComment) {
			return 0;
		}

		productComment.setDeleteType(1);
		int update = productCommentDao.update(productComment);

		ProductAddComment productAddComment = productAddCommentDao.getByProductCommentId(productComment.getId());
		if (null != productAddComment) {
			productAddComment.setDeleteType(1);
			productAddCommentDao.update(productAddComment);
		}

		if (update <= 0) {
			return 0;
		}

		R<OrderItemDTO> itemDTOR = orderItemApi.getById(productComment.getOrderItemId());

		// 发送评论回复通知站内信给用户
		messagePushClient.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
				.setReceiveIdArr(new Long[]{productComment.getUserId()})
				.setMsgSendType(MsgSendTypeEnum.ORDER_COMMENT)
				.setDetailId(itemDTOR.getData().getOrderId())
				.setSysParamNameEnum(SysParamNameEnum.ORDER_COMMENT_DELETE_USER)
		);

		return 1;
	}

	/**
	 * 批量更新评论统计信息, 包括评论统计, 店铺评分统计, 物流评分统计, 以及商品的评论数和分数
	 *
	 * @param productCommentList
	 */
	public void batchUpdateCommentStatInfo(List<ProductComment> productCommentList) {
		//更新评论统计信息
		//通过商品id分组
		Map<Long, List<ProductComment>> productCommentMap = productCommentList.stream().collect(Collectors.groupingBy(ProductComment::getProductId));
		//循环合并评分，更新统计信息
		for (Long productId : productCommentMap.keySet()) {
			List<ProductComment> productComments = productCommentMap.get(productId);
			int score = productComments.stream().mapToInt(ProductComment::getScore).sum();
			//保存评论统计表
			ProductCommentStatistics productCommentStat = productCommentStatisticsDao.getProductCommentStatByProductIdForUpdate(productId);
			if (ObjectUtil.isEmpty(productCommentStat)) {
				productCommentStat = new ProductCommentStatistics();
				productCommentStat.setProductId(productId);
				productCommentStat.setScore(score);
				//评论数
				productCommentStat.setComments(productComments.size());
				productCommentStatisticsDao.saveProductCommentStat(productCommentStat);
			} else {
				productCommentStatisticsDao.updateProductCommentStat(score, productComments.size(), productId);
			}
			/** 更新评论得分 和 评论数 , 评论得分是累加, 评论数是直接更新*/
			productDao.updateReviewScoresAndComments(score, productComments.size(), productId);

			/** 更新了评论数  需要更新商品索引 */
//			amqpSendMsgUtil.convertAndSend(AmqpConst.INDEX_EXCHANGE, AmqpConst.INDEX_CREATE_ROUTING_KEY,ImmutableList.of(productId));
		}

		//更新店铺统计信息
		//通过店铺id分组
		Map<Long, List<ProductComment>> shopCommentMap = productCommentList.stream().collect(Collectors.groupingBy(ProductComment::getShopId));
		//循环合并评分，更新统计信息
		for (Long shopId : shopCommentMap.keySet()) {
			List<ProductComment> shopComments = shopCommentMap.get(shopId);
			int shopScore = shopComments.stream().mapToInt(ProductComment::getShopScore).sum();
			//保存店铺统计表
			ShopCommentStatisticsDTO shopCommentStatisticsDTO = null;
			R<ShopCommentStatisticsDTO> shopCommStatResult = shopCommentStatisticsApi.getShopCommStatByShopIdForUpdate(shopId);
			if (shopCommStatResult.success()) {
				shopCommentStatisticsDTO = shopCommStatResult.getData();
			}
			if (ObjectUtil.isEmpty(shopCommentStatisticsDTO)) {
				shopCommentStatisticsDTO = new ShopCommentStatisticsDTO();
				shopCommentStatisticsDTO.setShopId(shopId);
				shopCommentStatisticsDTO.setCount(shopComments.size());
				shopCommentStatisticsDTO.setScore(shopScore);
				shopCommentStatisticsApi.saveShopCommStat(shopCommentStatisticsDTO);
			} else {
				shopCommentStatisticsApi.updateShopCommStat(shopScore, shopComments.size(), shopId);
			}
		}

		//更新物流统计信息
		//通过物流id分组
		Map<Long, List<ProductComment>> logisticsCommentMap = productCommentList.stream().collect(Collectors.groupingBy(ProductComment::getLogisticsCompanyId));
		//循环合并评分，更新统计信息
		for (Long logisticsId : logisticsCommentMap.keySet()) {
			List<ProductComment> logisticsComments = logisticsCommentMap.get(logisticsId);
			int logisticsScore = logisticsComments.stream().mapToInt(ProductComment::getLogisticsScore).sum();
			//保存或更新物流评分统计表
			LogisticsCommentStatisticsDTO logisticsStatistics = null;
			R<LogisticsCommentStatisticsDTO> logisticsStatisticsResult = logisticsCommentStatisticsApi.getLogisticsCommStatByLogisticsTemplateId(logisticsId);
			if (logisticsStatisticsResult.success()) {
				logisticsStatistics = logisticsStatisticsResult.getData();
			}
			if (ObjectUtil.isEmpty(logisticsStatistics)) {
				logisticsStatistics = new LogisticsCommentStatisticsDTO();
				logisticsStatistics.setLogisticsCompanyId(logisticsId);
				logisticsStatistics.setCount(logisticsComments.size());
				logisticsStatistics.setScore(logisticsScore);
				logisticsCommentStatisticsApi.saveLogisticsCommStat(logisticsStatistics);
			} else {
				logisticsCommentStatisticsApi.updateLogisticsCommStat(logisticsScore, logisticsComments.size(), logisticsId);
			}
		}
		//更新商品、店铺、物流评价统计缓存
		List<Long> shopIds = productCommentList.stream().map(ProductComment::getShopId).distinct().collect(Collectors.toList());
		shopIds.forEach(shopId -> clearCommStat(shopId));

		TransactionHookManager.registerHook(new TransactionHookAdapter() {
			@Override
			public void afterCommit() {
				for (Long productId : productCommentMap.keySet()) {
					// 更新评论和评分
					esIndexApi.reIndex(IndexTypeEnum.PRODUCT_INDEX.name(), IndexTargetMethodEnum.UPDATE.getValue(), ProductTargetTypeEnum.STATISTICS.getValue(), String.valueOf(productId));
				}
			}
		});
	}


	public void clearCommStat(Long shopId) {
		cacheManagerUtil.evictCache(COMMENT_SCORE + LOGISTICS_STATISTICS, shopId);
		cacheManagerUtil.evictCache(COMMENT_SCORE + SHOP_STATISTICS, shopId);
		cacheManagerUtil.evictCache(COMMENT_SCORE + PRODUCT_STATISTICS, shopId);
	}


	/**
	 * 更新评论统计信息, 包括评论统计, 店铺评分统计, 物流评分统计, 以及商品的评论数和分数
	 *
	 * @param productCommentDTO
	 */
	public void updateCommentStatInfo(ProductCommentDTO productCommentDTO) {
		Long productId = productCommentDTO.getProductId();
		Integer score = productCommentDTO.getScore();

		//保存评论统计表
		ProductCommentStatistics productCommentStat = productCommentStatisticsDao.getProductCommentStatByProductIdForUpdate(productId);
		if (ObjectUtil.isEmpty(productCommentStat)) {
			productCommentStat = new ProductCommentStatistics();
			productCommentStat.setProductId(productId);
			productCommentStat.setScore(score);
			//评论数
			productCommentStat.setComments(1);
			productCommentStatisticsDao.saveProductCommentStat(productCommentStat);
		} else {
			productCommentStatisticsDao.updateProductCommentStat(score, 1, productId);
		}

		//保存或更新店铺评分统计
		ShopCommentStatisticsDTO shopCommStat = null;
		Integer shopScore = productCommentDTO.getShopScore();
		Long shopId = productCommentDTO.getShopId();
		R<ShopCommentStatisticsDTO> shopCommStatResult = shopCommentStatisticsApi.getShopCommStatByShopIdForUpdate(productCommentDTO.getShopId());
		if (shopCommStatResult.success()) {
			shopCommStat = shopCommStatResult.getData();
		}
		if (ObjectUtil.isEmpty(shopCommStat)) {
			shopCommStat = new ShopCommentStatisticsDTO();
			shopCommStat.setShopId(productCommentDTO.getShopId());
			shopCommStat.setCount(1);
			shopCommStat.setScore(productCommentDTO.getShopScore());
			shopCommentStatisticsApi.saveShopCommStat(shopCommStat);
		} else {
			shopCommentStatisticsApi.updateShopCommStat(shopScore, 1, shopId);
		}

		//保存或更新物流评分统计表
		LogisticsCommentStatisticsDTO logisticsStatistics = null;
		Integer logisticsScore = productCommentDTO.getLogisticsScore();
		if (ObjectUtil.isNotEmpty(productCommentDTO.getLogisticsCompanyId())) {
			Long logisticsId = productCommentDTO.getLogisticsCompanyId();
			R<LogisticsCommentStatisticsDTO> logisticsStatisticsResult =
					logisticsCommentStatisticsApi.getLogisticsCommStatByLogisticsTemplateId(productCommentDTO.getLogisticsCompanyId());
			if (logisticsStatisticsResult.success()) {
				logisticsStatistics = logisticsStatisticsResult.getData();
			}
			if (ObjectUtil.isEmpty(logisticsStatistics)) {
				logisticsStatistics = new LogisticsCommentStatisticsDTO();
				logisticsStatistics.setLogisticsCompanyId(productCommentDTO.getLogisticsCompanyId());
				logisticsStatistics.setCount(1);
				logisticsStatistics.setScore(productCommentDTO.getLogisticsScore());
				logisticsCommentStatisticsApi.saveLogisticsCommStat(logisticsStatistics);
			} else {
				logisticsCommentStatisticsApi.updateLogisticsCommStat(logisticsScore, 1, logisticsId);
			}
		}


		/** 更新评论得分 和 评论数 , 评论得分是累加, 评论数是直接更新*/
		productDao.updateReviewScoresAndComments(productCommentDTO.getScore(), 1, productCommentDTO.getProductId());

		TransactionHookManager.registerHook(new TransactionHookAdapter() {
			@Override
			public void afterCommit() {
				/** 更新了评论数  需要更新商品索引 */
				esIndexApi.reIndex(IndexTypeEnum.PRODUCT_INDEX.name(), IndexTargetMethodEnum.UPDATE.getValue(), ProductTargetTypeEnum.STATISTICS.getValue(), productId.toString());

				/** 更新了评论数  需要更新店铺索引 */
				esIndexApi.reIndex(IndexTypeEnum.SHOP_INDEX.name(), IndexTargetMethodEnum.CREATE.getValue(), null, shopId.toString());
			}
		});

		//更新商品、店铺、物流评价统计缓存
		clearCommStat(productCommentDTO.getShopId());
	}


}
