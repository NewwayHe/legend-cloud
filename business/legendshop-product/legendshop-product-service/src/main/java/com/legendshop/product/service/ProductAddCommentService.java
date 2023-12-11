/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;


import com.legendshop.common.core.service.BaseService;
import com.legendshop.product.dto.ProductAddCommentDTO;

/**
 * 商品二次评论服务
 *
 * @author legendshop
 */
public interface ProductAddCommentService extends BaseService<ProductAddCommentDTO> {


	/**
	 * 追加商品评论
	 */
	boolean addProdComm(ProductAddCommentDTO productAddCommentDTO);

	/**
	 * 审核追加商品评论
	 */
	boolean auditAddComment(Long addId, Integer status);

	/**
	 * 回复追评
	 *
	 * @param addId
	 * @param content
	 * @param shopId
	 * @return
	 */
	boolean replyAdd(Long addId, String content, Long shopId);

	ProductAddCommentDTO getProductCommentById(Long id);
}
