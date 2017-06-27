package com.ustcinfo.ai.common;


public class NLPManager {	
	public static boolean isNegative(String sentence){
		return new BaiduSentimentAnalyzer().isSentimentNegative(sentence);
	}

}
