/*
 *
 *   Copyright 2018 org.jfaster.derror.
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

package org.jfaster.derror.manager.utils;

import org.jfaster.derror.manager.pojo.bo.UserBO;

import java.util.HashMap;

/**
 * @author yangnan
 * 系统上下文保存, 绑定当前线程
 */
public class RuntimeContext {
    private static final String KEY_USER = "user";
    private static ThreadLocal<HashMap<String, Object>> RUNTIME = ThreadLocal.withInitial(
            () -> new HashMap<>());

    private static void addItem(String key, Object value) {
        HashMap<String, Object> map = RUNTIME.get();
        map.put(key, value);
    }

    private static Object getValue(String key) {
        HashMap<String, Object> map = RUNTIME.get();
        return map.get(key);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static UserBO getCurrUser() {
        return (UserBO) getValue(KEY_USER);
    }

    /**
     * 设置当前登录用户信息
     *
     * @param user 用户信息
     */
    public static void setCurrUser(UserBO user) {
        addItem(KEY_USER, user);
    }
}
