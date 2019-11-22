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

package org.jfaster.derror.monitor.support;

import org.jfaster.derror.alarm.AlarmEvent;
import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.config.LoggingEvent;
import org.jfaster.derror.constant.DerrorConstant;
import org.jfaster.derror.enums.ConfigEnum;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.manager.ConfigManager;
import org.jfaster.derror.manager.ErrorSupervisorManager;
import org.jfaster.derror.manager.NameThreadFactory;
import org.jfaster.derror.monitor.Supervisor;
import org.jfaster.derror.remote.IDownloadRemoteService;
import org.jfaster.derror.remote.config.RemoteConfig;
import org.jfaster.derror.remote.support.DownloadRemoteServiceImpl;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 异常监控
 * @author yangnan
 */
public class ErrorSupervisor implements Supervisor {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(ErrorSupervisor.class);

    private IDownloadRemoteService downloadRemoteService;
    private volatile boolean init = false;
    private ErrorSupervisorManager monitorManager;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ClientConfig config;

    public void setDownloadRemoteService(IDownloadRemoteService downloadRemoteService) {
        this.downloadRemoteService = downloadRemoteService;
    }

    @Override
    public void onError(LoggingEvent event) {
        if (config.getErrorSwitch()) {
            monitorManager.putError(config, event);
        }
    }

    @Override
    public void start() {
        ClientConfig clientConfig = ConfigManager.getConfig();
        init(clientConfig);
    }

    @Override
    public void stop() {
        scheduledThreadPoolExecutor.shutdown();
        monitorManager.shutdown();
    }

    /**
     * 初始化
     * @param config
     */
    public void init(ClientConfig config) {
        if (!config.getErrorSwitch()) {
            LOGGER.debug("Unopened errorSwitch configInfo:{}", config);
            return;
        }
        if (!init) {
            synchronized (ErrorSupervisor.class) {
                if (!init) {
                    int remoteInterval = config.getRemoteInterval();
                    BlockingQueue<AlarmEvent> queue = new ArrayBlockingQueue<AlarmEvent>(config.getQueueSize());
                    if (downloadRemoteService == null) {
                        downloadRemoteService = new DownloadRemoteServiceImpl();
                    }
                    ThreadPoolExecutor executor = new ThreadPoolExecutor(ConfigEnum.THREAD_POOL_CORE.getIntValue(),
                                                                         ConfigEnum.THREAD_POOL_MAX.getIntValue(),
                                                                         DerrorConstant.KEEP_ALIVE_TIME, TimeUnit
                                                                                 .MILLISECONDS, new
                                                                                 LinkedBlockingDeque<Runnable>(config.getQueueSize()), new NameThreadFactory("ErrorSupervisor"), new DiscardPolicy());
                    executor.allowCoreThreadTimeOut(true);
                    monitorManager = new ErrorSupervisorManager(queue, executor, downloadRemoteService);
                    scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
                    scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            reloadConfig(config);
                        }
                    }, 0, remoteInterval, TimeUnit.SECONDS);
                    init = true;
                    this.config = config;
                }
            }
        }
    }

    /**
     * 读取配置
     *
     * @param config
     */
    private void reloadConfig(ClientConfig config) {
        try {
            RemoteConfig remoteConfig = downloadRemoteService.getRemoteConfig(config);
            //缓存RemoteConfig对象
            ConfigManager.cache(remoteConfig);
        } catch (Throwable e) {
            LOGGER.warn("reloadConfig error e.message={}", e.getMessage());
        }

    }
}
