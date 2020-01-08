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

import java.util.Map;

import static org.jfaster.derror.constant.DerrorConstant.KEY;
import static org.jfaster.derror.constant.DerrorConstant.KEY_PREFIX;

/**
 * 设置扩展字段
 *
 * @author yangnan
 */
public interface ExtendAdapter {

    /**
     * 扩展字段
     *
     * @param ext
     */
    default void put(Map<String, String> ext) {
        if (ext == null || ext.isEmpty()) {
            return;
        }
        ext.forEach((k, v) -> put(k, v));
    }

    /**
     * 扩展字段
     *
     * @param value
     * @param key
     */
    default void put(String key, String value) {
    }

    /**
     * 移除数据
     * @param key
     */
    default void remove(String key) {
    }

    /**
     * 获取value值
     *
     * @return
     * @param key
     */
    default String getValue(String key) {
        return null;
    }

    /**
     * 获取所有值
     *
     * @return
     */
    default Map<String, String> getAll() {
        return null;
    }

    /**
     * 清空值
     */
    default void clear() {

    }

    /**
     * 获取key
     *
     * @param key
     * @return
     */
    default String getKey(String key) {
        return String.format(KEY, key);
    }

    /**
     * 获取key前缀
     *
     * @return
     */
    default String getKeyPrefix() {
        return KEY_PREFIX;
    }
}
