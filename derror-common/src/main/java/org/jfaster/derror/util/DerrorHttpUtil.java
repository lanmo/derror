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

package org.jfaster.derror.util;

import okhttp3.*;
import org.jfaster.derror.enums.ConfigEnum;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.jfaster.derror.constant.DerrorConstant.SYSTEM;


/**
 * http工具类
 *
 * @author yangnan
 */
public class DerrorHttpUtil {

    private static InternalLogger LOGGER = InternalLoggerFactory.getLogger(DerrorHttpUtil.class);
    private static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";
    private OkHttpClient httpclient;

    private static class HttpUtilHolder {
        private static final DerrorHttpUtil INSTANCE = new DerrorHttpUtil();
    }

    public static DerrorHttpUtil getInstance() {
        return HttpUtilHolder.INSTANCE;
    }

    private DerrorHttpUtil() {
        ConnectionPool connectionPool = new ConnectionPool(30, 300, SECONDS);
        httpclient = new OkHttpClient.Builder().connectionPool(connectionPool)
                .retryOnConnectionFailure(true)
                .connectTimeout(ConfigEnum.CONNECTION_TIMEOUT.getIntValue(), MILLISECONDS)
                .writeTimeout(ConfigEnum.WRITE_TIMEOUT.getIntValue(), MILLISECONDS)
                .readTimeout(ConfigEnum.SOCKET_TIMEOUT.getIntValue(), MILLISECONDS)
                .build();
    }

    /**
     * 发送post数据体
     *
     * @param json 发送的实体数据
     * @param url  远程服务地址
     */
    public String doPostJson(String url, String json) {
        Headers.Builder headerBuilder = buildHeaders();
        RequestBody requestBody = FormBody.create(MediaType.parse(MEDIA_TYPE_JSON), json);
        Request request = new Request.Builder()
                .headers(headerBuilder.build())
                .url(url)
                .post(requestBody)
                .build();
        //得到Call对象
        Call call = httpclient.newCall(request);
        return parseResponse(call, url);
    }

    /**
     */
    public String doGet(String url) {
        Headers.Builder headerBuilder = buildHeaders();
        Request request = new Request.Builder().headers(headerBuilder.build()).url(url).build();
        Call call = httpclient.newCall(request);
        return parseResponse(call, url);
    }

    /**
     *
     */
    private Headers.Builder buildHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add("X-REQUEST-ID", UUID.randomUUID().toString().replace("-", ""));
        headerBuilder.add("bId", SYSTEM);
        return headerBuilder;
    }

    /**
     * 解析响应结果
     *
     * @param call
     * @return
     */
    private String parseResponse(Call call, String url) {
        Response execute = null;
        try {
            execute = call.execute();
            if (execute != null && execute.isSuccessful()) {
                return execute.body().string();
            }
        } catch (Throwable e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("url:[{}]", url, e);
            }
            if (execute != null && execute.body() != null) {
                execute.body().close();
            }
        }
        return null;
    }
}
