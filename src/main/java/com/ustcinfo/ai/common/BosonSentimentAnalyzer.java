package com.ustcinfo.ai.common;

import org.json.JSONArray;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.ustcinfo.common.nlp.SentimentAnalyzer;

public class BosonSentimentAnalyzer implements SentimentAnalyzer {
	public static final String BOSON_SENTIMENT_URL = "http://api.bosonnlp.com/sentiment/analysis";
	public static final String BOSON_API_TOKEN = "o_qHZ40G.16032.Hu917jLmYQTB";

	public boolean isSentimentNegative(String sentence){
		String body = new JSONArray(new String[]{sentence}).toString();
		HttpResponse<JsonNode> jsonResponse;
		double negative_prob = -1;
		String responseStr = null;
		try {
			jsonResponse = Unirest.post(PropertiesManager.getValue("boson.apiurl.sentiment"))
					.header("Accept", "application/json")
					.header("X-Token", PropertiesManager.getValue("boson.apitoken")).body(body).asJson();
			Unirest.shutdown();
			responseStr = jsonResponse.getBody().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (responseStr!=null){
			int beginIndex = responseStr.indexOf(",")+1;
			int endIndex = responseStr.length()-2;
			negative_prob = Double.parseDouble(
					responseStr.substring(beginIndex, endIndex));
		}	
		if(negative_prob>0.5){
			return true;
		}else{
			return false;
		}
	}

	public static void main(String[] args) {
		String sentence = "你太坏了，有问题";
		//sentence = "你好";
		System.out.println(new BosonSentimentAnalyzer().isSentimentNegative(sentence));
	}

}
