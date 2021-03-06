/*
 * Copyright 2018 org.jfaster.derror.
 *   <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package org.jfaster.derror.manager.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * http工具类
 *
 * @author yangnan
 */
public class DerrorHttpUtil {

    private final CloseableHttpClient httpclient;
    private static final int SIZE = 1024 * 1024;
    private static final Logger logger = LoggerFactory.getLogger(DerrorHttpUtil.class);

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private static final int CONNECTION_REQUEST_TIMEOUT = 3000;
    private static final int CONNECT_TIMEOUT = 2000;
    private static final int SOCKET_TIMEOUT = 2000;

    private static class HttpUtilHolder {
        private static final DerrorHttpUtil INSTANCE = new DerrorHttpUtil();
    }

    public static DerrorHttpUtil getIntance() {
        return HttpUtilHolder.INSTANCE;
    }

    private DerrorHttpUtil() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // 将最大连接数增加到300
        cm.setMaxTotal(300);
        // 将每个路由基础的连接增加到100
        cm.setDefaultMaxPerRoute(100);
        // 链接超时setConnectTimeout ，读取超时setSocketTimeout
        RequestConfig defaultRequestConfig;
        /**
         *  请求参数配置
         *  connectionRequestTimeout:
         *                          从连接池中获取连接的超时时间，超过该时间未拿到可用连接，
         *                          会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
         *  connectTimeout:
         *                  连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
         *  socketTimeout:
         *                  服务器返回数据(response)的时间，超过该时间抛出read timeout
         */
        defaultRequestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();

        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

        httpclient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setDefaultSocketConfig(socketConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler())
                .build();

        new IdleConnectionMonitorThread(cm).start();
    }


    private static final String APPLICATION_JSON = "application/json; charset=utf-8";

    protected String postJSON(String url, String json) {
        String res = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

            StringEntity se = new StringEntity(json, CHARSET_UTF8);
            se.setContentType(APPLICATION_JSON);
            se.setContentEncoding(CHARSET_UTF8.displayName());
            httpPost.setEntity(se);
            response = httpclient.execute(httpPost);
            HttpEntity entity2 = response.getEntity();
            res = EntityUtils.toString(entity2, CHARSET_UTF8);
            EntityUtils.consume(entity2);
        } catch (Exception e) {
            logger.warn(url, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return res;
    }




    /** 监控有异常的链接
     *
     */
    private static class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
            shutdown = false;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        // 关闭失效的连接
                        connMgr.closeExpiredConnections();
                        // 可选的, 关闭30秒内不活动的连接
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                logger.warn("ex.getMessage()={}", ex.getMessage(), ex);
            }
        }
    }
    public String post(String url, Map<String, ?> params) {
        return this.post(url, params, null, CHARSET_UTF8.toString());
    }
    /**
     * @param url
     * @param params
     * @param code
     * @return
     */
    public String post(String url, Map<String, ?> params, Map<String, String> headers, String code) {
        List<NameValuePair> nvps = null;
        if (params != null && params.size() > 0) {
            nvps = new ArrayList<>();
            for (Entry<String, ?> entry : params.entrySet()) {
                String value = entry.getValue() == null ? "" : entry.getValue().toString();
                nvps.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }
        return this.post(url, nvps, headers, code);
    }

    private String post(String url, List<NameValuePair> params, Map<String, String> headers, String code) {
        String res = null;
        CloseableHttpResponse response = null;
        HttpEntity entity2 = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    if (entry != null) {
                        httpPost.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params, code));
            }
            response = httpclient.execute(httpPost);
            entity2 = response.getEntity();
            res = EntityUtils.toString(entity2, code);
        } catch (Exception e) {
            logger.error(url, e);
        } finally {
            try {
                EntityUtils.consume(entity2);
            } catch (IOException e) {
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return res;
    }

}
