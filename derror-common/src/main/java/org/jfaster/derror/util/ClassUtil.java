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

import java.util.List;

/**
 * @author yangnan
 */
public final class ClassUtil {
    private ClassUtil() {}

    /**
     * 返回class的shortName
     * {@link #simpleClassName(Class)}
     * @param obj
     * @return
     */
    public static String simpleClassName(Object obj) {
        return simpleClassName(obj.getClass());
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static String simpleClassName(Class<?> clazz) {
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            return clazz.getName().substring(pkg.getName().length() + 1);
        }
        return clazz.getName();
    }

    /**
     * 返回class的className
     * {@link #getClassName(Class)}
     * @param obj
     * @return
     */
    public static String getClassName(Object obj) {
        return getClassName(obj.getClass());
    }

    /**
     * 返回class的shortName
     * {@link #getClassName(Class)}
     * @param clazz
     * @return
     */
    public static String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 判断cz是否在class里面
     *
     * @param classes
     * @param cz
     * @return
     */
    public static boolean contains(List<Class<?>> classes, Class<?> cz) {
        if (classes == null || classes.isEmpty() || cz == null) {
            return false;
        }
        for (Class<?> c : classes) {
            if (c.isAssignableFrom(cz)) {
                return true;
            }
        }
        return false;
    }
}
