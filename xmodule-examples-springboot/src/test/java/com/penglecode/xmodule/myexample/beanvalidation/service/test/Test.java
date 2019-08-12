package com.penglecode.xmodule.myexample.beanvalidation.service.test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Test {

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://172.16.18.225:8000/api/v1/device";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Host", "metadata");
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		System.out.println(response);
		System.out.println(response.getBody());
	}

}
