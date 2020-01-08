/*
 *
 *   Copyright 2018 derror.jfaster.org.
 *     <p>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   </p>
 */

package org.jfaster.derror.config;

import org.jfaster.derror.enums.ConfigEnum;
import org.jfaster.derror.exception.DerrorFrameException;
import org.jfaster.derror.monitor.Supervisor;
import org.jfaster.derror.monitor.SupervisorBuilder;
import org.jfaster.derror.util.StringUtil;

/**
 * @author yangnan
 */
public interface DerrorFilter {

    /**
     * 初始化
     *
     * @return
     */
    default Supervisor createInstance() {
        checkNull();
        SupervisorBuilder builder = new SupervisorBuilder();
        builder.appName(getAppName());
        builder.url(getUrl());
        builder.traceKey(getTraceKey());
        builder.token(getToken());
        int queueSize = getQueueSize();
        if (queueSize <= 0) {
            queueSize = ConfigEnum.QUEUE_SIZE.getIntValue();
        }
        builder.queueSize(queueSize);
        int remoteInterval = getRemoteInterval();
        if (remoteInterval <= 0) {
            remoteInterval = ConfigEnum.REMOTE_INTERVAL.getIntValue();
        }
        builder.remoteInterval(remoteInterval);
        int threadNum = getThreadNum();
        if (threadNum <= 0) {
            threadNum = ConfigEnum.THREAD_POOL_CORE.getIntValue();
        }
        builder.threadNum(threadNum);
        builder.adapter(getAdapter());
        return builder.build();
    }

    /**
     * 远程管理url
     * @return
     */
    String getUrl();
    /**
     * 远程管理token
     * @return
     */
    String getToken();
    /**
     * 远程管理appName
     * @return
     */
    String getAppName();

    /**
     * traceKey
     * @return
     */
    String getTraceKey();
    /**
     * 队列大小
     *
     * @return
     */
    int getQueueSize();

    /**
     * 线程数
     *
     * @return
     */
    int getThreadNum();

    /**
     * 获取定时间隔
     *
     * @return
     */
    int getRemoteInterval();

    /**
     * 获取扩展类
     *
     * @return
     */
    ExtendAdapter getAdapter();

    /**
     * 参数校验
     */
    default void checkNull() {
        if (StringUtil.isEmpty(getAppName())) {
            throw new DerrorFrameException("appName is null");
        }
        if (StringUtil.isEmpty(getToken())) {
            throw new DerrorFrameException("token is null");
        }
        if (StringUtil.isEmpty(getUrl())) {
            throw new DerrorFrameException("url is null");
        }
    }
}
