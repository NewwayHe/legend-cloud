/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.criterion.Restrictions;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.alibaba.excel.util.CollectionUtils;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.bo.HotSellProductBO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.ProductPlatformBO;
import com.legendshop.product.dao.ProductDao;
import com.legendshop.product.dao.SkuDao;
import com.legendshop.product.dto.InventoryOperationsDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.entity.Product;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductOnSaleWayEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.convert.ProductConverter;
import com.legendshop.shop.dto.ShopDecorateProductQuery;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品Dao.
 *
 * @author legendshop
 */
@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product, Long> implements ProductDao {

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private ProductConverter productConverter;

	@Override
	public synchronized void updateReviewScoresAndComments(Integer scores, Integer comments, Long productId) {
		update("UPDATE ls_product SET review_scores = review_scores + ?, comments = comments + ? WHERE id = ?", scores, comments, productId);
	}

	@Override
	public int updateStatus(Integer status, List<Long> productIds) {
		if (CollUtil.isEmpty(productIds)) {
			return 0;
		}
		List<Object> arr = new ArrayList<>();
		arr.add(status.longValue());
		StringBuffer sb;

		//修改商品状态
		//商品上架后，商品的上架方式修改为立即上架
		if (ProductStatusEnum.codeValue(status) == ProductStatusEnum.PROD_ONLINE) {
			sb = new StringBuffer("update ls_product set status = ?,update_time=NOW(),on_sale_way=");
			sb.append(ProductOnSaleWayEnum.ONSALE.getValue());
			sb.append(" where id in( ");
		} else {
			sb = new StringBuffer("update ls_product set status = ?,update_time=NOW() where id in( ");
		}
		for (Long id : productIds) {
			arr.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return update(sb.toString(), arr.toArray());
	}


	/**
	 * 更新商品的库存 = 所有sku库存总和
	 */
	@Override
	public void updateProdStocks(Long productId) {
		this.update("UPDATE ls_product SET stocks = (SELECT SUM(stocks) FROM ls_sku WHERE product_id =?) WHERE id=?", productId, productId);
	}

	/**
	 * 更新商品的库存 = 所有sku库存总和
	 */
	@Override
	public void batchUpdateProdStocks(List<Long> productIdList) {
		StringBuilder sb = new StringBuilder("UPDATE  ls_product a SET a.stocks = (SELECT SUM(stocks) FROM ls_sku sku where sku.product_id=a.id)  WHERE a.id in(");
		productIdList.forEach(productId -> {
			sb.append(productId).append(",");
		});
		sb.setLength(sb.length() - 1);
		sb.append(")");
		this.update(sb.toString());
	}

	/**
	 * 更新商品的库存 = 所有sku库存总和
	 */
	@Override
	public void updateProductAllStocks(Long productId) {
		this.update("UPDATE ls_product SET stocks = (SELECT SUM(stocks) FROM ls_sku WHERE product_id =?), actual_stocks = (SELECT SUM(actual_stocks) FROM ls_sku WHERE product_id = ?) WHERE id=?", productId, productId, productId);
	}


	@Override
	public List<Product> getRencFavorProduct(Integer amount, Long userId) {
		return queryLimit("select lp.* from ls_product lp, ls_favorite lf where lp.id=lf.product_id and lp.status=1 and lp.op_status = 1 and lp.del_status = 1 and lf.user_id=? order by lf.addtime desc", Product.class, 0, amount, userId);
	}

	@Override
	public PageSupport<Product> queryProductListPage(ProductQuery productQuery) {

		SimpleSqlQuery criteriaQuery = new SimpleSqlQuery(Product.class, productQuery.getPageSize(), productQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("shopFirstCatId", productQuery.getShopFirstCatId());
		map.put("shopSecondCatId", productQuery.getShopSecondCatId());
		map.put("shopThirdCatId", productQuery.getShopThirdCatId());
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.put("shopId", productQuery.getShopId());
		map.put("status", productQuery.getStatus());
		map.put("opStatus", productQuery.getOpStatus());
		map.put("delStatus", productQuery.getDelStatus());

		map.put("groupFlag", productQuery.getGroupFlag());
		map.put("prodType", productQuery.getProdType());
		map.put("categoryId", productQuery.getCategoryId());
		map.put("brandId", productQuery.getBrandId());

		map.put("supportDist", productQuery.getSupportDist());

		if (StrUtil.isNotBlank(productQuery.getOrder())) {
			String[] orders = productQuery.getOrder().split(",");
			if (orders.length == 2) {
				StringBuffer sortBy = new StringBuffer(" order by p.");
				if (StrUtil.equals("asc", orders[1])) {
					sortBy.append(orders[0]);
					sortBy.append(",p.updateTime desc");
				} else {
					sortBy.append(orders[0]);
					sortBy.append(" desc");
				}
				map.put("sortBy", sortBy.toString());
			}
		}
		criteriaQuery.setSqlAndParameter("Product.queryProductListPage", map);
		return querySimplePage(criteriaQuery);
	}

	/**
	 * 更新商品的实际库存 = 所有sku实际库存总和
	 */
	@Override
	public void updateProdActualStocks(Long productId) {
		this.update("UPDATE ls_product SET actual_stocks = (SELECT SUM(actual_stocks) FROM ls_sku WHERE product_id =?) WHERE id=?", productId, productId);
	}


	@Override
	public PageSupport<Product> queryProductListPage(ShopDecorateProductQuery shopDecorateProductQuery) {
		CriteriaQuery cq = new CriteriaQuery(Product.class, shopDecorateProductQuery);
		cq.like("name", shopDecorateProductQuery.getProductName(), MatchMode.ANYWHERE);
		cq.eq("shopId", shopDecorateProductQuery.getShopId());
		cq.eq("status", ProductStatusEnum.PROD_ONLINE.getValue());
		cq.eq("opStatus", OpStatusEnum.PASS.getValue());
		cq.eq("delStatus", ProductDelStatusEnum.PROD_NORMAL.getValue());
		if (StrUtil.isNotBlank(shopDecorateProductQuery.getOrder())) {
			String[] orders = shopDecorateProductQuery.getOrder().split(",");
			if (orders.length == 2) {
				if (StrUtil.equals("asc", orders[1])) {
					cq.addAscOrder(orders[0]);
					cq.addDescOrder("updateTime");
				} else {
					cq.addDescOrder(orders[0]);
				}
			}
		} else {
			cq.addDescOrder("comments");
			cq.addDescOrder("buys");
			cq.addDescOrder("views");
		}

		if (ObjectUtil.isNotEmpty(shopDecorateProductQuery.getCategoryId())) {
			cq.add(cq.or(
					Restrictions.eq("shopFirstCatId", shopDecorateProductQuery.getCategoryId()),
					Restrictions.eq("shopSecondCatId", shopDecorateProductQuery.getCategoryId()),
					Restrictions.eq("shopThirdCatId", shopDecorateProductQuery.getCategoryId())));
		}
		return queryPage(cq);
	}

	@Override
	public PageSupport<Product> queryProductOnline(ProductQuery query) {
		SimpleSqlQuery criteriaQuery = new SimpleSqlQuery(Product.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("preSellFlag", query.getPreSellFlag());
		map.put("status", ProductStatusEnum.PROD_ONLINE.getValue());
		map.put("shopStatus", ShopDetailStatusEnum.ONLINE.getStatus());
		map.put("opStatus", OpStatusEnum.PASS.getValue());
		map.put("delStatus", ProductDelStatusEnum.PROD_NORMAL.getValue());
		map.put("shopId", query.getShopId());
		map.like("productName", query.getName(), MatchMode.ANYWHERE);
		if (ObjectUtil.isNotNull(query.getIntegralProductId())) {
			map.put("integralProductId", query.getIntegralProductId());
		}
		criteriaQuery.setSqlAndParameter("Product.queryProductOnline", map);
		return querySimplePage(criteriaQuery);
	}

	@Override
	public PageSupport<ProductDTO> queryProductOnlineEs(ProductQuery query) {
		SimpleSqlQuery criteriaQuery = new SimpleSqlQuery(ProductDTO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("preSellFlag", query.getPreSellFlag());
		map.put("shopStatus", ShopDetailStatusEnum.ONLINE.getStatus());
		map.put("shopId", query.getShopId());
		map.like("productName", query.getName(), MatchMode.ANYWHERE);
		if (ObjectUtil.isNotNull(query.getIntegralProductId())) {
			map.put("integralProductId", query.getIntegralProductId());
		}
		criteriaQuery.setSqlAndParameter("Product.queryProductOnlineEs", map);
		return querySimplePage(criteriaQuery);
	}

	@Override
	public int updateOpStatus(List<Long> idList, Integer opStatus) {
		List<Long> arr = new ArrayList<>();
		arr.add(opStatus.longValue());
		StringBuffer sb = new StringBuffer("UPDATE ls_product p set p.op_status=?,update_time= NOW() where id in( ");
		for (Long id : idList) {
			arr.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return update(sb.toString(), arr.toArray());
	}

	/**
	 * 商品分页查询
	 */
	@Override
	public PageSupport<ProductBO> getProductPage(ProductQuery productQuery) {
		QueryMap map = new QueryMap();
		SimpleSqlQuery hql = new SimpleSqlQuery(ProductBO.class, productQuery.getPageSize(), productQuery.getCurPage());

		map.put("categoryId1", productQuery.getCategoryId());
		map.put("categoryId2", productQuery.getCategoryId());
		map.put("categoryId3", productQuery.getCategoryId());
		map.put("firstCategoryId", productQuery.getShopFirstCatId());
		map.put("secondCategoryId", productQuery.getShopSecondCatId());
		map.put("thirdCategoryId", productQuery.getShopThirdCatId());
		map.put("brandId", productQuery.getBrandId());
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.like("siteName", productQuery.getSiteName(), MatchMode.ANYWHERE);

		//1. 没有被删除的商品
		if (ObjectUtil.isEmpty(productQuery.getDelStatus()) || ProductDelStatusEnum.PROD_NORMAL.value().equals(productQuery.getDelStatus())) {
			map.put("delStatus", ProductDelStatusEnum.PROD_NORMAL.value());
			//2. 后台审核成功的商品
			if (ObjectUtil.isEmpty(productQuery.getOpStatus()) || OpStatusEnum.PASS.getValue().equals(productQuery.getOpStatus())) {
				map.put("opStatus", OpStatusEnum.PASS.getValue());
				//3. 正常商品
				if (!ProductStatusEnum.PROD_ALL.value().equals(productQuery.getStatus())) {
					map.put("status", productQuery.getStatus());
				}

			} else {
				//4. 审核异常的商品
				if (OpStatusEnum.PROD_ILLEGAL_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and p.op_status in (2, 3)");
				} else if (OpStatusEnum.PROD_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and p.op_status in (-1,0,1)");
				} else {
					map.put("opStatus", productQuery.getOpStatus());
				}
			}
		} else {
			map.put("delStatus", productQuery.getDelStatus());
		}
		/*如果商家查询商品列表，永久删除的商品不可被搜索出来*/
		if (productQuery.getShopId() != null) {
			map.put("illegalDelStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.value());
		}

		map.put("startDate", productQuery.getStartDate());
		map.put("endDate", productQuery.getEndDate());
		map.put("shopId", productQuery.getShopId());
		map.put("productIdList", productQuery.getProductIdList());
		map.put("skuIdList", productQuery.getSkuIdList());
		map.put("preSellFlag", productQuery.getPreSellFlag());
		map.put("payPctType", productQuery.getPayPctType());

		if (Objects.equals(productQuery.getProp(), "price")) {
			productQuery.setProp("min_price");
		}
		OrderBy orderBy = new OrderBy(productQuery.getProp(), productQuery.getOrder());
		//需要和前端确定排序的字段 销售价，购买数，库存, 创建时间
		map.addOrder(orderBy, Arrays.asList("min_price", "buys", "stocks", "create_time"), "p");
		hql.setSqlAndParameter("Product.getProdList", map);
		return querySimplePage(hql);
	}

	@Override
	public List<ProductBO> getProductList(ProductQuery productQuery) {
		QueryMap map = new QueryMap();
		map.put("categoryId1", productQuery.getCategoryId());
		map.put("categoryId2", productQuery.getCategoryId());
		map.put("categoryId3", productQuery.getCategoryId());
		map.put("firstCategoryId", productQuery.getShopFirstCatId());
		map.put("secondCategoryId", productQuery.getShopSecondCatId());
		map.put("thirdCategoryId", productQuery.getShopThirdCatId());
		map.put("brandId", productQuery.getBrandId());
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.like("siteName", productQuery.getSiteName(), MatchMode.ANYWHERE);

		//1. 没有被删除的商品
		if (ObjectUtil.isEmpty(productQuery.getDelStatus()) || ProductDelStatusEnum.PROD_NORMAL.value().equals(productQuery.getDelStatus())) {
			map.put("delStatus", ProductDelStatusEnum.PROD_NORMAL.value());
			//2. 后台审核成功的商品
			if (ObjectUtil.isEmpty(productQuery.getOpStatus()) || OpStatusEnum.PASS.getValue().equals(productQuery.getOpStatus())) {
				//3. 所有商品（包括审核通过的上下线商品，以及未发布的草稿商品）
				if (ProductStatusEnum.PROD_ALL.value().equals(productQuery.getStatus())) {
					map.put("opStatusExpand", " and ((p.op_status = 1 and p.status >= 0) or (p.op_status = 0 and p.status = -10))");
				} else if (ProductStatusEnum.UNPUBLISHED.value().equals(productQuery.getStatus())) {
					// 草稿商品
					map.put("opStatus", OpStatusEnum.WAIT.getValue());
					map.put("opStatusExpand", " and p.status = -10 ");
				} else {
					// 审核通过的上下线商品
					map.put("opStatus", OpStatusEnum.PASS.getValue());
					map.put("status", productQuery.getStatus());
				}

			} else {
				//4. 审核异常的商品
				if (OpStatusEnum.PROD_ILLEGAL_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and p.op_status in (2, 3)");
				} else if (OpStatusEnum.PROD_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and ((p.op_status in (-1,0,1) and p.status > -10) or ldp.status > -10) ");
				} else {
					if (OpStatusEnum.WAIT.getValue().equals(productQuery.getOpStatus())) {
						map.put("opStatusExpand", " and ((p.op_status = 0 and p.status > -10) or ldp.status = 0) ");
					} else if (OpStatusEnum.DENY.getValue().equals(productQuery.getOpStatus())) {
						map.put("opStatusExpand", " and ((p.op_status = -1 and p.status > -10) or ldp.status = -1) ");
					}
				}
			}
		} else {
			map.put("delStatus", productQuery.getDelStatus());
		}
		/*如果商家查询商品列表，永久删除的商品不可被搜索出来*/
		if (productQuery.getShopId() != null) {
			map.put("illegalDelStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.value());
		}

		map.put("shopId", productQuery.getShopId());
		map.put("productIdList", productQuery.getProductIdList());
		map.put("skuIdList", productQuery.getSkuIdList());
		map.put("preSellFlag", productQuery.getPreSellFlag());
		map.put("payPctType", productQuery.getPayPctType());

		if (Objects.equals(productQuery.getProp(), "price")) {
			productQuery.setProp("min_price");
		}
		OrderBy orderBy = new OrderBy(productQuery.getProp(), productQuery.getOrder());
		//需要和前端确定排序的字段 销售价，购买数，库存, 创建时间
		map.addOrder(orderBy, Arrays.asList("min_price", "buys", "stocks", "create_time"), "p");
		SQLOperation operation = this.getSQLAndParams("Product.getProductList", map);
		return query(operation.getSql(), ProductBO.class, operation.getParams());
	}

	@Override
	public List<ProductPlatformBO> getProductPlatformList(ProductQuery productQuery) {
		QueryMap map = new QueryMap();
		map.put("categoryId1", productQuery.getCategoryId());
		map.put("categoryId2", productQuery.getCategoryId());
		map.put("categoryId3", productQuery.getCategoryId());
		map.put("firstCategoryId", productQuery.getShopFirstCatId());
		map.put("secondCategoryId", productQuery.getShopSecondCatId());
		map.put("thirdCategoryId", productQuery.getShopThirdCatId());
		map.put("brandId", productQuery.getBrandId());
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.like("siteName", productQuery.getSiteName(), MatchMode.ANYWHERE);

		//1. 没有被删除的商品
		if (ObjectUtil.isEmpty(productQuery.getDelStatus()) || ProductDelStatusEnum.PROD_NORMAL.value().equals(productQuery.getDelStatus())) {
			map.put("delStatus", ProductDelStatusEnum.PROD_NORMAL.value());
			//2. 后台审核成功的商品
			if (ObjectUtil.isEmpty(productQuery.getOpStatus()) || OpStatusEnum.PASS.getValue().equals(productQuery.getOpStatus())) {
				map.put("opStatus", OpStatusEnum.PASS.getValue());
				//3. 正常商品
				if (!ProductStatusEnum.PROD_ALL.value().equals(productQuery.getStatus())) {
					map.put("status", productQuery.getStatus());
				}

			} else {
				//4. 审核异常的商品
				if (OpStatusEnum.PROD_ILLEGAL_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and p.op_status in (2, 3)");
				} else if (OpStatusEnum.PROD_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and p.op_status in (-1,0,1)");
				} else {
					map.put("opStatus", productQuery.getOpStatus());
				}
			}
		} else {
			map.put("delStatus", productQuery.getDelStatus());
		}
		/*如果商家查询商品列表，永久删除的商品不可被搜索出来*/
		if (productQuery.getShopId() != null) {
			map.put("illegalDelStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.value());
		}

		map.put("shopId", productQuery.getShopId());
		map.put("productIdList", productQuery.getProductIdList());
		map.put("skuIdList", productQuery.getSkuIdList());

		OrderBy orderBy = new OrderBy(productQuery.getProp(), productQuery.getOrder());
		//需要和前端确定排序的字段 销售价，购买数，库存, 创建时间
		map.addOrder(orderBy, Arrays.asList("min_price", "buys", "stocks", "create_time"), "p");
		SQLOperation operation = this.getSQLAndParams("Product.getProdList", map);
		return query(operation.getSql(), ProductPlatformBO.class, operation.getParams());
	}



	/*@Override
	public List<ProductBO> getNewProductList(ProductQuery productQuery) {
		QueryMap map = new QueryMap();
		map.put("categoryId1", productQuery.getCategoryId());
		map.put("categoryId2", productQuery.getCategoryId());
		map.put("categoryId3", productQuery.getCategoryId());
		map.put("firstCategoryId", productQuery.getShopFirstCatId());
		map.put("secondCategoryId", productQuery.getShopSecondCatId());
		map.put("thirdCategoryId", productQuery.getShopThirdCatId());
		map.put("brandId", productQuery.getBrandId());
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.like("siteName", productQuery.getSiteName(), MatchMode.ANYWHERE);

		//1. 没有被删除的商品
		if (ObjectUtil.isEmpty(productQuery.getDelStatus()) || ProductDelStatusEnum.PROD_NORMAL.value().equals(productQuery.getDelStatus())) {
			map.put("delStatus", ProductDelStatusEnum.PROD_NORMAL.value());

				//2. 正常商品
				if (!ProductStatusEnum.PROD_ALL.value().equals(productQuery.getStatus())) {
					map.put("status", productQuery.getStatus());
			} else {
				//4. 审核异常的商品
				if (OpStatusEnum.PROD_ILLEGAL_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and p.op_status in (2, 3)");
				} else if (OpStatusEnum.PROD_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and p.op_status in (-1,0,1)");
				} else {
					map.put("opStatus", productQuery.getOpStatus());
				}
			}
		} else {
			map.put("delStatus", productQuery.getDelStatus());
		}
		*//*如果商家查询商品列表，永久删除的商品不可被搜索出来*//*
		if (productQuery.getShopId() != null) {
			map.put("illegalDelStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.value());
		}

		map.put("shopId", productQuery.getShopId());
		map.put("productIdList", productQuery.getProductIdList());
		map.put("skuIdList", productQuery.getSkuIdList());

		OrderBy orderBy = new OrderBy(productQuery.getProp(), productQuery.getOrder());
		map.addOrder(orderBy, Arrays.asList("min_price", "buys", "stocks", "create_time"), "p");//需要和前端确定排序的字段 销售价，购买数，库存, 创建时间
		SQLOperation operation = this.getSQLAndParams("Product.getProdList", map);
		return query(operation.getSql(), ProductBO.class, operation.getParams());
	}*/


	@Override
	public List<HotSellProductBO> queryHotSellProductByShopId(Long shopId) {
		String sql = "SELECT p.id as product_id,p.name as product_name,p.status,p.shop_id,p.buys,p.pic,MAX(k.price) as maxPrice,MIN(k.price) as minPrice " +
				"FROM ls_product p INNER JOIN ls_sku k ON p.id = k.product_id " +
				"WHERE p.status=1 and p.shop_id = ? GROUP BY p.id ORDER BY p.buys DESC limit 3";

		return query(sql, HotSellProductBO.class, shopId);
	}


	@Override
	public ProductBO getProductBO(Long productId) {
		return get(getSQL("Product.getProductBO"), ProductBO.class, productId);
	}

	@Override
	public List<ProductBO> getProductBO(List<Long> productIds) {
		QueryMap map = new QueryMap();
		map.put("productIdList", productIds);
		SQLOperation operation = this.getSQLAndParams("Product.getProdList", map);
		return query(operation.getSql(), ProductBO.class, operation.getParams());
	}

	@Override
	public Integer batchUpdateStock(List<Long> ids, Integer stock) {
		List<Long> arr = new ArrayList<>();
		arr.add(stock.longValue());
		arr.add(stock.longValue());
		StringBuffer sb = new StringBuffer("update ls_sku  k set k.actual_stocks=(k.actual_stocks-(k.stocks- ? )),k.stocks = ? where id in( ");
		for (Long id : ids) {
			arr.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return update(sb.toString(), arr.toArray());
	}

	@Override
	public List<Long> batchCheckStock(List<Long> ids, Integer stock) {

		List<Long> arr = new ArrayList<>();
		arr.add(stock.longValue());
		StringBuffer sb = new StringBuffer("select k.id from  ls_sku  k where (k.actual_stocks-(k.stocks- ? ))<0 and id in( ");
		for (Long id : ids) {
			arr.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return query(sb.toString(), Long.class, arr.toArray());
	}

	@Override
	public Long getCountByTransId(Long transId) {
		// 商品永久删除后运费模板
		String sql = "SELECT count(*) FROM ls_product WHERE trans_id = ? and del_status != ?";
		return getLongResult(sql, transId, ProductDelStatusEnum.PROD_SHOP_DELETE.getValue());
	}

	@Override
	public List<Product> getProductListByIds(List<Long> productIds, Integer pageSize, String orderBy) {
		EntityCriterion criterion = new EntityCriterion();
		criterion.in("id", productIds);
		criterion.addDescOrder(StrUtil.toCamelCase(orderBy));
		return queryByProperties(criterion, 0, pageSize);
	}

	@Override
	public ProductDTO getOnlineById(Long productId) {
		return get("SELECT p.*,ldp.ratio FROM ls_product p INNER JOIN ls_shop_detail s ON s.id=p.shop_id  LEFT JOIN ls_distribution_prod ldp ON p.id=ldp.product_id  WHERE  p.status=? and s.status=? and p.id=? and p.op_status = ? and p.del_status = ?", ProductDTO.class, ProductStatusEnum.PROD_ONLINE.getValue(), ShopDetailStatusEnum.ONLINE.getStatus(), productId, OpStatusEnum.PASS.getValue(), ProductDelStatusEnum.PROD_NORMAL.value());
	}

	@Override
	public List<ProductDTO> calculateBuys(List<String> numberList) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("SELECT oi.product_id as id ,o.shop_id,SUM(oi.basket_count) AS buys FROM ls_order_item oi LEFT JOIN ls_order o on o.order_number = oi.order_number WHERE oi.order_number IN (");
		for (String number : numberList) {
			buffer.append("?,");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(") GROUP BY oi.product_id");
		return query(buffer.toString(), ProductDTO.class, numberList.toArray());
	}

	@Override
	public Boolean updateBuys(Long id, Integer buys) {
		return update("update ls_product set buys = (buys + ?) where 1=1 and id = ?", buys, id) > 0;
	}

	@Override
	public void updateProductViews(Long productId) {
		update("update ls_product set views = views+1 where id = ?", productId);
	}

	@Override
	public void updateProductDeliveryType(Integer deliveryType, Long productId) {
		update("update ls_product set delivery_type = ?  where id = ?", deliveryType, productId);
	}

	@Override
	public List<Long> queryIdByShopId(List<Long> shopIds) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT id from ls_product p where p.op_status=")
				.append(OpStatusEnum.PASS.getValue())
				.append(" and p.status=")
				.append(ProductStatusEnum.PROD_ONLINE.getValue())
				.append(" and p.del_status = ")
				.append(ProductDelStatusEnum.PROD_NORMAL.value())
				.append(" and shop_id in(");
		for (Long shopId : shopIds) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return query(sb.toString(), Long.class, shopIds.toArray());
	}

	@Override
	public Long getProductCountByShopId(Long shopId, OpStatusEnum opStatusEnum) {
		return getCount(new EntityCriterion().eq("shopId", shopId).eq("opStatus", opStatusEnum.getValue()).eq("delStatus", ProductDelStatusEnum.PROD_NORMAL.value()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateDelStatus(Integer delStatus, List<Product> productList) {
		if (CollUtil.isNotEmpty(productList)) {
			if (ProductDelStatusEnum.PROD_ADMIN_DELETE.getValue().equals(delStatus)) {
				skuDao.deleteByProdId(productList.stream().map(Product::getId).collect(Collectors.toList()));
				this.delete(productList);
			} else {
				// 更新商品状态
				productList.forEach(product -> {
					product.setDelStatus(delStatus);
				});
				this.update(productList);
			}
		}
	}

	/**
	 * 通过商品ID，更新商品库存， 当商品库存扣减为负数时，不允许更新。
	 *
	 * @param id
	 * @param basketCount
	 * @return
	 */
	@Override
	public Integer updateStocksByProductId(Long id, Integer basketCount, Long shopId) {
		return update("UPDATE ls_product SET stocks=(stocks + ?) WHERE id = ? AND (stocks + ?) >=0 AND shop_id =? ", basketCount, id, basketCount, shopId);
	}

	@Override
	public boolean reduceStock(Long productId, Integer stock) {
		String sql = "update ls_product set stocks=stocks-? where id=?";
		int update = update(sql, stock, productId);
		return update > 0;
	}

	@Override
	public boolean addBackStock(Long productId, Integer stock) {
		String sql = "update ls_product set stocks=stocks+? where id=?";
		int update = update(sql, stock, productId);
		return update > 0;
	}

	@Override
	public Integer getNewProduct(Date startDate, Date endDate) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("Product.queryNewProduct", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public Integer getNewOrder(Date startDate, Date endDate) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("Product.queryNewOrder", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public Integer getNewRefundOrder(Date startDate, Date endDate) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("Product.queryNewRefundOrder", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public Integer getNewAccusationProduct(Date startDate, Date endDate) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("Product.queryNewAccusationProduct", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public Integer getNewReferProduct(Date startDate, Date endDate) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("Product.queryNewReferProduct", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public PageSupport<ProductBO> getPage(ProductQuery productQuery) {
		QueryMap map = new QueryMap();
		SimpleSqlQuery hql = new SimpleSqlQuery(ProductBO.class, productQuery.getPageSize(), productQuery.getCurPage());

		map.put("categoryId1", productQuery.getCategoryId());
		map.put("categoryId2", productQuery.getCategoryId());
		map.put("categoryId3", productQuery.getCategoryId());
		map.put("firstCategoryId", productQuery.getShopFirstCatId());
		map.put("secondCategoryId", productQuery.getShopSecondCatId());
		map.put("thirdCategoryId", productQuery.getShopThirdCatId());
		map.put("brandId", productQuery.getBrandId());
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.like("siteName", productQuery.getSiteName(), MatchMode.ANYWHERE);

		//1. 没有被删除的商品
		if (ObjectUtil.isEmpty(productQuery.getDelStatus()) || ProductDelStatusEnum.PROD_NORMAL.value().equals(productQuery.getDelStatus())) {
			map.put("delStatus", ProductDelStatusEnum.PROD_NORMAL.value());
			//2. 后台审核成功的商品
			if (ObjectUtil.isEmpty(productQuery.getOpStatus()) || OpStatusEnum.PASS.getValue().equals(productQuery.getOpStatus())) {
				//3. 所有商品（包括审核通过的上下线商品，以及未发布的草稿商品）
				if (ProductStatusEnum.PROD_ALL.value().equals(productQuery.getStatus())) {
					map.put("opStatusExpand", " and ((p.op_status = 1 and p.status >= 0) or (p.op_status = 0 and p.status = -10))");
				} else if (ProductStatusEnum.UNPUBLISHED.value().equals(productQuery.getStatus())) {
					// 草稿商品
					map.put("opStatus", OpStatusEnum.WAIT.getValue());
					map.put("opStatusExpand", " and p.status = -10 ");
				} else {
					// 审核通过的上下线商品
					map.put("opStatus", OpStatusEnum.PASS.getValue());
					map.put("status", productQuery.getStatus());
				}

			} else {
				//4. 审核异常的商品
				if (OpStatusEnum.PROD_ILLEGAL_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and p.op_status in (2, 3)");
				} else if (OpStatusEnum.PROD_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and ((p.op_status in (-1,0,1) and p.status > -10) or ldp.status > -10) ");
				} else {
					if (OpStatusEnum.WAIT.getValue().equals(productQuery.getOpStatus())) {
						map.put("opStatusExpand", " and ((p.op_status = 0 and p.status > -10) or ldp.status = 0) ");
					} else if (OpStatusEnum.DENY.getValue().equals(productQuery.getOpStatus())) {
						map.put("opStatusExpand", " and ((p.op_status = -1 and p.status > -10) or ldp.status = -1) ");
					}
				}
			}
		} else {
			map.put("delStatus", productQuery.getDelStatus());
		}
		/*如果商家查询商品列表，永久删除的商品不可被搜索出来*/
		if (productQuery.getShopId() != null) {
			map.put("illegalDelStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.value());
		}

		map.put("shopId", productQuery.getShopId());
		map.put("productIdList", productQuery.getProductIdList());
		map.put("skuIdList", productQuery.getSkuIdList());
		map.put("preSellFlag", productQuery.getPreSellFlag());
		map.put("payPctType", productQuery.getPayPctType());

		if (Objects.equals(productQuery.getProp(), "price")) {
			productQuery.setProp("min_price");
		}
		OrderBy orderBy = new OrderBy(productQuery.getProp(), productQuery.getOrder());
		//需要和前端确定排序的字段 销售价，购买数，库存, 创建时间
		map.addOrder(orderBy, Arrays.asList("min_price", "buys", "stocks", "create_time"), "p");
		hql.setSqlAndParameter("Product.getProductList", map);
		return querySimplePage(hql);
	}

	@Override
	public List<Product> queryProductBySkuIdList(List<Long> skuIdList) {
		QueryMap map = new QueryMap();
		map.in("skuIdList", skuIdList);
		SQLOperation operation = this.getSQLAndParams("Product.queryProductBySkuIdList", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(Product.class));
	}

	@Override
	public Integer updateOpStatusAndAuditOpinion(List<Long> idList, Integer opStatus, String auditOpinion) {
		List<String> arr = new ArrayList<>();
		arr.add(opStatus.toString());
		arr.add(auditOpinion);
		StringBuffer sb = new StringBuffer("UPDATE ls_product p set p.op_status=?,update_time= NOW(), audit_opinion = ? where p.op_status=0 and id in( ");
		for (Long id : idList) {
			arr.add(id.toString());
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return update(sb.toString(), arr.toArray());
	}


	@Override
	public Integer illegalOffProduct(List<Long> productIds, Integer opStatus) {
		List<Long> arr = new ArrayList<>();
		arr.add(opStatus.longValue());
		StringBuffer sb = new StringBuffer("UPDATE ls_product p set p.op_status=?,update_time= NOW() where p.op_status <=2 and id in( ");
		for (Long id : productIds) {
			arr.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return update(sb.toString(), arr.toArray());
	}

	@Override
	public int[] updateActualProductStocks(List<InventoryOperationsDTO> actualList) {
		if (CollectionUtils.isEmpty(actualList)) {
			return new int[]{};
		}
		List<Object[]> params = new ArrayList<>(actualList.size());
		actualList.forEach(e -> params.add(new Object[]{e.getStocks(), e.getStocks(), e.getProductId(), e.getStocks(), e.getStocks()}));
		return super.batchUpdate("UPDATE ls_product SET stocks = (stocks - ?), actual_stocks = (actual_stocks - ?) WHERE id = ? AND (stocks - ?) >= 0 AND (actual_stocks - ?) >= 0 ", params);
	}

	@Override
	public Product getProductOnLineEsByProductId(Long productId) {
		QueryMap map = new QueryMap();
		map.put("productId", productId);
		SQLOperation operation = this.getSQLAndParams("Product.queryProductOnlineEs", map);
		return get(operation.getSql(), Product.class, operation.getParams());
	}

	@Override
	public List<Product> queryProductOnLineEsByProductId(List<Long> productIds) {
		if (CollUtil.isEmpty(productIds)) {
			return Collections.emptyList();
		}
		QueryMap map = new QueryMap();
		map.in("productIds", productIds);
		SQLOperation operation = this.getSQLAndParams("Product.queryProductOnlineEs", map);
		return query(operation.getSql(), Product.class, operation.getParams());
	}

	@Override
	public PageSupport<ProductBO> auditPage(ProductQuery productQuery) {
		QueryMap map = new QueryMap();
		SimpleSqlQuery hql = new SimpleSqlQuery(ProductBO.class, productQuery.getPageSize(), productQuery.getCurPage());

		map.put("categoryId1", productQuery.getCategoryId());
		map.put("categoryId2", productQuery.getCategoryId());
		map.put("categoryId3", productQuery.getCategoryId());
		map.put("firstCategoryId", productQuery.getShopFirstCatId());
		map.put("secondCategoryId", productQuery.getShopSecondCatId());
		map.put("thirdCategoryId", productQuery.getShopThirdCatId());
		map.put("brandId", productQuery.getBrandId());
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.like("siteName", productQuery.getSiteName(), MatchMode.ANYWHERE);

		//1. 没有被删除的商品
		if (ObjectUtil.isEmpty(productQuery.getDelStatus()) || ProductDelStatusEnum.PROD_NORMAL.value().equals(productQuery.getDelStatus())) {
			map.put("delStatus", ProductDelStatusEnum.PROD_NORMAL.value());
			//2. 后台审核成功的商品
			if (ObjectUtil.isEmpty(productQuery.getOpStatus()) || OpStatusEnum.PASS.getValue().equals(productQuery.getOpStatus())) {
				//3. 所有商品（包括审核通过的上下线商品，以及未发布的草稿商品）
				if (ProductStatusEnum.PROD_ALL.value().equals(productQuery.getStatus())) {
					map.put("opStatusExpand", " and ((p.op_status = 1 and p.status >= 0) or (p.op_status = 0 and p.status = -10))");
				} else if (ProductStatusEnum.UNPUBLISHED.value().equals(productQuery.getStatus())) {
					// 草稿商品
					map.put("opStatus", OpStatusEnum.WAIT.getValue());
					map.put("opStatusExpand", " and p.status = -10 ");
				} else {
					// 审核通过的上下线商品
					map.put("opStatus", OpStatusEnum.PASS.getValue());
					map.put("status", productQuery.getStatus());
				}

			} else {
				//4. 审核异常的商品
				if (OpStatusEnum.PROD_ILLEGAL_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and p.op_status in (2, 3)");
				} else if (OpStatusEnum.PROD_ALL.getValue().equals(productQuery.getOpStatus())) {
					map.put("opStatusExpand", " and ((p.op_status in (-1,0,1) and p.status > -10) or ldp.status > -10) ");
				} else {
					if (OpStatusEnum.WAIT.getValue().equals(productQuery.getOpStatus())) {
						map.put("opStatusExpand", " and ((p.op_status = 0 and p.status > -10) or ldp.status = 0) ");
					} else if (OpStatusEnum.DENY.getValue().equals(productQuery.getOpStatus())) {
						map.put("opStatusExpand", " and ((p.op_status = -1 and p.status > -10) or ldp.status = -1) ");
					}
				}
			}
		} else {
			map.put("delStatus", productQuery.getDelStatus());
		}
		/*如果商家查询商品列表，永久删除的商品不可被搜索出来*/
		if (productQuery.getShopId() != null) {
			map.put("illegalDelStatus", ProductDelStatusEnum.PROD_SHOP_DELETE.value());
		}

		map.put("shopId", productQuery.getShopId());
		map.put("productIdList", productQuery.getProductIdList());
		map.put("skuIdList", productQuery.getSkuIdList());
		map.put("preSellFlag", productQuery.getPreSellFlag());
		map.put("payPctType", productQuery.getPayPctType());

		if (Objects.equals(productQuery.getProp(), "price")) {
			productQuery.setProp("min_price");
		}
		OrderBy orderBy = new OrderBy(productQuery.getProp(), productQuery.getOrder());
		//需要和前端确定排序的字段 销售价，购买数，库存, 创建时间
		map.addOrder(orderBy, Arrays.asList("min_price", "buys", "stocks", "create_time"), "p");
		hql.setSqlAndParameter("Product.auditPage", map);
		return querySimplePage(hql);
	}

	@Override
	public Long getIndexCount() {
		QueryMap map = new QueryMap();
		map.put("status", ProductStatusEnum.PROD_ONLINE.getValue());
		SQLOperation operation = this.getSQLAndParams("Product.getProductOnlineCount", map);
		Long count = get(operation.getSql(), Long.class, operation.getParams());
		return Optional.ofNullable(count).orElse(0L);
	}

	@Override
	public List<Product> getBrandId(Long id) {
		return queryByProperties(new LambdaEntityCriterion<Product>(Product.class).eq(Product::getBrandId, id));
	}

	@Override
	public List<Long> getAdamin() {
		return query("SELECT id FROM ls_admin_user", Long.class);
	}

	@Override
	public Product getProduct(String productName) {
		return getByProperties(new EntityCriterion().eq("name", productName));
	}


}
