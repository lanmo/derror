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

import org.jfaster.derror.manager.pojo.dto.AlarmGroupDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmGroupConfigVO;

import java.util.List;

/**
 * @author yangnan
 */
public interface IAlarmGroupService {

    /**
     * 获取配置
     * @return
     */
    List<AlarmGroupConfigVO> load();

    /**
     * 获取单个配置
     *
     * @param groupDTO
     * @return
     */
    AlarmGroupConfigVO getGroup(AlarmGroupDTO groupDTO);

    /**
     * 添加更新
     * @param groupDTO
     * @return
     */
    Integer addAndUpdate(AlarmGroupDTO groupDTO);

    /**
     * 删除
     * @param groupDTO
     * @return
     */
    Integer delete(AlarmGroupDTO groupDTO);

    /**
     * 是否存在
     * @param groupDTO
     * @return
     */
    Integer existed(AlarmGroupDTO groupDTO);
}
