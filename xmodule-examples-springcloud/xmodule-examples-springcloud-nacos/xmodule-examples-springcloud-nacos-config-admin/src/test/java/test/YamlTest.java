package test;

import java.util.Map;

import org.springframework.core.io.Resource;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.ResourceUtils;
import com.penglecode.xmodule.common.util.YamlPropsUtils;

public class YamlTest {

	public static void test1() throws Exception {
		Resource resource1 = ResourceUtils.getResource("classpath:test.yml");
		String yaml1 = FileUtils.readFileToString(resource1.getFile(), GlobalConstants.DEFAULT_CHARSET);
		System.out.println(yaml1);
		System.out.println("-------------------------------------------------");
		Map<String,Object> map1 = YamlPropsUtils.yaml2Map(yaml1);
		System.out.println("map1 = " + map1);
		
		System.out.println("-------------------------------------------------");
		
		Resource resource2 = ResourceUtils.getResource("classpath:test.properties");
		String props1 = FileUtils.readFileToString(resource2.getFile(), GlobalConstants.DEFAULT_CHARSET);
		System.out.println(props1);
		System.out.println("-------------------------------------------------");
		Map<String,Object> map2 = YamlPropsUtils.props2Map(props1);
		System.out.println("map2 = " + map2);
		
	}
	
	public static void main(String[] args) throws Exception {
		test1();
	}

}
