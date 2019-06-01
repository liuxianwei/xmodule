package com.penglecode.xmodule.springcloud.nacosexample.service.api;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FallbackableFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.CommonHystrixFallbackFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.springcloud.nacosexample.model.Product;

/**
 * 商品API服务
 * 
 * @author 	pengpeng
 * @date	2018年10月8日 下午2:43:58
 */
@FeignClient(name="springcloud-nacos-producer", qualifier="productApiService", contextId="productApiService", fallbackFactory=CommonHystrixFallbackFactory.class)
public interface ProductApiService extends FallbackableFeignClient {

	/**
	 * 创建商品
	 * @param product
	 */
	@PostMapping(value="/api/product/add", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Long> createProduct(@RequestBody Product product);
	
	/**
	 * 修改商品
	 * @param product
	 */
	@PutMapping(value="/api/product/update", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> updateProduct(@RequestBody Product product);
	
	/**
	 * 删除商品
	 * @param productId
	 */
	@DeleteMapping(value="/api/product/delete/{productId}", produces=APPLICATION_JSON)
	public Result<Object> deleteProductById(@PathVariable("productId") Long productId);
	
	/**
	 * 根据productId获取商品信息
	 * @param productId
	 * @return
	 */
	@GetMapping(value="/api/product/{productId}", produces=APPLICATION_JSON)
	public Result<Product> getProductById(@PathVariable("productId") Long productId);
	
	/**
	 * 根据条件查询商品列表(分页、排序)
	 * @param condition
	 * @param page
	 * @param sort
	 * @return
	 */
	@GetMapping(value="/api/product/list1", produces=APPLICATION_JSON)
	public PageResult<List<Product>> getProductListByPage1(@RequestParam Product condition, @RequestParam Page page, @RequestParam Sort sort);
	
	/**
	 * 根据条件查询商品列表(分页、排序)
	 * @param statuses
	 * @return
	 */
	@GetMapping(value="/api/product/list2", produces=APPLICATION_JSON)
	public PageResult<List<Product>> getProductListByPage2(@RequestParam Map<String,Object> parameter);
	
}
