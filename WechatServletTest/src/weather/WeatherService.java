package weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class WeatherService {
	public static final String URL_QUERY_WEATHER="http://api.map.baidu.com/telematics/v3/weather?location=LOCATION&output=json&ak=81218080E79C9685b35e757566d8cbe5";
	/** 
     * 发送http请求 
     *  
     * @param requestUrl 请求地址 
     * @return String 
     */  
    private static String httpRequest(String requestUrl) {  
        StringBuffer buffer = new StringBuffer();  
        try {  
            URL url = new URL(requestUrl);  
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.connect();  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return buffer.toString();  
    }  
    
    public static Weather weatherDetect(String place){
    	Weather weather = new Weather();
    	//天气查询地址
    	String queryUrl = URL_QUERY_WEATHER;
    	
    	try {
			//对url进行编码
				queryUrl = queryUrl.replace("LOCATION", java.net.URLEncoder.encode(place, "UTF-8"));
				
				//调用天气查询接口
				String json = httpRequest(queryUrl);
				//System.out.println(json);
				//解析返回的json
				JSONObject jsonObj=JSONObject.fromObject(json);
				JSONArray results=jsonObj.getJSONArray("results");
				
				//System.out.println(results.toString());
				
				JSONObject resultsObject=(JSONObject) results.get(0);
				
				JSONArray index=resultsObject.getJSONArray("index");
				JSONArray weather_data=resultsObject.getJSONArray("weather_data");
				
				List<Map<String,String>> indexList=new ArrayList<Map<String,String>>();
				for(int i=0;i<index.size();i++){
					JSONObject info=index.getJSONObject(i);
					Map<String,String> map=new HashMap<String,String>();
					map.put("title", info.getString("title"));
					map.put("zs", info.getString("zs"));
					map.put("tipt", info.getString("tipt"));
					map.put("des", info.getString("des"));
					
					indexList.add(map);
				}
				
				weather.setIndex(indexList);
				
				//System.out.println(weather.getIndex());
				
				List<Map<String,String>> weather_dataList=new ArrayList<Map<String,String>>();
				for(int i=0;i<weather_data.size();i++){
					JSONObject info=weather_data.getJSONObject(i);
					Map<String,String> map=new HashMap<String,String>();
					map.put("date", info.getString("date"));
					map.put("dayPictureUrl", info.getString("dayPictureUrl"));
					map.put("nightPictureUrl", info.getString("nightPictureUrl"));
					map.put("weather", info.getString("weather"));
					map.put("wind", info.getString("wind"));
					map.put("temperature", info.getString("temperature"));
					
					weather_dataList.add(map);
				}
				weather.setWeather_data(weather_dataList);
				
				//System.out.println(weather.getWeather_data());
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return weather;	
    }
    
    public static void main(String[] args) {  
    	weatherDetect("鞍山").getIndex();
    	System.out.println(weatherDetect("鞍山").getIndex());
    }
    
}
