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


package org.jfaster.derror.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: derror
 * @description: 开关枚举
 * @author: Amos.Wxz
 * @create: 2018-07-05 11:20
 **/
@AllArgsConstructor
public enum SwitchEnum {
    /**
     * 1：开启
     */
    OPEN(1),
    /**
     * 0：关闭
     */
    CLOSE(0);
    @Getter
    private Integer switchType;
}
