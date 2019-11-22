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

package org.jfaster.derror.logging;

import org.jfaster.derror.util.ClassUtil;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * A skeletal implementation of {@link InternalLogger}.  This class implements
 * all methods that have a {@link InternalLogLevel} parameter by default to call
 * specific logger methods such as {@link #info(String)} or {@link #isInfoEnabled()}.
 * @author yangnan
 */
public abstract class AbstractInternalLogger implements InternalLogger, Serializable {
    private static final long serialVersionUID = 1237697433665991858L;
    private final String name;

    protected AbstractInternalLogger(String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isEnabled(InternalLogLevel level) {
        switch (level) {
            case INFO:
                return isInfoEnabled();
            case WARN:
                return isWarnEnabled();
            case DEBUG:
                return isDebugEnabled();
            case ERROR:
                return isErrorEnabled();
            case TRACE:
                return isTraceEnabled();
            default:
                throw new Error(level + " not found");
        }
    }

    @Override
    public void log(InternalLogLevel level, String msg) {
        switch (level) {
            case INFO:
                info(msg);
                break;
            case WARN:
                warn(msg);
                break;
            case DEBUG:
                debug(msg);
                break;
            case ERROR:
                error(msg);
                break;
            case TRACE:
                trace(msg);
                break;
            default:
                throw new Error(level + " not found");
        }
    }

    @Override
    public void log(InternalLogLevel level, String format, Object arg) {
        switch (level) {
            case INFO:
                info(format, arg);
                break;
            case WARN:
                warn(format, arg);
                break;
            case DEBUG:
                debug(format, arg);
                break;
            case ERROR:
                error(format, arg);
                break;
            case TRACE:
                trace(format, arg);
                break;
            default:
                throw new Error(level + " not found");
        }
    }

    @Override
    public void log(InternalLogLevel level, String format, Object arg1, Object arg2) {
        switch (level) {
            case INFO:
                info(format, arg1, arg2);
                break;
            case WARN:
                warn(format, arg1, arg2);
                break;
            case DEBUG:
                debug(format, arg1, arg2);
                break;
            case ERROR:
                error(format, arg1, arg2);
                break;
            case TRACE:
                trace(format, arg1, arg2);
                break;
            default:
                throw new Error(level + " not found");
        }
    }

    @Override
    public void log(InternalLogLevel level, String format, Object... arguments) {
        switch (level) {
            case INFO:
                info(format, arguments);
                break;
            case WARN:
                warn(format, arguments);
                break;
            case DEBUG:
                debug(format, arguments);
                break;
            case ERROR:
                error(format, arguments);
                break;
            case TRACE:
                trace(format, arguments);
                break;
            default:
                throw new Error(level + " not found");
        }
    }

    @Override
    public void log(InternalLogLevel level, String msg, Throwable t) {
        switch (level) {
            case INFO:
                info(msg, t);
                break;
            case WARN:
                warn(msg, t);
                break;
            case DEBUG:
                debug(msg, t);
                break;
            case ERROR:
                error(msg, t);
                break;
            case TRACE:
                trace(msg, t);
                break;
            default:
                throw new Error(level + " not found");
        }
    }

    /**
     * 解决单例问题,避免使用反序列导致出现多例问题
     *
     * @return
     * @throws ObjectStreamException
     */
    protected Object readResolve() throws ObjectStreamException {
        return InternalLoggerFactory.getLogger(name);
    }

    @Override
    public String toString() {
        return ClassUtil.simpleClassName(this) + '(' + name + ')';
    }
}
