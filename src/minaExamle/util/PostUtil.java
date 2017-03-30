package minaExamle.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class PostUtil {
	
	public static JSONArray doPost(String url,JSONArray json){
	    DefaultHttpClient client = new DefaultHttpClient();
	    System.out.println(json);
	    HttpPost post = new HttpPost(url);
	      String result=null;
	    JSONArray response = null;
	    try {
	    	List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
	    	nvps.add(new BasicNameValuePair("json",URLEncoder.encode(json.toString(), "UTF-8")));
/*			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
*/
	     StringEntity s = new StringEntity(json.toString());
/*		     post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
*/
/*		  s.setContentEncoding("UTF-8");
*/		  // s.setContentEncoding("gbk");
	      //application/soap+xml
	      //application/json
	      s.setContentType("application/json");//发送json数据需要设置contentType
	      
	    
	      post.setEntity(new UrlEncodedFormEntity(nvps));
	      
/*		      post.setEntity(s);
*/		      HttpResponse res = client.execute(post);
			System.out.println(res.getStatusLine().getStatusCode() +":"+HttpStatus.SC_OK);
	      if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	    	
	        HttpEntity entity = res.getEntity();
	        result = EntityUtils.toString(res.getEntity());// 返回json格式：
	        System.out.println("status:"+result);
	        /*//解析xml
	        Document  doc = DocumentHelper.parseText(result);
	        Element rootElt = doc.getRootElement();
	        result=rootElt.getText();*/
/*	        response = JSONArray.fromObject(result);
*/	      }
	    } catch (Exception e) {
	        System.out.println("远程http连接失败！");
/*	      throw new RuntimeException(e);
*/	  
	    }
	    return response;
	  }
}
