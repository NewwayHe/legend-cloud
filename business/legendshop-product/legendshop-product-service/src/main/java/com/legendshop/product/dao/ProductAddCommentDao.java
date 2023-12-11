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
import com.legendshop.product.dto.ProductAddCommentDTO;
import com.legendshop.product.entity.ProductAddComment;

import java.util.List;

/**
 * 商品二次评论服务Dao
 *
 * @author legendshop
 */
public interface ProductAddCommentDao extends GenericDao<ProductAddComment, Long> {


	/**
	 * 通过id列表活动待审核状态的评论列表
	 *
	 * @param addIdList
	 * @return
	 */
	List<ProductAddComment> getWaitAuditByIdList(List<Long> addIdList);

	/**
	 * 根据评论Id获取追评
	 *
	 * @param productCommentId
	 * @return
	 */
	ProductAddComment getByProductCommentId(Long productCommentId);

	/**
	 * 根据ID获取商品添加评论信息的方法。
	 *
	 * @param id 商品ID
	 * @return 商品添加评论信息
	 */
	ProductAddCommentDTO getAddComment(Long id);

}
