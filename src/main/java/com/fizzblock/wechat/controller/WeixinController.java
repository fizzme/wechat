package com.fizzblock.wechat.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;

import com.fizzblock.wechat.util.common.Decript;
import com.fizzblock.wechat.util.common.SignUtil;
import com.fizzblock.wechat.util.message.MessageHandler;

@Controller
public class WeixinController {
//    private static final String token = "fizzblock";  
//    private static final String token = "oFWRoUnETDW4XpORscqN";  
//    private static final String token = "微信公众号设置的自己账号token";  
    private static final String redirect_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx416c5da1eef311e6&redirect_uri=http%3A%2F%2Ffizzblock.bceapp.com%2FgetSNSUserinfo2.do&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";  
    //以ip方式测试授权
    private static final String redirect_url2 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx416c5da1eef311e6&redirect_uri=http%3A%2F%2Ffizzblock.bceapp.com%2FgetSNSUserinfo.do&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";  

    //日了狗....这个地址不对
//    private static final String redirect_url_login = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx416c5da1eef311e6&redirect_uri=http%3A%2F%2Ffizzblock.bceapp.com%2FgetSNSUserinfo.php&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";  
//    private static final String redirect_url_login = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx416c5da1eef311e6&redirect_uri=http%3A%2F%2Ffizzblock.bceapp.com%2FgetUserInfoBind.do&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";  
    private static final String redirect_url_login = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx416c5da1eef311e6&redirect_uri=http%3A%2F%2Ffizzblock.bceapp.com%2FgetUserInfoBind.do&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";  
    /**											https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
     * 接收微信服务器验证消息
     * @param request
     * @param response
     * @param model
     * @throws IOException 
     */
	@RequestMapping(value = "/wx" ,method=RequestMethod.GET)
	public void receiveServer(HttpServletRequest request,HttpServletResponse response) throws IOException {
			//判断请求的类型，微信验证使用的是get请求，其余post请求
		System.out.println("GET请求 [微信服务端发来消息]：开始签名校验");
		response.setContentType("text/html;charset=utf-8"); //设置输出编码格式
       String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String msg = "获取到的相关wx请求校验信息，{signature:%s timestamp:%s nonce:%s echostr:%s}";
        
        System.out.println("校验消息输出"+String.format(msg, signature,timestamp,nonce,echostr));
		PrintWriter out = response.getWriter();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒SSS毫秒");
		Date date = new Date();
		String time = sdf.format(date);
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			System.out.println(date+"签名校验通过。");
			out.print(echostr);
		}
		
		System.out.println(date+"签名校验失败。");
		out.close();
		out = null;
		
	}
	
	/**
	 * 接收微信服务器转发用户操作请求
	 * @param request
	 * @param response
	 * @param model
	 * @throws IOException 
	 */
	@RequestMapping(value = "/wx" ,method=RequestMethod.POST)
	public void receiveMessage(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8"); //设置输出编码格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		
		String nowDate =df.format(new Date());
		
	        System.out.println(nowDate+"微信转发粉丝消息：Post请求进入");
	        String result = "";
	        try {
	        	//解析微信的xml请求消息
	            Map map = MessageHandler.parseXml(request);
	            System.out.println(nowDate+"开始构造消息");
//	            String responseMsg = "欢迎来到，章鱼的学习探索订阅号。"+map.get("Content").toString();
	            String requestMsg = map.get("Content").toString();
	            String responseMsg = "";
	            switch(requestMsg){
	            	case "chanpin":
	            		responseMsg = "产品运营  链接：http://pan.baidu.com/s/1o8h0Zns 密码：qx9m";
	            		break;
	            	case "linux":
	            		responseMsg = "《linux命令行与shell编程大全》 链接：http://pan.baidu.com/s/1geJYFYz 密码：h11t";
	            		break;
	            	case "git":
	            		responseMsg = "《完全学会GIT SERVER的24堂课》 链接：http://pan.baidu.com/s/1pLsov2N 密码：4fn0";
	            		break;
	            	case "springboot":
	            		responseMsg = "《JavaEE开发的颠覆者springboot实战》 链接：http://pan.baidu.com/s/1cIxomi 密码：eo9g";
	            		break;
	            	case "design":
	            		responseMsg = "《JAVA设计模式深入研究》 链接：http://pan.baidu.com/s/1hrYPL3Y 密码：ofl5";
	            		break;
	            	case "es":
	            		responseMsg = "《深入理解ElasticSearch》 链接：http://pan.baidu.com/s/1kUPWh9d 密码：dmng";
	            		break;
	            	default:
	            		responseMsg = "回复如下信息获取相关资源： \n"+
//	            					"1.回复 ：chanpin  可以《获取产品运营训练营—腾讯产品经理》  \n "+
	            					"1.回复 ：linux  获取《linux命令行与shell编程大全》电子书资源  \n\n "+
	            					"2.回复 ：git  获取《完全学会GIT SERVER的24堂课》电子书资源  \n\n "+
	            					"3.回复 ：springboot  获取《JavaEE开发的颠覆者springboot实战》电子书资源  \n\n "+
	            					"4.回复 ：design  获取《JAVA设计模式深入研究》电子书资源  \n\n "+
	            					"5.回复 ：es  获取《深入理解ElasticSearch》电子书资源  \n\n "+
	            					"更多资源敬请期待...\n\n";
	            		responseMsg = responseMsg
//	            				+"<a href=\""+redirect_url2+"\">用户绑定</a>"
	            					+"\n\n"+"绑定地址：\n"+redirect_url
				            		+"\n\n"+"登陆地址：\n"+redirect_url_login;
	            		break;
	            		
	            }
	            
	            
	            result = MessageHandler.buildXml(map,responseMsg);
	            System.out.println(nowDate+"响应消息：\n"+result);
	            if(result.equals(""))
	                result = "未正确响应";
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println(nowDate+"发生异常："+ e);
	        }
	        response.getWriter().println(result);
	
	}
	
	
    /**
     * 排序
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String sort(String token, String timestamp, String nonce) {
        String[] strArray = { token, timestamp, nonce };
        Arrays.sort(strArray);

        StringBuilder sbuilder = new StringBuilder();
        for (String str : strArray) {
            sbuilder.append(str);
        }

        return sbuilder.toString();
    }


    
}
