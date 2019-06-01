package com.penglecode.xmodule.springcloud.nacosexample.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.springcloud.nacosexample.model.Product;
import com.penglecode.xmodule.springcloud.nacosexample.service.api.ProductApiService;

@RestController
public class ProductController implements ProductApiService {

	@Resource(name="productApiService")
	private ProductApiService productApiService;
	
	@Override
	public Result<Long> createProduct(Product product) {
		return productApiService.createProduct(product);
	}

	@Override
	public Result<Object> updateProduct(Product product) {
		return productApiService.updateProduct(product);
	}

	@Override
	public Result<Object> deleteProductById(Long productId) {
		return productApiService.deleteProductById(productId);
	}

	@Override
	public Result<Product> getProductById(Long productId) {
		return productApiService.getProductById(productId);
	}

	@Override
	public PageResult<List<Product>> getProductListByPage1(Product condition, Page page, Sort sort) {
		return productApiService.getProductListByPage1(condition, page, sort);
	}

	@Override
	public PageResult<List<Product>> getProductListByPage2(Map<String, Object> parameter) {
		return productApiService.getProductListByPage2(parameter);
	}

}
