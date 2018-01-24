package com.fizzblock.wechat.util.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.fizzblock.wechat.bean.message.BasicMessage;
import com.fizzblock.wechat.bean.message.MessageType;

/**
 * 消息处理工具类
 * @author glen
 *
 */
public class MessageHandler {

	/**
	 * 获取请求中的xml，解析请求中的xml文件流<br>
	 * 微信xml消息层次都是1层没有过多的嵌套
	 * @param request
	 * @return
	 * @throws Exception
	 */
    public static Map parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map map = new HashMap();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        System.out.println("parseXml-解析消息：获取输入流");
        /*
         * 读取request的body内容 此方法会导致流读取问题 Premature end of file. Nested exception:
		 * Premature end of file String requestBody =
		 * inputStream2String(inputStream); System.out.println(requestBody);
		 */
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            System.out.print(e.getName() + "|" + e.getText());
            map.put(e.getName(), e.getText());
        }
        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }
    
    /**
     * Inpustream流转String字符串
     * @param InputStream is
     * @return String
     * @throws IOException
     */
    private static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
    
    /**
     * 根据消息类型 构造返回消息
     * @param Map map 解析的消息内容
     * @param String responseMsg 返回消息内容
     * @return
     */
    public static String buildXml(Map map,String responseMsg) {
        String result = "";
        String msgType = map.get("MsgType").toString();
        System.out.println("MsgType:" + msgType);
        MessageType messageEnumType = MessageType.valueOf(MessageType.class, msgType.toUpperCase());
        switch (messageEnumType) {
            case TEXT:
                result = handleTextMessage(map ,responseMsg);
                break;
/*            case IMAGE:
                result = handleImageMessage(map);
                break;
            case VOICE:
                result = handleVoiceMessage(map);
                break;
            case VIDEO:
                result = handleVideoMessage(map);
                break;
            case SHORTVIDEO:
                result = handleSmallVideoMessage(map);
                break;
            case LOCATION:
                result = handleLocationMessage(map);
                break;
            case LINK:
                result = handleLinkMessage(map);
                break;*/
            default:
                break;
        }
        return result;
    }
    
    /**
     * 处理微信文本消息
     * @param Map map 解析的微信服务器发送的消息结果，可以传递新的消息信息 key = value形式
     * @param String responseMsg 要响应的消息
     * @return String xml拼接的消息结果
     */
    private static String handleTextMessage(Map map ,String responseMsg) {
    	System.out.println("handleTextMessage-解析用户消息：");
        String xml = "";
        //发送方粉丝用户
        String fromUserName = map.get("FromUserName").toString();
        System.out.println("消息发送方："+fromUserName);
        // 开发者微信号
        String toUserName = map.get("ToUserName").toString();
        System.out.println("消息接收方："+toUserName);
        // 消息内容
        String content = map.get("Content").toString();
        System.out.println("发送的消息内容："+content);
//        xml = buildTextMessage(map, "文本回复消息："+responseMsg);//构建xml格式的消息内容
        xml = buildTextMessage(map, responseMsg);//构建xml格式的消息内容

 /*       //调换发送和接收对象
        BasicMessage msg = new BasicMessage();
        msg.Sender = toUserName;//接收方微信公众号，变为发送方
        msg.Receiver = fromUserName;//发送方粉丝，变为接收方
        msg.CreateTime = getUtcTime();//获取时间
        switch (content) {
            case "文本":
                xml = buildTextMessage(map, "这是一条文本消息"+responseMsg);//构建xml格式的消息内容
                break;
            case "图片":
                xml = buildPicture(map);
                break;
            case "音乐":
                xml = buildMusic(map);
                break;
            case "图文":
                xml = buildNewsMessage(map);
                break;
            default:
                xml = String
                        .format("<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content></xml>",
                                msg.Receiver, msg.Sender, msg.CreateTime,
                                "请回复如下关键词：\n文本\n图片\n语音\n视频\n音乐\n图文");
                break;
        }*/
        return xml;
    }
    
    /**
     * 时间处理
     * @return
     */
    private static String getUtcTime() {
        Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
        String nowTime = "";
        nowTime = df.format(dt);
        long dd =  0;
        try {
            dd = df.parse(nowTime).getTime();
        } catch (Exception e) {

        }
        return String.valueOf(dd);
    }
    
    
    /**
     * 构造文本消息
     * @param map
     * @param String content  要返回给用户的消息内容 
     * @return
     */
	private static String buildTextMessage(Map map, String content) {
		System.out.println("buildTestMessage-构建返回消息：");
//        String fromUserName = map.get("FromUserName").toString();
//		String toUserName = map.get("ToUserName").toString();
		//调换发送方和接收方
		//用户发给微信号的时候，微信号是接收者toUserName。微信号返回消息给用户时，用户是接收者，微信号是发送者fromUser
		//微信号是发送者
        String fromUserName = map.get("ToUserName").toString();
        System.out.println("消息发送方："+fromUserName);
        //用户是接收者
        String toUserName = map.get("FromUserName").toString();
        System.out.println("消息接收方："+toUserName);
        System.out.println("发送的消息内容："+content);
        //使用了占位符
 /*       return String.format("<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content></xml>",
                fromUserName, toUserName, getUtcTime(), content);*/
        return String.format("<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content></xml>",
        		toUserName,fromUserName,  getUtcTime(), content);
    }
    
    
}
