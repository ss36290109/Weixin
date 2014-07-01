package com.wx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.topcheer.mapcache.MapCache;
import com.wx.jdbc.JdbcTools;
import com.wx.message.resp.Article;
import com.wx.util.BDApi;
import com.wx.util.ConstantUtil;
import com.wx.util.DZDPApi;
import com.wx.util.DateUtil;
import com.wx.util.LogUtil;
import com.wx.util.MessageUtil;
import com.wx.util.TuringRobotUtil;

/**
 * 核心服务类
 * 
 * @author sl
 */
public final class CoreService {
	private CoreService() {
	}

	private static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {

			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				respMessage = MessageUtil.textMessageToXml(fromUserName,
						toUserName, TuringRobotUtil.getResultByTR(requestMap
								.get("Content")));
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				String locationX = requestMap.get("Location_X");
				String locationY = requestMap.get("Location_Y");
				MapCache mapCache = MapCache.getMapCache(fromUserName, 10);
				mapCache.put("Location_X", locationX);
				mapCache.put("Location_Y", locationY);
				LogUtil.debug("MAPCACHE<<<<" + mapCache.getMapCacheInfo()
						+ "..." + mapCache.getCurrTime());
				respMessage = ConstantUtil
						.getZBFWMenu(fromUserName, toUserName);

			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					String content = "谢谢您的关注！" + ConstantUtil.LINE_SEPARATOR;
					// content += ConstantUtil.MENU_INFO;
					respMessage = MessageUtil.textMessageToXml(fromUserName,
							toUserName, content);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					respMessage = getRespContent(requestMap.get("EventKey"),
							fromUserName, toUserName, request);
				}
			}
		} catch (Exception e) {
			LogUtil.error("CoreService error", e);
		}
		return respMessage;
	}

	private static String getRespContentByLocation(String locationX,
			String locationY, String fromUserName, String toUserName) {

		List<Article> articles = DZDPApi.INSTANCE.getZBXL(locationX, locationY);

		if (articles == null) {
			return MessageUtil.textMessageToXml(fromUserName, toUserName,
					"请求异常，请稍后再试");
		}
		return MessageUtil.newsMessageToXml(fromUserName, toUserName, articles);
	}

	public static String getRespContent(String string, String fromUserName,
			String toUserName, HttpServletRequest request) {
		String info = "";
		if (string.equals("HLCK")) {
			Map<String, String> result = JdbcTools.getYelloPageResult(
					DateUtil.getYearByNow(), DateUtil.getMonthByNow(),
					DateUtil.getDayByNow());
			info = "今天是: " + DateUtil.formatDate("yyyy-MM-dd")
					+ ConstantUtil.LINE_SEPARATOR + ConstantUtil.LINE_SEPARATOR;
			;
			info += "宜: " + result.get("GOOD") + ConstantUtil.LINE_SEPARATOR
					+ ConstantUtil.LINE_SEPARATOR;
			info += "忌: " + result.get("BAD") + ConstantUtil.LINE_SEPARATOR;
		} else if (string.equals("ZBFW")) {
			MapCache mapCache = MapCache.getMapCache(fromUserName);

			LogUtil.debug("MAPCACHE<<<<" + mapCache.getMapCacheInfo() + "..."
					+ mapCache.getCurrTime());

			if (mapCache.get("Location_X") == null
					|| mapCache.get("Location_Y") == null) {
				info = ConstantUtil.GET_LOCATION;
			} else {

				return ConstantUtil.getZBFWMenu(fromUserName, toUserName);

				/*
				 * return getRespContentByLocation(mapCache.get("Location_X")
				 * .toString(), mapCache.get("Location_Y").toString(),
				 * fromUserName, toUserName);
				 */
			}

		} else if (string.equals("RMTG")) {
			String ip = getIpAddr(request);
			Map<String, String> xyMap = BDApi.INSTANCE.getLocationByIp(ip);
			return MessageUtil.newsMessageToXml(fromUserName, toUserName,
					DZDPApi.INSTANCE.getRMTG(xyMap.get("city")));

		} else if (string.equals("TQYB")) {
			String ip = getIpAddr(request);
			Map<String, String> xyMap = BDApi.INSTANCE.getLocationByIp(ip);
			String location = xyMap.get("x") + "," + xyMap.get("y");
			xyMap.clear();
			xyMap = null;
			return MessageUtil.newsMessageToXml(fromUserName, toUserName,
					BDApi.INSTANCE.getWeatherByLocation(location));
		} else if (string.equals("XH")) {
			info = TuringRobotUtil.getResultByTR("笑话");
		} else if (string.equals("LTLK")) {
			info = TuringRobotUtil.getResultByTR("陪我聊聊啊");
		}
		return MessageUtil.textMessageToXml(fromUserName, toUserName, info);
	}

}
