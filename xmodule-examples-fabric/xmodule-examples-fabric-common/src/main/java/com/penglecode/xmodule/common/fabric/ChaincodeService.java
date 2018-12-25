package com.penglecode.xmodule.common.fabric;

import java.util.Map;

import com.penglecode.xmodule.common.support.Result;

/**
 * 智能合约chaincode服务
 * 
 * @author 	pengpeng
 * @date	2018年11月10日 下午2:57:04
 */
public interface ChaincodeService {

	/**
	 * 调用查询智能合约代码
	 * @param fcn			- 方法名
	 * @param args			- 参数
	 * @return				- 返回值
	 * @throws Exception
	 */
	public Result<String> executeQuery(String fcn, String[] args) throws Exception;
	
	/**
	 * 调用查询智能合约代码
	 * @param fcn				- 方法名
	 * @param args				- 参数
	 * @param transients		- 临时数据不作为事务日志记录到账本中，应用场景：比如传递客户端的当前时间戳到chaincode，以避免在chaincode上获取当前系统时间，可能导致的背书结果不一致
	 * 							  (举个简单例子，创建用户账户使用了chaincode上获取当前系统时间，并且将创建的账户对象返回，很显然账户对象里面createdTime因为机器的差异导致数据的不一致，最终导致背书结果不一致而导致事物失败)
	 * @return					- 返回值
	 * @throws Exception
	 */
	public Result<String> executeQuery(String fcn, String[] args, Map<String,byte[]> transients) throws Exception;
	
	/**
	 * 调用更新智能合约代码
	 * @param fcn			- 方法名
	 * @param args			- 参数
	 * @return				- 返回值
	 * @throws Exception
	 */
	public Result<String> executeUpdate(String fcn, String[] args) throws Exception;
	
	/**
	 * 调用更新智能合约代码
	 * @param fcn			- 方法名
	 * @param args			- 参数
	 * @return				- 返回值
	 * @throws Exception
	 */
	public Result<String> executeUpdateAsync(String fcn, String[] args) throws Exception;
	
	/**
	 * 调用更新智能合约代码
	 * @param fcn				- 方法名
	 * @param args				- 参数
	 * @param transients		- 临时数据不作为事务日志记录到账本中，应用场景：比如传递客户端的当前时间戳到chaincode，以避免在chaincode上获取当前系统时间，可能导致的背书结果不一致
	 * 							  (举个简单例子，创建用户账户使用了chaincode上获取当前系统时间，并且将创建的账户对象返回，很显然账户对象里面createdTime因为机器的差异导致数据的不一致，最终导致背书结果不一致而导致事物失败)
	 * @return					- 返回值
	 * @throws Exception
	 */
	public Result<String> executeUpdate(String fcn, String[] args, Map<String,byte[]> transients) throws Exception;
	
	/**
	 * 调用更新智能合约代码
	 * @param fcn				- 方法名
	 * @param args				- 参数
	 * @param transients		- 临时数据不作为事务日志记录到账本中，应用场景：比如传递客户端的当前时间戳到chaincode，以避免在chaincode上获取当前系统时间，可能导致的背书结果不一致
	 * 							  (举个简单例子，创建用户账户使用了chaincode上获取当前系统时间，并且将创建的账户对象返回，很显然账户对象里面createdTime因为机器的差异导致数据的不一致，最终导致背书结果不一致而导致事物失败)
	 * @return					- 返回值
	 * @throws Exception
	 */
	public Result<String> executeUpdateAsync(String fcn, String[] args, Map<String,byte[]> transients) throws Exception;
	
	public static enum ChaincodeFunctionAccessType {
		
		QUERY, UPDATE;
		
	}
	
}
