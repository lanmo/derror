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

import org.jfaster.derror.manager.pojo.dto.AlarmMailConfigDTO;
import org.jfaster.derror.manager.service.IAlarmMailService;
import org.jfaster.derror.manager.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 邮件报警信息配置
 * @author: Amos
 * @create: 2018-07-13 16:41
 **/
@RestController
@RequestMapping("/alarm/mail")
public class AlarmMailController {


    @Autowired
    private IAlarmMailService alarmMailService;

    /**
     * 获取配置 list
     */
    @GetMapping("/query")
    public ApiResponse queryAlarmMails(AlarmMailConfigDTO alarmMailConfigDTO) {
        return ApiResponse.success(alarmMailService.queryAlarmMails(alarmMailConfigDTO));
    }

    /**
     * 获取配置 单个对象
     */
    @PostMapping("/query")
    public ApiResponse queryAlarmMail(@RequestBody AlarmMailConfigDTO alarmMailConfigDTO) {
        return ApiResponse.success(alarmMailService.queryAlarmMail(alarmMailConfigDTO));
    }



    /**
     * 添加修改
     */
    @PostMapping("/addAndUpdate")
    public ApiResponse addAndUpdate(@RequestBody AlarmMailConfigDTO alarmMailConfigDTO) {
        return ApiResponse.success(alarmMailService.addAndUpdate(alarmMailConfigDTO));
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public ApiResponse delete(@RequestBody AlarmMailConfigDTO alarmMailConfigDTO) {
        return ApiResponse.success(alarmMailService.delete(alarmMailConfigDTO));
    }

}
