package common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import weather.Weather;
import weather.WeatherService;

public class CoreService {  
	private static boolean mParkingSearch = false;
    /** 
     * 处理微信发来的请求 
     *  
     * @param request 
     * @return 
     */  
    public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;  
        try {  
            // 默认返回的文本消息内容  
            String respContent = "请求处理异常，请稍候尝试";  
  
            // xml请求解析  
            Map<String, String> map = MessageUtil.xmlToMap(request);
  
            // 发送方帐号（open_id）  
            String fromUserName = map.get("FromUserName");  
            // 公众帐号  
            String toUserName = map.get("ToUserName");  
            // 消息类型  
            String msgType = map.get("MsgType");  
  
            // 回复文本消息  
            TextMessage textMessage = new TextMessage();  
            textMessage.setToUserName(fromUserName);  
            textMessage.setFromUserName(toUserName);  
            textMessage.setCreateTime(new Date().getTime());  
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
  
            // 文本消息  
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {  
            	String content = map.get("Content");
            	if(mParkingSearch){
            		if(content.length() == 7 && isChinese(content.charAt(0))){
                		respContent = "您的车辆" + "\"" + content + "\"" + "位于" + "\"" + "A区001车位" + "\"";
                		mParkingSearch = false;
            		}else{
            			respContent = "请输入正确牌照";
                        textMessage.setContent(respContent);  
                        respMessage = MessageUtil.textMessageToXML(textMessage);  
            		}
            	}else{
                    respContent = "您发送的是文本消息，内容是" + "\"" + content + "\"";
            	}
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXML(textMessage);  
            }  
            // 图片消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
                respContent = "您发送的是图片消息";  
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXML(textMessage);  
            }  
            // 地理位置消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
                respContent = "您发送的是地理位置消息";  
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXML(textMessage);  
            }  
            // 链接消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
                respContent = "您发送的是链接消息";  
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXML(textMessage);  
            }  
            // 音频消息  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
                respContent = "您发送的是音频消息";  
                textMessage.setContent(respContent);  
                respMessage = MessageUtil.textMessageToXML(textMessage);  
            }  
            // 事件推送  
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
                // 事件类型  
                String eventType = map.get("Event");  
                // 订阅  
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
                    respContent = "欢迎光临辽宁鞍山千山风景区，千山是一个神奇的地方，是一个迷人的地方，"
                    		+ "更是一个令人流连忘返的地方。" + "\n" + "看尽江南山水美，常怜北国穷山水。" + "\n"
                    		+  "识得关东千山秀，不看五岳也无悔。";  
                    textMessage.setContent(respContent);  
                    respMessage = MessageUtil.textMessageToXML(textMessage);  
                }  
                // 取消订阅  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息  
                }  
                // 自定义菜单点击事件  
                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {  
                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应  
                    NewsMessage newsMessage = new NewsMessage(); 
                    newsMessage.setToUserName(fromUserName); 
                    newsMessage.setFromUserName(toUserName); 
                    newsMessage.setCreateTime(new Date().getTime()); 
                    newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);                    
                    List<Article> articleList = new ArrayList<Article>(); 
                    
                	String eventKey = map.get("EventKey");  
                    
                    if (eventKey.equals("11")) {  
                        respContent = "景区介绍被点击";  
                        Article article = new Article(); 
                        article.setTitle("辽宁鞍山千山景区"); 
                        article.setDescription("美不胜收——辽宁鞍山千山景区"); 
                        article.setPicUrl("http://m.tuniucdn.com/filebroker/cdn/online/b3/fa/b3faf40d_w800_h0_c0_t0.jpg"); 
                        article.setUrl("http://www.qianshan.ln.cn/jxgk/introduction.html"); 
                        articleList.add(article); 
                        // 设置图文消息个数 
                        newsMessage.setArticleCount(articleList.size()); 
                        // 设置图文消息包含的图文集合 
                        newsMessage.setArticles(articleList); 
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);
                    } else if (eventKey.equals("12")) {  
                        respContent = "景区地图被点击";  
                        Article article = new Article(); 
                        article.setTitle("景区地图"); 
                        article.setDescription(""); 
                        article.setPicUrl("http://www.qianshan.ln.cn/upload/contents/2016/09/57e21c22aa8db.jpg"); 
                        article.setUrl("http://www.qianshan.ln.cn/jxgk/index-9-6.html"); 
                        articleList.add(article); 
                        // 设置图文消息个数 
                        newsMessage.setArticleCount(articleList.size()); 
                        // 设置图文消息包含的图文集合 
                        newsMessage.setArticles(articleList); 
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);
                    }  else if (eventKey.equals("21")) {  
                        respContent = "天气查询被点击";  
                        Weather weather=new Weather();
                    	weather=WeatherService.weatherDetect("鞍山");
                    	List<Map<String,String>> weather_data=new ArrayList<Map<String,String>>();
                    	weather_data=weather.getWeather_data();
                        Article article1 = new Article(); 
                    	article1.setTitle("鞍山 " + weather_data.get(0).get("date")+"\n"+weather_data.get(0).get("weather") + " " + weather_data.get(0).get("wind") + " " + weather_data.get(0).get("temperature"));  
                        article1.setDescription("");  
                        article1.setPicUrl(weather_data.get(0).get("dayPictureUrl"));  
                        article1.setUrl("http://www.weather.com.cn/weather1d/101070301.shtml");
                        articleList.add(article1);
                        
                        Article article2 = new Article(); 
                    	article2.setTitle("鞍山 " + weather_data.get(1).get("date")+"\n"+weather_data.get(1).get("weather")+ " " + weather_data.get(1).get("wind") + " " + weather_data.get(1).get("temperature"));  
                        article2.setDescription("");  
                        article2.setPicUrl(weather_data.get(1).get("dayPictureUrl"));  
                        article2.setUrl("http://www.weather.com.cn/weather/101070301.shtml");
                        articleList.add(article2);                         
                        
                        // 设置图文消息个数 
                        newsMessage.setArticleCount(articleList.size()); 
                        // 设置图文消息包含的图文集合 
                        newsMessage.setArticles(articleList); 
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);
                    } else if (eventKey.equals("22")) {  
                        respContent = "票务查询被点击";  
                        Article article = new Article(); 
                        article.setTitle("票务查询"); 
                        article.setDescription(""); 
                        article.setPicUrl("http://www.zyue.com/UpLoad/UploadNews/1%288407%29.jpg"); 
                        article.setUrl("http://www.qianshan.ln.cn/jxgk/index-9-5.html"); 
                        articleList.add(article); 
                        // 设置图文消息个数 
                        newsMessage.setArticleCount(articleList.size()); 
                        // 设置图文消息包含的图文集合 
                        newsMessage.setArticles(articleList); 
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);
                    } else if (eventKey.equals("23")) {  
                        respContent = "推荐路线被点击";  
                        Article article = new Article(); 
                        article.setTitle("推荐路线"); 
                        article.setDescription(""); 
                        article.setPicUrl("http://www.hyits.cn/files/2014-4/f20140425073014125607.png"); 
                        article.setUrl("http://www.qianshan.ln.cn/wzqs/index-23-223.html"); 
                        articleList.add(article); 
                        // 设置图文消息个数 
                        newsMessage.setArticleCount(articleList.size()); 
                        // 设置图文消息包含的图文集合 
                        newsMessage.setArticles(articleList); 
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);
                    } else if (eventKey.equals("24")) {  
                        respContent = "周边美食被点击";  
                        Article article1 = new Article(); 
                        article1.setTitle("地方小吃"); 
                        article1.setPicUrl("http://www.qianshan.ln.cn/upload/contents/2013/09/5244f2a3469f9.gif"); 
                        article1.setUrl("http://www.qianshan.ln.cn/lxqs/details-31-395.html"); 
                        articleList.add(article1); 
                        
                        Article article2 = new Article(); 
                        article2.setTitle("餐饮名店"); 
                        article2.setPicUrl("http://www.qianshan.ln.cn/upload/contents/2013/09/5244f3817b4ea.jpg"); 
                        article2.setUrl("http://www.qianshan.ln.cn/lxqs/details-31-396.html"); 
                        articleList.add(article2); 
                        
                        Article article3 = new Article(); 
                        article3.setTitle("千山美食"); 
                        article3.setPicUrl("http://www.qianshan.ln.cn/upload/contents/2013/09/5244f27fc74aa.gif"); 
                        article3.setUrl("http://www.qianshan.ln.cn/lxqs/details-31-89.html"); 
                        articleList.add(article3); 
                        
                        Article article4 = new Article(); 
                        article4.setTitle("东北农家院"); 
                        article4.setPicUrl("http://www.qianshan.ln.cn/upload/contents/2013/09/5247825f2aedb.jpg"); 
                        article4.setUrl("http://www.qianshan.ln.cn/lxqs/details-31-98.html"); 
                        articleList.add(article4); 
                        // 设置图文消息个数 
                        newsMessage.setArticleCount(articleList.size()); 
                        // 设置图文消息包含的图文集合 
                        newsMessage.setArticles(articleList); 
                        respMessage = MessageUtil.newsMessageToXml(newsMessage);
                    } else if (eventKey.equals("31")) {  
                    	Date date=new Date();
                    	DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    	String time=format.format(date);
                        respContent = "(截止" + time + ")" + "\n" + "景区泊位共计150个，空闲100个";  
                        textMessage.setContent(respContent);  
                        respMessage = MessageUtil.textMessageToXML(textMessage);  
                    } else if (eventKey.equals("32")) {  
                        respContent = "请输入牌照，形如“辽A00000”";  
                        textMessage.setContent(respContent);  
                        respMessage = MessageUtil.textMessageToXML(textMessage);  
                        mParkingSearch = true;
                    } 
                }  
            }  
            if(!"请输入正确牌照".equals(respContent) && !"请输入牌照，形如“辽A00000”".equals(respContent)){
            	mParkingSearch = false;
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return respMessage;  
    }  
    
 // 判断一个字符是否是中文
    public static boolean isChinese(char c) {
          return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
    }
    
}  
