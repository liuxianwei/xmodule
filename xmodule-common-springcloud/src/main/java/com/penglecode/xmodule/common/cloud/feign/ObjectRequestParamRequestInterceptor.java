package com.penglecode.xmodule.common.cloud.feign;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.Sort.Order;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * spring-cloud-starter-openfeign中针对@RequestParam注解复杂自定义对象时,需要重写queries
 * 
 * @author 	pengpeng
 * @date	2018年10月11日 下午2:30:16
 */
public class ObjectRequestParamRequestInterceptor implements RequestInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectRequestParamRequestInterceptor.class);
	
	private final ObjectMapper objectMapper;
	
	public ObjectRequestParamRequestInterceptor() {
		super();
		this.objectMapper = JsonUtils.createDefaultObjectMapper();
		this.objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}
	
	@Override
	public void apply(RequestTemplate template) {
		try {
			if(template.method().equals("GET")) {
				Map<String, Collection<String>> newQueries = new LinkedHashMap<String, Collection<String>>();
				Map<String, Collection<String>> oldQueries = template.queries();
				Set<String> objectQueryNames = new HashSet<String>();
				if(!CollectionUtils.isEmpty(oldQueries)) {
					Collection<String> values = null;
					for(Map.Entry<String, Collection<String>> entry : oldQueries.entrySet()) {
						values = entry.getValue();
						if(!CollectionUtils.isEmpty(values)) {
							for(String value : values) {
								if(JsonUtils.isJsonObject(value)) {
									applyQueries(new JSONObject(value), newQueries);
									objectQueryNames.add(entry.getKey());
								}
							}
						}
					}
				}
				if(!newQueries.isEmpty()) {
					for(Map.Entry<String, Collection<String>> entry : oldQueries.entrySet()) {
						if(!objectQueryNames.contains(entry.getKey())) { //忽略对象类型的query,保留简单类型的query
							newQueries.put(entry.getKey(), entry.getValue());
						}
					}
					template.queries(null); //先清空
					template.queries(newQueries);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	protected void applyQueries(JSONObject jsonObject, Map<String, Collection<String>> newQueries) {
		String dtoModelTypeKey = DtoModelToStringConverter.PROP_DTO_MODEL_TYPE_KEY;
		String dtoModelTypeValue = jsonObject.optString(dtoModelTypeKey, null);
		if(!StringUtils.isEmpty(dtoModelTypeValue)) {
			if(Page.class.getName().equals(dtoModelTypeValue)) { //处理分页对象
				newQueries.put("currentPage", Collections.singletonList(jsonObject.optString("currentPage", "1")));
				newQueries.put("pageSize", Collections.singletonList(jsonObject.optString("pageSize", "10")));
			} else if(Sort.class.getName().equals(dtoModelTypeValue)) { //处理排序对象
				List<Order> orderList = JsonUtils.json2Object(jsonObject.optString("orders", "[]"), new TypeReference<List<Order>>() {});
				newQueries.put("orders", orderList.stream().map(o -> {
					return o.toString();
				}).collect(Collectors.toList()));
			} else {
				jsonObject.keySet().forEach(key -> {
					if(!dtoModelTypeKey.equals(key)) {
						newQueries.put(key, Collections.singletonList(jsonObject.optString(key, "")));
					}
				});
			}
		}
	}

}
