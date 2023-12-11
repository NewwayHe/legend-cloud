/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.util.StringUtils;
import com.legendshop.basic.api.*;
import com.legendshop.basic.dto.AttachmentFileFolderDTO;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.enums.*;
import com.legendshop.basic.query.AuditQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.CacheManagerUtil;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.data.dto.ShopViewDTO;
import com.legendshop.order.api.OrderBusinessDataApi;
import com.legendshop.order.dto.OrderBusinessSumDTO;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.ProductCommentStatisticsApi;
import com.legendshop.product.bo.ProductCommentStatisticsBO;
import com.legendshop.search.api.EsIndexApi;
import com.legendshop.search.dto.ShopDocumentDTO;
import com.legendshop.search.enmus.IndexTargetMethodEnum;
import com.legendshop.search.enmus.IndexTypeEnum;
import com.legendshop.shop.bo.LogisticsCommentStatisticsBO;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.dao.ShopDetailDao;
import com.legendshop.shop.dto.*;
import com.legendshop.shop.entity.ShopDetail;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import com.legendshop.shop.enums.ShopTypeEnum;
import com.legendshop.shop.query.SearchShopQuery;
import com.legendshop.shop.query.ShopDetailQuery;
import com.legendshop.shop.service.*;
import com.legendshop.shop.service.convert.ShopDetailConverter;
import com.legendshop.shop.vo.ShopDetailVO;
import com.legendshop.shop.vo.ShopInfoVO;
import com.legendshop.user.api.AdminUserApi;
import com.legendshop.user.api.ShopUserApi;
import com.legendshop.user.dto.ShopUserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.legendshop.common.core.constant.CacheConstants.*;

/**
 * 商城详细信息服务.
 *
 * @author legendshop
 */
@Service
@Slf4j
public class ShopDetailServiceImpl implements ShopDetailService {

	@Autowired
	private AuditApi auditApi;

	@Autowired
	private ProductApi productApi;

	@Autowired
	private ShopUserApi shopUserApi;

	@Autowired
	private OrderBusinessDataApi orderBusinessDataApi;

	@Autowired
	private ProductCommentStatisticsApi productCommentStatisticsApi;

	@Autowired
	private ShopFileApi shopFileApi;

	@Autowired
	private AdminFileApi adminFileApi;

	@Autowired
	private AdminUserApi adminUserApi;

	@Autowired
	private MessageApi messageApi;


	/////////////////// 以上是client ///////////////////////////

	@Autowired
	private ShopDetailDao shopDetailDao;


	@Autowired
	private ShopDetailConverter converter;


	@Autowired
	private CacheManagerUtil cacheManagerUtil;

	@Autowired
	private ShopParamsService shopParamsService;

	@Autowired
	private ShopDetailConverter shopDetailConverter;

	@Autowired
	private FavoriteShopService favoriteShopService;

	@Autowired
	private ShopCommentStatisticsService shopCommStatService;

	@Autowired
	private IndustryDirectoryService industryDirectoryService;

	@Autowired
	private LogisticsCommentStatisticsService logisticsCommentStatisticsService;


	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	@Autowired
	private SysParamsApi sysParamsApi;

	@Autowired
	private EsIndexApi esIndexApi;
	@Autowired
	private LocationApi locationApi;

	@Override
	public ShopDetailDTO getById(Long id) {
		return converter.to(shopDetailDao.getById(id));
	}

	@Override
	@Caching(evict = {
			@CacheEvict(cacheNames = SHOP_DETAIL_VIEW, key = "#shopDetailDTO.id"),
			@CacheEvict(cacheNames = SHOP_DETAIL, key = "#shopDetailDTO.id"),
			@CacheEvict(cacheNames = SHOP_DETAIL_PAGE, allEntries = true)
	})
	public boolean updateShop(ShopDetailDTO shopDetailDTO) {
		int update = shopDetailDao.update(converter.from(shopDetailDTO));
		if (update > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}


	@Override
	public Long getShopIdByUserId(Long userId) {
		return shopDetailDao.getShopIdByUserId(userId);
	}


	@Override
	public Long getMaxShopId() {
		return shopDetailDao.getMaxShopId();
	}

	@Override
	public long getCountShopId() {
		return shopDetailDao.getCountShopId();
	}

	@Override
	public Long getMinShopId() {
		return shopDetailDao.getMinShopId();
	}


	@Override
	public Long saveContactInfo(ShopDetailDTO shopDetailDTO, Long userId, String userName) {
		shopDetailDTO.setVisitTimes(0);
		shopDetailDTO.setProductNum(0);
		shopDetailDTO.setCommNum(0);
		shopDetailDTO.setCredit(0);
		return shopDetailDao.save(converter.from(shopDetailDTO));
	}


	@Override
	public String getShopName(Long shopId) {
		return shopDetailDao.getShopName(shopId);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(cacheNames = SHOP_DETAIL_VIEW, key = "#shopId"),
			@CacheEvict(cacheNames = SHOP_DETAIL, key = "#shopId"),
			@CacheEvict(cacheNames = SHOP_DETAIL_PAGE, allEntries = true)
	})
	public Integer updateShopType(Long shopId, Integer type) {
		return shopDetailDao.updateShopType(shopId, type);
	}


	@Override
	public PageSupport<ShopDetailDTO> getShopDetailPage(ShopDetailQuery shopDetailQuery) {
		return converter.page(shopDetailDao.getShopDetailPage(shopDetailQuery));
	}

	@Override
	public ShopDetailDTO getTypeShopDetailByUserId(Long userId) {
		return converter.to(shopDetailDao.getTypeShopDetailByUserId(userId));
	}


	@Override
	public List<ShopDetailDTO> queryOnlineList() {
		return converter.to(shopDetailDao.getAllOnline());
	}

	@Override
	public PageSupport<ShopDetailDTO> page(ShopDetailQuery query) {

		boolean flag = false;
		//走缓存
		if (query.getCurPage() == 1 && query.getPageSize() == 10
				&& StringUtils.isBlank(query.getShopName()) && StringUtils.isBlank(query.getContactPhone())
				&& query.getStatus() == null && query.getOpStatus() == null
				&& ObjectUtil.isNull(query.getApplyForType()) && ObjectUtil.isNull(query.getExcludeApplyForType())) {
			flag = true;
			PageSupport<ShopDetailDTO> cache = cacheManagerUtil.getCache(SHOP_DETAIL_PAGE, query.getCurPage() + StringConstant.UNDERLINE + query.getPageSize());
			if (cache != null) {
				return cache;
			}
		}

		PageSupport<ShopDetailDTO> page = converter.page(shopDetailDao.page(query));
		if (CollUtil.isNotEmpty(page.getResultList())) {
			page.getResultList().forEach(shopDetailDTO -> {

				//1. 计算营业数据
				R<OrderBusinessSumDTO> orderSumResult = orderBusinessDataApi.getOrderSumByShopId(shopDetailDTO.getId());
				if (orderSumResult.getData() != null) {
					OrderBusinessSumDTO data = orderSumResult.getData();
					//累计销售总额
					shopDetailDTO.setSalesAmount(data.getSalesAmount() == null ? BigDecimal.valueOf(0) : data.getSalesAmount());
					//累计销售订单数
					shopDetailDTO.setSalesOrderCount(data.getSalesOrderCount() == null ? 0 : data.getSalesOrderCount());
					//累计退款金额
					shopDetailDTO.setRefundAmount(data.getRefundAmount() == null ? BigDecimal.valueOf(0) : data.getRefundAmount());
				}
				//2. 计算商品数据
				R<Long> productCountR = productApi.getProductCountByShopId(shopDetailDTO.getId(), OpStatusEnum.PASS);
				if (productCountR.success()) {
					shopDetailDTO.setProductNum(productCountR.getData().intValue());
				}

				//3. 查询商家用户基本的信息
				ShopUserDTO shopUserSimpleDTO = shopUserApi.getShopUserInfo(shopDetailDTO.getShopUserId()).getData();
				if (shopUserSimpleDTO == null) {
					shopDetailDTO.setUsername(shopUserSimpleDTO.getUsername());
					shopDetailDTO.setMobile(shopUserSimpleDTO.getMobile());
					shopDetailDTO.setDelFlag(shopUserSimpleDTO.getDelFlag());
					shopDetailDTO.setLockFlag(shopUserSimpleDTO.getLockFlag());
					shopDetailDTO.setAvatar(shopUserSimpleDTO.getAvatar());
				}

			});

		}
		//缓存
		if (flag && CollUtil.isNotEmpty(page.getResultList())) {
			cacheManagerUtil.putCache(SHOP_DETAIL_PAGE, query.getCurPage() + StringConstant.UNDERLINE + query.getPageSize(), page);
		}

		return page;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@Caching(evict = {
			@CacheEvict(cacheNames = SHOP_DETAIL_VIEW, allEntries = true),
			@CacheEvict(cacheNames = SHOP_DETAIL, allEntries = true),
			@CacheEvict(cacheNames = SHOP_DETAIL_PAGE, allEntries = true)
	})
	public R audit(AuditDTO auditDTO) {
		List<Long> shopIds = auditDTO.getIdList();

		if (shopDetailDao.isShopAuditPass(shopIds)) {
			return R.fail("审核的店铺只能是未审核的店铺！");
		}

		Boolean result = shopDetailDao.updateOpStatus(auditDTO.getIdList(), auditDTO.getOpStatus());
		if (!result) {
			return R.fail("审核失败，未找到该店铺");
		}
		if (OpStatusEnum.PASS.getValue().equals(auditDTO.getOpStatus())) {
			shopDetailDao.updateStatus(auditDTO.getIdList(), ShopDetailStatusEnum.ONLINE.getStatus());
			//初始化店铺基础设置（默认取系统设置的SHOP分组）
			for (Long shopId : auditDTO.getIdList()) {
				shopParamsService.initShopParams(shopId);
			}

			//判断是否存在平台根目录, 并初始化
			R<Long> adminId = adminFileApi.checkFile();
			Long data = adminId.getData();
			//初始化商家根目录
			for (Long shopId : auditDTO.getIdList()) {
				AttachmentFileFolderDTO attachmentFileFolderDTO = new AttachmentFileFolderDTO();
				attachmentFileFolderDTO.setUserType(1);
				attachmentFileFolderDTO.setShopId(shopId);
				attachmentFileFolderDTO.setCreateTime(new Date());
				ShopDetail byShopId = shopDetailDao.getByShopId(shopId);
				attachmentFileFolderDTO.setFileName("商家" + byShopId.getShopName() + "根目录");
				attachmentFileFolderDTO.setTypeId(1);
				attachmentFileFolderDTO.setParentId(data);
				shopFileApi.save(attachmentFileFolderDTO);
			}

			// 重建店铺索引
			log.info("############# 重建店铺索引");
			amqpSendMsgUtil.convertAndSend(com.legendshop.search.constants.AmqpConst.INDEX_EXCHANGE, com.legendshop.search.constants.AmqpConst.INDEX_CREATE_BY_SHOP_ROUTING_KEY, auditDTO.getIdList(), true);
		}
		auditDTO.setAuditTime(DateUtil.date());
		auditDTO.setAuditUsername(SecurityUtils.getAdminUser().getUsername());
		auditDTO.setAuditType(AuditTypeEnum.SHOP_DETAIL.getValue());
		auditApi.batchSave(auditDTO);

		return R.ok("审核成功");
	}

	@Override
	public PageSupport<AuditDTO> auditHistory(Long shopId, PageParams pageParams) {
		AuditQuery auditQuery = new AuditQuery();
		auditQuery.setPageSize(pageParams.getPageSize());
		auditQuery.setCurPage(pageParams.getCurPage());
		auditQuery.setCommonId(shopId);
		auditQuery.setAuditType(AuditTypeEnum.SHOP_DETAIL.getValue());
		return auditApi.page(auditQuery).getData();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveShopDetail(ShopDetailDTO shopDetailDTO, String ip) {
		Long shopUserId = SecurityUtils.getShopUser().getUserId();
		//初始值
		shopDetailDTO.setShopUserId(shopUserId);
		shopDetailDTO.setShopType(ShopTypeEnum.FRANCHISE.getValue());
		shopDetailDTO.setCreateTime(new Date());
		shopDetailDTO.setProductNum(0);
		shopDetailDTO.setCommNum(0);
		shopDetailDTO.setVisitTimes(0);
		shopDetailDTO.setProdRequireAudit(1);
		shopDetailDTO.setCommissionRate(0D);
		shopDetailDTO.setOpStatus(OpStatusEnum.WAIT.getValue());
		shopDetailDTO.setStatus(ShopDetailStatusEnum.OFFLINE.getStatus());

		Long shopId = 0L;
		try {
			shopId = shopDetailDao.save(converter.from(shopDetailDTO));
		} catch (Exception e) {
			throw new BusinessException("该用户已有店铺，不能重复申请");
		}
//		保存店铺信息成功发送短信提醒管理员审核
		if (shopId > 0) {
			//根据商家店铺列表类目ID查找具有类目权限的管理员用户ID
			R<List<Long>> listR = adminUserApi.queryUsersByMenuId(14498);
			if (!listR.getSuccess() || CollUtil.isEmpty(listR.getData())) {
				return shopId;
			}

			// 组装发送参数
			messageApi.push(new MessagePushDTO()
					.setReceiveIdArr(listR.getData().toArray(new Long[listR.getData().size()]))
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ADMIN_USER)
					.setMsgSendType(MsgSendTypeEnum.SHOP_ENTER_NOTIFY)
					.setSysParamNameEnum(SysParamNameEnum.SHOP_ENTER_NOTIFY_TO_ADMIN)
					.setDetailId(shopId)
			);
		}

		return shopId;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	@Caching(evict = {
			@CacheEvict(cacheNames = SHOP_DETAIL_VIEW, key = "#shopDetailDTO.id"),
			@CacheEvict(cacheNames = SHOP_DETAIL, key = "#shopDetailDTO.id"),
			@CacheEvict(cacheNames = SHOP_DETAIL_PAGE, allEntries = true)
	})
	public R<Void> updateShopDetail(ShopDetailDTO shopDetailDTO) {
		ShopDetail shopDetail = shopDetailDao.getByShopId(shopDetailDTO.getId());
		if (shopDetail == null) {
			return R.fail("未找到该店铺信息");
		}

		// 检查商家名称，商家名称是唯一的
		boolean isShopNameExist = shopDetailDao.isOtherShopNameExist(shopDetail.getId(), shopDetailDTO.getShopName());

		if (isShopNameExist) {
			return R.fail("店铺名'" + shopDetailDTO.getShopName() + "'已经存在, 请换另外一个店铺名");
		}
		shopFileApi.updateFileName(shopDetailDTO.getShopName() + "根目录", shopDetailDTO.getId());
		// 更新商家用户头像
		if (ObjectUtil.isNotNull(shopDetailDTO.getUserAvatar())) {
			shopUserApi.updateAvatar(shopDetailDTO.getShopUserId(), shopDetailDTO.getUserAvatar());
		}
		// 更新营业执照
		if (ObjectUtil.isNotNull(shopDetailDTO.getBusinessLicense())) {
			shopDetail.setBusinessLicense(shopDetailDTO.getBusinessLicense());
		}
		// 可修改字段
		shopDetail.setUpdateTime(new Date());
		shopDetail.setShopName(shopDetailDTO.getShopName());
		shopDetail.setShopAvatar(shopDetailDTO.getShopAvatar());
		shopDetail.setContactName(shopDetailDTO.getContactName());
		shopDetail.setContactPhone(shopDetailDTO.getContactPhone());
		shopDetail.setProvinceId(shopDetailDTO.getProvinceId());
		shopDetail.setCityId(shopDetailDTO.getCityId());
		shopDetail.setAreaId(shopDetailDTO.getAreaId());
		shopDetail.setStreetId(shopDetailDTO.getStreetId());
		shopDetail.setShopAddress(shopDetailDTO.getShopAddress());
		shopDetail.setShopBrief(shopDetailDTO.getBriefDesc());
		shopDetail.setIndustryDirectoryId(shopDetailDTO.getIndustryDirectoryId());
		if (ObjectUtil.isNotEmpty(shopDetailDTO.getBusinessEndTime())) {
			shopDetail.setBusinessEndTime(shopDetailDTO.getBusinessEndTime());
		}
		shopDetail.setBusinessStartTime(shopDetailDTO.getBusinessStartTime());
		shopDetail.setBusinessScope(shopDetailDTO.getBusinessScope());
		Boolean result = shopDetailDao.update(shopDetail) > 0;
		if (!result) {
			return R.fail("更新店铺失败");
		}
		// 更新店铺索引
		esIndexApi.reIndex(IndexTypeEnum.SHOP_INDEX.name(), IndexTargetMethodEnum.CREATE.getValue(), null, shopDetail.getId().toString());
		return R.ok();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@Caching(evict = {
			@CacheEvict(cacheNames = SHOP_DETAIL_VIEW, key = "#shopDetailDTO.id"),
			@CacheEvict(cacheNames = SHOP_DETAIL, key = "#shopDetailDTO.id"),
			@CacheEvict(cacheNames = SHOP_DETAIL_PAGE, allEntries = true)
	})
	public R<Void> updateSettleInInfo(ShopDetailDTO shopDetailDTO) {
		ShopDetail shopDetail = shopDetailDao.getByShopId(shopDetailDTO.getId());
		if (shopDetail == null) {
			return R.fail("未找到该店铺信息");
		}

		// 更新商家用户头像
		if (ObjectUtil.isNotNull(shopDetailDTO.getUserAvatar())) {
			shopUserApi.updateAvatar(shopDetailDTO.getShopUserId(), shopDetailDTO.getUserAvatar());
		}
		//初始值
		shopDetail.setUpdateTime(new Date());
		shopDetail.setIdCardNumber(shopDetailDTO.getIdCardNumber());
		shopDetail.setIdCardBackPic(shopDetailDTO.getIdCardBackPic());
		shopDetail.setOpStatus(OpStatusEnum.WAIT.getValue());
		shopDetail.setShopName(shopDetailDTO.getShopName());
		shopDetail.setShopAvatar(shopDetailDTO.getShopAvatar());
		shopDetail.setContactName(shopDetailDTO.getContactName());
		shopDetail.setContactPhone(shopDetailDTO.getContactPhone());
		shopDetail.setProvinceId(shopDetailDTO.getProvinceId());
		shopDetail.setCityId(shopDetailDTO.getCityId());
		shopDetail.setAreaId(shopDetailDTO.getAreaId());
		shopDetail.setStreetId(shopDetailDTO.getStreetId());
		shopDetail.setShopAddress(shopDetailDTO.getShopAddress());
		shopDetail.setShopBrief(shopDetailDTO.getBriefDesc());
		shopDetail.setBusinessLicense(shopDetailDTO.getBusinessLicense());

		boolean success = shopDetailDao.update(shopDetail) > 0;

		if (success && OpStatusEnum.WAIT.getValue().equals(shopDetail.getOpStatus())) {
			//根据商家店铺列表类目ID查找具有类目权限的管理员用户ID
			R<List<Long>> listR = adminUserApi.queryUsersByMenuId(14498);
			if (!listR.getSuccess() || CollUtil.isEmpty(listR.getData())) {
				return R.ok();
			}

			// 组装发送参数
			messageApi.push(new MessagePushDTO()
					.setReceiveIdArr(listR.getData().toArray(new Long[listR.getData().size()]))
					.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ADMIN_USER)
					.setMsgSendType(MsgSendTypeEnum.SHOP_ENTER_NOTIFY)
					.setSysParamNameEnum(SysParamNameEnum.SHOP_ENTER_NOTIFY_TO_ADMIN)
					.setDetailId(shopDetail.getId())
			);
		}
		return R.process(success, "更新店铺信息失败");
	}

	@Override
	public ShopDetailVO getByShopUserId(Long shopUserId) {
		return shopDetailConverter.toVO(shopDetailDao.getByShopUserId(shopUserId));
	}

	@Override
	public boolean isShopNameExist(String shopName) {
		return shopDetailDao.isShopNameExist(shopName);
	}

	@Override
	public boolean isShopUserIdExist(Long shopUserId) {
		return shopDetailDao.isShopUserIdExist(shopUserId);
	}

	@Override
	public ShopDetailDTO getByUserId(Long userId) {
		return converter.to(shopDetailDao.getByUserId(userId));
	}

	@Override
	public List<ShopDetailDTO> queryByUserIds(List<Long> userIds) {
		return converter.to(shopDetailDao.getByShopUserId(userIds));
	}

	/**
	 * 商品详情店铺信息
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	@Override
	public R<ShopDetailBO> getUserShop(Long userId, Long shopId) {
		BigDecimal shopScore;
		BigDecimal prodScore;
		BigDecimal logisticsScore;
		//通过店铺id查到店铺信息
		ShopDetailBO shopDetailBO = shopDetailDao.getUserShop(shopId);

		if (ObjectUtil.isNull(shopDetailBO)) {
			return R.fail("该店铺不存在或已被下线");
		}

		//计算该店铺所有已评分物流的平均分（与search服务保持一致）
		LogisticsCommentStatisticsBO statBO = logisticsCommentStatisticsService.getLogisticsCommStatByShopId(shopId);
		if (statBO != null && statBO.getScore() != null && statBO.getCount() != null) {
			logisticsScore = new BigDecimal(statBO.getScore()).divide(new BigDecimal(statBO.getCount()), 2, RoundingMode.HALF_UP);
			shopDetailBO.setDvyTypeAvg(logisticsScore);
		}

		//计算该店铺服务的平均分（与search服务保持一致）
		ShopCommentStatisticsDTO shopCommentStat = shopCommStatService.getByShopId(shopId);
		if (shopCommentStat != null && shopCommentStat.getScore() != null && shopCommentStat.getCount() != null) {
			shopScore = new BigDecimal(shopCommentStat.getScore()).divide(new BigDecimal(shopCommentStat.getCount()), 2, RoundingMode.HALF_UP);
			shopDetailBO.setShopCommAvg(shopScore);
		}

		//计算该店铺所有已评分商品的平均分（与search服务保持一致）
		ProductCommentStatisticsBO prodCommentStat = productCommentStatisticsApi.getProductCommentStatByShopId(shopId).getData();
		if (prodCommentStat != null && prodCommentStat.getScore() != null && prodCommentStat.getComments() != null) {
			prodScore = new BigDecimal(prodCommentStat.getScore()).divide(new BigDecimal(prodCommentStat.getComments()), 2, RoundingMode.HALF_UP);
			shopDetailBO.setProductCommentAvg(prodScore);
		}

		//计算该店铺综合评分（与search服务保持一致）
		Optional<ShopDetailBO> optional = Optional.ofNullable(shopDetailBO);
		optional.ifPresent(a -> {
			a.setScore((a.getDvyTypeAvg().add(a.getShopCommAvg()).add(a.getProductCommentAvg())).divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP));
		});

		// 店铺是否已被收藏
		shopDetailBO.setCollectionFlag(Boolean.FALSE);
		if (ObjectUtil.isNotNull(userId) && favoriteShopService.isExists(userId, shopId)) {
			shopDetailBO.setCollectionFlag(Boolean.TRUE);
		}

		//发送店铺浏览记录MQ
		ShopViewDTO shopViewDTO = new ShopViewDTO();
		shopViewDTO.setShopId(shopId);
		shopViewDTO.setUserId(userId);
		sendShopViewMessage(shopViewDTO);

		return R.ok(shopDetailBO);
	}

	@Override
	@Cacheable(cacheNames = SHOP_DETAIL_VIEW, key = "#shopId")
	public ShopDetailVO getShopDetailVO(Long shopId) {
		ShopDetailVO shopDetailVO = shopDetailDao.getShopDetailVO(shopId);
		//获取最近一条审核记录
		R<AuditDTO> result = auditApi.getByAuditTypeAndCommonId(shopDetailVO.getId(), AuditTypeEnum.SHOP_DETAIL);
		if (result.getData() != null) {
			AuditDTO auditDTO = result.getData();
			shopDetailVO.setAuditStatus(auditDTO.getOpStatus());
			shopDetailVO.setAuditOpinion(auditDTO.getAuditOpinion());
			shopDetailVO.setAuditTime(auditDTO.getAuditTime());
		}
		return shopDetailVO;
	}

	@Override
	public ShopOrderSettingDTO getShopOrderSetting(Long shopId) {
		return shopDetailDao.getShopOrderSetting(shopId);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(cacheNames = SHOP_DETAIL_VIEW, key = "#shopOrderSettingDTO.shopId"),
			@CacheEvict(cacheNames = SHOP_DETAIL, key = "#shopOrderSettingDTO.shopId"),
			@CacheEvict(cacheNames = SHOP_DETAIL_PAGE, allEntries = true)
	})
	public R<Void> updateShopOrderSetting(ShopOrderSettingDTO shopOrderSettingDTO) {

		ShopDetail shopDetail = shopDetailDao.getById(shopOrderSettingDTO.getShopId());
		if (ObjectUtil.isNull(shopDetail)) {
			return R.fail("商家信息不存在或已被删除，请重试");
		}
		shopDetail.setInvoiceFlag(shopOrderSettingDTO.getInvoiceFlag());
		shopDetail.setInvoiceType(shopOrderSettingDTO.getInvoiceType());
		shopDetail.setReturnConsignee(shopOrderSettingDTO.getReturnConsignee());
		shopDetail.setReturnConsigneePhone(shopOrderSettingDTO.getReturnConsigneePhone());
		shopDetail.setReturnProvinceId(shopOrderSettingDTO.getReturnProvinceId());
		shopDetail.setReturnCityId(shopOrderSettingDTO.getReturnCityId());
		shopDetail.setReturnAreaId(shopOrderSettingDTO.getReturnAreaId());
		shopDetail.setReturnStreetId(shopOrderSettingDTO.getReturnStreetId());
		shopDetail.setReturnShopAddr(shopOrderSettingDTO.getReturnShopAddr());

		int updateResult = shopDetailDao.update(shopDetail);
		if (updateResult <= 0) {
			return R.fail("更新商家信息失败");
		}
		return R.ok();
	}

	@Override
	public PageSupport<ShopSelectDTO> querySelect2(ShopDetailQuery shopDetailQuery) {
		PageSupport<ShopDetail> ps = shopDetailDao.querySelect2(shopDetailQuery);
		return converter.convert2SelectDTO(converter.page(ps));
	}

	@Override
	@Caching(evict = {
			@CacheEvict(cacheNames = SHOP_DETAIL_VIEW, allEntries = true),
			@CacheEvict(cacheNames = SHOP_DETAIL, allEntries = true),
			@CacheEvict(cacheNames = SHOP_DETAIL_PAGE, allEntries = true)
	})
	public R batchUpdateStatus(BasicBatchUpdateStatusDTO dto) {
		List<ShopDetail> shopDetails = shopDetailDao.queryAllByIds(dto.getIds());
		//只有审核通过的才能进行修改状态
		if (CollUtil.isNotEmpty(shopDetails)) {
			List<Long> ids = shopDetails.stream()
					.filter(shopDetail -> OpStatusEnum.PASS.getValue().equals(shopDetail.getOpStatus()))
					.map(ShopDetail::getId)
					.collect(Collectors.toList());
			if (CollUtil.isNotEmpty(ids)) {
				shopDetailDao.batchUpdateStatus(ids, dto.getStatus());
				//店铺下架，对应的商品全部下架，并且删除商品索引
				if (ShopDetailStatusEnum.OFFLINE.getStatus().equals(dto.getStatus())) {
					productApi.offLineByShopIds(ids);
				}
			}
		}
		return R.ok();
	}

	@Override
	@Caching(evict = {
			@CacheEvict(cacheNames = SHOP_DETAIL_VIEW, key = "#shopId"),
			@CacheEvict(cacheNames = SHOP_DETAIL, key = "#shopId"),
			@CacheEvict(cacheNames = SHOP_DETAIL_PAGE, allEntries = true)
	})
	public Integer updateCommissionRate(Long shopId, Double commissionRate) {
		return shopDetailDao.updateCommissionRate(shopId, commissionRate);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(cacheNames = SHOP_DETAIL_VIEW, key = "#shopId"),
			@CacheEvict(cacheNames = SHOP_DETAIL, key = "#shopId"),
			@CacheEvict(cacheNames = SHOP_DETAIL_PAGE, allEntries = true)
	})
	public Integer updateProdAudit(Long shopId, Integer status) {
		return shopDetailDao.updateProdAudit(shopId, status);
	}


	@Override
	public ShopInfoVO getShopInfoVO(Long shopId) {
		ShopDetail shopDetail = this.shopDetailDao.getById(shopId);
		ShopInfoVO shopInfoVO = this.converter.toShopInfoVO(shopDetail);
		if (shopInfoVO == null || null == shopDetail.getIndustryDirectoryId()) {
			return shopInfoVO;
		}
		IndustryDirectoryDTO industryDirectoryDTO = this.industryDirectoryService.getById(shopDetail.getIndustryDirectoryId());
		if (null == industryDirectoryDTO) {
			return shopInfoVO;
		}
		shopInfoVO.setIndustryDirectoryName(industryDirectoryDTO.getName());
		shopInfoVO.setShopNewBieStatus(shopDetail.getShopNewBieStatus());
		return shopInfoVO;
	}


	@Override
	public PageSupport<SearchShopDTO> searchShop(SearchShopQuery query) {
		PageSupport<SearchShopDTO> ps = this.shopDetailDao.searchShop(query);
		List<SearchShopDTO> resultList = ps.getResultList();
		List<Long> shopIds = resultList.stream().map(SearchShopDTO::getShopId).collect(Collectors.toList());
		//查询店铺物流平均分 四舍五入保留一位小数
		List<LogisticsCommentStatisticsBO> logisticsCommentStatisticsBoList = shopDetailDao.queryDvyTypeAvg(shopIds);
		Map<Long, LogisticsCommentStatisticsBO> longLogisticsCommentStatisticsBoMap = logisticsCommentStatisticsBoList.stream().collect(Collectors.toMap(LogisticsCommentStatisticsBO::getShopId, e -> e));
		//查询店铺服务平均分 四舍五入保留一位小数
		List<ShopCommentStatisticsDTO> shopCommentStatisticsDtoList = shopDetailDao.queryShopCommAvg(shopIds);
		Map<Long, ShopCommentStatisticsDTO> shopCommentStatisticsDtoMap = shopCommentStatisticsDtoList.stream().collect(Collectors.toMap(ShopCommentStatisticsDTO::getShopId, e -> e));
		//查询店铺描述评价分 四舍五入保留一位小数
		List<ProductCommentStatisticsBO> productCommentStatisticsBoList = shopDetailDao.queryProductCommentAvg(shopIds);
		Map<Long, ProductCommentStatisticsBO> productCommentStatisticsBoMap = productCommentStatisticsBoList.stream().collect(Collectors.toMap(ProductCommentStatisticsBO::getShopId, e -> e));
		for (SearchShopDTO searchShopDTO : resultList) {
			LogisticsCommentStatisticsBO logisticsCommentStatisticsBO = longLogisticsCommentStatisticsBoMap.get(searchShopDTO.getShopId());
			searchShopDTO.setDvyTypeAvg(ObjectUtil.isEmpty(logisticsCommentStatisticsBO) ? new BigDecimal("0.00") : logisticsCommentStatisticsBO.getAvg());

			ShopCommentStatisticsDTO shopCommentStatisticsDTO = shopCommentStatisticsDtoMap.get(searchShopDTO.getShopId());
			searchShopDTO.setShopCommAvg(ObjectUtil.isEmpty(shopCommentStatisticsDTO) ? new BigDecimal("0.00") : shopCommentStatisticsDTO.getAvg());

			ProductCommentStatisticsBO productCommentStatisticsBO = productCommentStatisticsBoMap.get(searchShopDTO.getShopId());
			searchShopDTO.setProductCommentAvg(ObjectUtil.isEmpty(productCommentStatisticsBO) ? new BigDecimal("0.00") : productCommentStatisticsBO.getAvg());

			//计算该店铺好评率 好评率 = 店铺综合评分 / 满分值(5.00) * 100%；
			BigDecimal v = (searchShopDTO.getDvyTypeAvg().add(searchShopDTO.getShopCommAvg()).add(searchShopDTO.getProductCommentAvg())).divide(new BigDecimal("3"), 2, RoundingMode.DOWN);
			searchShopDTO.setFavorableRate(v.divide(new BigDecimal("5"), 2, RoundingMode.DOWN).multiply(new BigDecimal("100")));
			if (searchShopDTO.getFavorableRate().setScale(0, RoundingMode.DOWN).equals(BigDecimal.ZERO)) {
				searchShopDTO.setFavorableRate(new BigDecimal("100"));
			}
			searchShopDTO.setProduct(shopDetailDao.queryPicsByShopIds(searchShopDTO.getShopId()));
		}
		//前端传的值 好评率
		if ("praise".equals(query.getSortBy())) {
			if (query.getDescending()) {
				List<SearchShopDTO> collect = resultList.stream().sorted((e1, e2) -> {
					return e2.getFavorableRate().compareTo(e1.getFavorableRate());
				}).collect(Collectors.toList());
				ps.setResultList(collect);
			} else {
				List<SearchShopDTO> collect = resultList.stream().sorted((e1, e2) -> {
					return e1.getFavorableRate().compareTo(e2.getFavorableRate());
				}).collect(Collectors.toList());
				ps.setResultList(collect);
			}
			return ps;
		}

		return ps;
	}

	@Override
	public Long searchAllShop(SearchShopQuery query) {
		return shopDetailDao.searchAllShop(query);
	}

	@Override
	public R<Boolean> updateBuys(Long shopId, Integer buys) {
		int result = this.shopDetailDao.updateBuys(shopId, buys);
		if (result <= 0) {
			return R.fail();
		}
		return R.ok();
	}

	@Override
	public R<List<ShopDetailDTO>> queryAll() {
		return R.ok(shopDetailConverter.to(shopDetailDao.queryAll()));
	}

	@Override
	public List<ShopDetailDTO> queryByIds(List<Long> shopIds) {
		return shopDetailDao.queryByIds(shopIds);
	}

	/**
	 * 发送店铺浏览MQ
	 *
	 * @param shopViewDTO
	 */
	private void sendShopViewMessage(ShopViewDTO shopViewDTO) {
		HttpServletRequest http = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String source = http.getHeader(RequestHeaderConstant.SOURCE_KEY);
		shopViewDTO.setSource(source);
		shopViewDTO.setCreateTime(DateUtil.beginOfDay(new Date()));
		amqpSendMsgUtil.convertAndSend(com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_EXCHANGE, com.legendshop.data.constants.AmqpConst.LEGENDSHOP_DATA_SHOP_VIEW_LOG_ROUTING_KEY, shopViewDTO);
	}

	@Override
	public List<ShopListDTO> queryShopList(List<Long> ids) {
		List<ShopListDTO> shopListDTOList = shopDetailDao.queryShopList(ids);
		shopListDTOList.sort(Comparator.comparingInt(e -> ids.indexOf(e.getId())));
		return shopListDTOList;
	}

	@Override
	public Boolean getUserShopFlag(Long userId, Long shopId) {
		return favoriteShopService.isExists(userId, shopId);
	}

	@Override
	public ShopDetailBO getUserShopEs(Long userId, Long shopId) {
		ShopDocumentDTO shopDocumentDTO = esIndexApi.getShopById(shopId);
		if (ObjectUtil.isEmpty(shopDocumentDTO)) {
			return null;
		}
		ShopDetailBO shopDetailBO = createShopDetailBO(shopDocumentDTO);
		// 店铺是否已被收藏
		shopDetailBO.setCollectionFlag(Boolean.FALSE);
		if (ObjectUtil.isNotNull(userId) && favoriteShopService.isExists(userId, shopId)) {
			shopDetailBO.setCollectionFlag(Boolean.TRUE);
		}

		//发送店铺浏览记录MQ
		ShopViewDTO shopViewDTO = new ShopViewDTO();
		shopViewDTO.setShopId(shopId);
		shopViewDTO.setUserId(userId);
		sendShopViewMessage(shopViewDTO);
		return shopDetailBO;
	}

	@Override
	public R<ShopDetailDTO> updateDefaultDisScale(BigDecimal defaultDisScale, Long shopUserId) {
		if (ObjectUtil.isEmpty(defaultDisScale)) {
			return R.fail("更新失败");
		}

		log.info("开始更新默认分销比例, shopUserId:{}, 分销比例:{}", shopUserId, defaultDisScale);
		int count = shopDetailDao.updateDefaultDisScale(defaultDisScale, shopUserId);
		if (count <= 0) {
			return R.fail("更新失败");
		}
		return R.ok();
	}

	@Override
	public R<BigDecimal> getDefaultDisScale(Long shopUserId) {
		ShopDetail shopDetail = shopDetailDao.getByShopUserId(shopUserId);
		if (ObjectUtil.isEmpty(shopDetail)) {
			return R.fail("获取默认分销比例失败");
		}
		return R.ok(shopDetail.getDefaultDisScale());
	}

	@Override
	public int updateShopNewBieStatus(Integer shopNewBieStatus, Long shopUserId) {
		return shopDetailDao.updateShopNewBieStatus(shopNewBieStatus, shopUserId);
	}

	@Override
	public R<ShopMessageDTO> getshopDetail(Long shopId) {
		BigDecimal shopScore;
		BigDecimal prodScore;
		BigDecimal logisticsScore;
		if (ObjectUtil.isEmpty(shopId)) {
			return R.fail("店铺id为null");
		}
		//通过店铺id查到店铺信息
		ShopMessageDTO shopMessageDTO = shopDetailDao.getShopMessage(shopId);
		//ShopMessageDTO shopMessageDTO = new ShopMessageDTO();
		if (ObjectUtil.isEmpty(shopMessageDTO)) {
			return R.fail("该店铺不存在或已被下线");
		}

		//计算该店铺所有已评分物流的平均分（与search服务保持一致）
		LogisticsCommentStatisticsBO statBO = logisticsCommentStatisticsService.getLogisticsCommStatByShopId(shopId);
		if (statBO != null && statBO.getScore() != null && statBO.getCount() != null) {
			logisticsScore = new BigDecimal(statBO.getScore()).divide(new BigDecimal(statBO.getCount()), 2, RoundingMode.HALF_UP);
			shopMessageDTO.setDvyTypeAvg(logisticsScore);
		}

		//计算该店铺服务的平均分（与search服务保持一致）
		ShopCommentStatisticsDTO shopCommentStat = shopCommStatService.getByShopId(shopId);
		if (shopCommentStat != null && shopCommentStat.getScore() != null && shopCommentStat.getCount() != null) {
			shopScore = new BigDecimal(shopCommentStat.getScore()).divide(new BigDecimal(shopCommentStat.getCount()), 2, RoundingMode.HALF_UP);
			shopMessageDTO.setShopCommAvg(shopScore);
		}

		//计算该店铺所有已评分商品的平均分（与search服务保持一致）
		ProductCommentStatisticsBO prodCommentStat = productCommentStatisticsApi.getProductCommentStatByShopId(shopId).getData();
		if (prodCommentStat != null && prodCommentStat.getScore() != null && prodCommentStat.getComments() != null) {
			prodScore = new BigDecimal(prodCommentStat.getScore()).divide(new BigDecimal(prodCommentStat.getComments()), 2, RoundingMode.HALF_UP);
			shopMessageDTO.setProductCommentAvg(prodScore);
		}
		if (ObjectUtil.isEmpty(shopMessageDTO.getShopCommAvg())) {
			shopMessageDTO.setShopCommAvg(BigDecimal.ZERO);
		}
		if (ObjectUtil.isEmpty(shopMessageDTO.getProductCommentAvg())) {
			shopMessageDTO.setProductCommentAvg(BigDecimal.ZERO);
		}
		BigDecimal score = shopMessageDTO.getShopCommAvg().add(shopMessageDTO.getProductCommentAvg().divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP));
		shopMessageDTO.setScore(score);

		return R.ok(shopMessageDTO);
	}


	@Override
	public R<ShopDetailDocumentsDTO> getshopDetailDocuments(Long userId, Long shopId) {
		if (ObjectUtil.isEmpty(shopId)) {
			return R.fail("店铺id为空");
		}
		ShopDetailDocumentsDTO shopDetailDocumentsDTO = shopDetailDao.getshopDetailDocuments(shopId);

		if (ObjectUtil.isEmpty(userId)) {
			return R.fail("登录信息失效,请重新登陆");
		}
		String user = redisTemplate.opsForValue().get(userId.toString());
		if (StrUtil.isNotEmpty(user)) {
			return R.fail("请求过于频繁,请稍后");
		}
		redisTemplate.opsForValue().set(userId.toString(), userId.toString(), 2, TimeUnit.SECONDS);

		if (ObjectUtil.isNotEmpty(shopDetailDocumentsDTO.getBusinessStartTime()) && ObjectUtil.isNotEmpty(shopDetailDocumentsDTO.getBusinessEndTime())) {
			//获取营业执照有效期
			shopDetailDocumentsDTO.setBusinessLicenseTime(shopDetailDocumentsDTO.getBusinessStartTime().toString() + "至" + shopDetailDocumentsDTO.getBusinessEndTime().toString());
		}
		if (ObjectUtil.isNotEmpty(shopDetailDocumentsDTO.getBusinessStartTime()) && ObjectUtil.isEmpty(shopDetailDocumentsDTO.getBusinessEndTime())) {
			//获取营业执照有效期
			shopDetailDocumentsDTO.setBusinessLicenseTime(shopDetailDocumentsDTO.getBusinessStartTime().toString() + "至长期");
		}
		return R.ok(shopDetailDocumentsDTO);

	}

	/**
	 * 转换店铺详细对象
	 *
	 * @param shopDocumentDTO
	 * @return
	 */
	private ShopDetailBO createShopDetailBO(ShopDocumentDTO shopDocumentDTO) {
		ShopDetailBO shopDetailBO = new ShopDetailBO();
		shopDetailBO.setShopId(shopDocumentDTO.getShopId());
		shopDetailBO.setShopName(shopDocumentDTO.getShopName());
		shopDetailBO.setShopType(shopDocumentDTO.getShopType());
		shopDetailBO.setShopAvatar(shopDocumentDTO.getPic());
		shopDetailBO.setApplyForType(shopDocumentDTO.getApplyForType());
		shopDetailBO.setDvyTypeAvg(shopDocumentDTO.getDvyTypeAvg());
		shopDetailBO.setShopCommAvg(shopDocumentDTO.getShopCommAvg());
		shopDetailBO.setProductCommentAvg(shopDocumentDTO.getProductCommentAvg());
		shopDetailBO.setScore(shopDocumentDTO.getScore());
		return shopDetailBO;
	}
}
