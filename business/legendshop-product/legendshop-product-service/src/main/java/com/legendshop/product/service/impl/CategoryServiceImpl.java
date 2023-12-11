/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.bo.ProductCategoryRelationBO;
import com.legendshop.product.dao.CategoryDao;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dto.CategoryDTO;
import com.legendshop.product.dto.CategoryTree;
import com.legendshop.product.entity.Category;
import com.legendshop.product.entity.Product;
import com.legendshop.product.enums.CategoryStatusEnum;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductTypeEnum;
import com.legendshop.product.query.ProductCategoryRelationQuery;
import com.legendshop.product.service.CategoryService;
import com.legendshop.product.service.ProductPropertyAggCategoryService;
import com.legendshop.product.service.convert.CategoryConverter;
import com.legendshop.product.utils.TreeUtil;
import com.legendshop.search.api.EsIndexApi;
import com.legendshop.search.enmus.IndexTargetMethodEnum;
import com.legendshop.search.enmus.IndexTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品类目实现
 *
 * @author legendshop
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private CategoryConverter categoryConverter;

	@Autowired
	private ProductDao productDao;


	@Autowired
	private ProductPropertyAggCategoryService productPropertyAggCategoryService;

	@Autowired
	AmqpSendMsgUtil amqpSendMsgUtil;

	@Autowired
	EsIndexApi esIndexApi;

	@Override
	public CategoryBO getById(Long id) {
		return categoryDao.getCategoryBOById(id);
	}

	@Override
	public String getCategoryNameById(Long id) {
		return categoryDao.getCategoryNameById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.CATEGORY_DETAILS, allEntries = true)
	public R deleteById(Long id) {
		// 查询父节点为当前节点的节点
		List<CategoryBO> categoryList = categoryDao.queryByParentId(id, 2);
		if (CollUtil.isNotEmpty(categoryList)) {
			return R.fail("菜单含有下级不能删除");
		}
		if (checkExistProduct(id)) {
			return R.fail("已经存在相关联的商品，不能删除！");
		}
		productPropertyAggCategoryService.deleteByCategoryId(id);
		// 删除类目
		return R.ok(categoryDao.deleteById(id));
	}


	@Override
	@CacheEvict(value = CacheConstants.CATEGORY_DETAILS, allEntries = true)
	public Long save(CategoryDTO categoryDTO) {
		//设置类目类型为普通商品类目
		categoryDTO.setType(ProductTypeEnum.ENTITY.getValue());
		categoryDTO.setCreateTime(new Date());

		Long categoryId = categoryDao.save(categoryConverter.from(categoryDTO));
		// 保存参数组关联
		if (categoryDTO.getAggId() != null) {
			productPropertyAggCategoryService.save(categoryDTO.getAggId(), categoryId);
		}

		return categoryId;
	}

	@Override
	@CacheEvict(value = CacheConstants.CATEGORY_DETAILS, allEntries = true)
	public int update(CategoryDTO categoryDTO) {
		Category orginCategory = categoryDao.getById(categoryDTO.getId());
		if (null != orginCategory) {
			BeanUtil.copyProperties(categoryDTO, orginCategory, new CopyOptions().setIgnoreNullValue(true));
			orginCategory.setUpdateTime(new Date());
			// 保存参数组关联
			if (categoryDTO.getAggId() != null) {
				productPropertyAggCategoryService.save(categoryDTO.getAggId(), categoryDTO.getId());
			} else {
				//没有传值则删除
				productPropertyAggCategoryService.deleteByCategoryId(categoryDTO.getId());
			}
			return categoryDao.update(orginCategory);
		}
		return 0;
	}

	@Override
	public List<CategoryBO> getCategoryByIds(List<Long> catIdList) {
		return categoryConverter.convert2BoList(categoryDao.queryAllByIds(catIdList));
	}


	/**
	 * 注意下线的类目也要加入到商品是否有效的判断当中 newway TODO
	 *
	 * @param status
	 * @param id
	 * @param includeProduct 类目的上下架动作是否影响到类目中的商品
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.CATEGORY_DETAILS, allEntries = true)
	public R updateStatus(int status, Long id, Boolean includeProduct) {

		//1. 查询父节点为当前节点的节点
		List<CategoryBO> categoryList = categoryDao.queryByParentId(id, 2);
		if (ObjectUtil.isNotNull(categoryList)) {
			categoryList.forEach(categoryBO -> categoryDao.updateStatus(status, categoryBO.getId()));
		}

		//2.页面上要提醒用户下线类目会同时下线商品, 不用改变商品的状态，只需要加入商品类目的状态变更的判断。 还没有对接前端 newway
		if (status == CategoryStatusEnum.CATEGORY_OFFLINE.getValue() && Boolean.TRUE.equals(includeProduct)) {
			//下线ES的商品
			productDao.updateStatus(status, categoryDao.queryProductIdsListById(id));
			//删除索引
			esIndexApi.reIndex(IndexTypeEnum.PRODUCT_INDEX.name(), IndexTargetMethodEnum.DELETE.getValue(), null, id.toString());

		}
		if (status == CategoryStatusEnum.CATEGORY_ONLINE.getValue() && Boolean.TRUE.equals(includeProduct)) {
			//上线分类下的商品
			productDao.updateStatus(status, categoryDao.queryProductIdsListById(id));
		}

		return R.ok(categoryDao.updateStatus(status, id));
	}

	@Override
	@Cacheable(value = CacheConstants.CATEGORY_DETAILS, key = "#parentId  +'_'+#status+ '_category'", unless = "#result.isEmpty()")
	public List<CategoryBO> queryByParentId(Long parentId, int status) {
		return categoryDao.queryByParentId(parentId, status);
	}

	@Override
	@Cacheable(value = CacheConstants.CATEGORY_DETAILS, key = "#parentId  + '_briefAllOnline'", unless = "#result.isEmpty()")
	public List<CategoryBO> queryAllOnline() {
		return categoryDao.queryAllOnline();
	}

	@Override
	@Cacheable(value = CacheConstants.CATEGORY_DETAILS, key = "'queryAll'", unless = "#result.isEmpty()")
	public List<CategoryBO> queryAll() {
		return categoryConverter.convert2BoList(categoryDao.queryAll());
	}

	@Override
	public List<CategoryTree> filterCategory(Set<CategoryBO> all, Long parentId) {
		if (all != null) {
			List<CategoryTree> menuTreeList = all.stream().map(CategoryTree::new)
					.sorted(Comparator.comparingInt(CategoryTree::getSeq)).collect(Collectors.toList());
			Long parent = parentId == null ? CommonConstants.CATEGORY_TREE_ROOT_ID : parentId;
			return TreeUtil.build(menuTreeList, parent);
		}
		return null;
	}

	@Override
	public R<String> getDecorateCategoryList() {

		// 获取符合条件的类目
		Set<CategoryBO> all = new HashSet<>();
		all.addAll(this.queryAllOnline());
		List<CategoryTree> categoryTrees = this.filterCategory(all, null);

		if (CollUtil.isEmpty(categoryTrees)) {
			return R.fail("暂无分类信息");
		}
		return R.ok(JSONUtil.toJsonStr(categoryTrees));
	}

	@Override
	public R<List<CategoryTree>> getTreeById(Long productCategoryRoot) {
		// 获取符合条件的分类
		Set<CategoryBO> all = new HashSet<>();
		all.addAll(this.queryAllOnline());
		return R.ok(this.filterCategory(all, productCategoryRoot));
	}

	private boolean checkExistProduct(Long categoryId) {
		List<Product> productList = productDao.queryByProperties(new EntityCriterion().eq("globalThirdCatId", categoryId).notEq("delStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.getValue()));
		if (productList != null && productList.size() > 0) {
			return true;
		}
		productList.addAll(productDao.queryByProperties(new EntityCriterion().eq("globalSecondCatId", categoryId).notEq("delStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.getValue())));
		if (productList != null && productList.size() > 0) {
			return true;
		}
		productList.addAll(productDao.queryByProperties(new EntityCriterion().eq("globalFirstCatId", categoryId).notEq("delStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.getValue())));
		if (productList != null && productList.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<CategoryBO> queryByParentIdAndName(Long parentId, String name, int status) {
		return categoryConverter.convert2BoList(categoryDao.queryByParentIdAndName(parentId, name, status));
	}

	@Override
	public PageSupport<ProductCategoryRelationBO> queryProductList(ProductCategoryRelationQuery relationQuery) {
		return categoryDao.queryProductList(relationQuery);
	}

	@Override
	public List<CategoryBO> queryAllOnlineByProductPropertyAggId(Long productPropertyAggId) {
		List<CategoryBO> categoryList = categoryDao.queryAllOnlineByProductPropertyAggId();
		if (CollUtil.isNotEmpty(categoryList)) {
			for (CategoryBO categoryBO : categoryList) {
				categoryBO.setOptional(ObjectUtil.isNotEmpty(categoryBO.getAggId()));
				if (ObjectUtil.isNotEmpty(productPropertyAggId) && ObjectUtil.isNotEmpty(categoryBO.getAggId())) {
					if (categoryBO.getAggId().equals(productPropertyAggId)) {
						categoryBO.setOptional(false);
					}
				}
			}
		}
		return categoryList;
	}

	@Override
	public R<Boolean> updateStatusBefore(Long id) {
		List<CategoryBO> categoryBOS = categoryDao.queryByParentId(id, 2);
		return R.ok(ObjectUtil.isNotEmpty(categoryBOS));
	}

	@Override
	public Category queryId(Long categoryId) {

		return categoryDao.getById(categoryId);
	}
}
