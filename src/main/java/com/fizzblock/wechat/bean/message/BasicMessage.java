package com.fizzblock.wechat.bean.message;
/**
 * 微信的基本消息类，其他类型可以扩展
 * @author glen
 *
 */
public class BasicMessage {
	/**
	 * 消息发送者  双向的，可以是公众号，可以是用户
	 */
	public String Sender;
	
	/**
	 * 消息接收者，双向的
	 */
	public String Receiver;
	
	/**
	 * 消息生成时间
	 */
	public String CreateTime;
	
	/**
	 * 消息类型
	 */
	public MessageType MsgType;
	
}
