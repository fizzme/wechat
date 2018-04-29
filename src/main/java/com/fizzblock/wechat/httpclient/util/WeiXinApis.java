package com.fizzblock.wechat.httpclient.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fizzblock.wechat.pojo.WebAccessToken;
import com.fizzblock.wechat.pojo.SNSUserInfo;

public class WeiXinApis {

	
	/**
	 * 获取accessToken
	 * 
	 * @param appid
	 * @param secret
	 * @param code
	 * @return
	 */
	public static WebAccessToken getAccessToken(String appid, String secret, String code) {
		System.out.println(getNowDate()+">>>>>>>>>>>>获取accessToken中.....");
	       HttpUriRequest httpUriRequest = RequestBuilder.get().setUri("https://api.weixin.qq.com/sns/oauth2/access_token").addParameter("appid", appid).addParameter("secret", secret).addParameter("code", code).addParameter("grant_type", "authorization_code").build();
	       String jsonStr = HttpUtil.executeRequest(httpUriRequest);
	       System.out.println(getNowDate()+"请求网页授权accessToken结果:"+jsonStr);

	       JSONObject jsonObject = JSON.parseObject(jsonStr);
	       
	       WebAccessToken oauthToken = null;
	       if(null != jsonObject){
		    	try{   
		    	   oauthToken = new WebAccessToken();
		    	   oauthToken.setAccessToken(jsonObject.getString("access_token"));
		    	   oauthToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
		    	   oauthToken.setRefreshToken(jsonObject.getString("refresh_token"));
		    	   oauthToken.setOpenId(jsonObject.getString("openid"));
		    	   oauthToken.setScope(jsonObject.getString("scope"));
		    	   
		    	}catch(Exception e){
		    	   oauthToken = null;
		    	   requestFailLog(jsonObject,"获取网页授权凭证失败");
		    	   e.printStackTrace();
		    	}
	    	}
	       
	       return oauthToken;
	   }

	
	private static void requestFailLog(JSONObject jsonObject, String msg) {
		
 	   int errorCode = jsonObject.getIntValue("errcode");
 	   String errorMsg = jsonObject.getString("errmsg");
 	   if(null ==msg ||"".equals(msg)){
 		   System.out.println(String.format("请求微信服务失败  errcode:{%s} errmsg:{%s}", errorCode,errorMsg));
 	   }
 	   //打印日志信息
 	   System.out.println(String.format(msg +" errcode:{%s} errmsg:{%s}", errorCode,errorMsg));
		
	}


	/**
	 * 刷新获取网页授权，这是通过反射这种方式
	 * @param appid
	 * @param refresh_token
	 * @return
	 */
	public static WebAccessToken oauth2RefreshToken(String appid, String refresh_token) {
	       HttpUriRequest httpUriRequest = RequestBuilder.get().setUri("https://api.weixin.qq.com/sns/oauth2/refresh_token").addParameter("appid", appid).addParameter("refresh_token", refresh_token).addParameter("grant_type", "refresh_token").build();
	      String jsonStr = HttpUtil.executeRequest(httpUriRequest);
	      JSONObject jsonObject = JSON.parseObject(jsonStr);
	      WebAccessToken accessToken = null;
	       try{
	    	   accessToken = JSON.toJavaObject(jsonObject, WebAccessToken.class);
	       }catch(Exception ex){
	    	   accessToken = null;
	    	   requestFailLog(jsonObject, getNowDate()+"刷新获取网页授权凭证失败 ");
	    	   ex.printStackTrace();
//	    	   return accessToken;
	       }
	       
	       return accessToken;
	}
	
	
	/**
	 * 请求用户基本信息
	 * @param access_token 凭证
	 * @param openid 用户的openID
	 * @param lang 语言  zh_CN 简体，zh_TW 繁体，en 英语
	 * @return
	 */
	public static SNSUserInfo fetchUserinfo(String access_token, String openid,
			String lang) {
		HttpUriRequest httpUriRequest = RequestBuilder.get()
				.setUri("https://api.weixin.qq.com/sns/userinfo")
				.addParameter("access_token", access_token)
				.addParameter("openid", openid).addParameter("lang", lang)
				.build();
		//获取微信iso编码结果
		String jsonStr = HttpUtil.doGet(httpUriRequest);
		JSONObject jsonObject = null;
		SNSUserInfo userInfo = null;
		try{
			//处理中文乱码问题微信端是ISO-8859-1编码格式，这边要做处理
				String userInfoStr = new String(jsonStr.getBytes("ISO-8859-1"), "UTF-8");
				jsonObject = JSON.parseObject(userInfoStr);
				userInfo = JSON.toJavaObject(jsonObject, SNSUserInfo.class);
				
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println(getNowDate()+"中文编码问题："+e);
			}catch(Exception ex){
				userInfo = null;
				requestFailLog(jsonObject, getNowDate()+"拉取粉丝用户信息失败");
				ex.printStackTrace();
		}
		
		return userInfo;
	}

	
	
	//网页授权
	/**
	 * 生成网页授权重定向的url地址
	 * @param appid
	 * @param redirect_uri
	 * @param snsapi_userinfo
	 * @param state
	 * @return
	 */
	public static String getWebOauth2AuthURL(String appid,
			String redirect_uri, boolean snsapi_userinfo, String state) {
		return connectOauth2AuthorizeUrl(appid, redirect_uri, snsapi_userinfo,state, (String) null);
	}

	/**
	 * 网页授权
	 * @param appid
	 * @param redirect_uri
	 * @param snsapi_userinfo
	 * @param state
	 * @param component_appid
	 * @return
	 */
	public static String connectOauth2AuthorizeUrl(String appid,
			String redirect_uri, boolean snsapi_userinfo, String state,
			String component_appid) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?")
					.append("appid=")
					.append(appid)
					.append("&redirect_uri=")
					.append(URLEncoder.encode(redirect_uri, "utf-8"))
					.append("&response_type=code")
					.append("&scope=")
					.append(snsapi_userinfo ? "snsapi_userinfo" : "snsapi_base")
					.append("&state=").append(state == null ? "STATE" : state);

			if (component_appid != null) {
				sb.append("&component_appid=").append(component_appid);
			}
			sb.append("#wechat_redirect");
			return sb.toString();
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	//获取当前时间
	private static String getNowDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//设置日期格式
		return df.format(new Date());
	}

}
