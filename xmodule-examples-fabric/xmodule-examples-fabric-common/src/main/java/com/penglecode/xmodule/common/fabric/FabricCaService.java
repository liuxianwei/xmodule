package com.penglecode.xmodule.common.fabric;

/**
 * Fabric CA服务
 * 
 * @author 	pengpeng
 * @date	2018年11月22日 下午6:59:48
 */
public interface FabricCaService {

	/**
	 * 登记用户
	 * @param caClient
	 * @param unenrolledUser
	 * @return
	 * @throws Exception
	 */
	public FabricUser enrollUser(FabricUser unenrolledUser) throws Exception;

}
