package com.java.mobile.common.utils.httpclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpMessage;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("httpClientUtil")
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    @Autowired(required = false)
    private CloseableHttpClient httpClient;
    @Autowired(required = false)
    private RequestConfig requestConfig;

    /**
     * get请求
     * @param url 请求url
     * @param params 请求参数，可以为null
     * @return 返回请求结果
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult doGet(String url, Map<String, Object> params, Map<String, String> headers) throws URISyntaxException, IOException{
        CloseableHttpResponse response = null;
        HttpResult res = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if(null != params && !params.isEmpty()) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue().toString());
                }
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setConfig(this.requestConfig);
            this.setRequestheaders(headers,httpGet);
            response = httpClient.execute(httpGet);
            if (response.getEntity() != null) {
                res =  new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), "UTF-8"));
            } else {
                res = new HttpResult(response.getStatusLine().getStatusCode(), null);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            return res;
        }
    }

    /**
     * post请求(表单提交)
     * @param url 请求url
     * @param params 请求参数，可以为null
     * @return 返回请求结果
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String, Object> params, Map<String, String> headers) throws URISyntaxException, IOException{
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        this.setRequestheaders(headers, httpPost);
        httpPost.setConfig(this.requestConfig);
        if (params != null && !params.isEmpty()) {
            // 设置post参数
            List<NameValuePair> parameters = new ArrayList<>(0);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造一个form表单式的实体,并且指定参数的编码为UTF-8
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            if (response.getEntity() != null) {
                return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                        response.getEntity(), "UTF-8"));
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), null);
        } finally {
            if (response != null) {
                response.close();
            }
            return null;
        }
    }

    /**
     * post请求(表单提交)
     * @param url 请求url
     * @param params 请求参数，可以为null
     * @return 返回请求结果
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult doPost(String url, String params, Map<String, String> headers) throws URISyntaxException, IOException{
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        this.setRequestheaders(headers, httpPost);
        httpPost.setConfig(this.requestConfig);
        if (StringUtils.isNotBlank(params)) {
            // 构造一个body实体
            StringEntity entity = new StringEntity(params, Charset.forName("UTF-8"));
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(entity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            if (response.getEntity() != null) {
                return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                        response.getEntity(), "UTF-8"));
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), null);
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * post请求(请求头application/json，参数在entity中)
     * @param url 请求url
     * @param json 请求数据(json格式)
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult doPostJson(String url, String json, Map<String, String> headers) throws URISyntaxException, IOException{
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        this.setRequestheaders(headers, httpPost);
        httpPost.setConfig(this.requestConfig);
        if (json != null) {
            // 构造一个字符串的实体
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            if (response.getEntity() != null) {
                return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                        response.getEntity(), "UTF-8"));
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * put请求(请求头application/json，参数在entity中)
     * @param url 请求url
     * @param json 请求数据(json格式)
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult doPutJson(String url, String json, Map<String, String> headers) throws URISyntaxException, IOException{
        // 创建http POST请求
        HttpPut httpPut = new HttpPut(url);
        this.setRequestheaders(headers, httpPut);
        httpPut.setConfig(this.requestConfig);
        if (json != null) {
            // 构造一个字符串的实体
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPut.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPut);
            if (response.getEntity() != null) {
                return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                        response.getEntity(), "UTF-8"));
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), null);
        } finally {
            if (response != null) {
                response.close();
            }
            return null;
        }
    }

    /**
     * put请求(表单提交)
     * @param url 请求url
     * @param params 请求参数，可以为null
     * @return 返回请求结果
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult doPut(String url, Map<String, Object> params, Map<String, String> headers) throws URISyntaxException, IOException{
        // 创建http POST请求
        HttpPut httpPut = new HttpPut(url);
        this.setRequestheaders(headers, httpPut);
        httpPut.setConfig(this.requestConfig);
        if (params != null) {
            // 设置post参数
            List<NameValuePair> parameters = new ArrayList<>(0);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造一个form表单式的实体,并且指定参数的编码为UTF-8
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            // 将请求实体设置到httpPost对象中
            httpPut.setEntity(formEntity);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPut);
            if (response.getEntity() != null) {
                return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                        response.getEntity(), "UTF-8"));
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), null);
        } finally {
            if (response != null) {
                response.close();
            }
            return null;
        }
    }

    /**
     * 使用post发送delete请求(表单提交)
     * @param url 请求url
     * @param params 请求参数，可以为null
     * @return 返回请求结果
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult doDelete(String url, Map<String, Object> params, Map<String, String> headers) throws URISyntaxException,IOException {
        if (null == params) params = new HashMap<>();
        params.put("_method", "DELETE");
        return this.doPost(url, params, headers);
    }

    /**
     * delete请求
     * @param url 请求url
     * @return 返回请求结果
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult doDelete(String url, Map<String, String> headers) throws URISyntaxException, IOException {
        // 创建http DELETE请求
        HttpDelete httpDelete = new HttpDelete(url);
        this.setRequestheaders(headers, httpDelete);
        httpDelete.setConfig(this.requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
             response = httpClient.execute(httpDelete);
            if (response.getEntity() != null) {
                return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                        response.getEntity(), "UTF-8"));
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), null);
        } finally {
            if (response != null) {
                response.close();
            }
            return null;
        }
    }

    //设置http头
    private void setRequestheaders(Map<String, String> headers, HttpMessage httpMessage){
        if (null == httpMessage) return;
        if (null != headers && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpMessage.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }


    //将httpClient(http)转成sslClient(https)
    private static void sslClient(org.apache.http.client.HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] xcs, String str) {
                }
                public void checkServerTrusted(X509Certificate[] xcs, String str) {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }


    /*public HttpResult aa(String url, Map<String, Object> params, Map<String, String> headers) throws URISyntaxException, IOException{
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        this.setRequestheaders(headers, httpPost);
        httpPost.setConfig(this.requestConfig);
        if (params != null && !params.isEmpty()) {
            FileBody fileBody = new FileBody(new File("/"));
            StringBody stringBody = new StringBody("", ContentType.MULTIPART_FORM_DATA);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addPart("", fileBody);
            entityBuilder.addPart("", stringBody);
            HttpEntity build = entityBuilder.build();
            httpPost.setEntity(build);
        }

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            if (response.getEntity() != null) {
                return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                        response.getEntity(), "UTF-8"));
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), null);
        } finally {
            if (response != null) {
                response.close();
            }
            return null;
        }
    }*/




}
