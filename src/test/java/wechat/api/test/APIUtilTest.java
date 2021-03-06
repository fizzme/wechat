package wechat.api.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fizzblock.wechat.httpclient.util.HttpUtil;
import com.fizzblock.wechat.httpclient.util.WeiXinApis;
import com.fizzblock.wechat.pojo.SNSUserInfo;

public class APIUtilTest {

	public String APP_ID = "wx416c5da1eef311e6";

	public String APP_SECRET = "30d7fdede88444ff927934f50b367669";
	
	public String REDIRECT_URI = "http://118.25.4.250/wechat-demo/getSNSUserinfo.php";
	
	/**
	 * 重定向的controller
	 */
//	public String REDIRECT_CONTROLLER_URL = "http://118.25.4.250/wechat-demo/getUserInfoBind.do";
	public String REDIRECT_CONTROLLER_URL = "http://www.fizzblock.cn/wechat-demo/userAuth.action";
	
	
	/**
	 * 获取用户信息详情授权
	 */
	public boolean SNSAPI_USERINFO_DETAIL_FLAG = true;
	
	/**
	 * 基本信息授权
	 */
	public boolean SNSAPI_USERINFO_BASE_FLAG = false;
	
	
	/**
	 * state参数值可传可不传
	 */
	public String STATE_PARAM = null; 
	/**
	 * 测试反射解析工具的效果
	 */
	@Test
	public void weiXinWebAuthUrlTest(){
		
//		String redirect_uri = urlEncodeUTF8(REDIRECT_URI);
		if(null != REDIRECT_URI && !"".equals(REDIRECT_URI)){
//			String  webAuthUrl = WeiXinApis.getWebOauth2AuthURL(APP_ID, REDIRECT_URI, SNSAPI_USERINFO_DETAIL_FLAG, STATE_PARAM);
			String  webAuthUrl = WeiXinApis.getWebOauth2AuthURL(APP_ID, REDIRECT_CONTROLLER_URL, SNSAPI_USERINFO_DETAIL_FLAG, STATE_PARAM);
//			String  webAuthUrl = WeiXinApis.getWebOauth2AuthURL(APP_ID, REDIRECT_URI, SNSAPI_USERINFO_BASE_FLAG, STATE_PARAM);
			System.out.println("编码结果："+webAuthUrl);
		}else{
			System.out.println("编码异常，查看信息");
		}
	}
	
	
	@Test
	public void weiXinWebBase_urlTest(){
		String redirect_url =  WeiXinApis.connectOauth2AuthorizeUrl(APP_ID, REDIRECT_CONTROLLER_URL, false, null, null);
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
	
	/**
	 * 菜单跳转和网页授权的url转码
	 */
	@Test
	public void testUrlEncodeUTF8(){
		
		System.out.println("say hello");
		String result = urlEncodeUTF8(REDIRECT_URI);
		if(null !=result){
			System.out.println("编码结果："+result);
		}else{
			System.out.println("编码异常，查看信息");
		}
		
		
	}
	//原始地址：http://fizzblock.bceapp.com/wx
	//编码结果：http%3A%2F%2Ffizzblock.bceapp.com%2Fwx
	
	
	
	String appid = "wx416c5da1eef311e6";
	String secret = "30d7fdede88444ff927934f50b367669";
	
	/**
	 * 获取通用的accessToken
	 * @return
	 * @throws IOException
	 */
	private String fetcheAccessToken() throws IOException{
	    //获取accessToken的方式
//	    https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx416c5da1eef311e6&secret=30d7fdede88444ff927934f50b367669
		//获取的accessToken 5_rJ0lyh3jarA1NDeK_UICzIEhgG-IdcBIHblSxn2n-qEzpBN_eYnvhZnNRKtlsquXucjmuxlUBK-KJEcZ0ClrtveDCCFR6ozj0UeYVhBfa9ocOEBRJ8Wr7p2r_yl9y0WfxUa7vMJSEYPSTFdUOXFcAIARAI
		String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
		String params = "?grant_type=client_credential&appid=APPID&secret=APPSECRET"
						 .replace("APPID", appid)
						 .replace("APPSECRET", secret);
		
		String result = HttpUtil.doGet(accessTokenUrl+params);
		System.out.println("请求地址："+accessTokenUrl+params);
		
		JSONObject jsonObj = JSON.parseObject(result);
		String accessToken = jsonObj.getString("access_token"); 
		System.out.println("accessToken获取："+accessToken);
		
		return accessToken;
	}
	
	
	/**
	 * 获取fans信息
	 */
	@Test
	public void fetchFansUserInfo(){
		//获取访问的token凭证
//		String token = "5_ooUPkKnegNAztR9ytY5h3RCN3BOwnzSMmCgiX4tNq-YR6JyT1WqUC4HODKScqfVwwlTPcqQZJ5OqP7alpVVFcQgseQZkd2Nd7n_dFf73Rdc";
		String token = null;
		try {
			token = fetcheAccessToken();
		} catch (IOException e) {
			System.out.println("获取accessToken异常："+e);
			e.printStackTrace();
		}
//		System.out.println(nowDate+">>>>>>>>>>获取token凭证："+token);
		//获取用户标识
		String openId = "o3Wh70TjTIZk9VpOtcw5vbIQ1dN4";
//		System.out.println(nowDate+">>>>>>>>>>获取用户OpenId："+openId);
		//拉取用户信息
		SNSUserInfo userInfo = WeiXinApis.fetchFanUserinfo(token, openId, "zh_CN");
		System.out.println(">>>>>>>>>>>拉取用户信息："+JSON.toJSONString(userInfo));
		
		String template = " openId:%s 昵称：%s 性别：%d 省份：%s  城市 ：%s  国家:%s headimage:%s  unionid:%s";
		
		System.out.println(String.format(template, 
				userInfo.getOpenId(),
				userInfo.getNickname(),
				userInfo.getSex(),
				userInfo.getProvince(),
				userInfo.getCity(),
				userInfo.getCountry(),
				userInfo.getHeadimgurl(),
//				Arrays.toString(userInfo.getPrivilege().toArray()),
				userInfo.getUnionid()
				
				));
		
	}
}
