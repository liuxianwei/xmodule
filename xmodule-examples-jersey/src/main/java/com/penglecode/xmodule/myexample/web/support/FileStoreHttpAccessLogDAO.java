package com.penglecode.xmodule.myexample.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.web.support.HttpAccessLog;
import com.penglecode.xmodule.common.web.support.HttpAccessLogDAO;
import com.penglecode.xmodule.common.web.support.HttpAccessLogging.LoggingMode;
import com.penglecode.xmodule.myexample.model.User;

@Component
public class FileStoreHttpAccessLogDAO implements HttpAccessLogDAO {

	private static final Logger logger = LoggerFactory.getLogger(FileStoreHttpAccessLogDAO.class);
	
	public LoggingMode getLoggingMode() {
		return LoggingMode.FILE;
	}

	public void saveLog(HttpAccessLog<?> httpAccessLog) throws Exception {
		User user = (User) httpAccessLog.getAccessUser();
		if(user != null) {
			logger.info("【用户操作日志】>>> " + httpAccessLog);
		}
	}

}
