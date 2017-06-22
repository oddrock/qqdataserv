package com.ustcinfo.common.nlp;

import org.json.JSONObject;
import com.baidu.aip.nlp.AipNlp;
import com.jayway.jsonpath.JsonPath;
import com.ustcinfo.common.utils.SecretPropertiesUtil;

public class BaiduSentimentAnalyzer implements SentimentAnalyzer {
	public boolean isSentimentNegative(String sentence) {
		AipNlp client = new AipNlp(SecretPropertiesUtil.getValue("baidu.ai.appid"), 
				SecretPropertiesUtil.getValue("baidu.ai.appkey"), 
				SecretPropertiesUtil.getValue("baidu.ai.secretkey"));
		client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
		JSONObject response = client.sentimentClassify(sentence);
        String responseStr = response.toString();
        int sentiment = -1;
        double negative_prob = -1;
        if(JsonPath.read(responseStr, "$.items[0].sentiment")!=null){
        	sentiment = Integer.parseInt(JsonPath.read(responseStr, "$.items[0].sentiment").toString());
		}
        if(JsonPath.read(responseStr, "$..negative_prob")!=null){
        	negative_prob = Double.parseDouble(JsonPath.read(responseStr, "$.items[0].negative_prob").toString());
        }
        if (sentiment==0 && negative_prob>0.5){
			return true;
		}else{
			return false;
		}
	}
	
	public static void main(String[] args) {
		String sentence = "我顶不住这压力了，你们这样搞下去我受不了";
		//sentence = "你好";
		System.out.println(new BaiduSentimentAnalyzer().isSentimentNegative(sentence));
	}

}
