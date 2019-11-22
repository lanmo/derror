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

import org.jfaster.derror.manager.pojo.dto.ModeConfigDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmGroupConfigVO;
import org.jfaster.derror.manager.pojo.vo.AlarmModeConfigVO;

import java.util.List;

/**
 * @author yangnan
 */
public interface IModeConfigService {

    /**
     * 添加或者修改
     * @param configDTO
     * @return
     */
    Integer addAndUpdate(ModeConfigDTO configDTO);

    /**
     * 获取配置
     * @param configDTO
     * @return
     */
    AlarmModeConfigVO getModeConfig(ModeConfigDTO configDTO);

    /**
     * 加载配置
     * @return
     * @param configDTO
     */
    List<AlarmModeConfigVO> load(ModeConfigDTO configDTO);

    /**
     * 获取报警组
     *
     * @return
     */
    List<AlarmGroupConfigVO> getAlarmGroup();

    /**
     * 根据名称获取配置
     *
     * @param configDTO
     * @return
     */
    Integer existed(ModeConfigDTO configDTO);
}
