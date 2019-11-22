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

import com.alibaba.fastjson.JSON;
import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.constant.DerrorConstant;
import org.jfaster.derror.manager.ConfigManager;
import java.util.Map;
import org.slf4j.MDC;

/**
 * 保存线程变量
 * @author yangnan
 */
public class ThreadLocalUtil {

    /**
     * 扩展字段
     *
     * @param key
     * @param ext
     */
    public static void set(String key, Map<String, ?> ext) {
        if (errorSwitch() && ext != null && !ext.isEmpty()) {
            MDC.put(getKey(key), JSON.toJSONString(ext));
        }
    }

    /**
     * 扩展字段
     *
     * @param ext
     */
    public static void set(Map<String, ?> ext) {
        if (errorSwitch() && ext != null && !ext.isEmpty()) {
            MDC.put(getKey(DerrorConstant.EXT), JSON.toJSONString(ext));
        }
    }

    public static void remove(String key) {
        MDC.remove(getKey(key));
    }

    public static void remove() {
        MDC.remove(getKey(DerrorConstant.EXT));
    }

    /**
     * 是否开启监控开关
     * @return
     */
    private static boolean errorSwitch() {
        ClientConfig config = ConfigManager.getConfig();
        return config != null && config.getErrorSwitch();
    }

    private static String getKey(String key) {
        return String.format(DerrorConstant.KEY, key);
    }

}
