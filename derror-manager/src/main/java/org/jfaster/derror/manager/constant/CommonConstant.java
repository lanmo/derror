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

package org.jfaster.derror.manager.constant;

/**
 * @author yangnan
 */
public class CommonConstant {
    /**默认频次时间 单位s*/
    public static final int DEFAULT_FREQUENCY_TIME = 60;
    /**默认保留天数*/
    public static final int DEFAULT_RETAIN = 7;
    public static final String UTF_8 = "UTF-8";
    /**cookie 登录*/
    public static final String COOKIE_KEY_LOGIN = "_login_";
    /**cookie 拼接符*/
    public static final String SEPARATOR = "&";
    /**cookie 有效期*/
    public static final int SECONDS_PER_HOUR = 24 * 60;

    public static final String APP_NAME = "appName";
    public static final String ENV = "env";
    public static final String HOST = "host";
    public static final String TRACE_ID = "traceId";
    public static final String EXCEPTION_MSG = "exceptionMsg";
}
