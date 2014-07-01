package com.wx.message.req;

/**
 * 文本消息
 * @author sl
 *
 */
public class ReqTextMessage extends ReqBaseMessage {

	// 消息内容  
    private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	
}
