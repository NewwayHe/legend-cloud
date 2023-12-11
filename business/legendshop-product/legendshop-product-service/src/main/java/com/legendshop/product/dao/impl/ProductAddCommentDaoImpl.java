/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.product.dao.ProductAddCommentDao;
import com.legendshop.product.dto.ProductAddCommentDTO;
import com.legendshop.product.entity.ProductAddComment;
import com.legendshop.product.enums.ProductCommStatusEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品二次评论服务Dao
 *
 * @author legendshop
 */
@Repository
public class ProductAddCommentDaoImpl extends GenericDaoImpl<ProductAddComment, Long> implements ProductAddCommentDao {


	@Override
	public List<ProductAddComment> getWaitAuditByIdList(List<Long> addIdList) {
		return queryByProperties(new EntityCriterion().eq("status", ProductCommStatusEnum.WAIT_AUDIT.value()).in("id", addIdList));
	}

	@Override
	public ProductAddComment getByProductCommentId(Long productCommentId) {
		return getByProperties(new EntityCriterion().eq("productCommentId", productCommentId));
	}

	@Override
	public ProductAddCommentDTO getAddComment(Long id) {
		String sql = "select lv.* from ls_distribution ld left join ls_distribution_level lv on lv.id = ld.level_id where ld.user_id = ?";
		return get(sql, ProductAddCommentDTO.class, id);
	}

}
