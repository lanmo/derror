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
import lombok.Getter;
import org.jfaster.derror.config.DerrorFilter;
import org.jfaster.derror.config.ExtendAdapter;
import org.jfaster.derror.config.LoggingEvent;
import org.jfaster.derror.logback.plugin.event.LogbackEvent;
import org.jfaster.derror.logback.plugin.event.LogbackExtendAdapter;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.monitor.Supervisor;
import org.jfaster.derror.util.ExceptionUtil;
import lombok.Setter;

/**
 * 异常监控日志
 * @author yangnan
 */
public class LogbackErrorFilter extends AbstractMatcherFilter<ILoggingEvent> implements DerrorFilter {

    private final static InternalLogger LOGGER = InternalLoggerFactory.getLogger(LogbackErrorFilter.class);

    /**远程管理url*/
    @Setter
    @Getter
    private String url;
    /**远程管理token*/
    @Setter
    @Getter
    private String token;
    /**远程管理appName*/
    @Setter
    @Getter
    private String appName;
    /**是否开启监控开关 默认开启*/
    @Setter
    private boolean errorSwitch = true;
    /**traceKey*/
    @Setter
    @Getter
    private String traceKey;
    /**日志级别*/
    @Setter
    private Level level;
    /**队列大小默认256*/
    @Setter
    @Getter
    private int queueSize;
    @Setter
    @Getter
    /***轮询时间*/
    private int remoteInterval;
    /**线程数大小*/
    @Setter
    @Getter
    private int threadNum;

    private Supervisor supervisor;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (event.getLevel().equals(level)) {
            try {
                if (supervisor != null) {
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
        loggingEvent.setAppName(appName);
        return loggingEvent;
    }

    @Override
    public void start() {
        if (this.level != null) {
            super.start();
            if (errorSwitch) {
                supervisor = createInstance();
            }
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

    @Override
    public ExtendAdapter getAdapter() {
        return new LogbackExtendAdapter();
    }
}
