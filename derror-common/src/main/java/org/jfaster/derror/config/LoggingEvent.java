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

package org.jfaster.derror.config;

import org.jfaster.derror.logging.InternalLogLevel;

import java.util.Map;

/**
 * @author yangnan
 */
public interface LoggingEvent {

    /**
     * 日志级别
     * @return
     */
    InternalLogLevel getLevel();

    /**
     * 异常
     * @return
     */
    Throwable getThrowable();

    /**
     * Returns the MDC map. The returned value can be an empty map but not null.
     * @return
     */
    Map<String, String> getMDCPropertyMap();

    /**
     * 获取扩展字段
     * @return
     */
    Map<String, String> getExt();

    /**
     * 获取应用名称
     *
     * @return
     */
    String getAppName();

}
