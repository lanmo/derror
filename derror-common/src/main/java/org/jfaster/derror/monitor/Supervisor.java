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

package org.jfaster.derror.monitor;

import org.jfaster.derror.config.LoggingEvent;

/**
 * 监控类
 * @author yangnan
 */
public interface Supervisor {

    /**
     * 执行异常处理
     * @param event 日志事件
     */
    void onError(LoggingEvent event);

    /**
     * 启动
     */
    void start();

    /**
     * 关闭
     */
    void stop();
}
