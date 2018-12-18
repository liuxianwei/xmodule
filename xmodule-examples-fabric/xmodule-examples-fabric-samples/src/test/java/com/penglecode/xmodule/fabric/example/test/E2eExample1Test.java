package com.penglecode.xmodule.fabric.example.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.common.fabric.ChaincodeService;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.fabric.example.boot.FabricExampleApplication;
import com.penglecode.xmodule.fabric.example.config.E2eExample1Configuration;

/**
 * e2e_cli示例的智能合约：
 * Go  ：github.com/hyperledger/fabric/examples/chaincode/go/example02/cmd
 * 
 * @author 	pengpeng
 * @date	2018年11月14日 上午9:56:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={FabricExampleApplication.class})
public class E2eExample1Test {

	@Autowired
	private ChaincodeService chaincodeService;
	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("example.running", E2eExample1Configuration.EXAMPLE_APP_NAME);
	}
	
	/**
	 * 查询账户余额
	 * @throws Exception
	 */
	@Test
	public void queryAccount() throws Exception {
		String accountA = "a";
		String accountB = "b";
		Result<String> result = null;
		result = chaincodeService.executeQuery("query", new String[] {accountA});
		System.out.println("【queryAccount】>>> result = " + result);
		if(result.isSuccess()) {
			System.out.println("【queryAccount】>>> a : " + result.get());
		}
		result = chaincodeService.executeQuery("query", new String[] {accountB});
		System.out.println("【queryAccount】>>> result = " + result);
		if(result.isSuccess()) {
			System.out.println("【queryAccount】>>> b : " + result.get());
		}
	}
	
	/**
	 * 转账
	 * (该转账是不成功的，因为由{@E2eExample1Configuration}的配置可知,共两个区块peer0,peer1，两个组织org1,org2
	 * 	但是配置中只有addPeer("peer0.org1.example.com",...)
	 *  因此还缺少addPeer("peer0.org2.example.com",...)
	 *  
	 *  因为：在同一个区块上，更新数据需要得到org1、org2两者的共同背书(共识)才能成功提交最终的更新事务，否则事务不会生效的
	 * )
	 * @throws Exception
	 */
	@Test
	public void transferAccount() throws Exception {
		String accountA = "a";
		String accountB = "b";
		String amount = "10";
		Result<String> result = chaincodeService.executeUpdate("invoke", new String[] {accountB, accountA, amount});
		System.out.println("【transferAccount】>>> result = " + result);
	}
	
}
