package com.shorturl.utils;


import java.io.*;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.net.ssl.SSLContext;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http 工具类
 */
public class HttpHander {
    private static final Logger logger = LoggerFactory.getLogger(HttpHander.class);

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 8000;   //超时时间8s
    private static final int MAX_READ_TIMEOUT = 10000; //读取超时 15s

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(200);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_READ_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }


    /**
     * https get方法
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String get(String url) throws Exception {
        if (url != null) {
            HttpClient httpClient;
            if (url.startsWith("https")) {
                httpClient = getHttpsClient();
            } else {
                httpClient = getHttpClient();
            }

            return get(httpClient, url);
        } else {
            return "url is null";
        }
    }

    /**
     * https get方法
     *
     * @param httpClient
     * @param url
     * @return
     * @throws IOException
     */
    private static String get(HttpClient httpClient, String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        httpGet.setConfig(requestConfig);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
        String string;
        StringBuilder builder = new StringBuilder();
        while ((string = bufferedReader.readLine()) != null) {
            builder.append(string);
        }

        httpGet.abort();
        return builder.toString();
    }


    private static HttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    public static HttpClient getHttpsClient() throws Exception {
        SSLContext context = (new SSLContextBuilder()).loadTrustMaterial((KeyStore) null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        }).build();
        return HttpClients.custom().setSslcontext(context).build();
    }


    public static String postFromData(String serverUrl, Map<String, String> textParts) {
        if (serverUrl == null) {
            return null;
        }
        String httpStr = null;
        HttpPost post = null;
        //1、创建HttpClient
        HttpClient httpClient;
        try {
            if (serverUrl.startsWith("https")) {
                httpClient = getHttpsClient();
            } else {
                httpClient = getHttpClient();
            }
            post = new HttpPost(serverUrl);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            //2、设置 multipart/form-data text表单
            if (textParts != null) {
                Iterator<Map.Entry<String, String>> iter = textParts.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String paramName = entry.getKey();
                    String paramValue = entry.getValue();
                    builder.addTextBody(paramName, paramValue, ContentType.DEFAULT_TEXT);
                }
            }
            HttpEntity reqEntity = builder.build();
            // 4、设置POST请求实体
            post.setEntity(reqEntity);
            // 6、得到response
            HttpResponse response = httpClient.execute(post);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            logger.error("异常信息", e);
        } finally {
            post.abort();
        }
        return httpStr;
    }


}
