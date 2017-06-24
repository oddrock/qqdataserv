package com.ustcinfo.ai.wx;

import java.util.Date;

public class WXMsg {
	
	public WXMsg() {
		super();
		record_time = new Date();
	}
	/* 共同属性 */
	private String wx_class;
	private String content;
	private String format;
	private String id;
	private String info;
	private String msg;
	private String post_type;
	private String receiver;
	private String receiver_account;
	private String receiver_id;
	private String receiver_markname;
	private String receiver_name;
	private String receiver_uid;
	private String sender;
	private String sender_account;
	private String sender_id;
	private String sender_markname;
	private String sender_name;
	private String sender_uid;
	private String source;
	private long time;
	private String wx_type;
	
	/* 图片、音频、视频相关属性 */
	private long media_code;
	private String media_data;
	private String media_ext;
	private String media_id;
	private String media_mime;
	private long media_mtime;
	private String media_name;
	private String media_path;
	private long media_size;
	private String media_type;
	
	/* 名片相关属性 */
	private String card_account;
	private String card_avatar;
	private String card_city;
	private String card_id;
	private String card_name;
	private String card_province;
	private String card_sex;
	
	/* 收藏相关属性 */
	private String app_desc;
	private String app_id;
	private String app_name;
	private String app_title;
	private String app_url;
	
	/* 群消息相关属性 */
	private String wx_group;
	private String wx_group_id;
	private String wx_group_name;
	private String wx_group_uid;
	
	/* 附加属性，我自己加上去的 */
	private Date record_time;
	private String summary;
	public String getWx_class() {
		return wx_class;
	}
	public void setWx_class(String wx_class) {
		this.wx_class = wx_class;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getPost_type() {
		return post_type;
	}
	public void setPost_type(String post_type) {
		this.post_type = post_type;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiver_account() {
		return receiver_account;
	}
	public void setReceiver_account(String receiver_account) {
		this.receiver_account = receiver_account;
	}
	public String getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}
	public String getReceiver_markname() {
		return receiver_markname;
	}
	public void setReceiver_markname(String receiver_markname) {
		this.receiver_markname = receiver_markname;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	public String getReceiver_uid() {
		return receiver_uid;
	}
	public void setReceiver_uid(String receiver_uid) {
		this.receiver_uid = receiver_uid;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSender_account() {
		return sender_account;
	}
	public void setSender_account(String sender_account) {
		this.sender_account = sender_account;
	}
	public String getSender_id() {
		return sender_id;
	}
	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}
	public String getSender_markname() {
		return sender_markname;
	}
	public void setSender_markname(String sender_markname) {
		this.sender_markname = sender_markname;
	}
	public String getSender_name() {
		return sender_name;
	}
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}
	public String getSender_uid() {
		return sender_uid;
	}
	public void setSender_uid(String sender_uid) {
		this.sender_uid = sender_uid;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getWx_type() {
		return wx_type;
	}
	public void setWx_type(String wx_type) {
		this.wx_type = wx_type;
	}
	public long getMedia_code() {
		return media_code;
	}
	public void setMedia_code(long media_code) {
		this.media_code = media_code;
	}
	public String getMedia_data() {
		return media_data;
	}
	public void setMedia_data(String media_data) {
		this.media_data = media_data;
	}
	public String getMedia_ext() {
		return media_ext;
	}
	public void setMedia_ext(String media_ext) {
		this.media_ext = media_ext;
	}
	public String getMedia_id() {
		return media_id;
	}
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	public String getMedia_mime() {
		return media_mime;
	}
	public void setMedia_mime(String media_mime) {
		this.media_mime = media_mime;
	}
	public long getMedia_mtime() {
		return media_mtime;
	}
	public void setMedia_mtime(long media_mtime) {
		this.media_mtime = media_mtime;
	}
	public String getMedia_name() {
		return media_name;
	}
	public void setMedia_name(String media_name) {
		this.media_name = media_name;
	}
	public String getMedia_path() {
		return media_path;
	}
	public void setMedia_path(String media_path) {
		this.media_path = media_path;
	}
	public long getMedia_size() {
		return media_size;
	}
	public void setMedia_size(long media_size) {
		this.media_size = media_size;
	}
	public String getMedia_type() {
		return media_type;
	}
	public void setMedia_type(String media_type) {
		this.media_type = media_type;
	}
	public String getCard_account() {
		return card_account;
	}
	public void setCard_account(String card_account) {
		this.card_account = card_account;
	}
	public String getCard_avatar() {
		return card_avatar;
	}
	public void setCard_avatar(String card_avatar) {
		this.card_avatar = card_avatar;
	}
	public String getCard_city() {
		return card_city;
	}
	public void setCard_city(String card_city) {
		this.card_city = card_city;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getCard_name() {
		return card_name;
	}
	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}
	public String getCard_province() {
		return card_province;
	}
	public void setCard_province(String card_province) {
		this.card_province = card_province;
	}
	public String getCard_sex() {
		return card_sex;
	}
	public void setCard_sex(String card_sex) {
		this.card_sex = card_sex;
	}
	public String getApp_desc() {
		return app_desc;
	}
	public void setApp_desc(String app_desc) {
		this.app_desc = app_desc;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_title() {
		return app_title;
	}
	public void setApp_title(String app_title) {
		this.app_title = app_title;
	}
	public String getApp_url() {
		return app_url;
	}
	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}
	public String getWx_group() {
		return wx_group;
	}
	public void setWx_group(String wx_group) {
		this.wx_group = wx_group;
	}
	public String getWx_group_id() {
		return wx_group_id;
	}
	public void setWx_group_id(String wx_group_id) {
		this.wx_group_id = wx_group_id;
	}
	public String getWx_group_name() {
		return wx_group_name;
	}
	public void setWx_group_name(String wx_group_name) {
		this.wx_group_name = wx_group_name;
	}
	public String getWx_group_uid() {
		return wx_group_uid;
	}
	public void setWx_group_uid(String wx_group_uid) {
		this.wx_group_uid = wx_group_uid;
	}
	public Date getRecord_time() {
		return record_time;
	}
	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	@Override
	public String toString() {
		return "WXMsg [wx_class=" + wx_class + ", content=" + content
				+ ", format=" + format + ", id=" + id + ", info=" + info
				+ ", msg=" + msg + ", post_type=" + post_type + ", receiver="
				+ receiver + ", receiver_account=" + receiver_account
				+ ", receiver_id=" + receiver_id + ", receiver_markname="
				+ receiver_markname + ", receiver_name=" + receiver_name
				+ ", receiver_uid=" + receiver_uid + ", sender=" + sender
				+ ", sender_account=" + sender_account + ", sender_id="
				+ sender_id + ", sender_markname=" + sender_markname
				+ ", sender_name=" + sender_name + ", sender_uid=" + sender_uid
				+ ", source=" + source + ", time=" + time + ", wx_type="
				+ wx_type + ", media_code=" + media_code + ", media_data="
				+ media_data + ", media_ext=" + media_ext + ", media_id="
				+ media_id + ", media_mime=" + media_mime + ", media_mtime="
				+ media_mtime + ", media_name=" + media_name + ", media_path="
				+ media_path + ", media_size=" + media_size + ", media_type="
				+ media_type + ", card_account=" + card_account
				+ ", card_avatar=" + card_avatar + ", card_city=" + card_city
				+ ", card_id=" + card_id + ", card_name=" + card_name
				+ ", card_province=" + card_province + ", card_sex=" + card_sex
				+ ", app_desc=" + app_desc + ", app_id=" + app_id
				+ ", app_name=" + app_name + ", app_title=" + app_title
				+ ", app_url=" + app_url + ", wx_group=" + wx_group
				+ ", wx_group_id=" + wx_group_id + ", wx_group_name="
				+ wx_group_name + ", wx_group_uid=" + wx_group_uid
				+ ", record_time=" + record_time + ", summary=" + summary + "]";
	}
}
