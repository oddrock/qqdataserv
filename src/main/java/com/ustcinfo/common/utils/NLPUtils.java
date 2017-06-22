package com.ustcinfo.common.utils;

import com.ustcinfo.common.nlp.BaiduSentimentAnalyzer;
import com.ustcinfo.common.nlp.BosonSentimentAnalyzer;

public class NLPUtils {	
	public static boolean isNegative(String sentence){
		return new BaiduSentimentAnalyzer().isSentimentNegative(sentence);
	}

}
