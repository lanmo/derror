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

package org.jfaster.derror.enums;

import lombok.Getter;

/**
 * 默认值
 * @author yangnan
 */
public enum ConfigEnum {

    THREAD_POOL_CORE("thread_core", 1),
    QUEUE_SIZE("queue_size", 256),
    /***报警配置end**/

    /***读取远程配置start**/
    REMOTE_INTERVAL("remote_interval", 10),
    CONNECTION_TIMEOUT("remote_connection_timeout", 1000),
    SOCKET_TIMEOUT("remote_socket_timeout", 1000),
    WRITE_TIMEOUT("remote_write_timeout", 1000),
    /***读取远程配置end**/
    ;

    ConfigEnum(String key, int intValue) {
        this.key = key;
        this.intValue = intValue;
    }

    ConfigEnum(String key, boolean boolValue) {
        this.key = key;
        this.boolValue = boolValue;
    }

    ConfigEnum(String key, long longValue) {
        this.key = key;
        this.longValue = longValue;
    }

    ConfigEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Getter
    private String key;
    @Getter
    private int intValue;
    @Getter
    private boolean boolValue;
    @Getter
    private long longValue;
    @Getter
    private String value;
}
