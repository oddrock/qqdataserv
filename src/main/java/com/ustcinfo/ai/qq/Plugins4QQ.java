package com.ustcinfo.ai.qq;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.JsonPath;
import com.ustcinfo.common.utils.EmailUtils;
import com.ustcinfo.common.utils.MyBatisUtil;
import com.ustcinfo.common.utils.NLPUtils;
import com.ustcinfo.common.utils.QQUtils;

@Component("plugins4QQ")
public class Plugins4QQ {
	private static Logger logger = Logger.getLogger(Plugins4QQ.class);
	
	public String rcv(Exchange exchange, String payload){
		logger.warn("has enter rcv method");
		StringBuffer content = new StringBuffer();
		if(payload.contains("post_type")){
			if(JsonPath.read(payload, "$.post_type").equals("receive_message")){
				QQMsg qqmsg = parseJsonStr(payload);
				exchange.setProperty("QQMsg", qqmsg);
				content.append("\n---收到QQMsg start---\n")
						.append(qqmsg.toString())
						.append("\n---收到QQMsg end  ---\n");
			}
		}else{
			content.append("\n---收到请求 start---\n")
					.append(payload).append("\n---收到请求 end  ---\n");
		}
		logger.warn(content);
		return payload;
	}
	
	/**
	 * 将数据保存到数据库
	 * @param exchange
	 * @param payload
	 * @return
	 */
	public String saveDb(Exchange exchange, String payload){
		Object o = exchange.getProperty("QQMsg");
		if(o!=null){
			saveQQMsg((QQMsg)o);
		}
		return payload;
	}
	
	/**
	 * 告警
	 * @param exchange
	 * @param payload
	 * @return
	 * @throws Exception 
	 */
	public String alert(Exchange exchange, String payload) throws Exception{
		Object o = exchange.getProperty("QQMsg");
		if(o!=null){
			QQMsg msg = (QQMsg)o;
			if(msg.getContent()!=null){
				if(msg.getSender().trim().equals("刘兆刚")
						|| msg.getSender().trim().equals("小坏蛋")){
					if(NLPUtils.isNegative(msg.getContent())){
						logger.warn("负面情绪预警，发邮件");
						StringBuffer emailContent = new StringBuffer();
						emailContent.append("发送人：").append(msg.getSender()).append(" | ");
						if(msg.getQqgroup()!=null){
							emailContent.append("所在群：").append(msg.getQqgroup()).append(" | ");
						}
						emailContent.append("发送内容：").append(msg.getContent());
						EmailUtils.sendEmailByDefault("负面情绪预警", emailContent.toString());
					}
				}
				if((msg.getSender().trim().equals("杨总") 
						|| msg.getSender().trim().equals("飞总")
						|| msg.getSender().trim().equals("赵总")
						|| msg.getSender().trim().equals("况总")
						|| msg.getSender().trim().equals("朝天椒")
						)
						&& msg.getType().equals("friend_message")){
					StringBuffer emailContent = new StringBuffer();
					emailContent.append("发送人：").append(msg.getSender()).append(" | ");
					if(msg.getQqgroup()!=null){
						emailContent.append("所在群：").append(msg.getQqgroup()).append(" | ");
					}
					emailContent.append("发送内容：").append(msg.getContent());
					EmailUtils.sendEmailByDefault("重要联系人消息", emailContent.toString());
				}
				if(msg.getContent().contains("@荆棘谷的青山")){
					StringBuffer emailContent = new StringBuffer();
					emailContent.append("发送人：").append(msg.getSender()).append(" | ");
					if(msg.getQqgroup()!=null){
						emailContent.append("所在群：").append(msg.getQqgroup()).append(" | ");
					}
					emailContent.append("发送内容：").append(msg.getContent());
					EmailUtils.sendEmailByDefault("有人QQ上@你了", emailContent.toString());
				}
			}
		}
		return payload;
	}
	
	private QQMsg parseJsonStr(String jsonStr){
		QQMsg qqmsg = new QQMsg();
		qqmsg.setMsg_class(JsonPath.read(jsonStr, "$.class").toString());
		if(JsonPath.read(jsonStr, "$.content")!=null){
			String content = JsonPath.read(jsonStr, "$.content").toString();
			content = QQUtils.filterEmoji(content);
			if(content.length()>0){
				qqmsg.setContent(content);
			}
		}
		if(JsonPath.read(jsonStr, "$.id")!=null){
			qqmsg.setId(Long.parseLong(JsonPath.read(jsonStr, "$.id").toString()));
		}
		qqmsg.setPost_type(JsonPath.read(jsonStr, "$.post_type").toString());
		qqmsg.setReceiver(JsonPath.read(jsonStr, "$.receiver").toString());
		if(JsonPath.read(jsonStr, "$.receiver_id")!=null){
			qqmsg.setReceiver_id(Long.parseLong(JsonPath.read(jsonStr, "$.receiver_id").toString()));
		}
		if(JsonPath.read(jsonStr, "$.receiver_uid")!=null){
			qqmsg.setReceiver_uid(Long.parseLong(JsonPath.read(jsonStr, "$.receiver_uid").toString()));
		}
		qqmsg.setSender(JsonPath.read(jsonStr, "$.sender").toString());
		if(JsonPath.read(jsonStr, "$.sender_id")!=null){
			qqmsg.setSender_id(Long.parseLong(JsonPath.read(jsonStr, "$.sender_id").toString()));
		}
		if(JsonPath.read(jsonStr, "$.sender_uid")!=null){
			qqmsg.setSender_uid(Long.parseLong(JsonPath.read(jsonStr, "$.sender_uid").toString()));
		}
		qqmsg.setTime(Long.parseLong(JsonPath.read(jsonStr, "$.time").toString()));
		qqmsg.setType(JsonPath.read(jsonStr, "$.type").toString());
		String type = JsonPath.read(jsonStr, "$.type").toString();
		if(type.equals("group_message")){
			if(JsonPath.read(jsonStr, "$.group")!=null){
				qqmsg.setQqgroup(JsonPath.read(jsonStr, "$.group").toString());
			}
			if(JsonPath.read(jsonStr, "$.group_id")!=null){
				qqmsg.setQqgroup_id(Long.parseLong(JsonPath.read(jsonStr, "$.group_id").toString()));
			}
			if(JsonPath.read(jsonStr, "$.group_uid")!=null){
				qqmsg.setQqgroup_uid(Long.parseLong(JsonPath.read(jsonStr, "$.group_uid").toString()));
			}
		}else if(type.equals("discuss_message")){ 
			if(JsonPath.read(jsonStr, "$.discuss")!=null){
				qqmsg.setQqgroup(JsonPath.read(jsonStr, "$.discuss").toString());
			}
			if(JsonPath.read(jsonStr, "$.discuss_id")!=null){
				qqmsg.setQqgroup_id(Long.parseLong(JsonPath.read(jsonStr, "$.discuss_id").toString()));
			}
		}
		return qqmsg;
	}
	
	@SuppressWarnings("unused")
	private void saveQQMsg(QQMsg msg){
		SqlSession sqlSession = MyBatisUtil.getSqlSession(true);
        String statement = "com.ustcinfo.ai.qq.QQMsgMapper.addMsg";//映射sql的标识字符串
        if(msg.getContent()!=null){
        	if(msg.getContent().length()<64){
        		msg.setSummary(msg.getContent());
        	}else{
        		msg.setSummary(msg.getContent().substring(0, 64));
        	}
        	
        }
        msg.setRecord_time(new Date());
        int retResult = sqlSession.insert(statement,msg);
        sqlSession.close();
	}
	
	public static void main(String[] args){
		System.out.println(new Date());
	}
}
