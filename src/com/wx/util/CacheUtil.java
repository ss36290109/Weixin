package com.wx.util;

import java.util.HashMap;
import java.util.Map;

public enum CacheUtil {
	INSTANCE;
	private Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();

	public Map<String, Object> get(String key) {
		return map.get(key);
	}

	public void put(String key, Map<String, Object> value) {
		map.put(key, value);
	}

	public void remove(String key) {
		map.remove(key);
	}
}
