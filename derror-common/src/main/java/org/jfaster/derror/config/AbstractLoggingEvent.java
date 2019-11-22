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

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jfaster.derror.constant.DerrorConstant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yangnan
 */
public abstract class AbstractLoggingEvent implements LoggingEvent {

    @Setter
    @Getter
    private Throwable throwable;
    @Getter
    private Map<String, String> mdcPropertyMap;
    @Setter
    @Getter
    private String env;
    @Getter
    private Map<String, Object> ext;

    protected void transferThrowable(IThrowableProxy throwableProxy) {
        if (throwableProxy instanceof ThrowableProxy) {
            throwable = ((ThrowableProxy) throwableProxy).getThrowable();
        }
    }

    public void setMdcPropertyMap(Map<String, String> mdcPropertyMap) {
        this.mdcPropertyMap = new HashMap<String, String>(mdcPropertyMap);
        parseExt();
    }

    /**
     * 解析扩展字段
     */
    protected void parseExt() {
        Map<String, String> map = this.mdcPropertyMap;
        if (map == null || map.isEmpty()) {
            return;
        }
        if (ext == null) {
            ext = new HashMap<String, Object>(map.size());
        }
        String[] keys = map.keySet().toArray(new String[map.size()]);
        String keyPrefix = DerrorConstant.KEY_PREFIX;
        for (String key : keys) {
            int index = key.indexOf(keyPrefix);
            if (index >= 0) {
                JSONObject object = JSON.parseObject(map.get(key));
                ext.put(key.substring(index + keyPrefix.length()), object);
                map.remove(key);
            }
        }
    }


    @Override
    public Map<String, String> getMDCPropertyMap() {
        return this.mdcPropertyMap;
    }
}
