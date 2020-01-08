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

import java.util.HashMap;
import java.util.Map;

import static org.jfaster.derror.util.StringUtil.isEmpty;

/**
 * @author yangnan
 */
public abstract class AbstractExtendAdapter implements ExtendAdapter {

    @Override
    public void put(String key, String value) {
        if (isEmpty(key)) {
           return;
        }
        putExtend(getKey(key), value);
    }

    @Override
    public void remove(String key) {
        if (isEmpty(key)) {
            return;
        }
        removeExtend(getKey(key));
    }

    @Override
    public String getValue(String key) {
        if (isEmpty(key)) {
            return null;
        }
        return getExtendValue(getKey(key));
    }

    @Override
    public Map<String, String> getAll() {
        Map<String, String> map = getExtendMap();
        if (map == null || map.isEmpty()) {
            return new HashMap<>(1);
        }
        Map<String, String> result = new HashMap<>(map.size());
        map.forEach((k, v) -> {
            if (isEmpty(k)) {
                return;
            }
            int index = k.indexOf(getKeyPrefix());
            if (index < 0) {
                return;
            }
            result.put(k.substring(index + getKeyPrefix().length()), v);
        });
        return result;
    }

    @Override
    public void clear() {
        Map<String, String> map = getExtendMap();
        if (map == null || map.isEmpty()) {
            return;
        }
        String[] keys = map.keySet().toArray(new String[map.size()]);
        for (String key : keys) {
            if (isEmpty(key)) {
                return;
            }
            if (key.indexOf(getKeyPrefix()) < 0) {
                return;
            }
            removeExtend(key);
        }
    }

    /**
     * 获取扩展值
     *
     * @return
     */
    protected abstract Map<String, String> getExtendMap();

    /**
     * 设置参数
     *
     * @param key
     * @param value
     */
    protected abstract void putExtend(String key, String value);

    /**
     * 移除扩展值
     *
     * @param key
     */
    protected abstract void removeExtend(String key);

    /**
     * 获取扩展值
     *
     * @param key
     * @return
     */
    protected abstract String getExtendValue(String key);
}
