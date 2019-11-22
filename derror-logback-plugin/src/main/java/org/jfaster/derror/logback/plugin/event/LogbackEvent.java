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

package org.jfaster.derror.logback.plugin.event;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.IThrowableProxy;
import org.jfaster.derror.config.AbstractLoggingEvent;
import org.jfaster.derror.logging.InternalLogLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * logback日志事件
 * @author yangnan
 */
public class LogbackEvent extends AbstractLoggingEvent {

    @Setter
    @Getter
    private InternalLogLevel level;

    public LogbackEvent(Level logLevel) {
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

    public LogbackEvent(Level level, IThrowableProxy throwableProxy) {
        this(level);
        transferThrowable(throwableProxy);
    }
}
