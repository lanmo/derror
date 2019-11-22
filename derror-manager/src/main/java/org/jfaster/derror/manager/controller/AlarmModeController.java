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

import org.jfaster.derror.manager.pojo.dto.ModeConfigDTO;
import org.jfaster.derror.manager.service.IModeConfigService;
import org.jfaster.derror.manager.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangnan
 * 报警方式配置
 */
@RestController
@RequestMapping("/modeConfig/")
public class AlarmModeController {

    @Autowired
    private IModeConfigService modeConfigService;

    /**
     * 加载应用配置
     * @return
     */
    @PostMapping("/addAndUpdate")
    public ApiResponse add(@RequestBody ModeConfigDTO configDTO) {
        return ApiResponse.success(modeConfigService.addAndUpdate(configDTO));
    }

    /**
     * 加载应用配置
     * @return
     */
    @PostMapping("/getModeConfig")
    public ApiResponse getModeConfig(@RequestBody ModeConfigDTO configDTO) {
        return ApiResponse.success(modeConfigService.getModeConfig(configDTO));
    }

    /**
     * 判断引用名是否存在
     * @return
     */
    @PostMapping("/existed")
    public ApiResponse existed(@RequestBody ModeConfigDTO configDTO) {
        return ApiResponse.success(modeConfigService.existed(configDTO));
    }

    /**
     * 加载应用配置
     * @return
     */
    @GetMapping("/load")
    public ApiResponse load(ModeConfigDTO configDTO) {
        return ApiResponse.success(modeConfigService.load(configDTO));
    }

    @GetMapping("/getAlarmGroup")
    public ApiResponse getAlarmGroup() {
        return ApiResponse.success(modeConfigService.getAlarmGroup());
    }
}
