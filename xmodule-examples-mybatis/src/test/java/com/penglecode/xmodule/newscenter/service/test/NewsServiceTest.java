package com.penglecode.xmodule.newscenter.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.common.util.UUIDUtils;
import com.penglecode.xmodule.myexample.boot.MybatisExampleApplication;
import com.penglecode.xmodule.newscenter.model.News;
import com.penglecode.xmodule.newscenter.service.NewsService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={MybatisExampleApplication.class})
public class NewsServiceTest {

	@Autowired
	private NewsService newsService;
	
	@Test
	public void createNews() {
		News news = new News();
		news.setNewsId(UUIDUtils.uuid());
		news.setNewsTitle("test111");
		news.setNewsTags("aaa");
		news.setNewsContent("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		newsService.createNews(news);
	}
	
	@Test
	public void updateNews() {
		News news = new News();
		news.setNewsId("c0280bc52dde40d89657f36b19348f17");
		news.setNewsTitle("test222");
		news.setNewsTags("bbb");
		news.setNewsContent("asdsaddssqwwdsadsasdsa");
		newsService.updateNews(news);
	}
	
}
