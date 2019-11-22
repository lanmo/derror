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

package org.jfaster.derror.remote;

import com.alibaba.fastjson.JSON;
import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.config.LoggingEvent;
import org.jfaster.derror.constant.DerrorConstant;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.remote.config.AppConfig;
import org.jfaster.derror.remote.config.ExceptionLog;
import org.jfaster.derror.remote.config.RemoteConfig;
import org.jfaster.derror.util.ClassUtil;
import org.jfaster.derror.util.ExceptionUtil;
import org.jfaster.derror.util.NetUtil;
import org.jfaster.derror.util.StringUtil;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 远程调用线程
 * @author yangnan
 */
public class RemoteWorker implements Runnable {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(RemoteWorker.class);

    private final IDownloadRemoteService downloadRemoteService;
    private final LoggingEvent event;
    private final ClientConfig clientConfig;
    private final RemoteConfig remoteConfig;

    public RemoteWorker(IDownloadRemoteService downloadRemoteService, LoggingEvent event, ClientConfig clientConfig,
            RemoteConfig remoteConfig) {
        this.downloadRemoteService = downloadRemoteService;
        this.event = event;
        this.clientConfig = clientConfig;
        this.remoteConfig = remoteConfig;
    }

    @Override
    public void run() {
        try {
            if (ClassUtil.contains(remoteConfig.getFilterExceptions(), event.getThrowable().getClass())) {
                return;
            }
            ExceptionLog exceptionLog = new ExceptionLog();
            AppConfig appConfig = remoteConfig.getAppConfig();
            exceptionLog.setAppId(appConfig.getId());
            String shortName = ClassUtil.simpleClassName(event.getThrowable());
            exceptionLog.setShortName(shortName.toUpperCase());
            exceptionLog.setHost(getHost());
            exceptionLog.setExceptionMsg(event.getThrowable().getMessage());
            exceptionLog.setMdcValue(getMdcValue(event.getMDCPropertyMap()));
            exceptionLog.setTraceId(event.getMDCPropertyMap().get(clientConfig.getTraceKey()));
            exceptionLog.setClassName(ClassUtil.getClassName(event.getThrowable()));
            exceptionLog.setContent(ExceptionUtil.getStackTrace(event.getThrowable()));
            exceptionLog.setEnv(event.getEnv());
            if(event.getExt() != null && !event.getExt().isEmpty()) {
                exceptionLog.setExt(JSON.toJSONString(event.getExt()));
            }
            downloadRemoteService.sendExceptionToServer(clientConfig, exceptionLog);
        } catch (Exception e) {
            LOGGER.error("sendExceptionToServer error", ExceptionUtil.handleException(e));
        }
    }

    /**
     * 获取mdc value
     * @param mdcPropertyMap
     * @return
     */
    private String getMdcValue(Map<String,String> mdcPropertyMap) {
        if (mdcPropertyMap == null || mdcPropertyMap.isEmpty()) {
            return "";
        }
        StringBuilder value = new StringBuilder();
        for (Entry<String, String> entry : mdcPropertyMap.entrySet()) {
            String key = entry.getKey();
            String v = entry.getValue();
            if (StringUtil.isEmpty(key) || StringUtil.isEmpty(v)) {
                continue;
            }
            value.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        return value.length() > 0 ? value.deleteCharAt(value.length() - 1).toString() : "";
    }

    /**
     * 获取网卡ip
     * @return
     */
    private String getHost() {
        Set<String> ips = NetUtil.resolveLocalIps();
        for (String ip : ips) {
            return ip;
        }
        return DerrorConstant.DEFAULT_HOST;
    }
}
