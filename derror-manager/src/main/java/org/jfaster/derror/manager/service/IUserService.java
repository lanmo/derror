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
import org.jfaster.derror.manager.pojo.dto.UserProjectDTO;
import org.jfaster.derror.manager.pojo.vo.UserProjectRoleVO;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author yangnan
 */
@Service
public interface IUserService {

    /**
     * 根据用户名获取用户信息
     *
     * @param userName
     * @return
     */
    UserBO getByUserName(String userName);

    /**
     * 添加用户
     * @param userName
     * @return
     */
    UserBO addUser(String userName);

    /**
     * 获取当前登录用户的权限
     * @return
     */
    int getAuth();

    /**
     * 获取所有用户
     *
     * @return
     */
    List<UserBO> load();

    /**
     * 获取用户拥有的权限列表
     * @param projectDTO
     * @return
     */
    List<UserProjectRoleVO> authLoad(UserProjectDTO projectDTO);
}
