package com.ustcinfo.ai.wx;

import org.apache.camel.Exchange;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.jayway.jsonpath.JsonPath;
import com.ustcinfo.common.utils.EmailUtils;
import com.ustcinfo.common.utils.FreezingTimer;
import com.ustcinfo.common.utils.MyBatisUtil;
import com.ustcinfo.common.utils.PropertiesUtil;

@Component("plugins4WX")
public class Plugins4WX {
	private static Logger logger = Logger.getLogger(Plugins4WX.class);
	public String rcv(Exchange exchange, String payload){
		logger.warn("plugins4WX has enter rcv method");
		StringBuffer content = new StringBuffer();
		if(payload.contains("post_type")){
			if("receive_message".equals(JsonPath.read(payload, "$.post_type"))
					|| "send_message".equals(JsonPath.read(payload, "$.post_type"))){
				WXMsg wxmsg = parseJsonStr(payload);
				exchange.setProperty("WXMsg", wxmsg);
				content.append("\n---收到WXMsg start---\n")
						.append(wxmsg.toString())
						.append("\n---收到WXMsg end  ---\n");
			}
		}else{
			content.append("\n---收到请求 start---\n")
					.append(payload).append("\n---收到请求 end  ---\n");
		}
		logger.warn(content);
		return payload;
	}
	
	public String saveDb(Exchange exchange, String payload){
		Object o = exchange.getProperty("WXMsg");
		if(o!=null){
			saveWXMsg((WXMsg)o);
		}
		return payload;
	}
	
	public String alert(Exchange exchange, String payload) throws Exception{
		Object o = exchange.getProperty("WXMsg");
		if(o!=null){
			WXMsg msg = (WXMsg)o;
			if("receive_message".equals(msg.getPost_type()) && "friend_message".equals(msg.getWx_type())){
				String[] keysender = new String[]{
						"阿阳","李飞","飞总","折腾@苏州",
						"赵总","徐况","况总","三四郎","曾总",
						"测试号","独立思考"}; 
				for(String sender : keysender){
					if(sender.equals(msg.getSender()) || sender.equals(msg.getSender_markname())){
						if(!FreezingTimer.getInstance().isInFreezingTime("wx-sendmail-focus-"+msg.getSender().trim(), Long.parseLong(PropertiesUtil.getValue("inform.interval_in_millis")))){
							logger.warn("微信：重要联系人消息，发邮件");
							StringBuffer emailContent = new StringBuffer();
							emailContent.append("发送人：").append(msg.getSender());
							if(msg.getContent()!=null){
								emailContent.append(" | ").append("发送内容：").append(msg.getContent());
							}
							
							EmailUtils.sendEmailByDefault("微信：重要联系人消息", emailContent.toString());
						}else{
							logger.warn("微信：重要联系人消息，5分钟内已发过邮件，不再重复发邮件");
						}
						break;
					}
				}
			}
			if("receive_message".equals(msg.getPost_type()) && msg.getContent()!=null && msg.getContent().contains("@荆棘谷的青山")){
				if(!FreezingTimer.getInstance().isInFreezingTime("wx-sendmail-at-"+msg.getSender().trim(), Long.parseLong(PropertiesUtil.getValue("inform.interval_in_millis")))){
					logger.warn("有人微信上@你了，发邮件");
					StringBuffer emailContent = new StringBuffer();
					emailContent.append("发送人：").append(msg.getSender());
					if(msg.getWx_group()!=null){
						emailContent.append(" | ").append("所在群：").append(msg.getWx_group());
					}
					if(msg.getContent()!=null){
						emailContent.append(" | ").append("发送内容：").append(msg.getContent());
					}
					EmailUtils.sendEmailByDefault(msg.getSender().trim()+"在微信上@你了", emailContent.toString());
				}else{
					logger.warn(msg.getSender().trim()+"在微信上@你了，5分钟内已发过邮件，不再重复发邮件");
				}
			}
		}
		return payload;
	}
	
	private WXMsg parseJsonStr(String jsonStr){
		WXMsg msg = new WXMsg();
		msg.setWx_class(JsonPath.read(jsonStr, "$.class").toString());
		if(getValueFromJsonstr("$.format", jsonStr)!=null){
			msg.setFormat(getValueFromJsonstr("$.format", jsonStr));
		}
		if(getValueFromJsonstr("$.content", jsonStr)!=null){
			msg.setContent(getValueFromJsonstr("$.content", jsonStr));
		}
		if(getValueFromJsonstr("$.id", jsonStr)!=null){
			msg.setId(getValueFromJsonstr("$.id", jsonStr));
		}
		if(getValueFromJsonstr("$.info", jsonStr)!=null){
			msg.setInfo(getValueFromJsonstr("$.info", jsonStr));
		}
		if(getValueFromJsonstr("$.msg", jsonStr)!=null){
			msg.setMsg(getValueFromJsonstr("$.msg", jsonStr));
		}
		if(getValueFromJsonstr("$.post_type", jsonStr)!=null){
			msg.setPost_type(getValueFromJsonstr("$.post_type", jsonStr));
		}
		if(getValueFromJsonstr("$.receiver", jsonStr)!=null){
			msg.setReceiver(getValueFromJsonstr("$.receiver", jsonStr));
		}
		if(getValueFromJsonstr("$.receiver_account", jsonStr)!=null){
			msg.setReceiver_account(getValueFromJsonstr("$.receiver_account", jsonStr));
		}
		if(getValueFromJsonstr("$.receiver_id", jsonStr)!=null){
			msg.setReceiver_id(getValueFromJsonstr("$.receiver_id", jsonStr));
		}
		if(getValueFromJsonstr("$.receiver_markname", jsonStr)!=null){
			msg.setReceiver_markname(getValueFromJsonstr("$.receiver_markname", jsonStr));
		}
		if(getValueFromJsonstr("$.receiver_name", jsonStr)!=null){
			msg.setReceiver_name(getValueFromJsonstr("$.receiver_name", jsonStr));
		}
		if(getValueFromJsonstr("$.receiver_uid", jsonStr)!=null){
			msg.setReceiver_uid(getValueFromJsonstr("$.receiver_uid", jsonStr));
		}
		if(getValueFromJsonstr("$.sender", jsonStr)!=null){
			msg.setSender(getValueFromJsonstr("$.sender", jsonStr));
		}
		if(getValueFromJsonstr("$.sender_account", jsonStr)!=null){
			msg.setSender_account(getValueFromJsonstr("$.sender_account", jsonStr));
		}
		if(getValueFromJsonstr("$.sender_id", jsonStr)!=null){
			msg.setSender_id(getValueFromJsonstr("$.sender_id", jsonStr));
		}
		if(getValueFromJsonstr("$.sender_markname", jsonStr)!=null){
			msg.setSender_markname(getValueFromJsonstr("$.sender_markname", jsonStr));
		}
		if(getValueFromJsonstr("$.sender_name", jsonStr)!=null){
			msg.setSender_name(getValueFromJsonstr("$.sender_name", jsonStr));
		}
		if(getValueFromJsonstr("$.sender_uid", jsonStr)!=null){
			msg.setSender_uid(getValueFromJsonstr("$.sender_uid", jsonStr));
		}
		if(getValueFromJsonstr("$.source", jsonStr)!=null){
			msg.setSource(getValueFromJsonstr("$.source", jsonStr));
		}
		if(getValueFromJsonstr("$.time", jsonStr)!=null
				&& getValueFromJsonstr("$.time", jsonStr).trim().length()>0){
			msg.setTime(Long.parseLong(getValueFromJsonstr("$.time", jsonStr)));
		}
		if(getValueFromJsonstr("$.type", jsonStr)!=null){
			msg.setWx_type(getValueFromJsonstr("$.type", jsonStr));
		}
		if(getValueFromJsonstr("$.media_code", jsonStr)!=null 
				&& getValueFromJsonstr("$.media_code", jsonStr).trim().length()>0){
			msg.setMedia_code(Long.parseLong(getValueFromJsonstr("$.media_code", jsonStr)));
		}
		if(getValueFromJsonstr("$.media_data", jsonStr)!=null){
			boolean saveMediaData = Boolean.parseBoolean(PropertiesUtil.getValue("wx.media.savecontent","false"));
			if(saveMediaData){
				msg.setMedia_data(getValueFromJsonstr("$.media_data", jsonStr));
			}
			
		}
		if(getValueFromJsonstr("$.media_ext", jsonStr)!=null){
			msg.setMedia_ext(getValueFromJsonstr("$.media_ext", jsonStr));
		}
		if(getValueFromJsonstr("$.media_id", jsonStr)!=null){
			msg.setMedia_id(getValueFromJsonstr("$.media_id", jsonStr));
		}
		if(getValueFromJsonstr("$.media_mime", jsonStr)!=null){
			msg.setMedia_mime(getValueFromJsonstr("$.media_mime", jsonStr));
		}
		if(getValueFromJsonstr("$.media_mtime", jsonStr)!=null
				&& getValueFromJsonstr("$.media_mtime", jsonStr).trim().length()>0){
			msg.setMedia_mtime(Long.parseLong(getValueFromJsonstr("$.media_mtime", jsonStr)));
		}
		if(getValueFromJsonstr("$.media_name", jsonStr)!=null){
			msg.setMedia_name(getValueFromJsonstr("$.media_name", jsonStr));
		}
		if(getValueFromJsonstr("$.media_path", jsonStr)!=null){
			msg.setMedia_path(getValueFromJsonstr("$.media_path", jsonStr));
		}
		if(getValueFromJsonstr("$.media_size", jsonStr)!=null 
				&& getValueFromJsonstr("$.media_size", jsonStr).trim().length()>0){
			msg.setMedia_size(Long.parseLong(getValueFromJsonstr("$.media_size", jsonStr)));
		}
		if(getValueFromJsonstr("$.media_type", jsonStr)!=null){
			msg.setMedia_type(getValueFromJsonstr("$.media_type", jsonStr));
		}
		if(getValueFromJsonstr("$.card_account", jsonStr)!=null){
			msg.setCard_account(getValueFromJsonstr("$.card_account", jsonStr));
		}
		if(getValueFromJsonstr("$.card_avatar", jsonStr)!=null){
			msg.setCard_avatar(getValueFromJsonstr("$.card_avatar", jsonStr));
		}
		if(getValueFromJsonstr("$.card_city", jsonStr)!=null){
			msg.setCard_city(getValueFromJsonstr("$.card_city", jsonStr));
		}
		if(getValueFromJsonstr("$.card_id", jsonStr)!=null){
			msg.setCard_id(getValueFromJsonstr("$.card_id", jsonStr));
		}
		if(getValueFromJsonstr("$.card_name", jsonStr)!=null){
			msg.setCard_name(getValueFromJsonstr("$.card_name", jsonStr));
		}
		if(getValueFromJsonstr("$.card_province", jsonStr)!=null){
			msg.setCard_province(getValueFromJsonstr("$.card_province", jsonStr));
		}
		if(getValueFromJsonstr("$.card_sex", jsonStr)!=null){
			msg.setCard_sex(getValueFromJsonstr("$.card_sex", jsonStr));
		}
		if(getValueFromJsonstr("$.app_desc", jsonStr)!=null){
			msg.setApp_desc(getValueFromJsonstr("$.app_desc", jsonStr));
		}
		if(getValueFromJsonstr("$.app_id", jsonStr)!=null){
			msg.setApp_id(getValueFromJsonstr("$.app_id", jsonStr));
		}
		if(getValueFromJsonstr("$.app_name", jsonStr)!=null){
			msg.setApp_name(getValueFromJsonstr("$.app_name", jsonStr));
		}
		if(getValueFromJsonstr("$.app_title", jsonStr)!=null){
			msg.setApp_title(getValueFromJsonstr("$.app_title", jsonStr));
		}
		if(getValueFromJsonstr("$.app_url", jsonStr)!=null){
			msg.setApp_url(getValueFromJsonstr("$.app_url", jsonStr));
		}
		if(getValueFromJsonstr("$.group", jsonStr)!=null){
			msg.setWx_group(getValueFromJsonstr("$.group", jsonStr));
		}
		if(getValueFromJsonstr("$.group_id", jsonStr)!=null){
			msg.setWx_group_id(getValueFromJsonstr("$.group_id", jsonStr));
		}
		if(getValueFromJsonstr("$.group_id", jsonStr)!=null){
			msg.setWx_group_id(getValueFromJsonstr("$.group_id", jsonStr));
		}
		if(getValueFromJsonstr("$.group_name", jsonStr)!=null){
			msg.setWx_group_name(getValueFromJsonstr("$.group_name", jsonStr));
		}
		if(getValueFromJsonstr("$.group_uid", jsonStr)!=null){
			msg.setWx_group_uid(getValueFromJsonstr("$.group_uid", jsonStr));
		}
		if(msg.getContent()!=null && (
				"text".equals(msg.getFormat())
				|| "app".equals(msg.getFormat()) 
				|| "card".equals(msg.getFormat()))){
        	if(msg.getContent().length()<64){
        		msg.setSummary(msg.getContent());
        	}else{
        		msg.setSummary(msg.getContent().substring(0, 64));
        	}
        	
        }
		return msg;
	}
	
	private String getValueFromJsonstr(String jsonPath, String jsonStr){
		try{
			if(JsonPath.read(jsonStr, jsonPath)!=null){
				return JsonPath.read(jsonStr, jsonPath).toString();
			}
		}catch(com.jayway.jsonpath.PathNotFoundException e){}
		return null;
	}
	
	@SuppressWarnings("unused")
	private void saveWXMsg(WXMsg msg){
		SqlSession sqlSession = MyBatisUtil.getSqlSession(true);
        String statement = "com.ustcinfo.ai.wx.WXMsgMapper.addMsg";
		int retResult = sqlSession.insert(statement,msg);
		/*// 判断是否保存媒体数据
		boolean saveMediaData = Boolean.parseBoolean(PropertiesUtil.getValue("wx.media.savecontent","false"));
        if("media".equals(msg.getFormat())){
        	if(!saveMediaData){
        		msg.setMedia_data(null);
            }
        }*/
		sqlSession.close();
	}

	
	public static void main(String[] args) {
		String data = "{\"class\": \"recv\", \"content\": \"你好\", \"format\": \"text\", \"id\": \"7322479217907711490\", \"post_type\": \"receive_message\", \"receiver\": \"荆棘谷的青山\", \"receiver_account\": \"\", \"receiver_id\": \"@04d6255866cb496c341072a1a3fcd1434f8f84c50c9833970c3f23d74610615c\", \"receiver_markname\": \"\", \"receiver_name\": \"荆棘谷的青山\", \"receiver_uid\": 2038771341, \"sender\": \"独立思考\", \"sender_account\": \"\", \"sender_id\": \"@153ce6e706d40a12d7f045a68bf3625134c38a6bea29a37ad5eb2a5aa15b3797\", \"sender_markname\": \"独立思考\", \"sender_name\": \"独立思考\", \"sender_uid\": \"\", \"time\": 1498271456, \"type\": \"friend_message\"}";
		System.out.println(JsonPath.read(data, "$.info"));
		//System.out.println(JsonPath.isPathDefinite("$.recv"));
	}

}
