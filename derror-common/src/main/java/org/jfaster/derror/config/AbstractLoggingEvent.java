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

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static org.jfaster.derror.constant.DerrorConstant.KEY_PREFIX;
import static org.jfaster.derror.util.StringUtil.isEmpty;

/**
 * @author yangnan
 */
public abstract class AbstractLoggingEvent implements LoggingEvent {

    @Setter
    @Getter
    private Throwable throwable;
    @Getter
    private Map<String, String> mdcPropertyMap;
    @Getter
    private Map<String, String> ext;
    @Getter
    @Setter
    private String appName;

    public void setMdcPropertyMap(Map<String, String> mdcPropertyMap) {
        this.mdcPropertyMap = new HashMap<>(mdcPropertyMap);
        parseExt();
    }

    /**
     * 解析扩展字段
     */
    protected void parseExt() {
        if (mdcPropertyMap == null || mdcPropertyMap.isEmpty()) {
            return;
        }
        ext = new HashMap<>(mdcPropertyMap.size());
        String[] keys = mdcPropertyMap.keySet().toArray(new String[mdcPropertyMap.size()]);
        for (String key : keys) {
            if (isEmpty(key)) {
                continue;
            }
            int index = key.indexOf(KEY_PREFIX);
            if (index >= 0) {
                ext.put(key.substring(index + KEY_PREFIX.length()), mdcPropertyMap.remove(key));
            }
        }
    }

    @Override
    public Map<String, String> getMDCPropertyMap() {
        return this.mdcPropertyMap;
    }
}
