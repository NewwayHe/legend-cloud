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
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.ShopCategoryBO;
import com.legendshop.product.dao.DraftProductDao;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dao.ShopCategoryDao;
import com.legendshop.product.dto.ShopCategoryDTO;
import com.legendshop.product.dto.ShopCategoryTree;
import com.legendshop.product.entity.DraftProduct;
import com.legendshop.product.entity.Product;
import com.legendshop.product.entity.ShopCategory;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.ShopCategoryService;
import com.legendshop.product.service.convert.ShopCategoryConverter;
import com.legendshop.product.utils.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商家的商品类目服务
 *
 * @author legendshop
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Autowired
	private ShopCategoryConverter converter;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private DraftProductDao draftProductDao;

	@Override
	public ShopCategoryDTO getById(Long id) {
		return converter.to(shopCategoryDao.getById(id));
	}

	@Override
	@CacheEvict(value = CacheConstants.SHOP_CATEGORY_DETAILS, allEntries = true)
	public R deleteById(Long id) {
		// 查询父节点为当前节点的节点
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryByParentId(id);
		if (CollUtil.isNotEmpty(shopCategoryList)) {
			return R.fail("菜单含有下级不能删除");
		}
		if (checkExistProduct(id)) {
			return R.fail("已经存在相关联的商品，不能删除！");
		}

		if (checkExistDraftProduct(id)) {
			return R.fail("已经存在相关联的草稿商品，不能删除！");
		}
		// 删除类目
		return R.ok(shopCategoryDao.deleteById(id));
	}

	@Override
	@CacheEvict(value = CacheConstants.SHOP_CATEGORY_DETAILS, allEntries = true)
	public R save(ShopCategoryDTO shopCategoryDTO) {
		if (shopCategoryDTO.getParentId() == -1) {
			shopCategoryDTO.setGrade(1);
		} else {
			ShopCategory shopCategory = shopCategoryDao.getById(shopCategoryDTO.getParentId());
			if (ObjectUtil.isEmpty(shopCategory)) {
				return R.fail("父分类不存在");
			}
			if (ObjectUtil.isEmpty(shopCategory.getGrade()) || shopCategory.getGrade() == 3) {
				return R.fail("父分类已是三级分类");
			}
			shopCategoryDTO.setGrade(shopCategory.getGrade() + 1);
		}
		shopCategoryDTO.setCreateTime(new Date());
		return R.ok(shopCategoryDao.save(converter.from(shopCategoryDTO)));
	}

	@Override
	@CacheEvict(value = CacheConstants.SHOP_CATEGORY_DETAILS, allEntries = true)
	public R update(ShopCategoryDTO shopCategoryDTO) {
		ShopCategory orgin = shopCategoryDao.getById(shopCategoryDTO.getId());
		if (null == orgin) {
			return R.fail("类目更新失败");
		}
		orgin.setName(shopCategoryDTO.getName());
		orgin.setSeq(shopCategoryDTO.getSeq());
		orgin.setStatus(shopCategoryDTO.getStatus());
		orgin.setUpdateTime(new Date());
		return R.ok(shopCategoryDao.update(orgin));
	}

	@Override
	@Cacheable(value = CacheConstants.SHOP_CATEGORY_DETAILS, key = "#shopId + '_' + #status + '_List'", unless = "#result.isEmpty()")
	public List<ShopCategoryBO> queryByShopId(Long shopId, int status) {
		return shopCategoryDao.queryByShopId(shopId, status);
	}

	@Override
	@CacheEvict(value = CacheConstants.SHOP_CATEGORY_DETAILS, allEntries = true)
	public R updateStatus(Integer status, Long id) {
		return R.ok(shopCategoryDao.updateStatus(status, id));
	}

	@Override
	public List<ShopCategoryTree> filterShopCategory(Set<ShopCategoryBO> all) {
		if (all != null) {
			List<ShopCategoryTree> menuTreeList = all.stream().map(ShopCategoryTree::new)
					.sorted(Comparator.comparingInt(ShopCategoryTree::getSeq)).collect(Collectors.toList());
			Long parent = CommonConstants.CATEGORY_TREE_ROOT_ID;
			return TreeUtil.build(menuTreeList, parent);
		}
		return null;
	}

	private boolean checkExistProduct(Long id) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.setCategoryId(id);
		List<Product> productList = productDao.queryByProperties(new EntityCriterion().eq("shopThirdCatId", id).notEq("delStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.getValue()));
		if (productList != null && productList.size() > 0) {
			return true;
		}
		if (productList == null) {
			productList = new ArrayList<>();
		}
		productList.addAll(productDao.queryByProperties(new EntityCriterion().eq("shopSecondCatId", id).notEq("delStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.getValue())));
		if (productList.size() > 0) {
			return true;
		}
		productList.addAll(productDao.queryByProperties(new EntityCriterion().eq("shopFirstCatId", id).notEq("delStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.getValue())));
		return productList.size() > 0;
	}

	private boolean checkExistDraftProduct(Long id) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.setCategoryId(id);
		List<DraftProduct> productList = draftProductDao.queryByProperties(new EntityCriterion().eq("shopThirdCatId", id));
		if (productList != null && productList.size() > 0) {
			return true;
		}
		if (productList == null) {
			productList = new ArrayList<>();
		}
		productList.addAll(draftProductDao.queryByProperties(new EntityCriterion().eq("shopSecondCatId", id)));
		if (productList.size() > 0) {
			return true;
		}
		productList.addAll(draftProductDao.queryByProperties(new EntityCriterion().eq("shopFirstCatId", id)));
		return productList.size() > 0;
	}
}
