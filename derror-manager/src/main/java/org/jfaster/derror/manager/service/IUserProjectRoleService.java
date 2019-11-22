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

import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.bo.UserProjectRoleBO;
import org.jfaster.derror.manager.pojo.domain.UserProjectRoleDO;
import org.jfaster.derror.manager.pojo.dto.UserProjectDTO;

import java.util.List;

/**
 * @author yangnan
 */
public interface IUserProjectRoleService {

    /**
     * 获取用户拥有的权限
     *
     * @param userId
     * @return
     */
    List<UserProjectRoleDO> getProjectAppRole(Long userId);

    /**
     * 添加或者保存全选信息
     *
     * @param projectDTO
     * @return
     */
    int addAndUpdate(UserProjectDTO projectDTO);

    /**
     * 获取项目权限
     *
     * @param id
     * @param projectId
     * @return
     */
    UserProjectRoleDO getProjectAppRole(UserBO id, Integer projectId);

    /**
     * 获取用户授权的appId
     *
     * @param userBO
     * @param projectId
     * @return
     */
    UserProjectRoleBO getAuthAppIds(UserBO userBO, Integer projectId);

}
