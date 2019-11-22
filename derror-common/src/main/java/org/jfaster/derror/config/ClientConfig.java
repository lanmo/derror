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

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * client配置
 * @author yangnan
 */
@Data
@NoArgsConstructor
public class ClientConfig {
    /**远程管理url*/
    private String url;
    /**远程管理token*/
    private String token;
    /**远程管理appName*/
    private String appName;
    /**系统名*/
    private String system;
    /**是否开启监控开关 默认开启*/
    private Boolean errorSwitch;
    /**环境标识*/
    private String env;
    /**traceKey*/
    private String traceKey;
    /**队列大小*/
    private Integer queueSize;
    /**长轮询时间*/
    private int remoteInterval;

    public ClientConfig(ClientConfig config) {
        this.url = config.getUrl();
        this.appName = config.getAppName();
        this.token = config.getToken();
        this.system = config.getSystem();
        this.env = config.getEnv();
        this.queueSize = config.getQueueSize();
        this.traceKey = config.getTraceKey();
        this.errorSwitch = config.getErrorSwitch();
        this.remoteInterval = config.getRemoteInterval();
    }
}
