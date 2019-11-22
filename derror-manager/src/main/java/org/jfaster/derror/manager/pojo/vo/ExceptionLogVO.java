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

package org.jfaster.derror.manager.pojo.vo;

import lombok.Data;

/**
 * @description:
 * @author: Amos
 * @create: 2018-07-13 16:12
 **/
@Data
public class ExceptionLogVO {
    private Long id;
    private Long appId;
    private String host;
    private String traceId;
    private String shortName;
    private String className;
    private String mdcValue;
    private String ext;
    private String exceptionMsg;
    private String content;
    private String createDate;
    private String updateDate;
    private String env;
    private String appName;
}
