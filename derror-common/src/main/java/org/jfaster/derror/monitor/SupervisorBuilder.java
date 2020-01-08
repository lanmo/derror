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

package org.jfaster.derror.monitor;

import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.config.ExtendAdapter;
import org.jfaster.derror.monitor.support.DefaultSupervisor;

/**
 * @author yangnan
 */
public class SupervisorBuilder {

    /**
     * 远程管理url
     */
    private String url;
    /**
     * 远程管理token
     */
    private String token;
    /**
     * 远程管理appName
     */
    private String appName;
    /**
     * traceKey
     */
    private String traceKey;
    /**
     * 队列大小
     */
    private int queueSize;
    /**
     * 线程个数
     */
    private int threadNum;
    /**
     * 定时时间间隔
     */
    private int remoteInterval;

    /**
     * 扩展类
     */
    private ExtendAdapter adapter;

    public SupervisorBuilder url(String url) {
        this.url = url;
        return this;
    }

    public SupervisorBuilder threadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public SupervisorBuilder token(String token) {
        this.token = token;
        return this;
    }

    public SupervisorBuilder appName(String appName) {
        this.appName = appName;
        return this;
    }

    public SupervisorBuilder traceKey(String traceKey) {
        this.traceKey = traceKey;
        return this;
    }

    public SupervisorBuilder queueSize(int queueSize) {
        this.queueSize = queueSize;
        return this;
    }

    public SupervisorBuilder remoteInterval(int remoteInterval) {
        this.remoteInterval = remoteInterval;
        return this;
    }

    public SupervisorBuilder adapter(ExtendAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public Supervisor build() {
        ClientConfig config = ClientConfig.builder()
                        .appName(appName)
                        .token(token)
                        .traceKey(traceKey)
                        .url(url)
                        .adapter(adapter)
                        .build();
        return new DefaultSupervisor(threadNum, queueSize, remoteInterval, config);
    }
}
