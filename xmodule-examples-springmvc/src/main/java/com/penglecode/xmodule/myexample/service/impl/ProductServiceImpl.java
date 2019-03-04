package com.penglecode.xmodule.myexample.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.myexample.mapper.ProductMapper;
import com.penglecode.xmodule.myexample.model.Product;
import com.penglecode.xmodule.myexample.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productServiceMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createProduct(Product parameter) {
		Assert.notNull(parameter, "参数不能为空");
		productServiceMapper.insertModel(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateProduct(Product parameter) {
		Assert.notNull(parameter, "参数不能为空");
		productServiceMapper.updateModelById(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteProductById(Long id) {
		Assert.notNull(id, "id不能为空");
		productServiceMapper.deleteModelById(id);
	}

	@Override
	public Product getProductById(Long id) {
		return ModelDecodeUtils.decodeModel(productServiceMapper.selectModelById(id));
	}

	@Override
	public List<Product> getProductListByPage(Product condition, Page page, Sort sort) {
		List<Product> dataList = ModelDecodeUtils.decodeModel(productServiceMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(productServiceMapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<Product> getAllProductList() {
		return ModelDecodeUtils.decodeModel(productServiceMapper.selectAllModelList());
	}

}