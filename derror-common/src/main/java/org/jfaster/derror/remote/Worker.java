/*
 *
 *   Copyright 2018 derror.jfaster.org.
 *     <p>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   </p>
 */

package org.jfaster.derror.remote;

import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.config.LoggingEvent;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.manager.CacheManager;
import org.jfaster.derror.manager.NameThreadFactory;
import org.jfaster.derror.remote.config.AppConfig;
import org.jfaster.derror.remote.config.ExceptionLog;
import org.jfaster.derror.remote.config.RemoteConfig;
import org.jfaster.derror.util.ClassUtil;
import org.jfaster.derror.util.ExceptionUtil;
import org.jfaster.derror.util.NetUtil;
import org.jfaster.derror.util.StringUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author yangnan
 */
public class Worker {

    private static InternalLogger LOGGER = InternalLoggerFactory.getLogger(Worker.class);

    private final ArrayBlockingQueue<LoggingEvent> blockingQueue;
    private final IDownloadRemoteService downloadRemoteService;
    private boolean stop = false;
    private static final NameThreadFactory THREAD_FACTORY = new NameThreadFactory("DERROR_SEND_ERROR_WORKER", true);
    public Worker(int queueNum, IDownloadRemoteService downloadRemoteService) {
        this.blockingQueue = new ArrayBlockingQueue(queueNum);
        this.downloadRemoteService = downloadRemoteService;
        THREAD_FACTORY.newThread(new Dispatcher()).start();
    }

    /**
     * 停止线程
     */
    public void stop() {
        this.stop = true;
    }

    /**
     * 放入数据
     *
     * @param event
     * @return
     */
    public boolean offer(LoggingEvent event) {
        if (event == null) {
            return false;
        }
        return blockingQueue.offer(event);
    }

    private class Dispatcher implements Runnable {
        @Override
        public void run() {
            while (!stop && !Thread.currentThread().isInterrupted()) {
                try {
                    boolean debugEnable = LOGGER.isDebugEnabled();
                    LoggingEvent event = blockingQueue.take();
                    if (event == null || StringUtil.isEmpty(event.getAppName())) {
                        continue;
                    }
                    ClientConfig clientConfig = CacheManager.getConfig(event.getAppName());
                    if (clientConfig == null) {
                        if (debugEnable) {
                            LOGGER.debug("clientConfig is null appName:[{}]", event.getAppName());
                        }
                        continue;
                    }
                    RemoteConfig remoteConfig = CacheManager.getConfigFromCache(event.getAppName());
                    if (remoteConfig == null) {
                        if (debugEnable) {
                            LOGGER.debug("remoteConfig is null appName:[{}]", event.getAppName());
                        }
                        continue;
                    }
                    if (ClassUtil.contains(remoteConfig.getFilterExceptions(), event.getThrowable().getClass())) {
                        if (debugEnable) {
                            LOGGER.debug("filterExceptions:[{}] throwable:[{}]",
                                    Arrays.toString(remoteConfig.getFilterExceptions().toArray()),
                                    event.getThrowable().getClass());
                        }
                        continue;
                    }
                    ExceptionLog exceptionLog = new ExceptionLog();
                    AppConfig appConfig = remoteConfig.getAppConfig();
                    exceptionLog.setAppId(appConfig.getId());
                    String shortName = ClassUtil.simpleClassName(event.getThrowable());
                    exceptionLog.setShortName(shortName.toUpperCase());
                    exceptionLog.setHost(getHost());
                    exceptionLog.setExceptionMsg(event.getThrowable().getMessage());
                    exceptionLog.setMdcValue(mapToString(event.getMDCPropertyMap()));
                    exceptionLog.setTraceId(event.getMDCPropertyMap().get(clientConfig.getTraceKey()));
                    exceptionLog.setClassName(ClassUtil.getClassName(event.getThrowable()));
                    exceptionLog.setContent(ExceptionUtil.getStackTrace(event.getThrowable()));
                    if(event.getExt() != null && !event.getExt().isEmpty()) {
                        exceptionLog.setExt(mapToString(event.getExt()));
                    }
                    downloadRemoteService.sendExceptionToServer(clientConfig, exceptionLog);
                } catch (Throwable e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("sendExceptionToServer error", ExceptionUtil.handleException(e));
                    }
                }
            }
        }
    }

    /**
     * 获取mdc value
     * @param mdcPropertyMap
     * @return
     */
    private String mapToString(Map<String,String> mdcPropertyMap) {
        if (mdcPropertyMap == null || mdcPropertyMap.isEmpty()) {
            return "";
        }
        StringBuilder value = new StringBuilder();
        for (Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
            String key = entry.getKey();
            String v = entry.getValue();
            if (StringUtil.isEmpty(key) || StringUtil.isEmpty(v)) {
                continue;
            }
            value.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return value.length() > 0 ? value.substring(0, value.length() - 1) : "";
    }

    /**
     * 获取网卡ip
     * @return
     */
    private String getHost() {
        return NetUtil.getLocalIp();
    }
}
