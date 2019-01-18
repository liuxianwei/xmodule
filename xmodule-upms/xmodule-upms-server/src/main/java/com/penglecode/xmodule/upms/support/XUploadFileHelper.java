package com.penglecode.xmodule.upms.support;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;
import com.penglecode.xmodule.common.util.FileUtils;

/**
 * 小文件上传
 * 
 * @author 	pengpeng
 * @date	2018年4月18日 上午8:29:47
 */
public class XUploadFileHelper {

	/**
	 * 将上传的临时文件传送到本地磁盘永久存储
	 * @param contextRealPath			- 项目的contextpath真实文件路径,例如 D:/apache-tomcat-8.5.9/webapps/myproject
	 * @param tempUploadFileName		- 上传的临时文件的相对路径,例如 /upload/temp/ea2a10drt82j13e1j2wkq15gu.jpg
	 * @return 文件永久存储路径(相对), 例如 /images/usericon/abc.jpg
	 */
	public static String transferUserIconToLocalDir(String contextRealPath, String tempUploadFileName) {
		try {
			String srcFullFileName = FileUtils.formatFilePath(contextRealPath + "/" + tempUploadFileName);
			String storeUploadFileName = tempUploadFileName.replace(GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH, GlobalConstants.DEFAULT_USER_ICON_SAVE_PATH);
			String fileServerRootDir = GlobalConstants.MVVM_APP_CONFIG.getGlobalFileServerRoot();
			String destFullFileName = FileUtils.formatFilePath(fileServerRootDir + storeUploadFileName);
			FileUtils.copyFile(srcFullFileName, destFullFileName);
			return storeUploadFileName;
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e.getMessage(), e);
		}
	}
	
}
