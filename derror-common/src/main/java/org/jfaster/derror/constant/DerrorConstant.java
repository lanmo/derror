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

package org.jfaster.derror.constant;

/**
 * 常量信息
 * @author yangnan
 */
public final class DerrorConstant {
    public final static String UTF_8 = "UTF-8";
    public final static String DEFAULT_HOST = "127.0.0.1";
    /**制表符*/
    public final static String TAB = "\t";
    /**最大值*/
    public static final int MAX_SIZE = 100;
    public static final double MAX_PER = 0.8;

    /**log扩展字段前缀*/
    public final static String KEY = "derror_%s";
    /**log扩展字段前缀*/
    public final static String KEY_PREFIX = "derror_";
    public final static String EXT = "ext";

    /**获取应用配置*/
    public final static String GET_CONFIG = "/derror/app/getConfig";
    public final static String SAVE_EXCEPTION_LOG = "/derror/exception/alarm";
    /**
     * 当前系统名
     */
    public final static String SYSTEM = "DERROR";
}
