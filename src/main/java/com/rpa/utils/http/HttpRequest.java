package com.rpa.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * HTTP网络请求
 * 
 * @author 王存见
 *
 */
public class HttpRequest {

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 * @throws IOException
	 */
	public static String sendGet(String url, String param) throws IOException {
		String result = "";
		BufferedReader in = null;

		String urlNameString = url + "?" + param;
		URL realUrl = new URL(urlNameString);
		// 打开和URL之间的连接
		URLConnection connection = realUrl.openConnection();
		// 设置通用的请求属性
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 建立实际的连接
		connection.connect();
		// 获取所有响应头字段
		Map<String, List<String>> map = connection.getHeaderFields();
		// 遍历所有的响应头字段
		for (String key : map.keySet()) {
			System.out.println(key + "--->" + map.get(key));
		}
		// 定义 BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		// 输入流
		if (in != null) {
			in.close();
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            Map方式请求
	 * @return 所代表远程资源的响应结果
	 * @throws IOException
	 */
	public static String sendGet(String url, Map<String, String> param) throws IOException {
		String paramStr = "";
		if (null != param) {
			for (String key : param.keySet()) {
				if (!StringUtils.isEmpty(paramStr)) {
					paramStr += "&";
				}
				paramStr += key + "=" + param.get(key);
			}
		}
		return sendGet(url, paramStr);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws IOException
	 */
	public static String sendPost(String url, String param) throws IOException {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		out.print(param);
		// flush输出流的缓冲
		out.flush();
		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		if (out != null) {
			out.close();
		}
		if (in != null) {
			in.close();
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            Map方式请求
	 * @return 所代表远程资源的响应结果
	 * @throws IOException
	 */
	public static String sendPost(String url, Map<String, String> param) throws IOException {
		String paramStr = "";
		if (null != param) {
			for (String key : param.keySet()) {
				if (!StringUtils.isEmpty(paramStr)) {
					paramStr += "&";
				}
				paramStr += key + "=" + param.get(key);
			}
		}
		return sendPost(url, paramStr);
	}
	public static Map<String,Object> send(String url,List<NameValuePair> nameValuePairs){
		@SuppressWarnings({ "deprecation", "resource" })
		HttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(url);  
        String strResult = ""; 
        JSONArray jsonArray = null;
        JSONObject sobj = null;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success",false);
        map.put("entity", "调用接口出错");
        try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			HttpResponse response;
			response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {  
				 /*读返回数据*/  
               String conResult = EntityUtils.toString(response  
                       .getEntity()); 
               if(conResult.startsWith("[")){
            	   jsonArray = JSONArray.fromObject(conResult);
            	   map.put("success", true);
            	   map.put("entity", jsonArray);
               }else if(conResult.startsWith("{")){
                   sobj = JSONObject.fromObject(conResult);
                   jsonArray = new JSONArray();
                   jsonArray.add(sobj);
                   map.put("success", true);
                   map.put("entity", jsonArray);
               }else{
            	   map.put("success", true);
            	   map.put("entity", conResult);
               }
//               String result = sobj.getString("token");  
//               String code = sobj.getString("expire");  
//               if(code.equals("3600")){  
//                   strResult += "发送成功"+sobj;  
//               }else{  
//                   strResult += "发送失败，"+code;  
//               } 
			}else{
				String err = response.getStatusLine().getStatusCode()+"";  
				strResult += "调用接口失败:"+err; 
				JSONArray array = new JSONArray();
				JSONObject object = new JSONObject();
				object.put("errInfo",strResult);
				array.add(object);
               map.put("success", false);
               map.put("entity", strResult);
			}
		}  catch (NoRouteToHostException e){
			//err 捕获超时异常
			map.put("success", false);
			map.put("entity", "调用接口超时");
		} catch (UnsupportedEncodingException e) {
			//捕获编码格式异常
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// 客户端异常
			map.put("success", false);
			map.put("entity", "客户端异常");
		} catch (IOException e) {
			//流异常
			e.printStackTrace();
		}  
        return map;  
	}
}