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

package org.jfaster.derror.remote.config;

import lombok.Data;

/**
 * @author yangnan
 */
@Data
public class ExceptionLog {
    private Long appId;
    /**主机ip*/
    private String host;
    /**traceId*/
    private String traceId;
    /***异常类缩写(都大写)*/
    private String shortName;
    /**异常类名称*/
    private String className;
    /**从mdc中获取的值*/
    private String mdcValue;
    /**扩展值*/
    private String ext;
    /**异常信息*/
    private String exceptionMsg;
    /**异常栈信息*/
    private String content;
    /**环境标识*/
    private String env;
}
