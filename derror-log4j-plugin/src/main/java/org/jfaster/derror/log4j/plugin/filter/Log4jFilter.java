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

package org.jfaster.derror.log4j.plugin.filter;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Level;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.jfaster.derror.config.DerrorFilter;
import org.jfaster.derror.config.ExtendAdapter;
import org.jfaster.derror.log4j.plugin.event.Log4jEvent;
import org.jfaster.derror.log4j.plugin.event.Log4jExtendAdapter;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.monitor.Supervisor;

/**
 * @author yangnan
 */
public class Log4jFilter extends Filter implements DerrorFilter {

    private final static InternalLogger LOGGER = InternalLoggerFactory.getLogger(Log4jFilter.class);

    @Getter @Setter
    boolean acceptOnMatch = false;
    @Getter @Setter
    Level levelMin;
    @Getter @Setter
    Level levelMax;

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
    @Setter
    @Getter
    private int threadNum;
    @Getter
    @Setter
    private int remoteInterval;

    private Supervisor supervisor;

    @Override
    public int decide(LoggingEvent event) {

        if(this.levelMin != null) {
            if (event.getLevel().isGreaterOrEqual(levelMin) == false) {
                // level of event is less than minimum
                return Filter.DENY;
            }
        }

        if(this.levelMax != null) {
            if (event.getLevel().toInt() > levelMax.toInt()) {
                // level of event is greater than maximum
                // Alas, there is no Level.isGreater method. and using
                // a combo of isGreaterOrEqual && !Equal seems worse than
                // checking the int values of the level objects..
                return Filter.DENY;
            }
        }

        if (acceptOnMatch) {
            // this filter set up to bypass later filters and always return
            // accept if level in range
            return Filter.ACCEPT;
        }
        else {
            try {
                if (supervisor != null) {
                    org.jfaster.derror.config.LoggingEvent event1 = buildLoggingEvent(event);
                    supervisor.onError(event1);
                }
            } catch (Throwable e) {
                LOGGER.debug("supervisor error {}", e.getMessage(), e);
            }
            // event is ok for this filter; allow later filters to have a look..
            return Filter.NEUTRAL;
        }
    }

    private org.jfaster.derror.config.LoggingEvent buildLoggingEvent(LoggingEvent event) {
        Log4jEvent loggingEvent = new Log4jEvent(event.getLevel(), event.getThrowableInformation());
        loggingEvent.setMdcPropertyMap(MDC.getContext());
        loggingEvent.setAppName(appName);
        return loggingEvent;
    }

    @Override
    public void activateOptions() {
        if (errorSwitch) {
            supervisor = createInstance();
        }
    }

    @Override
    public ExtendAdapter getAdapter() {
        return new Log4jExtendAdapter();
    }
}
