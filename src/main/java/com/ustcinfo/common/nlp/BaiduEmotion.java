package com.ustcinfo.common.nlp;

public class BaiduEmotion {
	private long log_id = -1;
	private String text;
	private double positive_prob = -1;
	private double negative_prob = -1;
	// sentiment表示情感极性分类结果, 0:负向，1:中性，2:正向
	private int sentiment = -1;
	private double confidence = -1;
	public long getLog_id() {
		return log_id;
	}
	public void setLog_id(long log_id) {
		this.log_id = log_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public double getPositive_prob() {
		return positive_prob;
	}
	public void setPositive_prob(double positive_prob) {
		this.positive_prob = positive_prob;
	}
	public double getNegative_prob() {
		return negative_prob;
	}
	public void setNegative_prob(double negative_prob) {
		this.negative_prob = negative_prob;
	}
	public int getSentiment() {
		return sentiment;
	}
	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	@Override
	public String toString() {
		return "Emotion [log_id=" + log_id + ", text=" + text
				+ ", positive_prob=" + positive_prob + ", negative_prob="
				+ negative_prob + ", sentiment=" + sentiment + ", confidence="
				+ confidence + "]";
	}
	public boolean isNegative(){
		if (sentiment==0 && negative_prob>0.5){
			return true;
		}else{
			return false;
		}
	}
}
