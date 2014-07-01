package com.wx.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

/**
 * 图灵机器人相关API调用工具类
 * 
 * @author sl
 * 
 */
public final class TuringRobotUtil {
	private static final String TURING_ROBOT_API_URL = "http://www.wendacloud.com/openapi/api?key=4acc62fbedb42c54d8b8155a495ec2a3&info=INFO";

	/**
	 * 私有构造器
	 */
	private TuringRobotUtil() {
	}

	public static String getResultByTR(String info) {
		String url = TURING_ROBOT_API_URL.replace("INFO", info);
		String text = "";
		try {
			do {
				text = invokeTRAPI(url);
			} while (getByteSize(text) >= 2000);
			return invokeTRAPI(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	private static String invokeTRAPI(String url) throws IOException {
		String data = InvokeApiUtil.getApiResult(url);
		JSONObject jsonObject = new JSONObject(data);
		String text = (String) jsonObject.get("text");
		return text;
	}

	public static void main(String[] args) throws IOException {

		System.out.println(getResultByTR("笑话"));
	}

	/**
	 * 计算采用utf-8编码方式时字符串所占字节数
	 * 
	 * @param content
	 * @return
	 */
	private static int getByteSize(String content) {
		int size = 0;
		if (null != content) {
			try {
				// 汉字采用utf-8编码时占3个字节
				size = content.getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
}
