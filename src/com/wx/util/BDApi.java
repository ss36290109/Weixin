package com.wx.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wx.message.resp.Article;

public enum BDApi {

	INSTANCE;
	private static final String AK = "064a3dcbb0b5d08031909ab8aa3650eb";
	// private static final String ZBXL =
	// "http://api.map.baidu.com/telematics/v3/local?location=LOCATION_Y,LOCATION_X&keyWord=INFO&output=xml&ak=064a3dcbb0b5d08031909ab8aa3650eb";//
	// 周边系列
	private static final String IP_LOCATE = "http://api.map.baidu.com/location/ip?ip=IP&ak="
			+ AK + "&coor=bd09ll";
	private static final String WEATHER_API = "http://api.map.baidu.com/telematics/v3/weather?location=LOCATION&output=json&ak="
			+ AK;

	private BDApi() {
	}

	/**
	 * 得到微信图文消息的天气格式
	 * 
	 * @param location
	 *            可以是中文,比如：北京，或者是地区的经纬度用逗号分隔，比如 121.123,31.887
	 * @return
	 */
	public List<Article> getWeatherByLocation(String location) {
		String url = WEATHER_API.replace("LOCATION", location);
		List<Article> articles = new ArrayList<Article>();
		try {
			String data = InvokeApiUtil.getApiResult(url);
			JSONObject jsonObject = new JSONObject(data);
			JSONArray jsonArray = jsonObject.getJSONArray("results");
			jsonObject = null;
			jsonObject = jsonArray.getJSONObject(0);
			String city = jsonObject.getString("currentCity");
			jsonArray = null;
			jsonArray = jsonObject.getJSONArray("weather_data");
			jsonObject = null;
			// 百度API只返回今天，明天，后天 3天天气情况
			String title = "";
			Article article;
			LogUtil.debug("jsonArray:" + jsonArray);
			article = new Article();
			article.setDescription("");
			article.setUrl("");
			article.setPicUrl("http://1.slwxapp.sinaapp.com/img/weather.jpg");
			article.setTitle(city);
			articles.add(article);
			article = null;
			for (int i = 0; i < 3; i++) {
				jsonObject = jsonArray.getJSONObject(i);
				article = new Article();
				article.setDescription("");
				title = jsonObject.getString("date")
						+ ConstantUtil.LINE_SEPARATOR;
				title += "天气：" + jsonObject.getString("weather")
						+ ConstantUtil.LINE_SEPARATOR;
				title += "风力：" + jsonObject.getString("wind")
						+ ConstantUtil.LINE_SEPARATOR;
				title += "温度：" + jsonObject.getString("temperature");
				article.setTitle(title);
				article.setPicUrl(jsonObject.getString("dayPictureUrl"));
				article.setUrl("");
				articles.add(article);
				article = null;
				jsonObject = null;
			}
			jsonArray = null;
			return articles;
		} catch (Exception e) {
			LogUtil.error("BDApi.getWeatherByLocation error", e);
		}
		return null;
	}

	// 周边系列...目前用大众点评API
	public void getZBXL() {
	}

	/**
	 * 返回map;key:x为经度，y为维度
	 * 
	 * @param ip
	 * @return
	 */
	public Map<String, String> getLocationByIp(String ip) {
		String url = IP_LOCATE.replace("IP", ip);
		Map<String, String> map = new HashMap<String, String>();
		try {
			String result = InvokeApiUtil.getApiResult(url);
			JSONObject jsonObject = new JSONObject(result);
			jsonObject = jsonObject.getJSONObject("content");

			JSONObject cityOBJ = jsonObject.getJSONObject("address_detail");

			jsonObject = jsonObject.getJSONObject("point");
			String x = jsonObject.getString("x");
			String y = jsonObject.getString("y");
			String city = cityOBJ.getString("city");
			city=city.replaceAll("市", "");
			jsonObject = null;
			map.put("x", x);
			map.put("y", y);
			map.put("city", city);
		} catch (Exception e) {
			LogUtil.error("BDApi.getLocationByIp error", e);
		}
		return map;
	}
}
