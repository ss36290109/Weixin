package com.wx.util;

import org.apache.log4j.Logger;

public final class LogUtil {
	private static final Logger LOGGER = Logger.getLogger("");

	private LogUtil() {
	}

	public static void debug(Object msg) {
		LOGGER.debug(msg);
	}

	public static void error(Object msg, Throwable e) {
		LOGGER.error(msg, e);
	}
}
