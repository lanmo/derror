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

import org.jfaster.derror.manager.enums.ErrorCodeEnum;
import org.jfaster.derror.manager.pojo.dto.FilterExceptionDTO;
import org.jfaster.derror.manager.service.IFilterExceptionService;
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
@RestController
@RequestMapping("/filter/exception")
public class FilterExceptionController {

    @Autowired
    private IFilterExceptionService filterExceptionService;

    /**
     * 加载列表
     * @return
     */
    @GetMapping("/load")
    public ApiResponse load(FilterExceptionDTO exceptionDTO) {
        return ApiResponse.success(filterExceptionService.load(exceptionDTO));
    }

    /**
     * 添加或者修改
     * @return
     */
    @PostMapping("/addAndUpdate")
    public ApiResponse addAndUpdate(@RequestBody FilterExceptionDTO exceptionDTO) {
        if (exceptionDTO.getAppId() == null || exceptionDTO.getAppId() == 0) {
            return ApiResponse.error(ErrorCodeEnum.APP_NOT_FOUND.getMsg());
        }
        return ApiResponse.success(filterExceptionService.addAndUpdate(exceptionDTO));
    }

    /**
     * 获取异常
     * @return
     */
    @PostMapping("/getException")
    public ApiResponse getException(@RequestBody FilterExceptionDTO exceptionDTO) {
        return ApiResponse.success(filterExceptionService.getException(exceptionDTO));
    }

    /**
     * 删除
     * @return
     */
    @PostMapping("/delete")
    public ApiResponse delete(@RequestBody FilterExceptionDTO exceptionDTO) {
        return ApiResponse.success(filterExceptionService.delete(exceptionDTO));
    }

    /**
     * 获取应用配置
     * @return
     */
    @GetMapping("/getApps")
    public ApiResponse getApps() {
        return ApiResponse.success(filterExceptionService.getApps());
    }

    /**
     * 改状态
     * @return
     */
    @PostMapping("/changeStatus")
    public ApiResponse changeStatus(@RequestBody FilterExceptionDTO exceptionDTO) {
        return ApiResponse.success(filterExceptionService.changeStatus(exceptionDTO));
    }
}
