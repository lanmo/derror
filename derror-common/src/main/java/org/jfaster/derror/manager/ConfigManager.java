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

package org.jfaster.derror.manager;

import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.constant.DerrorConstant;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.remote.config.FilterExceptionConfig;
import org.jfaster.derror.remote.config.RemoteConfig;
import org.jfaster.derror.util.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 配置管理类
 * @author yangnan
 */
public class ConfigManager {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(ConfigManager.class);

    private static AtomicBoolean INIT = new AtomicBoolean(false);
    private static ConcurrentMap<String, RemoteConfig> extFields = new ConcurrentHashMap<String, RemoteConfig>();
    private static volatile ClientConfig config = null;
    /**缓存不能解析的类*/
    private static ConcurrentMap<String, Long> unResolveClass = new ConcurrentHashMap<String, Long>();

    /**
     * 初始化
     * @param clientConfig
     */
    public static void init(ClientConfig clientConfig) {
        if (INIT.compareAndSet(false, true)) {
            config = new ClientConfig(clientConfig);
            LOGGER.info("ConfigManager init success");
        }
    }

    public static ClientConfig getConfig() {
        return config;
    }

    public static RemoteConfig getConfigFromCache(String appName) {
        LOGGER.debug("获取配置appName:{}", appName);
        return extFields.get(appName);
    }

    /**
     * 本地缓存对象
     * @param config
     */
    public static void cache(RemoteConfig config) {
        if (config == null) {
            LOGGER.debug("RemoteConfig is null");
            return;
        }
        String appName = config.getAppConfig().getAppName();
        if (StringUtil.isEmpty(appName)) {
            LOGGER.debug("appName:{} is null", appName);
            return;
        }
        List<FilterExceptionConfig> exceptionConfigs = config.getExceptionConfigs();
        if (exceptionConfigs != null && exceptionConfigs.size() > 0) {
            config.setFilterExceptions(parseFilterException(exceptionConfigs));
        }
        extFields.put(appName, config);
    }

    /**
     * 解析异常信息
     *
     * @param exceptionConfigs
     * @return
     */
    private static List<Class<?>> parseFilterException(List<FilterExceptionConfig> exceptionConfigs) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (FilterExceptionConfig config : exceptionConfigs) {
            if (StringUtil.isEmpty(config.getClassName())) {
                continue;
            }
            String className = config.getClassName().trim();
            try {
                if (!unResolveClass.containsKey(className)) {
                    classes.add(Class.forName(className));
                }
            } catch (ClassNotFoundException e) {
                addUnResolveClass(config.getClassName());
                LOGGER.warn("ClassNotFoundException className:{}", config.getClassName());
            }
        }
        return classes;
    }

    /**
     * 添加不能解析的类,并清除一些保证map不会太大造成内存溢出
     *
     * @param className
     */
    public static void addUnResolveClass(String className) {
        if (unResolveClass.size() < DerrorConstant.MAX_SIZE) {
            unResolveClass.put(className, System.currentTimeMillis());
            return;
        }

        //删除20%的数据
        int removed = DerrorConstant.MAX_SIZE - (int) (DerrorConstant.MAX_SIZE * DerrorConstant.MAX_PER);
        List<Long> times = new ArrayList<Long>(unResolveClass.values());
        //从大到小排序
        Collections.sort(times, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1 > o2 ? -1 : o1.equals(o2) ? 0 : 1;
            }
        });

        long minRemoved = times.get(removed - 1);
        String[] keys = unResolveClass.keySet().toArray(new String[unResolveClass.size()]);
        for (String key : keys) {
            Long time = unResolveClass.get(key);
            if (time >= minRemoved) {
                unResolveClass.remove(key);
            }
        }
    }

}
