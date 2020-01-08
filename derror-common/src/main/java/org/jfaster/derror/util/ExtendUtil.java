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

package org.jfaster.derror.util;

import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.manager.CacheManager;

import java.util.Collection;
import java.util.Map;

import static org.jfaster.derror.util.StringUtil.isEmpty;

/**
 * 设置扩展字段
 *
 * @author yangnan
 */
public class ExtendUtil {

    /**
     * 扩展字段
     *
     * @param ext
     */
    public static void put(Map<String, String> ext) {
        if (ext == null || ext.isEmpty()) {
            return;
        }
        Collection<ClientConfig> clientConfigs = CacheManager.getAllClientConfig();
        if (clientConfigs == null || clientConfigs.isEmpty()) {
            return;
        }
        clientConfigs.forEach(r -> {
            if (r.getAdapter() == null) {
                return;
            }
            r.getAdapter().put(ext);
        });
    }

    /**
     * 扩展字段
     *
     * @param value
     */
    public static void put(String key, String value) {
        if (value == null || isEmpty(key)) {
            return;
        }
        Collection<ClientConfig> clientConfigs = CacheManager.getAllClientConfig();
        if (clientConfigs == null || clientConfigs.isEmpty()) {
            return;
        }
        clientConfigs.forEach(r -> {
            if (r.getAdapter() == null) {
                return;
            }
            r.getAdapter().put(key, value);
        });
    }

    /**
     * 移除数据
     * @param key
     */
    public static void remove(String key) {
        Collection<ClientConfig> clientConfigs = CacheManager.getAllClientConfig();
        if (clientConfigs == null || clientConfigs.isEmpty()) {
            return;
        }
        clientConfigs.forEach(r -> {
            if (r.getAdapter() == null) {
                return;
            }
            r.getAdapter().remove(key);
        });
    }

    /**
     * 清空数据
     */
    public static void clear() {
        Collection<ClientConfig> clientConfigs = CacheManager.getAllClientConfig();
        if (clientConfigs == null || clientConfigs.isEmpty()) {
            return;
        }
        clientConfigs.forEach(r -> {
            if (r.getAdapter() == null) {
                return;
            }
            r.getAdapter().clear();
        });
    }

    /**
     * 获取value值
     * 默认返回第一个不为空的值
     *
     * @return
     */
    public static String getValue(String key) {
        Collection<ClientConfig> clientConfigs = CacheManager.getAllClientConfig();
        if (clientConfigs == null || clientConfigs.isEmpty()) {
            return null;
        }
        String value = null;
        for (ClientConfig config : clientConfigs) {
            if (config.getAdapter() == null) {
                continue;
            }
            value = config.getAdapter().getValue(key);
            if (!isEmpty(value)) {
                return value;
            }
        }
        return value;
    }

    /**
     * 返回第一个不为空的map集合数据
     *
     * @return
     */
    public static Map<String, String> getValue() {
        Collection<ClientConfig> clientConfigs = CacheManager.getAllClientConfig();
        if (clientConfigs == null || clientConfigs.isEmpty()) {
            return null;
        }
        Map<String, String> result = null;
        for (ClientConfig config : clientConfigs) {
            if (config.getAdapter() == null) {
                continue;
            }
            result = config.getAdapter().getAll();
            if (result != null && !result.isEmpty()) {
                return result;
            }
        }
        return result;
    }
}
