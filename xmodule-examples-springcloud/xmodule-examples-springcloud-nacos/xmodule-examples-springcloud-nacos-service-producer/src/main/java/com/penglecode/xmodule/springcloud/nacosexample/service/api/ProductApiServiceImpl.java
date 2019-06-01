package com.penglecode.xmodule.springcloud.nacosexample.service.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.BeanUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.springcloud.nacosexample.model.Product;

@RestController("defaultProductApiService")
public class ProductApiServiceImpl extends HttpAPIResourceSupport implements ProductApiService {

	private static final List<Product> ALL_PRODUCT_LIST = new ArrayList<Product>();
	
	static {
		ALL_PRODUCT_LIST.add(new Product(1L, "华为 Mate 9 Pro", 4709.0, 13, "1"));
		ALL_PRODUCT_LIST.add(new Product(2L, "苹果 iPhone 7 Plus 5.5寸", 5056.0, 3, "1"));
		ALL_PRODUCT_LIST.add(new Product(3L, "苹果 iPhone 6 4.7寸", 2999.0, 0, "1"));
		ALL_PRODUCT_LIST.add(new Product(4L, "MyCard 台湾/香港 300台币游戏充值卡", 67.8, 11, "0"));
		ALL_PRODUCT_LIST.add(new Product(5L, "OPPO R9手机 全网通", 2168.0, 5, "1"));
		ALL_PRODUCT_LIST.add(new Product(6L, "Letv/乐视 乐Pro3全网通", 1799.0, 74, "1"));
		ALL_PRODUCT_LIST.add(new Product(7L, "Xiaomi/小米 5S Plus", 1599.0, 0, "1"));
		ALL_PRODUCT_LIST.add(new Product(8L, "中国移动充值卡100元", 98.0, 0, "0"));
	}
	
	@Override
	public Result<Long> createProduct(Product product) {
		ValidationAssert.notNull(product, "参数不能为空!");
		Long maxId = 1L;
		for(Product p : ALL_PRODUCT_LIST) {
			if(p.getProductId() > maxId) {
				maxId = p.getProductId();
			}
		}
		product.setProductId(maxId + 1);
		ALL_PRODUCT_LIST.add(product);
		return Result.success().message("OK").data(product.getProductId()).build();
	}

	@Override
	public Result<Object> updateProduct(Product product) {
		ValidationAssert.notNull(product, "参数不能为空!");
		Map<String,Object> parameter = BeanUtils.beanToMap(product);
		for(Iterator<Map.Entry<String,Object>> it = parameter.entrySet().iterator(); it.hasNext();) {
			if(it.next().getValue() == null) {
				it.remove();
			}
		}
		
		Product oldProduct = ALL_PRODUCT_LIST.stream().filter(p -> p.getProductId().equals(product.getProductId())).findFirst().orElse(null);
		if(oldProduct != null) {
			BeanUtils.populate(oldProduct, parameter);
		}
		return Result.success().message("OK").build();
	}

	@Override
	public Result<Object> deleteProductById(Long productId) {
		for(Iterator<Product> it = ALL_PRODUCT_LIST.iterator(); it.hasNext();) {
			if(it.next().getProductId().equals(productId)) {
				it.remove();
			}
		}
		return Result.success().message("OK").build();
	}

	@Override
	public Result<Product> getProductById(Long productId) {
		Product target = ALL_PRODUCT_LIST.stream().filter(p -> p.getProductId().equals(productId)).findFirst().orElse(null);
		return Result.success().message("OK").data(target).build();
	}

	@Override
	public PageResult<List<Product>> getProductListByPage1(Product condition, Page page, Sort sort) {
		System.out.println("server.port = " + getEnvironment().getProperty("server.port"));
		List<Product> resultList = ALL_PRODUCT_LIST.stream().filter(p -> {
			boolean matched = true;
			if(matched && !StringUtils.isEmpty(condition.getProductName())) {
				matched = matched && p.getProductName().contains(condition.getProductName());
			}
			if(matched && !StringUtils.isEmpty(condition.getProductType())) {
				matched = matched && p.getProductType().equals(condition.getProductType());
			}
			return matched;
		}).collect(Collectors.toList());
		return PageResult.success().message("OK").data(resultList).totalRowCount(resultList.size()).build();
	}

	@Override
	public PageResult<List<Product>> getProductListByPage2(Map<String, Object> parameter) {
		List<Product> resultList = ALL_PRODUCT_LIST.stream().filter(p -> {
			boolean matched = true;
			if(matched && !StringUtils.isEmpty(MapUtils.getString(parameter, "productName"))) {
				matched = matched && p.getProductName().contains(MapUtils.getString(parameter, "productName"));
			}
			if(matched && !StringUtils.isEmpty(MapUtils.getString(parameter, "productType"))) {
				matched = matched && p.getProductType().equals(MapUtils.getString(parameter, "productType"));
			}
			return matched;
		}).collect(Collectors.toList());
		return PageResult.success().message("OK").data(resultList).totalRowCount(resultList.size()).build();
	}

}
