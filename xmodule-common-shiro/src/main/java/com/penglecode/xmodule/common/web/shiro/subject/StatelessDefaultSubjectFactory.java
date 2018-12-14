package com.penglecode.xmodule.common.web.shiro.subject;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 无状态回话Subject工厂
 * 
 * @author 	pengpeng
 * @date	2018年1月4日 上午10:43:01
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {

	public Subject createSubject(SubjectContext context) {
        //不创建session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
	
}
