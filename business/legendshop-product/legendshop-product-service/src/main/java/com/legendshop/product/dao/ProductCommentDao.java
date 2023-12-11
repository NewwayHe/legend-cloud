/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.ProductCommentInfoBO;
import com.legendshop.product.dto.ProductCommentDTO;
import com.legendshop.product.entity.ProductComment;
import com.legendshop.product.query.MyProductCommentQuery;
import com.legendshop.product.query.ProductCommentQuery;

import java.util.List;

/**
 * 产品评论Dao.
 *
 * @author legendshop
 */
public interface ProductCommentDao extends GenericDao<ProductComment, Long> {

	/**
	 * 查询用户是否可以评论此商品，必须要订单状态为已完成
	 *
	 * @param productId   商品ID
	 * @param orderItemId 订单项ID
	 * @param userId      用户ID
	 * @param status      订单状态
	 * @return 是否可以评论此商品的布尔值
	 */
	boolean canCommentThisProd(Long productId, Long orderItemId, Long userId, Integer status);

	/**
	 * 更新添加评论标志的方法。
	 *
	 * @param addCommFlag 添加评论标志
	 * @param prodCommId  商品评论ID
	 * @return 更新操作影响的行数
	 */
	int updateAddCommFlag(boolean addCommFlag, Long prodCommId);

	/**
	 * 获取商品评论详情的方法。
	 *
	 * @param prodComId 商品评论ID
	 * @return 商品评论详情
	 */
	ProductCommentInfoBO getProductCommentDetail(Long prodComId);

	/**
	 * 获取管理后台商品评论列表的方法。
	 *
	 * @param productCommentQuery 商品评论查询对象
	 * @return 商品评论信息的分页支持对象
	 */
	PageSupport<ProductCommentInfoBO> getAdminProductCommentList(ProductCommentQuery productCommentQuery);


	/**
	 * 获取商品评论详情分页列表
	 *
	 * @param params
	 * @return
	 */
	PageSupport<ProductCommentInfoBO> queryProductComment(ProductCommentQuery params);

	/**
	 * 通过商品id获得评论综合平均分数
	 *
	 * @param productId
	 * @return
	 */
	Double getComScore(Long productId);

	/**
	 * 查询商品评论列表
	 *
	 * @param productCommentQuery
	 * @return
	 */
	PageSupport<ProductCommentInfoBO> queryMyProductComment(MyProductCommentQuery productCommentQuery);

	/**
	 * 通过id列表活动待审核状态的评论列表
	 *
	 * @param idList
	 * @return
	 */
	List<ProductComment> getWaitAuditByIdList(List<Long> idList);

	/**
	 * 查询spu商品评论列表
	 *
	 * @param productCommentQuery
	 * @return
	 */
	PageSupport<ProductCommentDTO> querySpuComment(ProductCommentQuery productCommentQuery);

	/**
	 * 查询sku商品评论列表
	 *
	 * @param productCommentQuery
	 * @return
	 */
	PageSupport<ProductCommentInfoBO> querySkuComment(ProductCommentQuery productCommentQuery);

	/**
	 * 通过id列表获取评论集合
	 *
	 * @param idList
	 * @return
	 */
	List<ProductComment> getByIdList(List<Long> idList);

	/**
	 * 根据商品ID获取评论列表
	 *
	 * @param productId
	 * @return
	 */
	List<ProductComment> queryCommentByProductId(Long productId);
}
