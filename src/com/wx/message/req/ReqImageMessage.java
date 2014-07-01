package com.wx.message.req;

/**
 * 图片消息
 * @author sl
 *
 */
public class ReqImageMessage extends ReqBaseMessage {

	//图片路径
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	
}
