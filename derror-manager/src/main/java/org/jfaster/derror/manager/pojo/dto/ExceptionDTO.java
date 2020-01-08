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

package org.jfaster.derror.manager.pojo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description: 异常报警dto
 * @author: Amos
 * @create: 2018-07-04 18:15
 **/
@Data
public class ExceptionDTO {
    @NotNull(message = "app config id can not null ")
    private Long appId;
    /**
     * 主机ip
     */
    @NotBlank(message = "host  can not null ")
    private String host;
    /**
     * traceId
     */
    @NotBlank(message = "traceId  can not null ")
    private String traceId;

    /***异常类缩写(都大写)*/
    @NotBlank(message = "shortName  can not null ")
    private String shortName;
    /**
     * 异常类名称
     */
    @NotBlank(message = "className  can not null ")
    private String className;
    /**
     * 从mdc中获取的值
     */
    @NotBlank(message = "mdcValue  can not null ")
    private String mdcValue;
    /**
     * 扩展值
     */
    private String ext;
    /**
     * 异常信息
     */
    @NotBlank(message = "exceptionMsg  can not null ")
    private String exceptionMsg;
    /**
     * 异常栈信息
     */
    @NotBlank(message = "content  can not null ")
    private String content;
}
