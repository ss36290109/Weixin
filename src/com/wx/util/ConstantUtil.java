package com.wx.util;

import java.util.ArrayList;
import java.util.List;

import com.wx.message.resp.Article;

/**
 * 常量UTIL
 * 
 * @author sl
 * 
 */
public final class ConstantUtil {
	// 换行符
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * 提升用户如何获取当前位置
	 */
	public static final String GET_LOCATION = "发送当前位置操作方法如下："
			+ ConstantUtil.LINE_SEPARATOR + ConstantUtil.LINE_SEPARATOR
			+ "1.切换到输入框 [左下角按钮]" + ConstantUtil.LINE_SEPARATOR
			+ ConstantUtil.LINE_SEPARATOR + "2.在点击右下角的 + 号,点击[位置]发送即可";

	public static String getZBFWMenu(String fromUserName, String toUserName) {

		List<Article> articles = new ArrayList<Article>();

		Article article = new Article();
		article.setTitle("有点功能[sl_6666]为您服务");
		article.setPicUrl("http://slwxapp.sinaapp.com/img/logo.jpg");
		article.setUrl("");
		article.setDescription("");
		articles.add(article);
		article = null;
		article = new Article();
		article.setTitle("周边美食");
		article.setPicUrl("http://slwxapp.sinaapp.com/img/ms.jpg");
		article.setUrl("");
		article.setDescription("");
		articles.add(article);
		article = null;
		article = new Article();
		article.setTitle("周边小吃");
		article.setPicUrl("http://slwxapp.sinaapp.com/img/xc.jpg");
		article.setUrl("");
		article.setDescription("");
		articles.add(article);
		article = null;
		article = new Article();
		article.setTitle("周边团购");
		article.setPicUrl("http://slwxapp.sinaapp.com/img/tg.jpg");
		article.setUrl("");
		article.setDescription("");
		articles.add(article);
		article = null;
		article = new Article();
		article.setTitle("周边酒店");
		article.setPicUrl("http://slwxapp.sinaapp.com/img/jd.jpg");
		article.setUrl("");
		article.setDescription("");
		articles.add(article);
		article = null;

		return MessageUtil.newsMessageToXml(fromUserName, toUserName, articles);
	}
}
