package com.penglecode.xmodule.myexample.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.myexample.mapper.ProductMapper;
import com.penglecode.xmodule.myexample.model.Product;
import com.penglecode.xmodule.myexample.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createProduct(Product parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		productMapper.insertModel(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateProduct(Product parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		Map<String, Object> paramMap = parameter.mapBuilder()
												.withProductId()
												.withProductType()
												.withCreateTime()
												.withProductName()
												.withUnitPrice()
												.withInventory()
												.withMainCategoryId()
												.build();
		productMapper.updateModelById(parameter.getProductId(), paramMap);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteProductById(Long id) {
		ValidationAssert.notNull(id, "id不能为空");
		productMapper.deleteModelById(id);
	}

	@Override
	public Product getProductById(Long id) {
		return ModelDecodeUtils.decodeModel(productMapper.selectModelById(id));
	}

	@Override
	public List<Product> getProductListByPage(Product condition, Page page, Sort sort) {
		List<Product> dataList = ModelDecodeUtils.decodeModel(productMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(productMapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<Product> getAllProductList() {
		return ModelDecodeUtils.decodeModel(productMapper.selectAllModelList());
	}

}