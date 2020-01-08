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

package org.jfaster.derror.log4j2.plugin.event;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.jfaster.derror.config.AbstractLoggingEvent;
import org.jfaster.derror.logging.InternalLogLevel;

/**
 * logback日志事件
 * @author yangnan
 */
public class Log4j2Event extends AbstractLoggingEvent {

    @Setter
    @Getter
    private InternalLogLevel level;

    public Log4j2Event(Level logLevel) {
        if (Level.ERROR.equals(logLevel)) {
            level = InternalLogLevel.ERROR;
        } else if (Level.INFO.equals(logLevel))  {
            level = InternalLogLevel.INFO;
        } else if (Level.WARN.equals(logLevel))  {
            level = InternalLogLevel.WARN;
        } else if (Level.TRACE.equals(logLevel))  {
            level = InternalLogLevel.TRACE;
        } else if (Level.DEBUG.equals(logLevel))  {
            level = InternalLogLevel.DEBUG;
        }
    }

    public Log4j2Event(Level level, ThrowableProxy throwableProxy) {
        this(level);
        if (throwableProxy != null) {
            setThrowable(throwableProxy.getThrowable());
        }
    }
}
