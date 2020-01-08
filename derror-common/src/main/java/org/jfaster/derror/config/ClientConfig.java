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

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * client配置
 * @author yangnan
 */
@Data
@Builder
public class ClientConfig {
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
     * 扩展
     */
    private ExtendAdapter adapter;

    public ClientConfig copy() {
        return ClientConfig.builder()
                .url(url)
                .traceKey(traceKey)
                .appName(appName)
                .adapter(adapter)
                .token(token)
                .build();
    }
}