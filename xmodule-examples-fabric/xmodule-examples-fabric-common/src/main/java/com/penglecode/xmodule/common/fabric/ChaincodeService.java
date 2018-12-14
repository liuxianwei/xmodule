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
	 * @param transientData		- 临时数据不作为事务日志记录到账本中
	 * @return					- 返回值
	 * @throws Exception
	 */
	public Result<String> executeQuery(String fcn, String[] args, Map<String,byte[]> transientData) throws Exception;
	
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
	 * @param transientData		- 临时数据不作为事务日志记录到账本中
	 * @return					- 返回值
	 * @throws Exception
	 */
	public Result<String> executeUpdate(String fcn, String[] args, Map<String,byte[]> transientData) throws Exception;
	
	/**
	 * 调用更新智能合约代码
	 * @param fcn				- 方法名
	 * @param args				- 参数
	 * @param transientData		- 临时数据不作为事务日志记录到账本中
	 * @return					- 返回值
	 * @throws Exception
	 */
	public Result<String> executeUpdateAsync(String fcn, String[] args, Map<String,byte[]> transientData) throws Exception;
	
	public static enum ChaincodeFunctionAccessType {
		
		QUERY, UPDATE;
		
	}
	
}
