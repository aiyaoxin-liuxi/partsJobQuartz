package com.dhb.util.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.dhb.util.JsonUtil;


//import com.zhl.common.util.JsonUtil;


public class HttpClientHelper{
	
	public static void main(String[] args) throws Exception {
//		test1();
//		test2();
//		System.out.println(("123456"));
	}
    private static void test2() throws Exception {
    	Map<String,String> map=new HashMap<String,String>();
    	map.put("ordercore", "1480477445009");
    	
    	
		/*map.put("userId", "1480477445009");
		map.put("pwd", MD5.MD5Encode("123456"));
		map.put("funCode", "0002");
		map.put("version", "1.0.0");
		map.put("subCode", "08");
		map.put("bankName", "昌平回龙观中区支行");*/
		
		
    	StringBuilder a = JsonUtil.toJson(map);
//		String a = JSONObject.fromObject(map).toString();
		//String o = HttpClientHelper.doHttpClient("http://218.240.148.183:8580/commonService/queryBankInfos", "POST", "utf-8", a.toString(), "60000","application/json","");
    	
    	String o = HttpClientHelper.doHttpClient("http://localhost:8080/order-site/service/orderDetailQuery", "POST", "utf-8", a.toString(), "60000","application/json","");
        JsonNode jsonNode = JsonUtil.toJsonNode(o);
        String returnCode = jsonNode.path("returnCode").getTextValue();
    	System.out.println(o+" \t\r\n: "+returnCode);
    	
    	JsonNode ordercore = jsonNode.path("ordercore");
//    	JsonUtil.toBean(json, clazz)
    	
    	
		
	}
	private static Logger logger = Logger.getLogger(HttpClientHelper.class);
    public static final String GET = "GET";
    public static final String POST = "POST";
    
    public static String getNvPairs(List<String[]> list, String charSet){
        if(list==null || list.size()==0){
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0; i<list.size(); i++){
            String[] nvPairStr = list.get(i);
            try{
                if(i>0){
                    stringBuffer.append("&");
                }
                stringBuffer.append(URLEncoder.encode(nvPairStr[0], charSet)).append("=").append(URLEncoder.encode(nvPairStr[1], charSet));
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
                return null;
            }
        }
        return stringBuffer.toString();
    }
    
    public static String doHttp(String urlStr, String method, String charSet, String postStr, String timeOut){
        if(method==null || (!GET.equalsIgnoreCase(method) && !POST.equalsIgnoreCase(method))){
            logger.error("无效http方法[" + method + "]");
            return null;
        }
        URL url = null;
        try{
            url = new URL(urlStr);
        }catch(MalformedURLException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        if("https".equalsIgnoreCase(urlStr.substring(0, 5))){
            SSLContext sslContext = null;
            try
            {
                sslContext = SSLContext.getInstance("TLS");
                X509TrustManager xtmArray[] = {
                    new HttpX509TrustManager()
                };
                sslContext.init(null, xtmArray, new SecureRandom());
            }
            catch(GeneralSecurityException gse)
            {
                gse.printStackTrace();
            }
            if(sslContext != null)
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HttpHostnameVerifier());
        }
        HttpURLConnection httpURLConnection = null;
        try{
            httpURLConnection = (HttpURLConnection)url.openConnection ();
        }catch(IOException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        httpURLConnection.setConnectTimeout(Integer.parseInt(timeOut));
        httpURLConnection.setReadTimeout(Integer.parseInt(timeOut));
        try{
            httpURLConnection.setRequestMethod(method.toUpperCase());
        }catch(ProtocolException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        if(POST.equalsIgnoreCase(method)){
            httpURLConnection.setDoOutput(true);  
            PrintWriter printWriter = null;
            try{
                printWriter = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), charSet));
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
                logger.error(e.toString());
                return null;
            }catch(IOException e){
                e.printStackTrace();
                logger.error(e.toString());
                return null;
            }  
            printWriter.write(postStr);
            printWriter.flush();
        }
        InputStream inputStream = null;  
        try{
            inputStream = httpURLConnection.getInputStream();
        }catch(IOException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int data = 0;
        try{
            int statusCode = httpURLConnection.getResponseCode();
            if(statusCode<HttpURLConnection.HTTP_OK || statusCode>=HttpURLConnection.HTTP_MULT_CHOICE){
                logger.error("失败返回码[" + statusCode + "]");
                return null;
            }
            while((data=inputStream.read())!=-1){
                byteArrayOutputStream.write(data);
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        byte[] returnBytes = byteArrayOutputStream.toByteArray();
        String returnStr = null;
        try{
            returnStr = new String(returnBytes, charSet);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        return returnStr;
    }
    
    public static String doHttp(String urlStr, String method, List<String[]> headers, String reqCharSet, String postStr, String timeOut, String rspCharSet){
        if(method==null || (!GET.equalsIgnoreCase(method) && !POST.equalsIgnoreCase(method))){
            logger.error("无效http方法[" + method + "]");
            return null;
        }
        URL url = null;
        try{
            url = new URL(urlStr);
        }catch(MalformedURLException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        if("https".equalsIgnoreCase(urlStr.substring(0, 5))){
            SSLContext sslContext = null;
            try
            {
                sslContext = SSLContext.getInstance("TLS");
                X509TrustManager xtmArray[] = {
                    new HttpX509TrustManager()
                };
                sslContext.init(null, xtmArray, new SecureRandom());
            }
            catch(GeneralSecurityException gse)
            {
                gse.printStackTrace();
            }
            if(sslContext != null)
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HttpHostnameVerifier());
        }
        HttpURLConnection httpURLConnection = null;
        try{
            httpURLConnection = (HttpURLConnection)url.openConnection ();
        }catch(IOException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        System.setProperty("sun.net.client.defaultConnectTimeout", timeOut);
        System.setProperty("sun.net.client.defaultReadTimeout", timeOut);
        try{
            if(headers!=null && headers.size()>0){
                for(int i=0; i<headers.size(); i++){
                    String[] nvPairStr = headers.get(i);
                    if(nvPairStr.length==2){
                        httpURLConnection.setRequestProperty(nvPairStr[0], nvPairStr[1]);
                    }
                }
            }
            httpURLConnection.setRequestMethod(method.toUpperCase());
        }catch(ProtocolException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        if(POST.equalsIgnoreCase(method)){
            
            httpURLConnection.setDoOutput(true);  
            PrintWriter printWriter = null;
            try{
                printWriter = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), reqCharSet));
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
                logger.error(e.toString());
                return null;
            }catch(IOException e){
                e.printStackTrace();
                logger.error(e.toString());
                return null;
            }  
            printWriter.write(postStr);
            printWriter.flush();
        }
        InputStream inputStream = null;  
        try{
            inputStream = httpURLConnection.getInputStream();
        }catch(IOException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int data = 0;
        try{
            int statusCode = httpURLConnection.getResponseCode();
            if(statusCode<HttpURLConnection.HTTP_OK || statusCode>=HttpURLConnection.HTTP_MULT_CHOICE){
                logger.error("失败返回码[" + statusCode + "]");
                return null;
            }
            while((data=inputStream.read())!=-1){
                byteArrayOutputStream.write(data);
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        byte[] returnBytes = byteArrayOutputStream.toByteArray();
        String returnStr = null;
        try{
            returnStr = new String(returnBytes, rspCharSet);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        return returnStr;
    }
    
    public static HttpRsp doHttp(String urlStr, String method, List<String[]> headers, String charSet, String postStr, String timeOut){
        if(method==null || (!GET.equalsIgnoreCase(method) && !POST.equalsIgnoreCase(method))){
            logger.error("无效http方法[" + method + "]");
            return null;
        }
        URL url = null;
        try{
            url = new URL(urlStr);
        }catch(MalformedURLException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        if("https".equalsIgnoreCase(urlStr.substring(0, 5))){
            SSLContext sslContext = null;
            try
            {
                sslContext = SSLContext.getInstance("TLS");
                X509TrustManager xtmArray[] = {
                    new HttpX509TrustManager()
                };
                sslContext.init(null, xtmArray, new SecureRandom());
            }
            catch(GeneralSecurityException gse)
            {
                gse.printStackTrace();
            }
            if(sslContext != null)
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HttpHostnameVerifier());
        }
        HttpURLConnection httpURLConnection = null;
        try{
            httpURLConnection = (HttpURLConnection)url.openConnection ();
        }catch(IOException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        System.setProperty("sun.net.client.defaultConnectTimeout", timeOut);
        System.setProperty("sun.net.client.defaultReadTimeout", timeOut);
        try{
            if(headers!=null && headers.size()>0){
                for(int i=0; i<headers.size(); i++){
                    String[] nvPairStr = headers.get(i);
                    if(nvPairStr.length==2){
                        httpURLConnection.setRequestProperty(nvPairStr[0], nvPairStr[1]);
                    }
                }
            }
            httpURLConnection.setRequestMethod(method.toUpperCase());
        }catch(ProtocolException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        if(POST.equalsIgnoreCase(method)){
            httpURLConnection.setDoOutput(true);  
            PrintWriter printWriter = null;
            try{
                printWriter = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), charSet));
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
                logger.error(e.toString());
                return null;
            }catch(IOException e){
                e.printStackTrace();
                logger.error(e.toString());
                return null;
            }  
            printWriter.write(postStr);
            printWriter.flush();
        }
        InputStream inputStream = null;  
        try{
            inputStream = httpURLConnection.getInputStream();
        }catch(IOException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int data = 0;
        int statusCode = HttpURLConnection.HTTP_OK;
        try{
            statusCode = httpURLConnection.getResponseCode();
            if(statusCode<HttpURLConnection.HTTP_OK || statusCode>=HttpURLConnection.HTTP_MULT_CHOICE){
                logger.error("失败返回码[" + statusCode + "]");
                HttpRsp httpRsp = new HttpRsp();
                httpRsp.setStatusCode(statusCode);
                return httpRsp;
            }
            while((data=inputStream.read())!=-1){
                byteArrayOutputStream.write(data);
            }
        }catch(IOException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        byte[] returnBytes = byteArrayOutputStream.toByteArray();
        String returnStr = null;
        try{
            returnStr = new String(returnBytes, charSet);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        HttpRsp httpRsp = new HttpRsp();
        httpRsp.setStatusCode(statusCode);
        httpRsp.setRspStr(returnStr);
        return httpRsp;
    }
    
    /**
     * add by limeng 20151203 增加了一个Content-type的参数传递
     * @param urlStr
     * @param method
     * @param charSet
     * @param postStr
     * @param timeOut
     * @return
     */
    public static String doHttpClient(String urlStr, String method, String charSet, String postStr, String timeOut,String contentType,String token){
        if(method==null || (!GET.equalsIgnoreCase(method) && !POST.equalsIgnoreCase(method))){
            logger.error("无效http方法[" + method + "]");
            return null;
        }
        URL url = null;
        try{
            url = new URL(urlStr);
        }catch(MalformedURLException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        if("https".equalsIgnoreCase(urlStr.substring(0, 5))){
            SSLContext sslContext = null;
            try
            {
                sslContext = SSLContext.getInstance("TLS");
                X509TrustManager xtmArray[] = {
                    new HttpX509TrustManager()
                };
                sslContext.init(null, xtmArray, new SecureRandom());
            }
            catch(GeneralSecurityException gse)
            {
                gse.printStackTrace();
            }
            if(sslContext != null)
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HttpHostnameVerifier());
        }
        HttpURLConnection httpURLConnection = null;
        try{
            httpURLConnection = (HttpURLConnection)url.openConnection ();
        }catch(IOException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        httpURLConnection.setConnectTimeout(Integer.parseInt(timeOut));
        httpURLConnection.setReadTimeout(Integer.parseInt(timeOut));
        try{
            httpURLConnection.setRequestMethod(method.toUpperCase());
            httpURLConnection.setRequestProperty("Content-type", contentType);//add by limeng 20151203
            httpURLConnection.addRequestProperty("accessToken",token);
//            httpURLConnection.setRequestProperty("charSet", charSet);
        }catch(ProtocolException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        if(POST.equalsIgnoreCase(method)){
            httpURLConnection.setDoOutput(true);  
            PrintWriter printWriter = null;
            try{
                printWriter = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), charSet));
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
                logger.error(e.toString());
                return null;
            }catch(IOException e){
                e.printStackTrace();
                logger.error(e.toString());
                return null;
            }  
            printWriter.write(postStr);
            printWriter.flush();
        }
        InputStream inputStream = null;  
        try{
            inputStream = httpURLConnection.getInputStream();
        }catch(IOException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int data = 0;
        try{
            int statusCode = httpURLConnection.getResponseCode();
            if(statusCode<HttpURLConnection.HTTP_OK || statusCode>=HttpURLConnection.HTTP_MULT_CHOICE){
                logger.error("失败返回码[" + statusCode + "]");
                return null;
            }
            while((data=inputStream.read())!=-1){
                byteArrayOutputStream.write(data);
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        byte[] returnBytes = byteArrayOutputStream.toByteArray();
        String returnStr = null;
        try{
            returnStr = new String(returnBytes, charSet);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
        return returnStr;
    }

}
