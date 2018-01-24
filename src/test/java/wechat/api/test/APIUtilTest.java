package wechat.api.test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fizzblock.wechat.httpclient.util.WeiXinApis;
import com.fizzblock.wechat.pojo.SNSUserInfo;

public class APIUtilTest {

	public String APP_ID = "wx416c5da1eef311e6";

	public String APP_SECRET = "30d7fdede88444ff927934f50b367669";
	
	public String REDIRECT_URI = "http://180.76.245.252/getSNSUserinfo.php";
	public String REDIRECT_URI_OpenId = "http://fizzblock.bceapp.com/getUserInfoBind.do";
	
	public boolean SNSAPI_USERINFO = true;
	
	public String STATE_PARAM = null; 
	/**
	 * 测试反射解析工具的效果
	 */
	@Test
	public void weiXinWebAuthUrlTest(){
		
//		String redirect_uri = urlEncodeUTF8(REDIRECT_URI);
		if(null != REDIRECT_URI && !"".equals(REDIRECT_URI)){
			String  webAuthUrl = WeiXinApis.getOauth2AuthURL(APP_ID, REDIRECT_URI, SNSAPI_USERINFO, STATE_PARAM);
			System.out.println("编码结果："+webAuthUrl);
			
			
		}else{
			System.out.println("编码异常，查看信息");
		}
	}
	
	@Test
	public void weiXinWebBase_urlTest(){
		String redirect_url =  WeiXinApis.connectOauth2Authorize(APP_ID, REDIRECT_URI_OpenId, false, null, null);
		System.out.println("openID的总体请求地址\n"+redirect_url);
		
	}
	
	
	//url编码
	/**
	 * 需要对返回值进行校验
	 * @param sourceUrl
	 * @return
	 */
	public String urlEncodeUTF8(String sourceUrl){
		
		String result = null;
		if(null != sourceUrl &&!"".equals(sourceUrl)){
			
			try {
				result = java.net.URLEncoder.encode(sourceUrl, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("转换出现异常");
			}
		}
		
		return result;
	}
	
	@Test
	public void testUrlEncodeUTF8(){
		
		String result = urlEncodeUTF8(REDIRECT_URI);
		if(null !=result){
			System.out.println("编码结果："+result);
		}else{
			System.out.println("编码异常，查看信息");
		}
		
		
	}
	//原始地址：http://fizzblock.bceapp.com/wx
	//编码结果：http%3A%2F%2Ffizzblock.bceapp.com%2Fwx
	
	
	@Test
	public void fetchUserInfo(){
		//获取访问的token凭证
		String token = "5_ooUPkKnegNAztR9ytY5h3RCN3BOwnzSMmCgiX4tNq-YR6JyT1WqUC4HODKScqfVwwlTPcqQZJ5OqP7alpVVFcQgseQZkd2Nd7n_dFf73Rdc";
//		System.out.println(nowDate+">>>>>>>>>>获取token凭证："+token);
		//获取用户标识
		String openId = "o3Wh70TjTIZk9VpOtcw5vbIQ1dN4";
//		System.out.println(nowDate+">>>>>>>>>>获取用户OpenId："+openId);
		//拉取用户信息
		SNSUserInfo userInfo = WeiXinApis.fetchUserinfo(token, openId, "zh_CN");
		System.out.println(">>>>>>>>>>>拉取用户信息："+JSON.toJSONString(userInfo));
		
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
