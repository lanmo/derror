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

package org.jfaster.derror.alarm.ext;

import org.jfaster.derror.alarm.Alarm;
import org.jfaster.derror.cache.CacheLoader;
import org.jfaster.derror.cache.support.DoubleCheckCache;
import org.jfaster.derror.constant.DerrorConstant;
import org.jfaster.derror.exception.DerrorFrameException;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.util.ExceptionUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * alarm类加载器
 * @author yangnan
 */
public class AlarmClassLoader<T> {
    private static InternalLogger LOGGER = InternalLoggerFactory.getLogger(AlarmClassLoader.class);

    private static final DoubleCheckCache<Class<?>, AlarmClassLoader<?>> ALARM_CLASS_LOADER = new
            DoubleCheckCache<Class<?>, AlarmClassLoader<?>>(new CacheLoader<Class<?>, AlarmClassLoader<?>>() {
        @Override
        public AlarmClassLoader<?> load(Class<?> type) {
            return new AlarmClassLoader(type);
        }
    });

    private AtomicBoolean init = new AtomicBoolean(false);

    /**alarm服务的前缀*/
    private final String ALARM_PREFIX = "META-INF/alarms/";
    private List<T> extensions;
    private Class<T> type;
    private ClassLoader classLoader;

    public AlarmClassLoader(Class<T> type) {
        this(type, Thread.currentThread().getContextClassLoader());
    }
    public AlarmClassLoader(Class<T> type, ClassLoader classLoader) {
        this.type = type;
        this.classLoader = classLoader;
        checkInit();
    }

    /**
     * 获取AlarmClassLoader
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> AlarmClassLoader<T> getClassLoader(Class<T> type) {
        checkInterfaceType(type);
        return (AlarmClassLoader<T>) ALARM_CLASS_LOADER.get(type);
    }

    public List<T> getExtensions() {
        return this.extensions;
    }

    /**
     * 检测type是否合法
     *
     * @param type
     */
    private static void checkInterfaceType(Class<?> type) {
        if (type == null) {
            throw new DerrorFrameException("Error type is null");
        }

        if (!type.isInterface()) {
            throw new DerrorFrameException("Error '%s' is not interface", type.getName());
        }

        if (!isAlarmType(type)) {
            throw new DerrorFrameException("Error '%s' is alarm type", type.getName());
        }
    }

    /**
     * 判断是否是alarm类型
     *
     * @param type
     * @return
     */
    private static boolean isAlarmType(Class<?> type) {
        return type.isAssignableFrom(Alarm.class);
    }

    /**
     * 初始化
     */
    private void checkInit() {
        if (init.compareAndSet(false, true)) {
            loadExtensionClasses();
        }
    }

    /**
     * 加载扩展类
     */
    private void loadExtensionClasses() {
        extensions = loadExtensionClasses(ALARM_PREFIX);
    }

    /**
     * 加载扩展类
     *
     * @param prefix
     * @return
     */
    private List<T> loadExtensionClasses(String prefix) {
        String fullName = prefix + type.getName();
        List<String> classNames = new ArrayList<String>();
        try {
            Enumeration<URL> urls;
            if (classLoader == null) {
                urls = ClassLoader.getSystemResources(fullName);
            } else {
                urls = classLoader.getResources(fullName);
            }
            if (urls == null || !urls.hasMoreElements()) {
                return Collections.emptyList();
            }

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                parseUrl(url, type, classNames);
            }
        } catch (Exception e) {
            throw ExceptionUtil.handleException(e);
        }

        return loadClass(classNames);
    }

    /**
     * 解析url
     *
     * @param url
     * @param type
     * @param classNames
     */
    private void parseUrl(URL url, Class<T> type, List<String> classNames) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = url.openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, DerrorConstant.UTF_8));
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                parseLine(type, url, line, lineNumber, classNames);
            }
        } catch (Exception e) {
            throw new DerrorFrameException("Error read configuration file", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("Error close reader", e);
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("Error close inputStream", e);
                }
            }
        }
    }

    /**
     * 解析每行数据
     *
     * @param type
     * @param url
     * @param line
     * @param lineNumber
     * @param classNames
     */
    private void parseLine(Class<T> type, URL url, String line, int lineNumber, List<String> classNames) {
        int ci = line.indexOf("#");
        if (ci > 0) {
            line = line.substring(0, ci);
        }
        line = line.trim();
        if (line.length() <= 0) {
            return;
        }
        if (line.indexOf(DerrorConstant.EMPTY) >= 0 || line.indexOf(DerrorConstant.TAB) >= 0) {
            throw new DerrorFrameException("Illegal '%s' configuration-file syntax lineNumber:%d url:%s", type.getName(),
                                           lineNumber, url.getPath());
        }

        int cp = line.codePointAt(0);
        if (!Character.isJavaIdentifierPart(cp)) {
            throw new DerrorFrameException("Illegal provider-class name:%s,url:%s,lineNumber:%d,line:%s", type
                    .getName(), url.getPath(), lineNumber, line);
        }
        for (int i=Character.charCount(cp); i<line.length(); i += Character.charCount(cp)) {
            cp = line.codePointAt(i);
            if (!Character.isJavaIdentifierPart(cp) && cp != '.') {
                throw new DerrorFrameException("Illegal provider-class name:%s,url:%s,lineNumber:%d,line:%s", type
                        .getName(), url.getPath(), lineNumber, line);
            }
        }

        if (!classNames.contains(line)) {
            classNames.add(line);
        }
    }

    /**
     * 加载class
     *
     * @param classNames
     * @return
     */
    private List<T> loadClass(List<String> classNames) {
        List<T> alarms = new ArrayList<T>();
        for (String className : classNames) {
            try {
                Class<T> cz;
                if (classLoader == null) {
                    cz = (Class<T>) Class.forName(className);
                } else {
                    cz = (Class<T>) Class.forName(className, true, classLoader);
                }
                checkAlarmType(cz);
                alarms.add(cz.newInstance());
            } catch (Exception e) {
                LOGGER.error("Error load class", ExceptionUtil.handleException(e));
            }
        }
        return alarms;
    }

    /**
     * 校验 type
     *
     * @param cz
     */
    private void checkAlarmType(Class<T> cz) {
        checkClassPublic(cz);
        checkConstructorPublic(cz);
        checkClassInherit(cz);
    }

    /**
     * 检测类型是否匹配
     * @param cz
     */
    private void checkClassInherit(Class<T> cz) {
        if (!type.isAssignableFrom(cz)) {
            throw new DerrorFrameException("Error %s is not instanceof %s ", cz.getName(), type.getName());
        }
    }

    /**
     * 检测是否有无惨构造函数
     * @param cz
     */
    private void checkConstructorPublic(Class<T> cz) {
        Constructor<?>[] constructors = cz.getConstructors();
        if (constructors == null || constructors.length <= 0) {
            throw new DerrorFrameException("Error %s has no public no-args constructor", cz.getName());
        }
    }

    /**
     * 校验class方法权限
     *
     * @param cz
     */
    private void checkClassPublic(Class<T> cz) {
        if (!Modifier.isPublic(cz.getModifiers())) {
            throw new DerrorFrameException("Error %s not a public class", cz.getName());
        }
    }
}
