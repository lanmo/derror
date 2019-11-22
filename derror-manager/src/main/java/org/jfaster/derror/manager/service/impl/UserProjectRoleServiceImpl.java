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

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jfaster.derror.manager.dao.AppConfigDAO;
import org.jfaster.derror.manager.dao.UserDAO;
import org.jfaster.derror.manager.dao.UserProjectRoleDAO;
import org.jfaster.derror.manager.enums.RoleEnum;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.bo.UserProjectRoleBO;
import org.jfaster.derror.manager.pojo.domain.AppConfigDO;
import org.jfaster.derror.manager.pojo.domain.UserDO;
import org.jfaster.derror.manager.pojo.domain.UserProjectRoleDO;
import org.jfaster.derror.manager.pojo.dto.UserProjectDTO;
import org.jfaster.derror.manager.service.IUserProjectRoleService;
import org.jfaster.derror.manager.utils.AppUtil;
import org.jfaster.derror.manager.utils.RuntimeContext;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangnan
 */
@Service
public class UserProjectRoleServiceImpl implements IUserProjectRoleService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserProjectRoleDAO userProjectRoleDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private AppConfigDAO appConfigDAO;

    @Override
    public List<UserProjectRoleDO> getProjectAppRole(Long userId) {
        return userProjectRoleDAO.getProjectAppRole(userId);
    }

    @Override
    public int addAndUpdate(UserProjectDTO projectDTO) {

        UserBO userBO = RuntimeContext.getCurrUser();
        if (!userBO.isProjectManager()) {
            logger.warn("该登录用户没有项目管理权限 userBO:{}", JSON.toJSONString(userBO));
            return 0;
        }

        long operateId = projectDTO.getUserId();
        String selectApp = projectDTO.getSelectApp();
        String selectProject = projectDTO.getSelectProject();

        Set<Integer> managerProject = processProject(operateId, selectProject);
        processApp(operateId, selectApp, managerProject);

        return 1;
    }

    @Override
    public UserProjectRoleDO getProjectAppRole(UserBO userBO, Integer projectId) {
        if (userBO.isSuperAdmin()) {
            UserProjectRoleDO roleDO = new UserProjectRoleDO();
            roleDO.setRoleId(userBO.getRoleId());
            roleDO.setUserId(userBO.getId());
            roleDO.setProjectId(projectId);
            return roleDO;
        }
        return userProjectRoleDAO.getUserProject(userBO.getId(), projectId);
    }

    @Override
    public UserProjectRoleBO getAuthAppIds(UserBO userBO, Integer projectId) {
        UserProjectRoleBO roleBO = new UserProjectRoleBO();
        UserProjectRoleDO roleDO = userProjectRoleDAO.getUserProject(userBO.getId(), projectId);
        if (roleDO == null && !userBO.isSuperAdmin()) {
            return roleBO;
        }
        if (userBO.isSuperAdmin()) {
            roleBO.setRoleId(userBO.getRoleId());
        } else {
            roleBO.setRoleId(roleDO.getRoleId());
        }
        //项目管理员
        if (userBO.isSuperAdmin() || RoleEnum.canOperator(roleDO.getRoleId())) {
            List<AppConfigDO> appConfigDOS = appConfigDAO.getProjectApps(projectId);
            roleBO.setAppIds(appConfigDOS.stream().map(AppConfigDO::getId).collect(Collectors.toList()));
            return roleBO;
        }
        roleBO.setAppIds(AppUtil.getAppIds(roleDO.getAuthorizedAppIds()));
        return roleBO;
    }

    /**
     * 处理应用权限
     * @param operateId
     * @param selectApp
     * @param managerProject
     */
    private void processApp(long operateId, String selectApp, Set<Integer> managerProject) {
        String[] selectApps = StringUtils.split(selectApp, ",");
        if (selectApps == null || selectApps.length <=0) {
            return;
        }

        Map<Integer, Set<Integer>> appMap = Maps.newHashMap();
        for (String app : selectApps) {
            //添加或者更新用户的权限
            String[] apps = StringUtils.split(app, "_");
            int projectId = Integer.parseInt(apps[0]);
            int appId = Integer.parseInt(apps[1]);
            if (managerProject.contains(projectId)) {
                continue;
            }
            Set<Integer> appIds = appMap.get(projectId);
            if (appIds == null) {
                appIds = Sets.newHashSet();
                appMap.put(projectId, appIds);
            }
            appIds.add(appId);
        }

        appMap.forEach((k, v) -> {
            UserProjectRoleDO roleDO = userProjectRoleDAO.getUserProject(operateId, k);
            if (roleDO == null) {
                roleDO = new UserProjectRoleDO();
                roleDO.setRoleId(RoleEnum.VIEWER.getRoleId());
                roleDO.setProjectId(k);
                roleDO.setUpdateDate(new Date());
                roleDO.setCreateDate(new Date());
                roleDO.setAuthorizedAppIds(StringUtils.join(v, ","));
                roleDO.setUserId(operateId);
                userProjectRoleDAO.add(roleDO);
            } else {
                roleDO.setAuthorizedAppIds(StringUtils.join(v, ","));
                roleDO.setUpdateDate(new Date());
                roleDO.setRoleId(RoleEnum.VIEWER.getRoleId());
                userProjectRoleDAO.update(roleDO);
            }
        });
    }

    /**
     * 处理项目权限
     * @param operateId
     * @param selectProject
     */
    private Set<Integer> processProject(long operateId, String selectProject) {
        String[] selectProjects = StringUtils.split(selectProject, ",");
        if (selectProjects == null || selectProjects.length <=0) {
            return Collections.emptySet();
        }

        Set<Integer> projectSet = Sets.newHashSet();
        for (String sp : selectProjects) {
            //添加或者更新用户的权限
            String[] projects = StringUtils.split(sp, "_");
            int projectId = Integer.parseInt(projects[0]);
            int roleId = Integer.parseInt(projects[1]);

            if (roleId == RoleEnum.NONE.getRoleId()) {
                userProjectRoleDAO.deleteUserRole(operateId, projectId);
                projectSet.add(projectId);
                continue;
            }

            //项目管理员
            if (RoleEnum.canOperator(roleId)) {
                projectSet.add(projectId);
                UserProjectRoleDO roleDO = userProjectRoleDAO.getUserProject(operateId, projectId);
                if (roleDO == null) {
                    roleDO = new UserProjectRoleDO();
                    roleDO.setRoleId(roleId);
                    roleDO.setProjectId(projectId);
                    roleDO.setUpdateDate(new Date());
                    roleDO.setCreateDate(new Date());
                    roleDO.setAuthorizedAppIds("");
                    roleDO.setUserId(operateId);
                    userProjectRoleDAO.add(roleDO);
                } else {
                    roleDO.setAuthorizedAppIds("");
                    roleDO.setRoleId(roleId);
                    roleDO.setUpdateDate(new Date());
                    userProjectRoleDAO.update(roleDO);
                }
                UserDO userDO = new UserDO();
                userDO.setId(operateId);
                userDO.setRoleId(roleId);
                userDO.setUpdateDate(new Date());
                userDAO.update(userDO);
                continue;
            }
        }

        return projectSet;
    }
}
