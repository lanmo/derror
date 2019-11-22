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

package org.jfaster.derror.manager.controller;

import org.jfaster.derror.manager.pojo.dto.dingding.ExceptionLogDTO;
import org.jfaster.derror.manager.service.IExceptionLogService;
import org.jfaster.derror.manager.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 异常报警
 * @author: Amos
 * @create: 2018-07-13 15:56
 **/
@RestController
@RequestMapping("/exception/log")
public class ExceptionLogController {

    @Autowired
    private IExceptionLogService exceptionLogService;

    /**
     * 查询异常
     */
    @RequestMapping("/query")
    public ApiResponse query(ExceptionLogDTO exceptionLogDTO) {
        return ApiResponse.success(exceptionLogService.query(exceptionLogDTO));
    }

    /**
     * 统计异常
     */
    @RequestMapping("/statistics")
    public ApiResponse statistics() {
        return ApiResponse.success(exceptionLogService.statistics());
    }

}
