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

package org.jfaster.derror.monitor.support;

import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.config.LoggingEvent;
import org.jfaster.derror.exception.DerrorFrameException;
import org.jfaster.derror.exception.DerrorTimeoutException;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.manager.CacheManager;
import org.jfaster.derror.manager.NameThreadFactory;
import org.jfaster.derror.monitor.Supervisor;
import org.jfaster.derror.remote.IDownloadRemoteService;
import org.jfaster.derror.remote.Worker;
import org.jfaster.derror.remote.config.RemoteConfig;
import org.jfaster.derror.remote.support.DownloadRemoteServiceImpl;
import org.jfaster.derror.util.StringUtil;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author yangnan
 */
public class DefaultSupervisor implements Supervisor {
    private static InternalLogger LOGGER = InternalLoggerFactory.getLogger(DefaultSupervisor.class);

    private Worker[] workers;
    private IDownloadRemoteService downloadRemoteService;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private String appName;

    public DefaultSupervisor(int threadNum, int queueSize, int remoteInterval, ClientConfig clientConfig) {
        CacheManager.init(clientConfig);
        downloadRemoteService = new DownloadRemoteServiceImpl();
        this.appName = clientConfig.getAppName();
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,  new NameThreadFactory("DERROR_SCHEDULED_CONFIG", true));
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> reloadConfig(CacheManager.getConfig(clientConfig.getAppName())), 1, remoteInterval, TimeUnit.SECONDS);
        workers = new Worker[threadNum];
        for (int i=0; i<threadNum; i++) {
            workers[i] = new Worker(queueSize, downloadRemoteService);
        }
    }

    @Override
    public void onError(LoggingEvent event) {
        if (event.getThrowable() == null) {
            return;
        }
        if (workers == null || workers.length <= 0) {
            return;
        }
        if (event.getThrowable() instanceof DerrorFrameException) {
            return;
        }
        if (event.getThrowable() instanceof DerrorTimeoutException) {
            return;
        }
        if (StringUtil.isEmpty(appName)) {
            return;
        }
        workers[ThreadLocalRandom.current().nextInt(workers.length)].offer(event);
    }

    @Override
    public void stop() {
        if (workers == null || workers.length <= 0) {
            return;
        }
        for (Worker worker : workers) {
            worker.stop();
        }
    }

    /**
     * 读取配置
     *
     * @param config
     */
    private void reloadConfig(ClientConfig config) {
        try {
            LOGGER.info("开始了啊啊啊啊啊啊啊啊config:[{}]", config);
            RemoteConfig remoteConfig = downloadRemoteService.getRemoteConfig(config);
            //缓存RemoteConfig对象
            CacheManager.cache(remoteConfig);
        } catch (Throwable e) {
            LOGGER.warn("reloadConfig error e.message={}", e.getMessage());
        }
    }
}
