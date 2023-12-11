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
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dao.SkuDao;
import com.legendshop.product.dto.InventoryOperationsDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.SkuAmountDTO;
import com.legendshop.product.dto.SkuUpdateSkuDTO;
import com.legendshop.product.entity.Sku;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.product.excel.StockExportDTO;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.query.SkuQuery;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class SkuDaoImpl.
 *
 * @author legendshop
 */
@Repository
public class SkuDaoImpl extends GenericDaoImpl<Sku, Long> implements SkuDao {

	@Override
	public List<Sku> getSkuByProductId(Long productId) {
		return queryByProperties(new EntityCriterion().eq("productId", productId));
	}

	@Override
	public List<Sku> getSku(Long[] skuId) {
		if (ArrayUtil.isEmpty(skuId)) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder("select s.productId as productId,s.id ,s.properties as properties,s.user_properties " +
				"as userProperties,p.status as status,s.original_price ,p.name as name,s.pic as pic,s.cn_properties as " +
				"cnProperties,s.stocks as stocks,s.actual_stocks as actualStocks from ls_sku s,ls_product p  where p.id=s.product_id and s.id in(");
		for (int i = 0; i < skuId.length; i++) {
			sql.append("?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return this.query(sql.toString(), Sku.class, skuId);
	}

	@Override
	public Integer getStocksByLockMode(Long skuId) {
		return get("select stocks from ls_sku where id = ?  for update", Integer.class, skuId);
	}

	/**
	 * SKU的实际库存
	 *
	 * @param skuId
	 * @return
	 */
	@Override
	public Integer getActualStocksByLockMode(Long skuId) {
		return get("select actual_stocks from ls_sku where id = ? for update", Integer.class, skuId);
	}

	/**
	 * 更新销售库存
	 */
	@Override
	@CacheEvict(value = "Sku", key = "#skuId")
	public Integer updateSkuStocks(Long skuId, long stocks) {
		return update("update ls_sku set stocks = (stocks + ?) where id = ? and (stocks + ?)>=0", stocks, skuId, stocks);
	}

	@Override
	@CacheEvict(value = "Sku", key = "#skuId")
	public Integer directDeductionSkuStocks(Long skuId, long stocks) {
		return update("update ls_sku set stocks = stocks - ? where id = ?  and (stocks - ?)>=0", stocks, skuId, stocks);
	}

	@Override
	@CacheEvict(value = "Sku", key = "#skuId")
	public Integer directAddSkuStocks(Long skuId, long stocks) {
		return update("update ls_sku set stocks = stocks + ? where id = ?", stocks, skuId);
	}

	@Override
	@CacheEvict(value = "Sku", key = "#skuId")
	public Integer directDeductionAddSkuStocks(Long skuId, long stocks) {
		return update("update ls_sku set stocks = stocks + ? where id = ?  and (stocks - ?)>=0", stocks, skuId, stocks);
	}

	@Override
	@CacheEvict(value = "Sku", key = "#skuId")
	public int updateSkuActualStocks(Long skuId, long basketCount) {
		return update("update ls_sku set actual_stocks = (actual_stocks + ?) where id = ? and (actual_stocks + ?)>=0", basketCount, skuId, basketCount);
	}

	@Override
	public Sku getByProductIdSkuId(Long productId, Long skuId) {
		return getByProperties(new EntityCriterion().eq("productId", productId).eq("id", skuId));
	}

	/**
	 * 更新sku所占用的活动
	 */
	@Override
	public int updateSkuTypeById(Long skuId, String skuType, String originSkuType) {
		return update("UPDATE ls_sku SET sku_type = ? WHERE id = ? and sku_type = ?", skuType, skuId, originSkuType);
	}

	@Override
	@CacheEvict(value = "Sku", key = "#skuId")
	public int updateStocksBySkuId(Long skuId, Integer basketCount) {
		return update("UPDATE ls_sku SET stocks=(stocks + ?) WHERE id = ?", basketCount, skuId);
	}

	@Override
	public void batchUpdateSkuType(List<Long> skuIds, String skuType, String originalSkuType) {
		List<Object[]> params = new ArrayList<>();
		if (ObjectUtil.isNull(originalSkuType)) {
			String sql = "UPDATE ls_sku SET sku_type = ?,update_time = NOW() WHERE id = ?";
			skuIds.forEach(s -> {
				params.add(new Object[]{skuType, s});
			});
			batchUpdate(sql, params);
		} else {
			String sql = "UPDATE ls_sku SET sku_type = ?,update_time = NOW() WHERE id = ? and sku_type = ?";
			skuIds.forEach(s -> {
				params.add(new Object[]{skuType, s, originalSkuType});
			});
			batchUpdate(sql, params);
		}
	}

	@Override
	public void updateSkuAllStocks(Long skuId, Integer basketCount) {
		update("UPDATE ls_sku SET stocks = stocks + ?, actual_stocks = actual_stocks + ? WHERE id = ? ", basketCount, basketCount, skuId);
	}


	@Override
	public PageSupport<SkuBO> stocksPage(ProductQuery productQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(SkuBO.class, productQuery.getPageSize(), productQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.put("shopId", productQuery.getShopId());
		map.put("productId", productQuery.getProductId());
		//如果永久删除 就不显示
		map.in("delStatus", CollUtil.newArrayList(ProductDelStatusEnum.PROD_NORMAL.getValue(), ProductDelStatusEnum.PROD_DELETE.getValue()));
		query.setSqlAndParameter("Sku.stockPage", map);
		return querySimplePage(query);
	}

	@Override
	public List<StockExportDTO> stocksExport(ProductQuery query) {
		if (ObjectUtil.isNotNull(query)) {
			StringBuilder sb = new StringBuilder();
			List<Object> obj = new ArrayList<Object>();
			sb.append("select sku.id,sku.product_id,sku.pic,p.name,sku.cn_properties,sku.price,sku.buys,sku.stocks,");
			sb.append("sku.actual_stocks,p.stocks_arm,p.status as statusStr from ls_sku sku,ls_product p ");
			sb.append("where p.id=sku.product_id and p.op_status=1 ");
			if (CollUtil.isNotEmpty(query.getSkuIdList())) {
				sb.append(" AND sku.id in(");
				for (Long skuid : query.getSkuIdList()) {
					sb.append("?,");
					obj.add(skuid);
				}
				sb.setLength(sb.length() - 1);
				sb.append(")");
			} else {
				if (StrUtil.isNotBlank(query.getName())) {
					sb.append(" AND p.name LIKE ?");
					obj.add("%" + query.getName().trim() + "%");
				}
				if (ObjectUtil.isNotNull(query.getShopId())) {
					sb.append(" AND p.shop_id = ?");
					obj.add(query.getShopId());
				}
				sb.append(" AND p.del_status = 1");
			}
			sb.append(" ORDER BY p.create_time DESC");
			return this.query(sb.toString(), StockExportDTO.class, obj.toArray());
		}
		return null;
	}

	@Override
	public List<SkuBO> stocksList(SkuQuery query) {
		return query(getSQL("Sku.stocksList"), SkuBO.class, query.getProductId());
	}

	@Override
	public List<Sku> queryByProductId(List<Long> productId) {
		StringBuilder sql = new StringBuilder("select a.id,a.product_id,a.stocks,a.actual_stocks from ls_sku a where a.product_id in(");
		for (int i = 0; i < productId.size(); i++) {
			sql.append("?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return this.query(sql.toString(), Sku.class, productId.toArray());
	}

	@Override
	public List<SkuBO> queryStocksListBySkuIdList(List<Long> skuIdList) {
		StringBuilder sql = new StringBuilder(" select a.buys,a.original_price,a.model_id,a.cn_properties,a.weight,a.pic,a.product_id,a.integral_flag,a.integral_deduction_flag,");
		sql.append("a.sku_type,a.stocks,a.volume,a.actual_stocks,a.create_time,a.update_time,a.price,a.party_code,a.name,a.user_properties,a.properties,");
		sql.append("a.id,a.cost_price,p.name as productName,a.integral_flag from ls_sku a,ls_product p where p.id=a.product_id and a.id in(");
		for (int i = 0; i < skuIdList.size(); i++) {
			sql.append("?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return query(sql.toString(), SkuBO.class, skuIdList.toArray());
	}

	@Override
	public List<Sku> queryBySkuIds(List<Long> skuIds) {
		StringBuffer sql = new StringBuffer("select s.* from ls_sku s,ls_product p where s.product_id = p.id and s.id in (");
		for (int i = 0; i < skuIds.size(); i++) {
			sql.append("?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		sql.append(" and p.status = ?");
		skuIds.add(Long.parseLong(ProductStatusEnum.PROD_ONLINE.getValue().toString()));
		sql.append(" and p.op_status = ?");
		skuIds.add(Long.parseLong(OpStatusEnum.PASS.getValue().toString()));
		sql.append(" and p.del_status = ?");
		skuIds.add(Long.parseLong(ProductDelStatusEnum.PROD_NORMAL.getValue().toString()));
		return query(sql.toString(), Sku.class, skuIds.toArray());
	}

	@Override
	public List<Sku> querySkuByProductIdList(List<Long> productIdList) {
		if (CollectionUtils.isEmpty(productIdList)) {
			return new ArrayList<>();
		}

		StringBuilder sql = new StringBuilder("select sku.*,p.name as productName from ls_sku sku,ls_product p where p.id=sku.product_id and p.id in(");
		for (int i = 0; i < productIdList.size(); i++) {
			if (i != 0) {
				sql.append(", ");
			}
			sql.append("?");
		}
		sql.append(")");
		return this.query(sql.toString(), Sku.class, productIdList.toArray());
	}

	@Override
	public void deleteByProdId(List<Long> prodIds) {
		StringBuilder sql = new StringBuilder("delete from ls_sku where product_id in (");
		for (Long prodId : prodIds) {
			sql.append("?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		this.update(sql.toString(), prodIds.toArray());
	}

	/**
	 * 根据商品Id查询对应的Sku售价
	 */
	@Override
	public List<SkuAmountDTO> querySkuAmount(List<Long> prodIds) {
		if (CollectionUtils.isEmpty(prodIds)) {
			return new ArrayList<>();
		}
		StringBuilder sql = new StringBuilder("SELECT id, product_id, price, cost_price FROM ls_sku WHERE product_id IN (");
		for (int i = 0; i < prodIds.size(); i++) {
			if (i != 0) {
				sql.append(", ");
			}
			sql.append("?");
		}
		sql.append(")");
		return super.query(sql.toString(), SkuAmountDTO.class, prodIds.toArray());
	}

	@Override
	public List<SkuAmountDTO> querySkuAmount(Long prodId) {
		return super.query("SELECT sku_id, product_id, price, cost_price FROM ls_sku WHERE product_id = ?", SkuAmountDTO.class, prodId);
	}

	@Override
	public List<SkuBO> queryCouponSkuByShopId(List<Long> shopIds) {
		StringBuilder sql = new StringBuilder("select lp.buys, lp.status, lp.id as prodId, lsk.*, ls.id as shopId from ls_shop_detail ls left join ls_product lp on ls.id = lp.shop_id left join ls_sku lsk on lp.id = lsk.product_id where ls.id in (");
		for (Long shopId : shopIds) {
			sql.append(shopId);
			sql.append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(") AND lp.status = 1 group by lp.id order by lp.buys desc");
		return query(sql.toString(), SkuBO.class);
	}

	@Override
	public boolean reduceStock(Long skuId, Integer stock) {
		String sql = "update ls_sku set stocks=stocks-? where id=?";
		int update = update(sql, stock, skuId);
		return update > 0;
	}

	@Override
	public boolean addBackStock(Long skuId, Integer stock) {
		String sql = "update ls_sku set stocks=stocks+? where id=?";
		int update = update(sql, stock, skuId);
		return update > 0;
	}

	@Override
	public List<SkuBO> queryBOBySkuIds(List<Long> skuIds) {
		if (CollUtil.isEmpty(skuIds)) {
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer("select s.*,p.name as productName from ls_sku s ,ls_product p WHERE  p.id=s.product_id and s.id in(");
		for (Long skuId : skuIds) {
			stringBuffer.append(skuId);
			stringBuffer.append(",");
		}
		stringBuffer.setLength(stringBuffer.length() - 1);
		stringBuffer.append(")");
		return query(stringBuffer.toString(), SkuBO.class);
	}

	@Override
	public List<SkuBO> calculateBuys(List<String> numberList) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("SELECT sku_id as id ,basket_count AS buys FROM ls_order_item WHERE order_number IN (");
		for (String number : numberList) {
			buffer.append("?,");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(") GROUP BY sku_id");
		return query(buffer.toString(), SkuBO.class, numberList.toArray());
	}

	@Override
	public boolean updateBuys(Long id, Integer buys) {
		return update("update ls_sku set buys = (buys + ?) where id = ?", buys, id) > 0;
	}

	@Override
	public PageSupport<SkuBO> stocksArmPage(ProductQuery productQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(SkuBO.class, productQuery.getPageSize(), productQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("name", productQuery.getName(), MatchMode.ANYWHERE);
		map.put("shopId", productQuery.getShopId());
		map.put("productId", productQuery.getProductId());
		query.setSqlAndParameter("Sku.stocksArmPage", map);
		return querySimplePage(query);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "Sku", key = "#skuDTO.id"),
			@CacheEvict(value = "SkuBO", key = "#skuDTO.id")
	}
	)
	public void cleanCache(SkuUpdateSkuDTO skuDTO) {

	}

	@Override
	public Integer getSkuStock(Long skuId) {
		return get("select stocks from ls_sku where id=?", Integer.class, skuId);
	}

	@Override
	public Integer getProductStock(Long productId) {
		return get("select stocks from ls_product where id=?", Integer.class, productId);
	}

	@Override
	public List<ProductDTO> querySkuCount(List<Long> productIdList) {
		if (CollUtil.isEmpty(productIdList)) {
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer("select product_id as id, count(*) as skuCount from ls_sku WHERE  product_id in(");
		for (Long productId : productIdList) {
			stringBuffer.append(productId);
			stringBuffer.append(",");
		}
		stringBuffer.setLength(stringBuffer.length() - 1);
		stringBuffer.append(") group by product_id");
		return query(stringBuffer.toString(), ProductDTO.class);
	}

	@Override
	public List<SkuBO> querySkuBOByProductIdList(List<Long> productIdList) {
		if (CollectionUtils.isEmpty(productIdList)) {
			return new ArrayList<>();
		}

		StringBuilder sql = new StringBuilder("select sku.*,p.name as productName from ls_sku sku,ls_product p where p.id=sku.product_id and p.id in(");
		for (int i = 0; i < productIdList.size(); i++) {
			if (i != 0) {
				sql.append(", ");
			}
			sql.append("?");
		}
		sql.append(")");
		return this.query(sql.toString(), SkuBO.class, productIdList.toArray());
	}

	@Override
	public List<SkuBO> getSkuBOByProductId(Long productId) {
		return query("select sku.*,p.name as productName from ls_sku sku,ls_product p where p.id=sku.product_id and p.id = ?", SkuBO.class, productId);
	}

	@Override
	public int[] updateAllSkuStocks(List<InventoryOperationsDTO> dtoList) {
		if (CollectionUtils.isEmpty(dtoList)) {
			return new int[]{};
		}
		List<Object[]> params = new ArrayList<>(dtoList.size());
		dtoList.forEach(e -> params.add(new Object[]{e.getStocks(), e.getStocks(), e.getSkuId(), e.getStocks(), e.getStocks()}));
		return super.batchUpdate("UPDATE ls_sku SET stocks = (stocks + ?), actual_stocks = (actual_stocks + ?) WHERE id = ? AND (stocks + ?) >= 0 AND (actual_stocks + ?) >= 0 ", params);
	}

	@Override
	public int[] updateSalesSkuStocks(List<InventoryOperationsDTO> dtoList) {
		if (CollectionUtils.isEmpty(dtoList)) {
			return new int[]{};
		}
		List<Object[]> params = new ArrayList<>(dtoList.size());
		dtoList.forEach(e -> params.add(new Object[]{e.getStocks(), e.getSkuId(), e.getStocks()}));
		return super.batchUpdate("UPDATE ls_sku SET stocks = (stocks + ?) WHERE id = ? AND (stocks + ?) >= 0", params);
	}

	@Override
	public int[] updateActualSkuStocks(List<InventoryOperationsDTO> dtoList) {
		if (CollectionUtils.isEmpty(dtoList)) {
			return new int[]{};
		}
		List<Object[]> params = new ArrayList<>(dtoList.size());
		dtoList.forEach(e -> params.add(new Object[]{e.getStocks(), e.getSkuId(), e.getStocks()}));
		return super.batchUpdate("UPDATE ls_sku SET actual_stocks = (actual_stocks - ?) WHERE id = ? AND (actual_stocks - ?) >= 0", params);
	}

	@Override
	public SkuBO getSkuByIdToCustom(Long skuId) {
		String sql = "select ls.product_id, ls.id, ls.price, ls.pic, lp.name as productName, ls.name from ls_sku ls left join ls_product lp on ls.product_id = lp.id where ls.id = ?";
		return get(sql, SkuBO.class, skuId);
	}
}
