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

package org.jfaster.derror.manager.service;

import org.jfaster.derror.manager.pojo.dto.AppConfigDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmModeConfigVO;
import org.jfaster.derror.manager.pojo.vo.AppConfigurationVO;

import java.util.List;

/**
 * @author yangnan
 */
public interface IAppConfigService {

    /**
     * 加载配置信息
     *
     * @return
     * @param appConfigDTO
     */
    List<AppConfigurationVO> load(AppConfigDTO appConfigDTO);

    /**
     * 开启或者关闭
     *
     * @param configDTO
     * @return
     */
    Integer changeStatus(AppConfigDTO configDTO);

    /**
     * 获取appConfig
     *
     * @param configDTO
     * @return
     */
    AppConfigurationVO getAppConfig(AppConfigDTO configDTO);

    /**
     * 添加或修改
     *
     * @param configDTO
     * @return
     */
    Integer addAndUpdate(AppConfigDTO configDTO);

    /**
     * 获取报警方式
     *
     * @return
     */
    List<AlarmModeConfigVO> getMode();

    /**
     * 判断应用名是否存在
     * @param configDTO
     * @return
     */
    Integer existed(AppConfigDTO configDTO);

    /**
     * 获取授权的app
     * @return
     */
    List<AppConfigurationVO> getAuthApps();
    /**
     * 根据项目id查询应用
     * @return
     */
    List<AppConfigurationVO> getConfigByProjectId();
}
