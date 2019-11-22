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

import org.jfaster.derror.manager.pojo.dto.AlarmGroupDTO;
import org.jfaster.derror.manager.service.IAlarmGroupService;
import org.jfaster.derror.manager.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangnan
 */
@RequestMapping("/group")
@RestController
public class AlarmGroupController {

    @Autowired
    private IAlarmGroupService alarmGroupService;

    /**
     * 加载配置
     * @return
     */
    @GetMapping("/load")
    public ApiResponse load() {
        return ApiResponse.success(alarmGroupService.load());
    }

    /**
     * 获取单个配置
     * @return
     */
    @PostMapping("/getGroup")
    public ApiResponse getGroup(@RequestBody AlarmGroupDTO groupDTO) {
        return ApiResponse.success(alarmGroupService.getGroup(groupDTO));
    }

    /**
     * 添加修改
     * @return
     */
    @PostMapping("/addAndUpdate")
    public ApiResponse addAndUpdate(@RequestBody AlarmGroupDTO groupDTO) {
        return ApiResponse.success(alarmGroupService.addAndUpdate(groupDTO));
    }

    /**
     * 删除
     * @return
     */
    @PostMapping("/delete")
    public ApiResponse delete(@RequestBody AlarmGroupDTO groupDTO) {
        return ApiResponse.success(alarmGroupService.delete(groupDTO));
    }

    /**
     * 判断是否存在
     * @return
     */
    @PostMapping("/existed")
    public ApiResponse existed(@RequestBody AlarmGroupDTO groupDTO) {
        return ApiResponse.success(alarmGroupService.existed(groupDTO));
    }
}
