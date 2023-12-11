/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.product.bo.ProductCommentInfoBO;
import com.legendshop.product.dao.ProductCommentDao;
import com.legendshop.product.dto.ProductCommentDTO;
import com.legendshop.product.entity.ProductComment;
import com.legendshop.product.enums.MyProductCommentEnum;
import com.legendshop.product.enums.ProductCommConditionEnum;
import com.legendshop.product.enums.ProductCommStatusEnum;
import com.legendshop.product.query.MyProductCommentQuery;
import com.legendshop.product.query.ProductCommentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品评论Dao.
 *
 * @author legendshop
 */
@Repository
public class ProductCommentDaoImpl extends GenericDaoImpl<ProductComment, Long> implements ProductCommentDao {


	@Override
	public boolean canCommentThisProd(Long productId, Long orderItemId, Long userId, Integer status) {
		String sql = "select si.comm_flag from ls_order_item si, ls_order s where si.order_number = s.order_number " +
				"and si.id = ? and si.user_id = ? AND si.product_id = ? and s.status = ?";
		Integer commFlag = this.get(sql, Integer.class, orderItemId, userId, productId, status);
		return commFlag > 0;
	}


	@Override
	public int updateAddCommFlag(boolean addCommFlag, Long prodCommId) {
		String sql = "UPDATE ls_product_comment SET add_comm_flag = ? WHERE id = ? and add_comm_flag = 0";
		return this.update(sql, addCommFlag, prodCommId);
	}


	@Override
	public ProductCommentInfoBO getProductCommentDetail(Long prodComId) {
		String sql = getSQL("ProductComment.getProductCommentDetail");
		return this.get(sql, ProductCommentInfoBO.class, prodComId);
	}


	@Override
	public PageSupport<ProductCommentInfoBO> queryProductComment(ProductCommentQuery params) {

		SimpleSqlQuery query = new SimpleSqlQuery(ProductCommentInfoBO.class, params.getPageSize(), params.getCurPage());
		QueryMap map = new QueryMap();

		map.put("productId", params.getProductId());
		// 移动端 筛选条件
		if (ProductCommConditionEnum.GOOD.getValue().equals(params.getCondition())) {
			map.put("scoreCondition", " AND p.averageScore >= 4");
		}
		if (ProductCommConditionEnum.MEDIUM.getValue().equals(params.getCondition())) {
			map.put("scoreCondition", " AND p.averageScore >= 3 AND p.averageScore < 4");
		}
		if (ProductCommConditionEnum.POOR.getValue().equals(params.getCondition())) {
			map.put("scoreCondition", " AND p.averageScore < 3");
		}
		if (ProductCommConditionEnum.PHOTO.getValue().equals(params.getCondition())) {
			map.put("isHasPhoto", " AND (c.photos IS NOT NULL OR ac.photos IS NOT NULL)");
		}
		if (ProductCommConditionEnum.APPEND.getValue().equals(params.getCondition())) {
			map.put("addCommFlag", 1);
		}

		// 用户PC端
		if ("averageScore".equals(params.getOrderBy())) {
			map.put("orderByAverageScore", "order by p.averageScore desc");
		}
		if ("addtime".equals(params.getOrderBy())) {
			map.put("orderByAddtime", "order by p.addTime desc");
		}
		query.setParam(map.toArray());
		query.setSqlAndParameter("ProductComment.queryProductComments", map);
		return querySimplePage(query);
	}


	@Override
	public Double getComScore(Long productId) {
		return get("select avg((score+shop_score+logistics_score)/3) from ls_product_comment where status = 1 and product_id = ?", Double.class, productId);
	}

	@Override
	public PageSupport<ProductCommentInfoBO> queryMyProductComment(MyProductCommentQuery params) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductCommentInfoBO.class, params.getPageSize(), params.getCurPage());
		QueryMap map = new QueryMap();
		map.put("status", OrderStatusEnum.SUCCESS.getValue());
		map.put("userId", params.getUserId());
		map.put("now", DateUtil.date());
		map.like("productName", params.getProductName(), MatchMode.ANYWHERE);
		map.put("orderNumber", params.getOrderNumber());
		map.put("startTime", params.getStartTime());
		if (StrUtil.isNotBlank(params.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(DateUtil.parse(params.getEndTime())));
		}
		map.put("addStartTime", params.getAddStartTime());
		if (StrUtil.isNotBlank(params.getAddEndTime())) {
			map.put("addEndTime", DateUtil.endOfDay(DateUtil.parse(params.getAddEndTime())));
		}
		String condition = params.getCondition();
		if (!MyProductCommentEnum.ALL.value().equals(condition)) {
			if (MyProductCommentEnum.WAIT_COMMENT.value().equals(condition)) {
				map.put("waitComment", " AND oi.comm_flag = 0");
			}
			if (MyProductCommentEnum.ALL_COMMENT.value().equals(condition)) {
				map.put("waitComment", " AND oi.comm_flag = 1");
			}
		}
		query.setParam(map.toArray());
		query.setSqlAndParameter("ProductComment.queryMyProductComment", map);
		return querySimplePage(query);
	}

	@Override
	public List<ProductComment> getWaitAuditByIdList(List<Long> idList) {
		return queryByProperties(new EntityCriterion().eq("status", ProductCommStatusEnum.WAIT_AUDIT.value()).in("id", idList));
	}

	@Override
	public PageSupport<ProductCommentDTO> querySpuComment(ProductCommentQuery productCommentQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductCommentDTO.class, productCommentQuery.getPageSize(), productCommentQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("shopId", productCommentQuery.getShopId());
		if (StrUtil.isNotEmpty(productCommentQuery.getOrder())) {


			if ("composite".equals(productCommentQuery.getOrderBy())) {
				map.put("orderBy", " order by composite" + " " + productCommentQuery.getOrder());
			} else {
				if ("addtime".equals(productCommentQuery.getOrderBy())) {
					if ("desc".equals(productCommentQuery.getOrder())) {
						map.put("orderBy", " order by " + "timedesc" + " " + productCommentQuery.getOrder());
					}
					if ("asc".equals(productCommentQuery.getOrder())) {
						map.put("orderBy", " order by " + "timeasc" + " " + productCommentQuery.getOrder());
					}
				}
				if ("count".equals(productCommentQuery.getOrderBy())) {
					map.put("orderBy", " order by " + productCommentQuery.getOrderBy() + " " + productCommentQuery.getOrder());
				}
			}
		} else {
			map.put("orderBy", "ORDER BY timedesc DESC");
		}
		map.like("productName", productCommentQuery.getProductName(), MatchMode.ANYWHERE);
		query.setSqlAndParameter("ProductComment.querySpuComment", map);
		return querySimplePage(query);
	}

	@Override
	public PageSupport<ProductCommentInfoBO> querySkuComment(ProductCommentQuery productCommentQuery) {
		QueryMap map = new QueryMap();
		SimpleSqlQuery hql = new SimpleSqlQuery(ProductCommentInfoBO.class, productCommentQuery.getPageSize(), productCommentQuery.getCurPage());

		map.put("shopId", productCommentQuery.getShopId());
		map.put("productId", productCommentQuery.getProductId());
		map.like("shopName", productCommentQuery.getShopName(), MatchMode.ANYWHERE);
		map.like("productName", productCommentQuery.getProductName(), MatchMode.ANYWHERE);
		map.put("orderNumber", productCommentQuery.getOrderNumber());
		if (ObjectUtil.isNotNull(productCommentQuery.getScoreRange())) {
			map.put("minScore", productCommentQuery.getScoreRange() - 1);
			map.put("maxScore", productCommentQuery.getScoreRange());
		}
		map.put("startTime", productCommentQuery.getStartTime());
		if (StrUtil.isNotBlank(productCommentQuery.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(DateUtil.parse(productCommentQuery.getEndTime())));
		}
		map.put("addStartTime", productCommentQuery.getAddStartTime());
		if (StrUtil.isNotBlank(productCommentQuery.getAddEndTime())) {
			map.put("addEndTime", DateUtil.endOfDay(DateUtil.parse(productCommentQuery.getAddEndTime())));
		}
		if ("0".equals(productCommentQuery.getReplyStatus())) {
			map.put("replyStatus", "AND (pc.reply_flag = 0 OR ac.reply_flag = 0)");
		}
		if ("1".equals(productCommentQuery.getReplyStatus())) {
			map.put("replyStatus", "AND ((pc.reply_flag = 1 and ISNULL(ac.reply_flag)) OR (pc.reply_flag = 1 and ac.reply_flag=1))");
		}
		hql.setSqlAndParameter("ProductComment.querySkuComment", map);
		return querySimplePage(hql);
	}

	@Override
	public List<ProductComment> getByIdList(List<Long> idList) {
		return queryByProperties(new EntityCriterion().in("id", idList));
	}

	@Override
	public List<ProductComment> queryCommentByProductId(Long productId) {
		return queryByProperties(new EntityCriterion().eq("productId", productId).eq("status", 1));
	}


	@Override
	public PageSupport<ProductCommentInfoBO> getAdminProductCommentList(ProductCommentQuery productCommentQuery) {
		QueryMap map = new QueryMap();
		SimpleSqlQuery hql = new SimpleSqlQuery(ProductCommentInfoBO.class, productCommentQuery.getPageSize(), productCommentQuery.getCurPage());

		map.put("userId", productCommentQuery.getUserId());
		map.like("shopName", productCommentQuery.getShopName(), MatchMode.ANYWHERE);
		map.like("productName", productCommentQuery.getProductName(), MatchMode.ANYWHERE);
		map.put("orderNumber", productCommentQuery.getOrderNumber());
		if (ObjectUtil.isNotNull(productCommentQuery.getScoreRange())) {
			map.put("minScore", productCommentQuery.getScoreRange() - 1);
		}
		map.put("maxScore", productCommentQuery.getScoreRange());
		map.put("startTime", productCommentQuery.getStartTime());
		if (StrUtil.isNotBlank(productCommentQuery.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(DateUtil.parse(productCommentQuery.getEndTime())));
		}
		map.put("addStartTime", productCommentQuery.getAddStartTime());
		if (StrUtil.isNotBlank(productCommentQuery.getAddEndTime())) {
			map.put("addEndTime", DateUtil.endOfDay(DateUtil.parse(productCommentQuery.getAddEndTime())));
		}
		if ("0".equals(productCommentQuery.getStatus())) {
			map.put("auditStatus", "AND (pc.status = 0 OR (pc.status = 1 AND ac.status = 0))");
		}
		if ("1".equals(productCommentQuery.getStatus())) {
			map.put("auditStatus", "AND ((pc.status = 1 and ISNULL(ac.status)) OR ac.status = 1)");
		}
		if ("-1".equals(productCommentQuery.getStatus())) {
			map.put("auditStatus", "AND (pc.status = -1  OR ac.status = -1)");
		}
		hql.setSqlAndParameter("ProductComment.queryAdminProductComments", map);
		return querySimplePage(hql);
	}


}
