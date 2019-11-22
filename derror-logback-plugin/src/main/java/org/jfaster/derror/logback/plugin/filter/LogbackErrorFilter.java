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

package org.jfaster.derror.logback.plugin.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.config.LoggingEvent;
import org.jfaster.derror.enums.ConfigEnum;
import org.jfaster.derror.exception.DerrorFrameException;
import org.jfaster.derror.logback.plugin.event.LogbackEvent;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.manager.ConfigManager;
import org.jfaster.derror.monitor.support.ErrorSupervisor;
import org.jfaster.derror.monitor.Supervisor;
import org.jfaster.derror.util.ExceptionUtil;
import org.jfaster.derror.util.StringUtil;
import lombok.Setter;

/**
 * 异常监控日志
 * @author yangnan
 */
public class LogbackErrorFilter extends AbstractMatcherFilter<ILoggingEvent> {

    private final static InternalLogger LOGGER = InternalLoggerFactory.getLogger(LogbackErrorFilter.class);

    /**远程管理url*/
    @Setter
    private String url;
    /**远程管理token*/
    @Setter
    private String token;
    /**远程管理appName*/
    @Setter
    private String appName;
    /**是否开启监控开关 默认开启*/
    @Setter
    private boolean errorSwitch = true;
    /**环境标识*/
    @Setter
    private String env;
    /**traceKey*/
    @Setter
    private String traceKey;
    /**日志级别*/
    @Setter
    private Level level;
    /**队列大小默认256*/
    @Setter
    private int queueSize = 256;
    @Setter
    /***轮询时间*/
    private int remoteInterval = ConfigEnum.REMOTE_INTERVAL.getIntValue();

    private Supervisor supervisor;
    private ClientConfig config;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (event.getLevel().equals(level)) {
            try {
                if (errorSwitch) {
                    LoggingEvent event1 = buildLoggingEvent(event);
                    supervisor.onError(event1);
                }
            } catch (Throwable e) {
                LOGGER.warn("supervisor error {}", e.getMessage(), e);
            }
            return onMatch;
        } else {
            return onMismatch;
        }
    }

    private LoggingEvent buildLoggingEvent(ILoggingEvent event) {
        LogbackEvent loggingEvent = new LogbackEvent(event.getLevel(), event.getThrowableProxy());
        loggingEvent.setMdcPropertyMap(event.getMDCPropertyMap());
        loggingEvent.setEnv(env);
        return loggingEvent;
    }

    /**
     * 初始化
     */
    private void init() {

        if (!errorSwitch) {
            LOGGER.warn("errorSwitch is false");
            return;
        }

        if (StringUtil.isEmpty(url)) {
            throw new DerrorFrameException("url can not be null");
        }

        if (StringUtil.isEmpty(appName)) {
            throw new DerrorFrameException("appName can not be null");
        }

        if (StringUtil.isEmpty(token)) {
            throw new DerrorFrameException("token can not be null");
        }

        config = new ClientConfig();
        config.setAppName(appName);
        config.setErrorSwitch(errorSwitch);
        config.setEnv(env);
        config.setToken(token);
        config.setTraceKey(traceKey);
        config.setErrorSwitch(errorSwitch);
        config.setQueueSize(queueSize);
        config.setUrl(url);
        config.setRemoteInterval(remoteInterval);
        if (supervisor == null) {
            supervisor = new ErrorSupervisor();
        }
        ConfigManager.init(config);
        supervisor.start();
    }

    @Override
    public void start() {
        if (this.level != null) {
            super.start();
            init();
        }
    }

    @Override
    public void stop() {
        super.stop();
        try {
            if (supervisor != null) {
                supervisor.stop();
            }
        } catch (Throwable e) {
            LOGGER.error("supervisor stop error", ExceptionUtil.handleException(e));
        }
    }
}
