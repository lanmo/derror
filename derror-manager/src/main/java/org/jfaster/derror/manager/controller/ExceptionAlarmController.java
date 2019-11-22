/*
 *
 *  * Copyright 2018 org.jfaster.derror.
 *  *   <p>
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  * </p>
 *
 */

package org.jfaster.derror.manager.controller;

import org.jfaster.derror.manager.pojo.dto.ExceptionDTO;
import org.jfaster.derror.manager.service.IExceptionAlarmService;
import org.jfaster.derror.manager.utils.ApiResponse;
import org.jfaster.derror.manager.worker.ExceptionAlarmWorker;
import org.jfaster.derror.manager.worker.WorkerManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 异常报警
 * @author: Amos
 * @create: 2018-07-04 18:09
 **/
@RestController
@RequestMapping("/derror/exception")
@Validated
public class ExceptionAlarmController {


    @Autowired
    private IExceptionAlarmService exceptionAlarmService;

    @RequestMapping("/alarm")
    public ApiResponse alarm(@RequestBody ExceptionDTO exceptionDTO) {
        WorkerManager.submit(new ExceptionAlarmWorker(exceptionDTO));
        //exceptionAlarmService.alarm(exceptionDTO);
        return ApiResponse.success();
    }
}




