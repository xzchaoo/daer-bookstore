package org.xzc.sshb.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static Date getCurrentMonth() {
		return org.apache.commons.lang3.time.DateUtils.truncate( new Date(), Calendar.MONTH );
	}

	/**
	 * 获得两个日期差几天
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDayDiff(Date d1, Date d2) {
		long now = d1.getTime();
		long birth = d2.getTime();
		return (int) ( ( now - birth ) / 1000 / 3600 / 24 );
	}
}
