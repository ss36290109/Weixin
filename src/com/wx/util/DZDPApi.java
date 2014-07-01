package com.wx.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wx.message.resp.Article;

/**
 * 大众点评API相关调用类
 * 
 * @author sl
 * 
 */
public enum DZDPApi {

	INSTANCE;
	private static final String AppKey = "77657307";
	private static final String AppSecret = "cb3bad6ebe7747059ef950c900ed7143";
	// 周边美食系列
	private static final String ZBXL_URL = "http://api.dianping.com/v1/business/find_businesses?appkey="
			+ AppKey;

	// 团购
	private static final String SEARCH_TG_URL = "http://api.dianping.com/v1/deal/find_deals?appkey="
			+ AppKey;

	private DZDPApi() {
	}

	// 周边系列 默认搜美食
	public List<Article> getZBXL(String x, String y) {

		return getZBXL(x, y, "美食");
	}

	// d当前城市热门美食团购
	public List<Article> getRMTG(String city) {

		// 创建参数表
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("city", city);
		paramMap.put("category", "美食");
		paramMap.put("is_local", "1");
		paramMap.put("sort", "4");
		paramMap.put("limit", "10");
		paramMap.put("sign",getSign(paramMap));

		return getTGData(SEARCH_TG_URL + getUrlParam(paramMap));
	}

	// 周边系列
	public List<Article> getZBXL(String x, String y, String category) {

		// 创建参数表
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("latitude", x);
		paramMap.put("longitude", y);
		paramMap.put("category", category);
		paramMap.put("platform", "2");
		paramMap.put("sort", "2");
		paramMap.put("limit", "10");
		paramMap.put("sign", getSign(paramMap));

		return getZBXLData(ZBXL_URL + getUrlParam(paramMap));
	}

	private String getUrlParam(Map<String, String> paramMap) {
		StringBuilder sbBuilder = new StringBuilder();
		for (String key : paramMap.keySet()) {
			sbBuilder.append("&").append(key).append("=");
			// 判断是否为中文
			if (paramMap.get(key).matches("[\\u4E00-\\u9FA5]+")) {
				try {
					sbBuilder.append(URLEncoder.encode(paramMap.get(key),
							"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				sbBuilder.append(paramMap.get(key));
			}

		}
		return sbBuilder.toString();

	}

	private String getSign(Map<String, String> paramMap) {

		// 对参数名进行字典排序
		String[] keyArray = paramMap.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);

		// 拼接有序的参数名-值串
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(AppKey);
		for (String key : keyArray) {
			stringBuilder.append(key).append(paramMap.get(key));
		}

		stringBuilder.append(AppSecret);
		String codes = stringBuilder.toString();

		String sign = org.apache.commons.codec.digest.DigestUtils.shaHex(codes)
				.toUpperCase();
		return sign;

	}

	private List<Article> getZBXLData(String url) {
		List<Article> articles = null;
		try {
			String data = InvokeApiUtil.getApiResult(url);
			JSONObject jsonObject = new JSONObject(data);
			JSONArray jsonArray = (JSONArray) jsonObject.get("businesses");
			JSONObject result;
			String title;
			int avgPrice;
			articles = new ArrayList<Article>();
			for (int i = 0; i < jsonArray.length(); i++) {
				result = jsonArray.getJSONObject(i);
				Article article = new Article();
				title = result.getString("name");
				title += ConstantUtil.LINE_SEPARATOR;
				title += "星级评分：" + result.get("avg_rating");
				title += ConstantUtil.LINE_SEPARATOR;
				avgPrice = result.getInt("avg_price");
				if (avgPrice != -1) {
					title += "人均价格：" + avgPrice + "元";
				}
				article.setTitle(title);
				if (i == 0) {
					article.setPicUrl(result.getString("photo_url"));
				} else {
					article.setPicUrl(result.getString("s_photo_url"));
				}
				article.setUrl(result.getString("business_url"));
				article.setDescription("");
				articles.add(article);
				article = null;
			}
			return articles;
		} catch (IOException e) {
			LogUtil.error("DZDPApi.getZBXLData error", e);
		}
		return null;
	}
	public static void main(String[] args) {
		if ("美食".matches("[\\u4E00-\\u9FA5]+")) {System.out.println("1");}

	}
	private List<Article> getTGData(String url) {
		System.out.println(url);
		List<Article> articles = null;
		try {
			String data = InvokeApiUtil.getApiResult(url);
			JSONObject jsonObject = new JSONObject(data);
			JSONArray jsonArray = (JSONArray) jsonObject.get("deals");
			JSONObject result;
			String title;
			articles = new ArrayList<Article>();
			for (int i = 0; i < jsonArray.length(); i++) {
				result = jsonArray.getJSONObject(i);
				Article article = new Article();
				title = result.getString("title");
				title += ConstantUtil.LINE_SEPARATOR;
				title += result.get("description");
				title += ConstantUtil.LINE_SEPARATOR;
				article.setTitle(title);
				if (i == 0) {
					article.setPicUrl(result.getString("image_url"));
				} else {
					article.setPicUrl(result.getString("s_image_url"));
				}
				article.setUrl(result.getString("deal_h5_url"));
				article.setDescription("");
				articles.add(article);
				article = null;
			}
			return articles;
		} catch (IOException e) {
			LogUtil.error("DZDPApi.getTGData error", e);
		}
		return null;
	}
}
