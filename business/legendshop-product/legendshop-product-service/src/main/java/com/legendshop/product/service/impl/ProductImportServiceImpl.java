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
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.oss.properties.OssProperties;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.bo.ProductPropertyValueBO;
import com.legendshop.product.dao.*;
import com.legendshop.product.dto.*;
import com.legendshop.product.entity.*;
import com.legendshop.product.enums.*;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.*;
import com.legendshop.product.service.convert.*;
import com.legendshop.product.service.listener.SkuImportInfoExcelListener;
import com.legendshop.shop.api.ShopDetailApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductImportServiceImpl implements ProductImportService {

	final ProductDao productDao;
	final ProductPropertyDao productPropertyDao;
	final ProductPropertyValueDao productPropertyValueDao;
	final SkuImportConverter skuImportConverter;
	final ProductImportDao productImportDao;
	final ProductImportConverter productImportConverter;
	final ProductImportDetailService productImportDetailService;
	final ShopDetailApi shopDetailApi;
	final ProductConverter productConverter;
	final ProductPropertyValueService productPropertyValueService;
	final SkuDao skuDao;
	final SkuConverter skuConverter;
	final ProductPropertyImageDao productPropertyImageDao;
	final ProductPropertyImageConverter productPropertyImageConverter;
	final MessageApi messagePushClient;
	final MessageApi messageApi;
	final AmqpSendMsgUtil amqpSendMsgUtil;
	final DraftSkuService draftSkuService;
	final DraftProductService draftProductService;
	final TransportService transportService;
	final BrandDao brandDao;
	final CategoryDao categoryDao;
	final ProductService productService;
	private final String DETAIL_IMG_FORMAT = "<p><img src=\"{}\" /></p>";
	final ProductPropertyConverter productPropertyConverter;
	final ProductPropertyValueConverter productPropertyValueConverter;
	final OssProperties picConfig;
	final ProductImportDetailConverter productImportDetailConverter;
	final ProductPropertyService productPropertyService;

	@Override
	public R<Void> batchInsert(String username, MultipartFile file, ProductDTO productDTO) throws IOException {
		Long shopUserId = productDTO.getShopUserId();
		String prefix = picConfig.getEndpoint() + "/" + picConfig.getBucketName() + "/";
		Date now = new Date();
		// 文件校验
		String fileName = file.getOriginalFilename();
		if (StringUtil.isEmpty(fileName)) {
			throw new BusinessException("错误的文件类型！");
		}
		String fileSuffixType = fileName.substring(fileName.lastIndexOf(".") + 1);
		InputStream inputStream = file.getInputStream();
		String fileType = FileTypeUtil.getType(inputStream);
		if ("xlsx".equalsIgnoreCase(fileSuffixType) ? Boolean.FALSE :
				("xls".equalsIgnoreCase(fileType) ? Boolean.FALSE :
						("xlsm".equalsIgnoreCase(fileType) ? Boolean.FALSE : Boolean.TRUE))) {
//			throw new BusinessException("不支持" + fileType + "文件格式！！");
			throw new BusinessException("请使用正确的导入模板！");
		}

		// 读取excel
		SkuImportInfoExcelListener excelListener = new SkuImportInfoExcelListener();
		try {
			EasyExcel.read(file.getInputStream(), SkuImportDTO.class, excelListener).sheet().doRead();

		} catch (Exception e) {
			throw new BusinessException("错误的文件！！");
		}

		// 获取excel内容
		List<SkuImportDTO> list = excelListener.getList();
		if (CollectionUtils.isEmpty(list)) {
			log.warn("没有需要处理的商品");
			return R.fail("没有需要处理的商品！");
		}
		log.info("读取的商品信息如下：{}", JSON.toJSONString(list));
		Long shopId = productDTO.getShopId();


		// 初始化结果信息
		List<ProductImportDetailDTO> importList = new ArrayList<>(list.size());
		for (SkuImportDTO e : list) {
			String productName = e.getProductName();
			Product product = productDao.getProduct(productName);
			StringBuffer reason = new StringBuffer();
			if (ObjectUtil.isNotEmpty(product)) {
				log.info("该商品已发布,批量发布失败！");
				reason.append("该商品已发布!");
			}
			if (StrUtil.isBlank(e.getProductName())) {
				log.info("存在商品名称为空的数据,批量发布失败！");
				reason.append("存在商品名称为空的数据!");

			}
			if (StrUtil.isBlank(e.getSkuName())) {
				log.info("存在SKU名称为空的数据,批量发布失败！");
				reason.append("存在SKU名称为空的数据!");

			}

			if (StrUtil.isBlank(e.getCateName())) {
				log.info("存在类目为空的数据,批量发布失败！");
				reason.append("存在类目为空的数据!");

			}

			if (StrUtil.isBlank(e.getPic())) {
				log.info("存在商品主图为空的数据,批量发布失败！");
				reason.append("存在商品主图为空的数据!");

			}
			if (ObjectUtil.isNotEmpty(e.getPic()) && e.getPic().length() > 500) {
				log.info("存在商品主图过长的数据,批量发布失败！");
				reason.append("存在商品主图过长的数据!");

			}
			if (StrUtil.isBlank(e.getSpecification())) {
				log.info("存在规格参数为空的数据,批量发布失败！");
				reason.append("存在规格参数为空的数据!");

			}
			if (ObjectUtil.isNotEmpty(e.getSpecification()) && e.getSpecification().length() > 100) {
				log.info("存在规格参数过长的数据,批量发布失败！");
				reason.append("存在规格参数过长的数据!");
			}
			if (StrUtil.isBlank(e.getProperties())) {
				log.info("存在商品属性为空的数据,批量发布失败！");
				reason.append("存在商品属性为空的数据!");
			}
			if (ObjectUtil.isNotEmpty(e.getProperties()) && e.getProperties().length() > 100) {
				log.info("存在属性参数过长的数据,批量发布失败！");
				reason.append("存在属性参数过长的数据!");
			}
			if (StrUtil.isBlank(e.getStocks())) {
				log.info("存在库存为空的数据,批量发布失败！");
				reason.append("存在库存为空的数据!");
			}
			String stocks = e.getStocks();
			if (StrUtil.isNotBlank(e.getStocks()) && !NumberUtil.isNumber(stocks)) {
				log.info("库存数据存在非法字符的数据,批量发布失败！");
				reason.append("库存数据存在非法字符的数据!");
			}
			if (ObjectUtil.isNotEmpty(stocks) && NumberUtil.isNumber(stocks) && Integer.parseInt(stocks) > 999999) {
				log.info("库存量不能大于999999,批量发布失败！");
				reason.append("库存量不能大于999999!");
			}
			if (NumberUtil.isNumber(stocks)) {
				stocks = checkNum(stocks, 6);
				e.setStocks(stocks);
				int i = Integer.parseInt(stocks);
				if (i < 0) {
					log.info("存在库存为负数的数据,批量发布失败！");
					reason.append("存在库存为负数的数据!");

				}
			}

			if (ObjectUtil.isNotEmpty(e.getDetailPic())) {
				String detailPic = e.getDetailPic();
				detailPic = checks(detailPic);
				String[] productDetailPicSplit = detailPic.split(";");
				for (String pic : productDetailPicSplit) {
					if (pic.length() > 200) {
						log.info("每张详情图片字符不能超过200");
						reason.append("每张详情图片字符不能超过200!");
					}
				}

			}
			if (ObjectUtil.isEmpty(e.getPrice())) {
				log.info("存在销售价为空的数据,批量发布失败！");
				reason.append("存在销售价为空的数据!");
			}
			if (!NumberUtil.isNumber(e.getPrice())) {
				log.info("销售价存在非法字符的数据,批量发布失败！");
				reason.append("销售价存在非法字符的数据!");
			}
			String price = e.getPrice();
			if (NumberUtil.isNumber(e.getPrice())) {
				if (Integer.parseInt(price) > 9999999) {
					log.info("销售价不能大于七位数,批量发布失败！");
					reason.append("销售价不能大于七位数!");
				}
				price = checkNum(price, 7);
				e.setPrice(price);
				if (new BigDecimal(price).compareTo(BigDecimal.ZERO) < 0) {
					log.info("存在销售价为负的数据,批量发布失败！");
					reason.append("存在销售价为负的数据!");
				}

			}
			if (NumberUtil.isNumber(e.getCostPrice())) {
				if (ObjectUtil.isEmpty(e.getCostPrice())) {
					log.info("存在成本价为空的数据,批量发布失败！");
					reason.append("存在成本价为空的数据!");
				}
			}

			if (ObjectUtil.isNotEmpty(e.getOriginalPrice())) {
				String originalPrice = e.getOriginalPrice();
				if (!NumberUtil.isNumber(originalPrice)) {
					log.info("原价存在非法字符的数据,批量发布失败！");
					reason.append("原价存在非法字符的数据!");
				}
				if (NumberUtil.isNumber(originalPrice)) {
					if (Integer.parseInt(originalPrice) > 9999999) {
						log.info("原价不能大于七位数,批量发布失败！");
						reason.append("原价不能大于七位数!");
					}
					if (new BigDecimal(originalPrice).compareTo(BigDecimal.ZERO) < 0) {
						log.info("存在原价为负的数据,批量发布失败！");
						reason.append("存在原价为负的数据!");
					}
					if (new BigDecimal(originalPrice).compareTo(new BigDecimal(price)) > 0) {
						log.info("原价不能大于销售价,批量发布失败！");
						reason.append("原价不能大于销售价!");
					}
					originalPrice = checkNum(originalPrice, 7);
					e.setOriginalPrice(originalPrice);
				}


			}
			if (!NumberUtil.isNumber(e.getCostPrice())) {
				log.info("成本价存在非法字符的数据,批量发布失败！");
				reason.append("成本价存在非法字符的数据!");
			}
			if (NumberUtil.isNumber(e.getCostPrice())) {
				String costPrice = e.getCostPrice();
				costPrice = checkNum(costPrice, 7);
				if (new BigDecimal(costPrice).compareTo(BigDecimal.ZERO) < 0) {
					log.info("存在成本价为负的数据,批量发布失败！");
					reason.append("存在成本价为负的数据!");
				}
				e.setCostPrice(costPrice);
				if (ObjectUtil.isNotEmpty(price) && ObjectUtil.isNotEmpty(price)) {
					if (new BigDecimal(costPrice).compareTo(new BigDecimal(price)) > 0) {
						log.info("销售价不能大于成本价,批量发布失败！");
						reason.append("销售价不能大于成本价!");
					}
				}

			}


			if (!NumberUtil.isNumber(e.getDeliveryType())) {
				log.info("配送类型存在非法字符的数据,批量发布失败！");
				reason.append("配送类型存在非法字符的数据!");
			}
			if (NumberUtil.isNumber(e.getDeliveryType())) {
				if (Integer.parseInt(e.getDeliveryType()) > 4 || Integer.parseInt(e.getDeliveryType()) < 1) {
					log.info("配送类型异常,批量发布失败！");
					reason.append("配送类型异常!");
				}
			}


			if (ObjectUtil.isNotEmpty(e.getTransportName())) {
				String transportName = e.getTransportName();
				Long id = transportService.getByName(transportName, shopId);
				if (ObjectUtil.isEmpty(id)) {
					log.info("运费模板不存在,批量发布失败！");
					reason.append("运费模板不存在!");
				}
				e.setTransportId(id);

			} else {
				//待优化,以后有了平台默认模板再改
				log.info("运费模板为空,批量发布失败！");
				reason.append("运费模板为空!");
			}
			if (ObjectUtil.isNotEmpty(e.getBrand())) {

				//品牌校验
				Brand brand = brandDao.getBrand(shopId, e.getBrand());

				if (ObjectUtil.isEmpty(brand)) {
					log.info("品牌不存在,批量发布失败！");
					reason.append("品牌不存在!");
				}
				if (ObjectUtil.isNotEmpty(brand)) {
					e.setBrandId(brand.getId());
				}


			}


			//类目校验
			String[] cates = e.getCateName().split("/");
			if (cates.length > 3 || cates.length < 3) {
				log.info("存在类目参数异常的数据,批量发布失败！");
				reason.append("存在类目参数异常的数据!");
			}

			if (cates.length == 3) {
				CategoryBO categoryBO = categoryDao.queryByIdName(cates[0]);
				CategoryBO categoryBO1 = categoryDao.queryByIdName(cates[1]);
				CategoryBO categoryBO2 = categoryDao.queryByIdName(cates[2]);
				if (ObjectUtil.isEmpty(categoryBO)) {
					log.info("找不到一级类目,批量发布失败！{}", cates[0]);
					reason.append("找不到一级类目!");
				}
				if (ObjectUtil.isEmpty(categoryBO1)) {
					log.info("找不到二级类目,批量发布失败！{}", cates[1]);
					reason.append("找不到二级类目!");
				}
				if (ObjectUtil.isEmpty(categoryBO2)) {
					log.info("找不到三级类目,批量发布失败！{}", cates[1]);
					reason.append("找不到三级类目!");
				}
				if (ObjectUtil.isNotEmpty(categoryBO) && ObjectUtil.isNotEmpty(categoryBO1) && ObjectUtil.isNotEmpty(categoryBO2) && categoryBO.getParentId().equals(-1L) && categoryBO1.getParentId().equals(categoryBO.getId()) && categoryBO2.getParentId().equals(categoryBO1.getId())) {
					e.setGlobalFirstCatId(categoryBO.getId());
					e.setGlobalSecondCatId(categoryBO1.getId());
					e.setGlobalThirdCatId(categoryBO2.getId());

				} else {
					log.info("类目不匹配,批量发布失败！{}", "一级类目" + cates[0] + "二级类目" + cates[1] + "三级类目" + cates[2]);
					reason.append("类目不匹配!");
				}

			}


			if (ObjectUtil.isNotEmpty(e.getSpecification())) {
				String parameter = e.getSpecification();
				//校验参数
				parameter = checks(parameter);
				String[] parameterList = parameter.split(";");

				String propertiesIds = null;
				//规格参数校验
				StringBuffer stringBuffer = new StringBuffer();
				for (String param : parameterList) {

					String[] paramSplit = param.split(":");

					if (paramSplit.length != 2) {
						log.info("存在规格参数异常的数据,批量发布失败！");
						reason.append("存在规格参数异常的数据!");
					}

					String name = paramSplit[0];
					String value = paramSplit[1];
					//校验规格参数,拼接当前规格id规格值id
					String propertiesId = getPropertiesId(name, value, shopId);
					if (StrUtil.isBlank(propertiesId)) {
						log.info("规格异常新建规格失败,批量发布失败！");
						reason.append("规格异常新建规格失败!");
					}
					//拼接所有选中规格id规格值id
					stringBuffer.append(propertiesId);
				}
				propertiesIds = stringBuffer.toString();
				// 检查是否字符串以分号结尾
				if (propertiesIds.endsWith(";")) {
					// 删除末尾的分号字符
					propertiesIds = propertiesIds.substring(0, propertiesIds.length() - 1);
				}
				//获取主规格参数
				String main = parameterList[0];
				String[] mainList = main.split(":");
				String[] split = propertiesIds.split(";");
				String s = split[0];
				String[] split1 = s.split(":");
				ProductPropertyImageDTO productPropertyImageDTO = new ProductPropertyImageDTO();
				productPropertyImageDTO.setValueName(mainList[1]);
				productPropertyImageDTO.setPropId(Long.valueOf(split1[0]));
				productPropertyImageDTO.setValueId(Long.valueOf(split1[1]));
				e.setProductPropertyImageDTO(productPropertyImageDTO);
				e.setPropertiesId(propertiesIds);

				List<String> nameId = new ArrayList();
				List<String> valueId = new ArrayList();
				String[] split2 = propertiesIds.toString().split(";");
				for (String s1 : split2) {
					String[] split3 = s1.split(":");
					nameId.add(split3[0]);
					valueId.add(split3[1]);
				}
				e.setSpecificationId(nameId);
				e.setSpecificationValueId(valueId);
			}

			//商品属性校验
			if (ObjectUtil.isNotEmpty(e.getProperties())) {
				String properties = e.getProperties();

				properties = checks(properties);
				String[] propertiesList = properties.split(";");


				StringBuffer stringBuffer = new StringBuffer();
				StringBuffer propertie = new StringBuffer();
				String userPropertie = null;
				List<ProductPropertyDTO> productPropertyList = new ArrayList<>();
				List<Long> propertiesIdList = new ArrayList<>();
				List<Long> propertiesValueIdList = new ArrayList<>();
				for (String productProperties : propertiesList) {
					String[] paramSplit = productProperties.split(":");
					if (paramSplit.length != 2) {
						log.info("存在属性参数异常的数据,批量发布失败！");
						reason.append("存在属性参数异常的数据!");
					}
					String name = paramSplit[0];
					String value = paramSplit[1];

					ProductProperty property = productPropertyDao.getProperty(name, ProductPropertyAttributeTypeEnum.PARAMETER.value(), shopId);

					Long id;
					//没有对应属性，就新建
					if (ObjectUtil.isEmpty(property)) {
						ProductProperty productProperty = new ProductProperty();
						productProperty.setDeleteFlag(Boolean.FALSE);
						productProperty.setPropName(name);
						productProperty.setType(ProductPropertyTypeEnum.TXT.getValue());
						productProperty.setAttributeType(ProductPropertyAttributeTypeEnum.PARAMETER.value());
						productProperty.setSource(ProductPropertySourceEnum.USER.value());
						productProperty.setCreateTime(now);
						productProperty.setShopId(shopId);
						Long propertyId = productPropertyDao.save(productProperty);
						ProductPropertyValue propertyValue = new ProductPropertyValue();
						propertyValue.setPropId(propertyId);
						propertyValue.setDeleteFlag(Boolean.FALSE);
						propertyValue.setShopId(shopId);
						propertyValue.setSequence(1);
						propertyValue.setName(value);
						propertyValue.setCreateTime(now);
						id = productPropertyValueDao.save(propertyValue);
					} else {
						id = property.getId();

					}
					propertiesIdList.add(id);

					String ids = id.toString();
					String[] valueList = value.split(",");
					Long productPropertyValueId = null;
					//有属性,开始校验属性值，没有属性值就新建属性值
					if (ObjectUtil.isNotEmpty(property)) {
						ProductPropertyValue productPropertyValue = productPropertyValueDao.getByPropId(property.getId(), value);
						//
						List<ProductPropertyValueDTO> productPropertyValueDTOS = null;

						if (ObjectUtil.isEmpty(productPropertyValue)) {

							ProductPropertyValue propertyValue = new ProductPropertyValue();
							propertyValue.setPropId(id);
							propertyValue.setDeleteFlag(Boolean.FALSE);
							if (property.getSource().equals(ProductPropertySourceEnum.USER.getValue())) {
								propertyValue.setShopId(shopId);
							}

							propertyValue.setName(value);
							propertyValue.setSequence(1);
							propertyValue.setCreateTime(now);
							productPropertyValueId = productPropertyValueDao.save(propertyValue);
						} else {

							productPropertyValueId = productPropertyValue.getId();

						}
						propertiesValueIdList.add(productPropertyValueId);

					}
				}
				e.setPropertiesIdList(propertiesIdList);
				e.setPropertiesValueIdList(propertiesValueIdList);
				e.setProperties(JSONUtil.toJsonStr(productPropertyList));

				if (productName.length() > 60) {
					productName = productName.substring(0, 60);
					e.setProductName(productName);
				}
				String skuName = e.getSkuName();
				if (ObjectUtil.isNotEmpty(skuName) && skuName.length() > 60) {
					skuName = skuName.substring(0, 60);
					e.setSkuName(skuName);
				}

				if (ObjectUtil.isNotEmpty(e.getBrief())) {
					String brief = e.getBrief();
					if (brief.length() > 500) {
						brief = brief.substring(0, 60);
						e.setBrief(brief);
					}

				}//
				if (ObjectUtil.isNotEmpty(e.getPartyCode())) {
					String partyCode = e.getPartyCode();
					if (partyCode.length() > 30) {
						partyCode = partyCode.substring(0, 30);
						e.setPartyCode(partyCode);
					}

				}
				if (ObjectUtil.isNotEmpty(e.getModelId())) {
					String modelId = e.getModelId();
					if (modelId.length() > 50) {
						modelId = modelId.substring(0, 50);
						e.setModelId(modelId);
					}

				}
			}
			if (StrUtil.isNotBlank(reason.toString())) {
				ProductImportDetailDTO productImportDetailDTO = skuConverter(e, reason.toString());
				importList.add(productImportDetailDTO);
			}

		}
		//正常sku集合
		List<SkuImportDTO> skuList = new ArrayList<>();
		log.info("错误sku列表{}", importList);
		//获取异常数据商品名字
		List<String> nameList = importList.stream().distinct().map(ProductImportDetailDTO::getProductName).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
		//过滤异常数据获取正常的sku数据
		list.forEach(e ->
				{
					String productName = e.getProductName();
					if (!nameList.contains(productName)) {
						skuList.add(e);
					}
				}

		);
		List<String> productName = skuList.stream().map(SkuImportDTO::getProductName).distinct().collect(Collectors.toList());
		productName.forEach(
				e ->

				{
//					List<String> brief = new ArrayList<>();
//					List<String> type = new ArrayList<>();
					List<String> picList = new ArrayList<>();

					List<String> contentList = new ArrayList<>();

					ProductDTO product = new ProductDTO();
					List<SkuImportDTO> sku = skuList.stream().filter(SkuImportDTO -> SkuImportDTO.getProductName().equals(e)).collect(Collectors.toList());
					List<String> specification = sku.stream().distinct().map(SkuImportDTO::getSpecification).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
					List<ProductPropertyImageDTO> productPropertyImage = new ArrayList<>();
					if (sku.size() > 0) {
						List<String> idList = new ArrayList<>();

						int idSize = sku.get(0).getSpecificationId().size();
						int size = sku.size();

						for (int i = 0; i < size; i++) {
							List<String> specificationId = sku.get(i).getSpecificationId();
							specificationId.forEach(id -> idList.add(id));
						}
						List<String> id = idList.stream().distinct().collect(Collectors.toList());
						//以第一个sku为主规格其他sku规格多了或者错了比如主规格的规格重复,子规格和主规格不一致
						if (id.size() < idSize || id.size() > idSize) {
							for (SkuImportDTO skuImportDTO : sku) {
								log.info("sku规格不一致或规格重复,批量发布失败！");
								ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "sku规格不一致或规格重复,批量发布失败！");
								importList.add(productImportDetailDTO);

							}
							return;

						}

						//以第一个sku为主规格其他sku规格少了
						Boolean flag = Boolean.TRUE;
						for (SkuImportDTO skuImportDTO : sku) {
							if (idSize > skuImportDTO.getSpecificationId().size()) {
								flag = Boolean.FALSE;
								return;
							}
						}
						if (!flag) {
							for (SkuImportDTO skuImportDTO : sku) {
								log.info("sku规格不一致,批量发布失败！");
								ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "sku规格不一致,批量发布失败！");
								importList.add(productImportDetailDTO);

							}
							return;
						}
					}
					if (sku.size() > 1 && sku.size() > specification.size()) {
						for (SkuImportDTO skuImportDTO : sku) {
							log.info("存在规格参数重复或者多规格商品规格为空的数据,批量发布失败！");
							ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "存在规格参数重复或者多规格商品规格为空的数据,批量发布失败！");
							importList.add(productImportDetailDTO);

						}
						return;
					}

					List<SkuDTO> skuDTOS = new ArrayList<>();

					List<String> specificationValueList = new ArrayList<>();
					int stocks = 0;
					//规格图片标识
					Boolean flag = Boolean.TRUE;
					StringBuffer properties = new StringBuffer();
					//所有选中规格值id
					List<String> allValueId = new ArrayList<>();
					for (SkuImportDTO skuImportDTO : sku) {
						if (ObjectUtil.isNotEmpty(skuImportDTO.getSpecificationId()) && ObjectUtil.isNotEmpty(skuImportDTO.getSpecificationValueId())) {

							List<String> specificationValueId = skuImportDTO.getSpecificationValueId();

							specificationValueId.forEach(a -> {
								specificationValueList.add(a);
							});
						}
						List<String> specificationList = new ArrayList<>();
						if (sku.size() == 1 && ObjectUtil.isEmpty(skuImportDTO.getSpecification())) {
							//单规格
							product.setSpecification("[]");
							product.setMultipleSpecificationFlag(Boolean.FALSE);
							product.setSpecificationStyle(ProductSpecificationStyle.TXT.getValue());

						} else {
							//多规格

							List<String> specificationValueId = skuImportDTO.getSpecificationValueId();
							for (String id : specificationValueId) {
								allValueId.add(id);
							}
							//productPropertyValueDao.getAllProductPropertyValue()
							product.setMultipleSpecificationFlag(Boolean.TRUE);
							product.setSpecificationStyle(ProductSpecificationStyle.TXT.getValue());
						}

						//校验图片获取图片路径
						if (ObjectUtil.isNotEmpty(skuImportDTO.getDetailPic())) {
							String productDetailPic = skuImportDTO.getDetailPic();
							productDetailPic = checks(productDetailPic);
							skuImportDTO.setDetailPic(productDetailPic);
							String[] productDetailPicSplit = productDetailPic.split(";");
							if (ObjectUtil.isNotEmpty(skuImportDTO.getPropertiesId())) {
								String propertiesId = skuImportDTO.getPropertiesId();
								properties = properties.append(propertiesId);
							}

							//content取第一个sku的详情图片(多张需拼接)
//							String content = null;
							StringBuffer buffer = new StringBuffer();
							for (String path : productDetailPicSplit) {
								StringBuffer stringBuffer = new StringBuffer();
								String url = productImportDetailService.getUrl(path, shopUserId);
								if (StrUtil.isNotBlank(url)) {
									String urlPath = stringBuffer.append(prefix).append(url).toString();
									String format = StrUtil.format(DETAIL_IMG_FORMAT, urlPath);
//									content = buffer.append(format).toString();
									buffer.append(format);

								}

							}
							contentList.add(buffer.toString());


						}
						String pic = skuImportDTO.getPic();
						pic = checks(pic);
						String[] picSplit = pic.split(";");
						for (String picPath : picSplit) {
							String url = productImportDetailService.getUrl(picPath, shopUserId);
							if (StrUtil.isNotBlank(url)) {
								picList.add(url);
							}

						}


						//多规格商品只要有一个sku没有图片就报错
						if (ObjectUtil.isNotEmpty(skuImportDTO.getSpecificationPic())) {
							String specificationPic = skuImportDTO.getSpecificationPic();
							specificationPic = checks(specificationPic);
							skuImportDTO.setSpecificationPic(specificationPic);
							String[] specificationPicSplit = specificationPic.split(";");
							List<String> specificationPicUrl = new ArrayList<>();
							for (String specifications : specificationPicSplit) {
								String url = productImportDetailService.getUrl(specifications, shopUserId);
								specificationList.add(url);
								specificationPicUrl.add(url);
								if (StrUtil.isBlank(url)) {
									flag = Boolean.FALSE;
									continue;
								}
							}

							ProductPropertyImageDTO productPropertyImageDTO = skuImportDTO.getProductPropertyImageDTO();
							productPropertyImageDTO.setUrl(JSONUtil.toJsonStr(specificationList));
							productPropertyImageDTO.setCreateDate(now);
							productPropertyImageDTO.setImgList(specificationList);
							productPropertyImage.add(productPropertyImageDTO);
							skuImportDTO.setSpecificationUrl(specificationPicUrl);
						}

						SkuDTO skuDTO = new SkuDTO();
						skuDTO.setName(skuImportDTO.getSkuName());

						int stock = Integer.parseInt(skuImportDTO.getStocks());
						stocks = stocks + stock;

						skuDTO.setStocks(stock);
						skuDTO.setActualStocks(stock);
						skuDTO.setBuys(0);
						skuDTO.setCostPrice(new BigDecimal(skuImportDTO.getCostPrice()).setScale(2, RoundingMode.HALF_UP));
						skuDTO.setPrice(new BigDecimal(skuImportDTO.getPrice()).setScale(2, RoundingMode.HALF_UP));
						if (ObjectUtil.isNotEmpty(skuImportDTO.getOriginalPrice())) {
							skuDTO.setOriginalPrice(new BigDecimal(skuImportDTO.getOriginalPrice()).setScale(2, RoundingMode.HALF_UP));
						}
						if (ObjectUtil.isNotEmpty(specificationList)) {
							skuDTO.setPic(specificationList.get(0));
						}
						skuDTO.setCnProperties(skuImportDTO.getSpecification());
						String propertiesId = skuImportDTO.getPropertiesId();
						String[] split = propertiesId.split(";");
						skuDTO.setProperties(skuImportDTO.getPropertiesId());
						if (split.length == 1) {
							skuDTO.setProperties(split[0]);
						}

						skuDTO.setIntegralFlag(Boolean.FALSE);
						skuDTO.setIntegralDeductionFlag(Boolean.FALSE);
						skuDTO.setCreateTime(now);
						skuDTOS.add(skuDTO);
						if (ObjectUtil.isNotEmpty(skuImportDTO.getModelId())) {
							skuDTO.setModelId(skuImportDTO.getModelId());
						}
						if (ObjectUtil.isNotEmpty(skuImportDTO.getPartyCode())) {
							skuDTO.setModelId(skuImportDTO.getPartyCode());
						}


					}
//					int size = allValueId.size();
//					int size1 = allValueId.stream().distinct().collect(Collectors.toList()).size();
//					if(size>size1){
//						for (SkuImportDTO skuImportDTO : sku) {
//							log.info("存在规格值重复数据,批量发布失败！");
//							ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "存在规格值重复的数据,批量发布失败！");
//							importList.add(productImportDetailDTO);
//
//						}
//					}
					if (!flag) {
						for (SkuImportDTO skuImportDTO : sku) {
							log.info("规格图url错误请上传图片,批量发布失败！");
							ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "规格图url错误请上传图片,批量发布失败！");
							importList.add(productImportDetailDTO);
						}
						return;
					}

					if (stocks > 999999) {
						for (SkuImportDTO skuImportDTO : sku) {
							log.info("sku库存总和不能超过七位数,批量发布失败！");
							ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "sku库存总和不能超过七位数批量发布失败！");
							importList.add(productImportDetailDTO);
						}
						return;
					}
					if (ObjectUtil.isEmpty(picList)) {
						for (SkuImportDTO skuImportDTO : sku) {
							log.info("图片主图url错误请上传图片,批量发布失败！");
							ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "图片主图url错误请上传图片,批量发布失败！");
							importList.add(productImportDetailDTO);
						}
						return;
					}
					if (ObjectUtil.isNotEmpty(sku.get(0).getDetailPic()) && ObjectUtil.isEmpty(contentList.get(0))) {
						for (SkuImportDTO skuImportDTO : sku) {
							log.info("图片详情url错误请上传图片,批量发布失败！");
							ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "图片详情url错误请上传图片,批量发布失败！");
							importList.add(productImportDetailDTO);
						}
						return;
					}
//					//specification字段规格属性拼接
//					if(ObjectUtil.isNotEmpty(properties)){
//						String[] specifications = properties.toString().split(";");
//						for (String str : specifications) {
//
//						}
//					}

					List<ProductPropertyBO> customPropertyValueList = new ArrayList<>();
					//获得属性id数据
					if (ObjectUtil.isNotEmpty(sku.get(0).getPropertiesIdList()) && ObjectUtil.isNotEmpty(sku.get(0).getPropertiesValueIdList())) {
						List<Long> propertiesIdList = sku.get(0).getPropertiesIdList();
						List<Long> propertiesValueId = sku.get(0).getPropertiesValueIdList();

						propertiesIdList.forEach(
								propertiesId -> {
									List<ProductPropertyValueBO> prodPropertyList = new ArrayList<>();
									ProductPropertyBO detailById = productPropertyService.getDetailById(propertiesId);
									List<ProductPropertyValueBO> prodPropList = detailById.getProdPropList();
									for (ProductPropertyValueBO productPropertyValueBO : prodPropList) {
										ProductPropertyValueBO productPropertyValueDTO = new ProductPropertyValueBO();
										productPropertyValueDTO.setPropId(propertiesId);
										productPropertyValueDTO.setDeleteFlag(Boolean.FALSE);
										Long valueId = productPropertyValueBO.getId();
										productPropertyValueDTO.setSelectFlag(Boolean.FALSE);
										if (propertiesValueId.contains(valueId)) {
											productPropertyValueDTO.setSelectFlag(Boolean.TRUE);
										}
										productPropertyValueDTO.setId(valueId);
										productPropertyValueDTO.setName(productPropertyValueBO.getName());
										productPropertyValueDTO.setSequence(productPropertyValueBO.getSequence());
										prodPropertyList.add(productPropertyValueDTO);
									}
									ProductPropertyBO productProperty = new ProductPropertyBO();
									productProperty.setId(propertiesId);
									productProperty.setType(ProductPropertyTypeEnum.PICTURE.getValue());
									productProperty.setPropName(detailById.getPropName());
									productProperty.setProdPropList(prodPropertyList);
									customPropertyValueList.add(productProperty);
								}
						);
					}
					if (ObjectUtil.isNotEmpty(customPropertyValueList)) {
						product.setUserParameter(JSONUtil.toJsonStr(customPropertyValueList));
					}
					//获取主规格id
					String propertiesId = sku.get(0).getPropertiesId();
					String[] split = propertiesId.split(":");
					product.setMainSpecificationId(Long.parseLong(split[0]));
					List<String> specificationId = sku.get(0).getSpecificationId();
					List<SpecificationDTO> specificationDTOS = new ArrayList<>();
					specificationId.forEach(id ->
					{
						List<ProductPropertyValueDTO> prodPropertyList = new ArrayList<>();
						long ids = Long.parseLong(id);
						ProductPropertyBO detailById = productPropertyService.getDetailById(ids);
						List<ProductPropertyValueBO> prodPropList = detailById.getProdPropList();
						for (ProductPropertyValueBO productPropertyValueBO : prodPropList) {
							ProductPropertyValueDTO productPropertyValueDTO = new ProductPropertyValueDTO();
							productPropertyValueDTO.setPropId(ids);
							productPropertyValueDTO.setDeleteFlag(Boolean.FALSE);
							Long valueId = productPropertyValueBO.getId();
							productPropertyValueDTO.setSelectFlag(Boolean.FALSE);
							if (allValueId.contains(valueId.toString())) {
								productPropertyValueDTO.setSelectFlag(Boolean.TRUE);
							}
							if (product.getMainSpecificationId().equals(ids)) {
								for (SkuImportDTO skuImportDTO : sku) {
									if (skuImportDTO.getSpecificationValueId().contains(valueId.toString())) {
										productPropertyValueDTO.setImgList(skuImportDTO.getSpecificationUrl());
									}
								}
							}
							productPropertyValueDTO.setId(valueId);
							productPropertyValueDTO.setName(productPropertyValueBO.getName());
							productPropertyValueDTO.setSequence(productPropertyValueBO.getSequence());
							prodPropertyList.add(productPropertyValueDTO);
						}
						SpecificationDTO specificationDTO = new SpecificationDTO();
						specificationDTO.setId(ids);
						specificationDTO.setType(ProductPropertyTypeEnum.PICTURE.getValue());
						specificationDTO.setPropName(detailById.getPropName());
						specificationDTO.setProdPropList(prodPropertyList);
						specificationDTOS.add(specificationDTO);
					});

					product.setSpecification(JSONUtil.toJsonStr(specificationDTOS));


					product.setImageList(productPropertyImage);
					if (ObjectUtil.isNotEmpty(sku.get(0).getBrief())) {
						product.setBrief(sku.get(0).getBrief());
					}
					if (ObjectUtil.isNotEmpty(picList.get(0))) {
						product.setPic(picList.get(0));
						//去重
						List<String> deduplicatedPicList = picList.stream().distinct().collect(Collectors.toList());
						product.setImg(deduplicatedPicList);
					}
					product.setCreateTime(now);
					if (ObjectUtil.isNotEmpty(contentList.get(0))) {
						product.setContent(contentList.get(0));
					} else if (ObjectUtil.isNotEmpty(sku.get(0).getDetailText())) {
						String detailText = sku.get(0).getDetailText();
						product.setContent("<p>" + detailText + "</p>");
					}

					if (ObjectUtil.isNotEmpty(sku.get(0).getDeliveryType())) {
						String integer = sku.get(0).getDeliveryType();
						if ("1".equals(integer)) {
							product.setDeliveryType(ProductDeliveryTypeEnum.SINCEMENTION.getCode());
						}
						if ("2".equals(integer)) {
							product.setDeliveryType(ProductDeliveryTypeEnum.EXPRESS_DELIVERY.getCode());
						}
						if ("3".equals(integer)) {
							product.setDeliveryType(ProductDeliveryTypeEnum.EXPRESS_DELIVERY_ADN_SINCEMENTION.getCode());
						}

					} else {
						product.setDeliveryType(ProductDeliveryTypeEnum.EXPRESS_DELIVERY.getCode());
					}
					if (ObjectUtil.isNotEmpty(sku.get(0).getBrief())) {
						product.setBrief(sku.get(0).getBrief());
					}
//					if (ObjectUtil.isNotEmpty(sku.get(0).getProperties())) {
//						product.setUserParameter(sku.get(0).getProperties());
//					}
					product.setShopId(shopId);
					product.setShopUserId(shopUserId);
					product.setTransId(sku.get(0).getTransportId());
					product.setSku(skuDTOS);
					product.setDelStatus(ProductDelStatusEnum.PROD_NORMAL.getValue());
					product.setStockCounting(Boolean.FALSE);
					product.setProdType(ProductTypeEnum.ENTITY.value());
					product.setName(sku.get(0).getProductName());
					if (ObjectUtil.isNotEmpty(productPropertyImage)) {
						product.setImageList(productPropertyImage);
					}
					product.setPreSellFlag(Boolean.FALSE);
					product.setGlobalFirstCatId(sku.get(0).getGlobalFirstCatId());
					product.setGlobalSecondCatId(sku.get(0).getGlobalSecondCatId());
					product.setGlobalThirdCatId(sku.get(0).getGlobalThirdCatId());
					product.setOnSaleWay(ProductOnSaleWayEnum.ONSALE.getValue());
					product.setBuys(0);
					product.setComments(0);
					product.setStockCounting(Boolean.FALSE);
					product.setArraignment(Boolean.TRUE);
					product.setOpStatus(OpStatusEnum.PASS.getValue());
					product.setStatus(ProductStatusEnum.PROD_ONLINE.value());
					product.setBatchFlag(Boolean.TRUE);
					try {
						if (flag) {
							R r = productService.save(product);
							if (r.getSuccess()) {
								log.info("商品发布成功！{}", product.getName());
							} else {
								for (SkuImportDTO skuImportDTO : sku) {
									log.info("批量发布失败！");
									ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "批量发布失败！");
									importList.add(productImportDetailDTO);
								}
							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
						log.info("商品发布失败！错误信息:" + ex.getMessage());
						for (SkuImportDTO skuImportDTO : sku) {
							ProductImportDetailDTO productImportDetailDTO = skuConverter(skuImportDTO, "商品发布失败！错误信息:" + ex.getMessage());
							importList.add(productImportDetailDTO);
						}

					}

				}
		);


		// 汇总数据保存
		ProductImportDTO importDTO = new ProductImportDTO();
		importDTO.setShopId(shopId);
		importDTO.setCreateTime(now);
		importDTO.setOperator(username);
		importDTO.setCount(list.size());
		importDTO.setSuccess(list.size() - importList.size());
		importDTO.setFail(importList.size());

		Long id = productImportDao.save(productImportConverter.from(importDTO));
		importList.forEach(e -> e.setImportId(id));
		if (CollUtil.isNotEmpty(importList)) {
			productImportDetailService.save(importList);
		}
		return R.ok();

	}

	@Override
	public List<ImportDTO> template() {
		List<ImportDTO> skuImportDTOS = new ArrayList<>();
		return skuImportDTOS;
	}

	@Override
	public R<PageSupport<ProductImport>> page(ProductQuery query) {
		return productImportDao.page(query);
	}

	@Override
	public List<ProductImportErrorDetailDTO> getInsertProductFailPage(Long importId) {
		return productImportDetailConverter.converterTo(productImportDetailService.getImportId(importId));
	}


	private String checkNum(String string, int i) {
		if (i == 6) {
			if (Integer.parseInt(string) > 999999) {
				string = "999999";
			}
			return string;
		}
		BigDecimal prices = new BigDecimal(string);
		string = prices.setScale(2, RoundingMode.HALF_UP).toString();
		if (string.length() > i) {
			if (i == 7) {
				if (Integer.parseInt(string) > 9999999) {
					string = "9999999.00";
				}

			}

		}

		return string;
	}

	private String checks(String string) {
		//去换行符
		string = StrUtil.removeAllLineBreaks(string);
		//去空格
		string = string.replaceAll(" ", "");
		//标点符号转换防止全角输入
		string = Convert.toDBC(string);
		return string;
	}


	public String getPropertiesId(String name, String value, Long shopId) {
		ProductProperty property = productPropertyDao.getProperty(name, ProductPropertyAttributeTypeEnum.SPECIFICATION.value(), shopId);
		StringBuffer stringBuffer = new StringBuffer();
		Date date = new Date();
		Long productPropertyValueId = null;
		Long id;
		//如果找不到对应规格，就新建
		if (ObjectUtil.isEmpty(property)) {
			ProductProperty productProperty = new ProductProperty();
			productProperty.setDeleteFlag(Boolean.FALSE);
			productProperty.setPropName(name);
			productProperty.setType(ProductPropertyTypeEnum.TXT.getValue());
			productProperty.setAttributeType(ProductPropertyAttributeTypeEnum.SPECIFICATION.value());
			productProperty.setCreateTime(date);
			productProperty.setType(ProductPropertyTypeEnum.TXT.getValue());
			productProperty.setSource(ProductPropertySourceEnum.USER.value());
			productProperty.setShopId(shopId);
			id = productPropertyDao.save(productProperty);
			ProductPropertyValue propertyValue = new ProductPropertyValue();
			propertyValue.setPropId(id);
			propertyValue.setDeleteFlag(Boolean.FALSE);
			propertyValue.setShopId(shopId);
			propertyValue.setSequence(1);
			propertyValue.setName(value);
			propertyValue.setCreateTime(date);
			productPropertyValueId = productPropertyValueDao.save(propertyValue);
		} else {
			id = property.getId();
		}


		//校验规格值，就新建规格值
		if (ObjectUtil.isNotEmpty(property)) {
			ProductPropertyValue productPropertyValue = productPropertyValueDao.getByPropId(property.getId(), value);

			if (ObjectUtil.isEmpty(productPropertyValue)) {
				//如果该规格是平台规格就创建商家自定义规格及参数
				if (property.getSource().equals(ProductPropertySourceEnum.SYSTEM.getValue())) {
					ProductProperty productProperty = new ProductProperty();
					productProperty.setDeleteFlag(Boolean.FALSE);
					productProperty.setPropName(name);
					productProperty.setType(ProductPropertyTypeEnum.TXT.getValue());
					productProperty.setAttributeType(ProductPropertyAttributeTypeEnum.SPECIFICATION.value());
					productProperty.setCreateTime(date);
					productProperty.setType(ProductPropertyTypeEnum.TXT.getValue());
					productProperty.setSource(ProductPropertySourceEnum.USER.value());
					productProperty.setShopId(shopId);
					id = productPropertyDao.save(productProperty);
					ProductPropertyValue propertyValue = new ProductPropertyValue();
					propertyValue.setPropId(id);
					propertyValue.setDeleteFlag(Boolean.FALSE);
					propertyValue.setShopId(shopId);
					propertyValue.setSequence(1);
					propertyValue.setName(value);
					propertyValue.setCreateTime(date);
					productPropertyValueId = productPropertyValueDao.save(propertyValue);
				} else {
					ProductPropertyValue propertyValue = new ProductPropertyValue();
					propertyValue.setPropId(property.getId());
					propertyValue.setDeleteFlag(Boolean.FALSE);
					propertyValue.setShopId(shopId);
					propertyValue.setName(value);
					propertyValue.setSequence(1);
					propertyValue.setCreateTime(date);
					productPropertyValueId = productPropertyValueDao.save(propertyValue);
				}
			} else {
				productPropertyValueId = productPropertyValue.getId();
			}
		}
		String propertiesId = stringBuffer.append(id.toString() + ":").append(productPropertyValueId.toString() + ";").toString();
		return propertiesId;
	}

	private ProductImportDetailDTO skuConverter(SkuImportDTO e, String fail) {
		ProductImportDetailDTO productImportDetailDTO = skuImportConverter.to(e);
		productImportDetailDTO.setFailReason(fail);
		productImportDetailDTO.setCreateTime(new Date());
		if (ObjectUtil.isNotEmpty(e.getCateName())) {
			productImportDetailDTO.setCateName(e.getCateName());
		}

		log.info("错误数据{}", productImportDetailDTO);
		return productImportDetailDTO;
	}
}


