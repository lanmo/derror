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

import com.alibaba.fastjson.JSON;
import org.jfaster.derror.manager.pojo.dto.AppClientConfigDTO;
import org.jfaster.derror.manager.pojo.vo.RemoteConfigVO;
import org.jfaster.derror.manager.service.IRemoteConfigService;
import org.jfaster.derror.manager.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangnan
 */
@RestController
@RequestMapping("/derror")
public class RemoteConfigController {

    @Autowired
    private IRemoteConfigService remoteConfigService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 客户端获取配置信息
     *
     * @return
     */
    @GetMapping("/app/getConfig")
    public ApiResponse getConfig(AppClientConfigDTO configDTO) {
        logger.info("请求参数:config:{}", JSON.toJSONString(configDTO));
        RemoteConfigVO configVO = remoteConfigService.getConfig(configDTO);
        return ApiResponse.success(configVO);
    }
}
