/*
 *  Copyright 2018 derror.jfaster.org
 *
 *  The Derror Project licenses this file to you under the Apache License,
 *  version 2.0 (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.jfaster.derror.log4j2.plugin.filter;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.jfaster.derror.config.DerrorFilter;
import org.jfaster.derror.config.ExtendAdapter;
import org.jfaster.derror.config.LoggingEvent;
import org.jfaster.derror.log4j2.plugin.event.Log4j2Event;
import org.jfaster.derror.log4j2.plugin.event.Log4j2ExtendAdapter;
import org.jfaster.derror.monitor.Supervisor;
import org.jfaster.derror.util.ExceptionUtil;

/**
 * @author yangnan
 */
@Plugin(
        name = "Log4j2Filter",
        category = Node.CATEGORY,
        elementType = Filter.ELEMENT_TYPE,
        printObject = true
)
public class Log4j2Filter extends AbstractFilter implements DerrorFilter {

    private final Level level;

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
    /**队列大小默认256*/
    @Setter
    @Getter
    private int queueSize;
    @Getter
    @Setter
    private int threadNum;
    @Getter
    @Setter
    private int remoteInterval;

    private Supervisor supervisor;

    private Log4j2Filter(final Level level, final Result onMatch, final Result onMismatch) {
        super(onMatch, onMismatch);
        this.level = level;
    }

    private Log4j2Filter(final Level level, final Result onMatch, final Result onMismatch, String url,
            String token, String appName, boolean errorSwitch, String traceKey, int queueSize,
                         int threadNum, int remoteInterval) {
        super(onMatch, onMismatch);
        this.level = level;
        this.errorSwitch = errorSwitch;
        if (errorSwitch) {
            this.appName = appName;
            this.url = url;
            this.token = token;
            this.traceKey = traceKey;
            this.queueSize = queueSize;
            this.threadNum = threadNum;
            this.remoteInterval = remoteInterval;
            supervisor = createInstance();
        }
    }

    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return filter(level);
    }

    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return filter(level);
    }

    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return filter(level);
    }

    @Override
    public Result filter(final LogEvent event) {
        Result result = filter(event.getLevel());
        try {
            if (supervisor != null && result == Result.ACCEPT) {
                org.jfaster.derror.config.LoggingEvent event1 = buildLoggingEvent(event);
                supervisor.onError(event1);
            }
        } catch (Throwable e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("supervisor error {}", e.getMessage(), ExceptionUtil.handleException(e));
            }
        }
        return result;
    }

    private Result filter(final Level level) {
        return level.isMoreSpecificThan(this.level) ? onMatch : onMismatch;
    }

    /**
     * Create a MyCustomFilter.
     *
     * @param level
     *            The log Level.
     * @param match
     *            The action to take on a match.
     * @param mismatch
     *            The action to take on a mismatch.
     * @return The created MyCustomFilter.
     */
    @PluginFactory
    public static Log4j2Filter createFilter(
            @PluginAttribute("level") final Level level,
            @PluginAttribute("onMatch") final Result match,
            @PluginAttribute("onMismatch") final Result mismatch,
            @PluginAttribute("url") String url,
            @PluginAttribute("token") String token,
            @PluginAttribute("appName") String appName,
            @PluginAttribute("errorSwitch") boolean errorSwitch,
            @PluginAttribute("traceKey") String traceKey,
            @PluginAttribute("queueSize") int queueSize,
            @PluginAttribute("threadNum") int threadNum,
            @PluginAttribute("remoteInterval") int remoteInterval
            ) {
        final Level actualLevel = level == null ? Level.ERROR : level;
        final Result onMatch = match == null ? Result.NEUTRAL : match;
        final Result onMismatch = mismatch == null ? Result.DENY : mismatch;
        return new Log4j2Filter(actualLevel, onMatch, onMismatch, url, token, appName, errorSwitch, traceKey,
                                queueSize, threadNum, remoteInterval);
    }

    private LoggingEvent buildLoggingEvent(LogEvent event) {
        Log4j2Event loggingEvent = new Log4j2Event(event.getLevel(), event.getThrownProxy());
        loggingEvent.setMdcPropertyMap(event.getContextData().toMap());
        loggingEvent.setAppName(appName);
        return loggingEvent;
    }

    @Override
    public ExtendAdapter getAdapter() {
        return new Log4j2ExtendAdapter();
    }
}
