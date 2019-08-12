package com.penglecode.xmodule.myexample.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.ImagePixel;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Result.Builder;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.ImageUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.UUIDUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

/**
 * Element-UI前端Upload上传组件专用Controller
 * 
 * @author 	pengpeng
 * @date	2019年4月19日 下午1:37:13
 */
@RestController
@RequestMapping("/api/xupload")
public class ElementUIXUploadController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ElementUIXUploadController.class);
	
	@Autowired
	private MultipartResolver multipartResolver;
	
	/**
	 * 上传图片
	 * @param request
	 * @param response
	 * @param multiple			- 客户端开启多文件同时上传?
	 * @param formatLimit		- 上传图片限制的格式,例如"jpg,png"
	 * @param pixelLimit		- 上传图片像素限制,例如 "200x200", "600x*"
	 * @param pixelDeviation	- 上传图片像素宽高浮动限制, 例如宽高上下浮动10个像素也是允许的
	 * @return
	 */
	@PostMapping(value="/image")
	public Object uploadImage(HttpServletRequest request, HttpServletResponse response, Boolean multiple, String formatLimit, String pixelLimit, Integer pixelDeviation) {
		List<String> fileFormats = GlobalConstants.DEFAULT_UPLOAD_IMAGE_FORMATS;
		pixelDeviation = pixelDeviation == null ? GlobalConstants.DEFAULT_UPLOAD_IMAGE_PIXEL_DEVIATION : pixelDeviation;
		List<Object> dataList = new ArrayList<Object>();
		int code = 200;
		String message = "上传成功!";
		try {
			ImagePixel imagePixel = null;
			if(!StringUtils.isEmpty(pixelLimit)){
				imagePixel = ImagePixel.createImagePixel(pixelLimit);
			}
			if(!StringUtils.isEmpty(formatLimit)){
				fileFormats = Arrays.asList(formatLimit.split(","));
			}
			String httpContextPath = request.getRequestURL().toString();
			String requestUri = request.getRequestURI();
			httpContextPath = httpContextPath.substring(0, httpContextPath.indexOf(requestUri));
			
			String realContextPath = request.getSession().getServletContext().getRealPath("/");
			realContextPath = FileUtils.formatFilePath(realContextPath);
			//判断 request 是否有文件上传,即多部分请求  
	        if(multipartResolver.isMultipart(request)){
	            //转换成多部分request
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
	            //取得request中的所有文件名
	            Iterator<String> fileNames = multiRequest.getFileNames();
	            while(fileNames.hasNext()){
	                //记录上传过程起始时的时间，用来计算上传时间
	                int start = (int) System.currentTimeMillis();
	                //取得上传文件  
	                MultipartFile file = multiRequest.getFile(fileNames.next());
	                if(file != null){
	                    //取得当前上传文件的文件名称
	                    String originalFileName = file.getOriginalFilename();
	                    //如果名称不为空,说明该文件存在，否则说明该文件不存在
	                    if(!StringUtils.isEmpty(originalFileName)){
	                    	originalFileName = originalFileName.trim().toLowerCase();
	                        LOGGER.info(">>> Upload original file name : " + originalFileName);
	                        String contentType = file.getContentType().toLowerCase();
	                        String fileFormat = StringUtils.stripStart(contentType, "image/");
	                        if(!fileFormats.contains(fileFormat)){
	                        	code = 500;
	                        	message = String.format("上传文件(%s)必须是格式为：%s的图片文件!", originalFileName, fileFormats);
	            	        	break;
	                        }
	                        if(imagePixel != null){
	                        	ImagePixel targetImagePixel = ImageUtils.getImagePixel(file.getInputStream());
	                        	if(imagePixel.getWidth() != null && Math.abs(imagePixel.getWidth() - targetImagePixel.getWidth()) > pixelDeviation){
	                        		code = 500;
	                        		message = "上传图片像素宽度超出限制!";
		            	        	break;
	                        	}
	                        	if(imagePixel.getHeight() != null && Math.abs(imagePixel.getHeight() - targetImagePixel.getHeight()) > pixelDeviation){
	                        		code = 500;
	                        		message = "上传图片像素高度超出限制!";
		            	        	break;
	                        	}
	                        }
	                        //重命名上传后的文件名
	                        String renamedFileName = UUIDUtils.uuid() + originalFileName.substring(originalFileName.lastIndexOf('.'));
	                        LOGGER.info(">>> Upload renamed file name : " + renamedFileName);
	                        String fileRelativePath = GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH + renamedFileName;
	                        String fileRealSavePath = FileUtils.formatFilePath(realContextPath + fileRelativePath);
	                        FileUtils.mkDirIfNecessary(fileRealSavePath);
	                        LOGGER.info(">>> Upload file real save path : " + fileRealSavePath);
	                        //定义上传路径
	                        File destFile = new File(fileRealSavePath);
	                        file.transferTo(destFile);
	                        Map<String,Object> data = new HashMap<String,Object>();
	                        data.put("name", originalFileName);
	                        data.put("url", FileUtils.formatFilePath(httpContextPath + fileRelativePath));
	                        data.put("path", fileRelativePath);
	                        dataList.add(data);
	                    }
	                }
	                //记录上传该文件后的时间
	                int end = (int) System.currentTimeMillis();
	                LOGGER.info(">>> Upload file cost time : {} ms", (end - start));
	            }
	        }else{
	        	code = 500;
	        	message = "请求中未发现有文件上传!";
	        }
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			code = 500;
			message = String.format("上传图片出现未知错误!(错误信息：%s)", e.getMessage());
		}
		Object data = null;
		if(!dataList.isEmpty()){
			data = Boolean.TRUE.equals(multiple) ? dataList : (CollectionUtils.isEmpty(dataList) ? null : dataList.get(0));
		}
		response.setStatus(Integer.valueOf(code));
		return createResult(code, message, data);
	}
	
	/**
	 * 上传普通文件
	 * @param request
	 * @param response
	 * @param multiple			- 客户端开启多文件同时上传?
	 * @param formatLimit		- 上传文件限制的格式,例如"jpg,png"
	 * @return
	 */
	@PostMapping(value="/file")
	public Object uploadFile(HttpServletRequest request, HttpServletResponse response, Boolean multiple, String formatLimit) {
		List<String> fileFormats = null;
		List<Object> dataList = new ArrayList<Object>();
		int code = 200;
		String message = "上传成功!";
		try {
			if(!StringUtils.isEmpty(formatLimit)){
				fileFormats = Arrays.asList(formatLimit.split(","));
			}
			String httpContextPath = request.getRequestURL().toString();
			String requestUri = request.getRequestURI();
			httpContextPath = httpContextPath.substring(0, httpContextPath.indexOf(requestUri));
			
			String realContextPath = request.getSession().getServletContext().getRealPath("/");
			realContextPath = FileUtils.formatFilePath(realContextPath);
			//判断 request 是否有文件上传,即多部分请求  
	        if(multipartResolver.isMultipart(request)){
	            //转换成多部分request
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
	            //取得request中的所有文件名
	            Iterator<String> fileNames = multiRequest.getFileNames();
	            while(fileNames.hasNext()){
	                //记录上传过程起始时的时间，用来计算上传时间
	                int start = (int) System.currentTimeMillis();
	                //取得上传文件  
	                MultipartFile file = multiRequest.getFile(fileNames.next());
	                if(file != null){
	                    //取得当前上传文件的文件名称
	                    String originalFileName = file.getOriginalFilename();
	                    //如果名称不为空,说明该文件存在，否则说明该文件不存在
	                    if(!StringUtils.isEmpty(originalFileName)){
	                    	originalFileName = originalFileName.trim().toLowerCase();
	                        LOGGER.info(">>> Upload original file name : " + originalFileName);
	                        String fileFormat = FileUtils.getFileFormat(originalFileName);
	                        if(fileFormats != null && !fileFormats.contains(fileFormat)){
	                        	code = 500;
	            	        	message = String.format("上传文件(%s)必须是：%s格式的文件!", originalFileName, fileFormats);
	            	        	break;
	                        }
	                        //重命名上传后的文件名
	                        String renamedFileName = UUIDUtils.uuid() + originalFileName.substring(originalFileName.lastIndexOf('.'));
	                        LOGGER.info(">>> Upload renamed file name : " + renamedFileName);
	                        String fileRelativePath = GlobalConstants.DEFAULT_UPLOAD_TEMP_SAVE_PATH + renamedFileName;
	                        String fileRealSavePath = FileUtils.formatFilePath(realContextPath + fileRelativePath);
	                        FileUtils.mkDirIfNecessary(fileRealSavePath);
	                        LOGGER.info(">>> Upload file real save path : " + fileRealSavePath);
	                        //定义上传路径
	                        File destFile = new File(fileRealSavePath);
	                        file.transferTo(destFile);
	                        Map<String,Object> data = new HashMap<String,Object>();
	                        data.put("name", originalFileName);
	                        data.put("url", FileUtils.formatFilePath(httpContextPath + fileRelativePath));
	                        data.put("path", fileRelativePath);
	                        dataList.add(data);
	                    }
	                }
	                //记录上传该文件后的时间
	                int end = (int) System.currentTimeMillis();
	                LOGGER.info(">>> Upload file cost time : {} ms", (end - start));
	            }
	        }else{
	        	code = 500;
	        	message = "请求中未发现有文件上传!";
	        }
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			code = 500;
			message = String.format("上传图片出现未知错误!(错误信息：%s)", e.getMessage());
		}
		Object data = null;
		if(!dataList.isEmpty()){
			data = Boolean.TRUE.equals(multiple) ? dataList : (CollectionUtils.isEmpty(dataList) ? null : dataList.get(0));
		}
		response.setStatus(Integer.valueOf(code));
		return createResult(code, message, data);
	}
	
	/**
	 * 删除文件
	 * @param request
	 * @param response
	 * @param path
	 * @return
	 */
	@GetMapping(value="/remove")
	public Object removeFile(HttpServletRequest request, HttpServletResponse response, String path) {
		String message = "删除成功!";
		try {
			if(!StringUtils.isEmpty(path)){
				String realContextPath = request.getSession().getServletContext().getRealPath("/");
				String fileRealPath = FileUtils.formatFilePath(realContextPath + path);
				FileUtils.deleteFileQuietly(fileRealPath);
			}
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
			message = "删除失败!";
		}
		return Result.success().message(message).build();
	}
	
	protected Object createResult(int code, String message, Object data) {
		Result<Object> result = null;
		Builder builder = null;
		if(code == 200) {
			builder = Result.success();
		} else {
			builder = Result.failure();
		}
		result = builder.code(code).message(message).data(data).build();
		return result;
	}
	
}
