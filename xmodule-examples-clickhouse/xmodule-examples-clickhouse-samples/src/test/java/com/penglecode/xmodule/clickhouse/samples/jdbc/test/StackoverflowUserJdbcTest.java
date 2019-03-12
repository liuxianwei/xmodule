package com.penglecode.xmodule.clickhouse.samples.jdbc.test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.penglecode.xmodule.common.support.ValueHolder;
import com.penglecode.xmodule.common.util.FileUtils;

public class StackoverflowUserJdbcTest extends AbstractJdbcTest {

	private static final String DATA_FILE_PATH = "C:\\Users\\Pengle\\Downloads\\stackoverflow.com-Users\\Users.xml";
	
	private static final SAXReader SAXREADER = new SAXReader();
	
	@Test
	public void batchInsertUsers() throws Exception {
		int batchSize = 2500;
		LineIterator lit = FileUtils.lineIterator(new File(DATA_FILE_PATH), "UTF-8");
		AtomicInteger count = new AtomicInteger(0);
		
		List<String> lines = new ArrayList<String>();
		ValueHolder<Integer> batchCount = new ValueHolder<Integer>(0);
		lit.forEachRemaining(line -> {
			line = line.trim();
			if(line.startsWith("<row")) {
				lines.add(line);
				int index = count.incrementAndGet();
				if(index % batchSize == 0) {
					batchCount.setValue(batchCount.getValue() + 1);
					insertUsers(lines);
					System.out.println(String.format("【%s】>>> batch inserts: %s", batchCount.getValue(), lines.size()));
					lines.clear();
				}
			}
		});
	}
	
	protected void insertUsers(List<String> users) {
		try {
			Document document = linesToDocument(users);
			Element rootElement = document.getRootElement();
			List<Element> rowElements = rootElement.elements("row");
			List<Object[]> argsList = new ArrayList<Object[]>();
			String value = null;
			for(Element element : rowElements) {
				value = element.attributeValue("Id");
				Long id = Long.valueOf(value);
				String name = element.attributeValue("DisplayName");
				value = element.attributeValue("Reputation");
				Integer reputation = Integer.valueOf(value);
				String websiteUrl = element.attributeValue("WebsiteUrl");
				String location = element.attributeValue("Location");
				String aboutMe = element.attributeValue("AboutMe");
				value = element.attributeValue("Views");
				Integer views = Integer.valueOf(value);
				value = element.attributeValue("UpVotes");
				Integer upVotes = Integer.valueOf(value);
				value = element.attributeValue("DownVotes");
				Integer downVotes = Integer.valueOf(value);
				String profileImageUrl = element.attributeValue("ProfileImageUrl");
				String lastAccessTime = formatDateTime(element.attributeValue("LastAccessDate"));
				String createTime = formatDateTime(element.attributeValue("CreationDate"));
				Object[] args = new Object[] {
					id, name, reputation, websiteUrl, location, aboutMe, views, upVotes, downVotes, profileImageUrl, lastAccessTime, createTime
				};
				argsList.add(args);
			}
			String sql = "insert into stackoverflow_user(id, name, reputation, websiteUrl, location, aboutMe, views, upVotes, downVotes, profileImageUrl, lastAccessTime, createTime) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.batchUpdate(sql, argsList);
		} catch (Exception e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder();
			users.stream().forEach(sb::append);
			try {
				IOUtils.write(sb.toString(), new FileOutputStream("d:/users_error_" + System.currentTimeMillis() + ".xml"), "UTF-8");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	protected Document linesToDocument(List<String> lines) throws Exception {
		StringBuilder sb = new StringBuilder();
		lines.add(0, "<users>");
		lines.add("</users>");
		lines.stream().forEach(sb::append);
		return SAXREADER.read(IOUtils.toInputStream(sb.toString(), "UTF-8"));
	}
	
	protected String formatDateTime(String dateTime) {
		if(dateTime != null) {
			dateTime = dateTime.replace("T", " ");
			return dateTime.substring(0, dateTime.lastIndexOf('.'));
		}
		return "0000-00-00 00:00:00";
	}
	
}
