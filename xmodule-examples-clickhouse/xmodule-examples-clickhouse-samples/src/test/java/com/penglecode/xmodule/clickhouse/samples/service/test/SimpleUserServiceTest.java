package com.penglecode.xmodule.clickhouse.samples.service.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.clickhouse.samples.boot.ClickHouseExampleApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={ClickHouseExampleApplication.class})
public class SimpleUserServiceTest {

	
	
}
