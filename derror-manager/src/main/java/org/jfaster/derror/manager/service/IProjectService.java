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

import org.jfaster.derror.manager.pojo.domain.ProjectDO;
import org.jfaster.derror.manager.pojo.dto.ProjectDTO;
import org.jfaster.derror.manager.pojo.vo.ProjectVO;

import java.util.List;

/**
 * @author yangnan
 */
public interface IProjectService {

    /**
     * 加载项目列表
     *
     * @return
     */
    List<ProjectVO> load();

    /**
     * 添加项目
     *
     * @param projectDTO
     * @return
     */
    int add(ProjectDTO projectDTO);

    /**
     * 根据名称获取项目
     *
     * @param projectDTO
     * @return
     */
    int existed(ProjectDTO projectDTO);

    /**
     * 返回当前用户授权的项目
     * @return
     */
    List<ProjectVO> getAuthProjects();

    /**
     * 获取全部
     * @return
     */
    List<ProjectDO> getAll();
}
