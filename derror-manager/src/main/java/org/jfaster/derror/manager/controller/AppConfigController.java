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

import org.jfaster.derror.manager.pojo.dto.AppConfigDTO;
import org.jfaster.derror.manager.pojo.vo.AppConfigurationVO;
import org.jfaster.derror.manager.service.IAppConfigService;
import org.jfaster.derror.manager.utils.ApiResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangnan
 * 应用配置中心
 */
@RestController
@RequestMapping("/appConfig")
public class AppConfigController {
    @Autowired
    private IAppConfigService configService;

    /**
     * 加载应用配置
     * @return
     */
    @PostMapping("/addAndUpdate")
    public ApiResponse add(@RequestBody AppConfigDTO configDTO) {
        return ApiResponse.success(configService.addAndUpdate(configDTO));
    }

    /**
     * 加载应用配置
     * @return
     */
    @PostMapping("/getAppConfig")
    public ApiResponse getAppConfig(@RequestBody AppConfigDTO configDTO) {
        return ApiResponse.success(configService.getAppConfig(configDTO));
    }

    /**
     * 加载应用配置
     * @return
     */
    @GetMapping("/load")
    public ApiResponse load(AppConfigDTO appConfigDTO) {
        return ApiResponse.success(configService.load(appConfigDTO));
    }

    /**
     * 修改状态
     * @param configDTO
     * @return
     */
    @PostMapping("/changeStatus")
    public ApiResponse changeStatus(@RequestBody AppConfigDTO configDTO) {
        return ApiResponse.success(configService.changeStatus(configDTO));
    }

    /**
     * 判断引用名是否存在
     * @return
     */
    @PostMapping("/existed")
    public ApiResponse existed(@RequestBody AppConfigDTO configDTO) {
        return ApiResponse.success(configService.existed(configDTO));
    }

    /**
     * 获取授权的appId
     * @return
     */
    @GetMapping("/getAuthApps")
    public ApiResponse getAuthApps() {
        List<AppConfigurationVO> configurationVOS = configService.getAuthApps();
        return ApiResponse.success(configurationVOS);
    }
    /**
     * 加载应用配置
     * @return
     */
    @GetMapping("/load/ProjectId")
    public ApiResponse load() {
        return ApiResponse.success(configService.getConfigByProjectId());
    }

}
