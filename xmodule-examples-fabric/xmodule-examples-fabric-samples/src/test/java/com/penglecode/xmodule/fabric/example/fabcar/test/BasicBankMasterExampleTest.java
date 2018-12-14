package com.penglecode.xmodule.fabric.example.fabcar.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.common.fabric.ChaincodeService;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.fabric.example.boot.FabricExampleApplication;
import com.penglecode.xmodule.fabric.example.config.BasicBankMasterExampleConfiguration;

/**
 * bankmaster示例的智能合约：
 * JAVA：fabric-samples/chaincode/bankmaster/java
 * 
 * @author 	pengpeng
 * @date	2018年11月14日 上午9:56:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={FabricExampleApplication.class})
public class BasicBankMasterExampleTest {

	@Autowired
	private ChaincodeService chaincodeService;
	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("example.running", BasicBankMasterExampleConfiguration.EXAMPLE_APP_NAME);
	}
	
	/**
	 * 创建账户
	 * @throws Exception
	 */
	@Test
	public void createAccount1() throws Exception {
		Map<String,Object> account = new HashMap<String,Object>();
		account.put("realName", "彭鹏");
		account.put("idCardNo", "342425198805281442");
		account.put("mobilePhone", "13812345678");
		account.put("accountBalance", 1000);
		Result<String> result = chaincodeService.executeUpdate("createAccount", new String[] {JsonUtils.object2Json(account)});
		System.out.println("【createAccount】>>> result = " + result);
		//【createAccount】>>> result = Result [success=true, code=200, message=null, data={"accountNo":"6225867744627441","realName":"彭鹏","idCardNo":"342425198805281442","mobilePhone":"13812345678","createdTime":"2018-12-07 02:31:29","accountBalance":1000.0}]
	}
	
	/**
	 * 创建账户
	 * @throws Exception
	 */
	@Test
	public void createAccount2() throws Exception {
		Map<String,Object> account = new HashMap<String,Object>();
		account.put("realName", "马伟");
		account.put("idCardNo", "320800198805281442");
		account.put("mobilePhone", "13812345678");
		account.put("accountBalance", 1000);
		Result<String> result = chaincodeService.executeUpdate("createAccount", new String[] {JsonUtils.object2Json(account)});
		System.out.println("【createAccount】>>> result = " + result);
		//【createAccount】>>> result = Result [success=true, code=200, message=null, data={"accountNo":"6225487335899570","realName":"马伟","idCardNo":"320800198805281442","mobilePhone":"13812345678","createdTime":"2018-12-07 02:39:37","accountBalance":1000.0}]
	}
	
	/**
	 * 查询账户余额
	 * @throws Exception
	 */
	@Test
	public void getAccountBalance() throws Exception {
		//String accountNo = "6225487335899570";
		String accountNo = "6225867744627441";
		Result<String> result = chaincodeService.executeQuery("getAccountBalance", new String[] {accountNo});
		System.out.println("【getAccountBalance】>>> result = " + result);
	}
	
	/**
	 * 存款
	 * @throws Exception
	 */
	@Test
	public void depositMoney() throws Exception {
		String accountNo = "6225867744627441";
		String amount = "500";
		Result<String> result = chaincodeService.executeUpdate("depositMoney", new String[] {accountNo, amount});
		System.out.println("【depositMoney】>>> result = " + result);
	}
	
	/**
	 * 取款
	 * @throws Exception
	 */
	@Test
	public void drawalMoney() throws Exception {
		String accountNo = "6225867744627441";
		String amount = "500";
		Result<String> result = chaincodeService.executeUpdate("drawalMoney", new String[] {accountNo, amount});
		System.out.println("【drawalMoney】>>> result = " + result);
	}
	
	/**
	 * 转账
	 * @throws Exception
	 */
	@Test
	public void transferAccount() throws Exception {
		Result<String> result = null;
		String accountA = "6225867744627441";
		String accountB = "6225487335899570";
		String amount = "200";
		
		result = chaincodeService.executeQuery("getAccountBalance", new String[] {accountA});
		System.out.println("【getAccountBalance】>>> accountA = " + accountA + " result = " + result);
		
		result = chaincodeService.executeQuery("getAccountBalance", new String[] {accountB});
		System.out.println("【getAccountBalance】>>> accountB = " + accountB + " result = " + result);
		
		System.out.println("--------------------------------------转账前----------------------------------------");
		
		result = chaincodeService.executeUpdate("transferAccount", new String[] {accountA, accountB, amount});
		System.out.println("【transferAccount】>>> result = " + result);
		
		System.out.println("--------------------------------------转账后----------------------------------------");
		
		result = chaincodeService.executeQuery("getAccountBalance", new String[] {accountA});
		System.out.println("【getAccountBalance】>>> accountA = " + accountA + " result = " + result);
		
		result = chaincodeService.executeQuery("getAccountBalance", new String[] {accountB});
		System.out.println("【getAccountBalance】>>> accountB = " + accountB + " result = " + result);
	}
	
}
