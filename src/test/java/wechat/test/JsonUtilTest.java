package wechat.test;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fizzblock.wechat.pojo.SNSUserInfo;

public class JsonUtilTest {

	
	/**
	 * 测试反射解析工具的效果
	 */
	@Test
	public void parseSNSUser(){

//		String userInfoJson = "{\"openid\": \" OPENID\",\"nickname\": \"NICKNAME\",\"sex\": 1,\"province\": \"PROVINCE\",\"city\": \"CITY\",\"country\": \"COUNTRY\",\"headimgurl\": \"http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46\",\"privilege\": [\"PRIVILEGE1\",\"PRIVILEGE2\"],\"unionid\": \"o6_bmasdasdsad6_2sgVt7hMZOPfL\"}";
		String userInfoJson = "{\"city\":\"é\\u0083\\u0091å·\\u009E\",\"country\":\"ä¸å\\u009B½\",\"headimgurl\":\"http://wx.qlogo.cn/mmopen/vi_32/O9ylcAg1qeib3J1ZZUkp7X9hBCkOBzPVp7LfGTIoSiccYdsDWMGToCnic9yvvEFibyUBqjrTwibHWaxmUW5Mtm0UukQ/132\",\"nickname\":\"å\\u0090\\u008Eæµ·å¤§ç«é±¼ð\\u009F\\u0090\\u0099\",\"openId\":\"o3Wh70TjTIZk9VpOtcw5vbIQ1dN4\",\"privilege\":[],\"province\":\"æ²³å\\u008D\\u0097\",\"sex\":1}";
		
		JSONObject jsonObjcet = JSON.parseObject(userInfoJson);
		System.out.println(jsonObjcet);
		
		SNSUserInfo userInfo = JSON.toJavaObject(jsonObjcet, SNSUserInfo.class);
		String template = " openId:%s 昵称：%s 性别：%d 省份：%s  城市 ：%s  国家:%s headimage:%s privilege:%s unionid:%s";
				
		System.out.println(String.format(template, 
				userInfo.getOpenId(),
				userInfo.getNickname(),
				userInfo.getSex(),
				userInfo.getProvince(),
				userInfo.getCity(),
				userInfo.getCountry(),
				userInfo.getHeadimgurl(),
				Arrays.toString(userInfo.getPrivilege().toArray()),
				userInfo.getUnionid()
				
				));
		
	}
	
}
