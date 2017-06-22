package com.ustcinfo.common.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 冷冻计时器，查看某一个元素是否过了冷冻时间
 * @author oddrock
 *
 */
public class FreezingTimer {
	private Map<String,Long> timermap = new HashMap<String,Long>();
	
	/* 单例模式 */
	private static final FreezingTimer single = new FreezingTimer();
	private FreezingTimer(){}
	public static FreezingTimer getInstance(){
		return single;
	}
	
	/**
	 * 对一个key计算是否当前已经过了他的冷却时间。
	 * 过了就返回false，并将上一次冷却时间置为当前时间，否则返回true。
	 * @param key	
	 * @param intervalInMillis		间隔时间毫秒数
	 * @return
	 */
	public boolean isInFreezingTime(String key, long intervalInMillis){
		if(key==null || key.trim().length()==0){
			return true;
		}
		key = key.trim();
		Long currentMillis = Calendar.getInstance().getTimeInMillis();
		if(timermap.containsKey(key)){
			Long millis = timermap.get(key);
			if((millis+intervalInMillis)<=currentMillis){
				timermap.put(key, currentMillis);
				return false;
			}else{
				return true;
			}
		}else{
			timermap.put(key, currentMillis);
			return false;
		}
	}
	
	public static void main(String[] args) {
		System.out.println();
	}

}
