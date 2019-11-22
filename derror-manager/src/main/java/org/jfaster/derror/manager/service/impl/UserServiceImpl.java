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

package org.jfaster.derror.manager.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jfaster.derror.manager.dao.AppConfigDAO;
import org.jfaster.derror.manager.dao.ProjectDAO;
import org.jfaster.derror.manager.dao.UserDAO;
import org.jfaster.derror.manager.dao.UserProjectRoleDAO;
import org.jfaster.derror.manager.enums.RoleEnum;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.domain.AppConfigDO;
import org.jfaster.derror.manager.pojo.domain.ProjectDO;
import org.jfaster.derror.manager.pojo.domain.UserDO;
import org.jfaster.derror.manager.pojo.domain.UserProjectRoleDO;
import org.jfaster.derror.manager.pojo.dto.UserProjectDTO;
import org.jfaster.derror.manager.pojo.vo.AppConfigurationVO;
import org.jfaster.derror.manager.pojo.vo.UserProjectRoleVO;
import org.jfaster.derror.manager.service.IUserService;
import org.jfaster.derror.manager.utils.AppUtil;
import org.jfaster.derror.manager.utils.RuntimeContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangnan
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserProjectRoleDAO projectRoleDAO;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private AppConfigDAO appConfigDAO;

    @Override
    public UserBO getByUserName(String userName) {
        UserDO userDO = userDAO.getByUserName(userName);
        if (userDO == null) {
            return null;
        }
        UserBO userBO = new UserBO();
        BeanUtils.copyProperties(userDO, userBO);
        return userBO;
    }

    @Override
    public UserBO addUser(String userName) {
        UserDO userDO = userDAO.getByUserName(userName);
        if (userDO != null) {
            return null;
        }

        userDO = new UserDO(userName);
        int id = userDAO.addAndReturnGeneratedId(userDO);
        userDO.setId(new Long(id));
        UserBO userBO = new UserBO();
        BeanUtils.copyProperties(userDO, userBO);
        return userBO;
    }

    @Override
    public int getAuth() {
        UserBO userBO = RuntimeContext.getCurrUser();
        return userBO.getRoleId();
    }

    @Override
    public List<UserBO> load() {

        UserBO user = RuntimeContext.getCurrUser();
        if (!RoleEnum.canOperator(user.getRoleId())) {
            return Collections.emptyList();
        }
        List<UserDO> userDOS = userDAO.getAll();
        List<UserBO> users = Lists.newArrayListWithCapacity(userDOS.size());
        userDOS.stream().filter(f-> !f.getId().equals(user.getId())).forEach(r -> {
            UserBO userBO = new UserBO();
            BeanUtils.copyProperties(r, userBO);
            users.add(userBO);
        });
        return users;
    }

    @Override
    public List<UserProjectRoleVO> authLoad(UserProjectDTO projectDTO) {
        UserBO user = RuntimeContext.getCurrUser();
        if (!user.isProjectManager()) {
            return Collections.emptyList();
        }
        List<ProjectDO> projects = null;
        List<Integer> authorizedProjects = user.getAuthorizedProjects();
        if (user.isSuperAdmin()) {
            projects = projectDAO.getAll();
        } else if (CollectionUtils.isNotEmpty(authorizedProjects)){
            projects = projectDAO.getMulti(authorizedProjects);
        }
        if (CollectionUtils.isEmpty(projects)) {
            return Collections.emptyList();
        }
        Map<Integer, ProjectDO> projectDOMap = Maps.newHashMapWithExpectedSize(projects.size());
        projects.forEach(r -> projectDOMap.put(r.getId(), r));

        List<UserProjectRoleDO> projectRoles = projectRoleDAO.getUserProjects(projectDTO.getUserId(), projectDOMap.keySet());
        Map<Integer, UserProjectRoleDO> projectRoleDOMap = Maps.newHashMapWithExpectedSize(projectRoles.size());
        projectRoles.stream().forEach(r -> projectRoleDOMap.put(r.getProjectId(), r));

        List<UserProjectRoleVO> vos = Lists.newArrayListWithCapacity(projects.size());

        UserDO userDO = userDAO.getOne(projectDTO.getUserId());
        boolean superAdmin = RoleEnum.SUPER_ADMIN.getRoleId() == userDO.getRoleId();

        //获取应用权限列表
        List<AppConfigDO> appConfigDOS = appConfigDAO.getApp(projectDOMap.keySet());
        Map<Integer, List<AppConfigurationVO>> appConfigDOMap = Maps.newHashMapWithExpectedSize(appConfigDOS.size());
        appConfigDOS.forEach(r -> {
            List<AppConfigurationVO> voList = appConfigDOMap.get(r.getProjectId());
            if (voList == null) {
                voList = Lists.newArrayList();
                appConfigDOMap.put(r.getProjectId(), voList);
            }
            AppConfigurationVO vo = new AppConfigurationVO();
            vo.setAppName(r.getAppName());
            vo.setId(r.getId());
            vo.setProjectId(r.getProjectId());
            UserProjectRoleDO roleDO = projectRoleDOMap.get(r.getProjectId());
            if (roleDO == null) {
                if (superAdmin) {
                    vo.setChecked(false);
                } else {
                    List<Long> appIds = AppUtil.getAppIds(roleDO.getAuthorizedAppIds());
                    if (appIds.contains(r.getId())) {
                        vo.setChecked(true);
                    }
                }
            } else {
                vo.setChecked(false);
            }
            voList.add(vo);
        });

        projectDOMap.forEach((k, v) -> {
            UserProjectRoleVO roleVO = new UserProjectRoleVO();
            if (projectRoleDOMap.containsKey(k)) {
                UserProjectRoleDO roleDO = projectRoleDOMap.get(k);
                BeanUtils.copyProperties(roleDO, roleVO);
            } else {
                roleVO.setId(v.getId());
                roleVO.setProjectId(k);
                roleVO.setRoleId(RoleEnum.NONE.getRoleId());
                roleVO.setUserId(projectDTO.getUserId());
            }

            if (superAdmin) {
                roleVO.setRoleId(RoleEnum.PROJECT_ADMIN.getRoleId());
            }
            roleVO.setAuthorizedApps(appConfigDOMap.get(k));
            roleVO.setProjectName(v.getName());
            roleVO.setRoles(RoleEnum.getRole());
            vos.add(roleVO);
        });

        return vos;
    }
}
