package com.penglecode.xmodule.fabric.example.test;

import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.xmodule.common.fabric.ChaincodeService;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.fabric.example.boot.FabricExampleApplication;
import com.penglecode.xmodule.fabric.example.config.FabcarExampleConfiguration;

/**
 * fabcar示例的智能合约：
 * Node：fabric-samples/chaincode/fabcar/node
 * Go  ：fabric-samples/chaincode/fabcar/go
 * 
 * @author 	pengpeng
 * @date	2018年11月14日 上午9:56:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={FabricExampleApplication.class})
public class FabcarExampleTest {

	@Autowired
	private ChaincodeService chaincodeService;
	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("example.running", FabcarExampleConfiguration.EXAMPLE_APP_NAME);
	}
	
	@Test
	public void createCar() throws Exception {
		Result<String> result = chaincodeService.executeUpdate("createCar", new String[] {"CAR10", "Buick", "Regal", "White", "Pengle"});
		System.out.println("【changeCarOwner】>>> result = " + result);
	}
	
	@Test
	public void changeCarOwner() throws Exception {
		Result<String> result = chaincodeService.executeUpdate("changeCarOwner", new String[] {"CAR1", "Pengle"});
		System.out.println("【changeCarOwner】>>> result = " + result);
	}
	
	@Test
	public void queryCar() throws Exception {
		Result<String> result = chaincodeService.executeQuery("queryCar", new String[] {"CAR1"});
		System.out.println("【queryCar】>>> result = " + result);
	}
	
	@Test
	public void queryAllCars() throws Exception {
		Result<String> result = chaincodeService.executeQuery("queryAllCars", null);
		System.out.println("【queryAllCars】>>> result = " + result);
		if(result.isSuccess()) {
			List<Map<String,Object>> dataList = JsonUtils.json2Object(result.getData(), new TypeReference<List<Map<String,Object>>>(){});
			if(!CollectionUtils.isEmpty(dataList)) {
				for(Map<String,Object> item : dataList) {
					System.out.println(">>> " + item);
				}
			}
		}
	}
	
}
