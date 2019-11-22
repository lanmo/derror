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
 * @author yangnan
 */
@Data
public class AppConfigVO {
    private String appName;
    private Long id;
    /**异常报警频次 N/1分钟*/
    private Integer frequency;
    /**默认1分钟 单位s*/
    private Integer frequencyTime;
    /**1表示开启 0表示未开启*/
    private Integer status;
}
