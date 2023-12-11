/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.product.bo.ProductCommentAnalysisBO;
import com.legendshop.product.bo.ProductCommentBO;
import com.legendshop.product.bo.ProductCommentInfoBO;
import com.legendshop.product.dto.ProductCommentDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.query.MyProductCommentQuery;
import com.legendshop.product.query.ProductCommentQuery;

import java.util.List;

/**
 * 商品评论服务
 *
 * @author legendshop
 */
public interface ProductCommentService extends BaseService<ProductCommentDTO> {

	/**
	 * 根据ID获取商品评论
	 *
	 * @param id
	 * @return
	 */
	ProductCommentBO getProductCommentById(Long id);


	/**
	 * 获取商品评论详情分页列表
	 *
	 * @param productCommentQuery
	 * @return
	 */
	PageSupport<ProductCommentInfoBO> queryProductComment(ProductCommentQuery productCommentQuery);


	/**
	 * 判断是否能追加评论的方法。
	 *
	 * @param prodCommId 商品评论ID
	 * @param userId     用户ID
	 * @return 是否能追加评论的布尔值
	 */
	boolean isCanAddComment(Long prodCommId, Long userId);

	/**
	 * 查看评论详情的方法。
	 *
	 * @param prodComId 商品评论ID
	 * @return 商品评论详情
	 */
	ProductCommentInfoBO getProductCommentDetail(Long prodComId);


	/**
	 * 后台分页查询评论列表
	 *
	 * @param productCommentQuery
	 * @return
	 */
	PageSupport<ProductCommentInfoBO> getAdminProductCommentList(ProductCommentQuery productCommentQuery);


	/**
	 * 通过商品id获取商品的综合平均评分
	 *
	 * @param productId
	 * @return
	 */
	ProductDTO getComScore(Long productId);

	/**
	 * 查询商品评论列表
	 *
	 * @param productCommentQuery
	 * @return
	 */
	PageSupport<ProductCommentInfoBO> queryMyProductComment(MyProductCommentQuery productCommentQuery);

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
	 * 回复初评
	 *
	 * @param id
	 * @param content
	 * @param shopId
	 * @return
	 */
	boolean replyFirst(Long id, String content, Long shopId);


	/******  domain搬迁方法 ***/
	/**
	 * 保存商品初评
	 *
	 * @param productCommentDTO
	 * @return
	 */
	R<Void> saveProductComment(ProductCommentDTO productCommentDTO);

	/**
	 * 审核商品初评
	 *
	 * @param commentId
	 * @param status
	 * @return
	 */
	boolean auditFirstComment(Long commentId, Integer status);

	/**
	 * 批量审核初评追评
	 *
	 * @param ids
	 * @param addIds
	 * @param status
	 * @return
	 */
	boolean batchAuditComment(List<Long> ids, List<Long> addIds, Integer status);

	/**
	 * 获取评论商品详情
	 *
	 * @param orderItemId
	 * @return
	 */
	R<ProductCommentInfoBO> getProductDetailByComment(Long orderItemId);

	/**
	 * 平台-商品评论统计分析
	 *
	 * @return
	 */
	R<ProductCommentAnalysisBO> getAdminProductCommentAnalysis();

	/**
	 * 商品评论统计分析
	 *
	 * @param productId
	 * @return
	 */
	ProductCommentAnalysisBO getProductProductCommentAnalysis(Long productId);


	/**
	 * 商品评论逻辑删除
	 *
	 * @param id
	 * @return
	 */
	Integer deleteByProductCommentId(Long id);
}
