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
import org.jfaster.derror.manager.dao.ProjectDAO;
import org.jfaster.derror.manager.dao.UserProjectRoleDAO;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.domain.ProjectDO;
import org.jfaster.derror.manager.pojo.domain.UserProjectRoleDO;
import org.jfaster.derror.manager.pojo.dto.ProjectDTO;
import org.jfaster.derror.manager.pojo.vo.ProjectVO;
import org.jfaster.derror.manager.service.IProjectService;
import org.jfaster.derror.manager.utils.DateUtil;
import org.jfaster.derror.manager.utils.RuntimeContext;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangnan
 */
@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private UserProjectRoleDAO projectRoleDAO;

    @Override
    public List<ProjectVO> load() {
        UserBO userBO = RuntimeContext.getCurrUser();
        List<ProjectDO> projects = Lists.newArrayList();
        List<Integer> authorizedProjects = userBO.getAuthorizedProjects();
        if (userBO.isSuperAdmin()) {
            projects = projectDAO.getAll();
        } else if (CollectionUtils.isNotEmpty(authorizedProjects)) {
            projects = projectDAO.getMulti(authorizedProjects);
        }
        if (CollectionUtils.isEmpty(projects)) {
            return Collections.emptyList();
        }
        List<ProjectVO> projectVOS = Lists.newArrayListWithCapacity(projects.size());
        projects.stream().forEach(r -> {
            ProjectVO vo = new ProjectVO();
            BeanUtils.copyProperties(r, vo);
            vo.setCreateDate(DateUtil.format(r.getCreateDate(), DateUtil.DEFAULT_TIME));
            vo.setUpdateDate(DateUtil.format(r.getUpdateDate(), DateUtil.DEFAULT_TIME));
            projectVOS.add(vo);
        });
        return projectVOS;
    }

    @Override
    public int add(ProjectDTO projectDTO) {
        ProjectDO projectDO = projectDAO.getProjectByName(projectDTO.getName());
        if (projectDO != null) {
            return 0;
        }

        Date now = new Date();
        projectDO = new ProjectDO();
        UserBO userBO = RuntimeContext.getCurrUser();
        projectDO.setName(projectDTO.getName());
        projectDO.setCreator(userBO.getUserName());
        projectDO.setExt(projectDTO.getExt());
        projectDO.setCreateDate(now);
        projectDO.setUpdateDate(now);
        int id = projectDAO.addAndReturnGeneratedId(projectDO);
        if (id <= 0) {
            return 0;
        }

        UserProjectRoleDO roleDO = new UserProjectRoleDO();
        roleDO.setCreateDate(now);
        roleDO.setUpdateDate(now);
        roleDO.setProjectId(id);
        roleDO.setRoleId(userBO.getRoleId());
        roleDO.setUserId(userBO.getId());
        roleDO.setAuthorizedAppIds("");
        projectRoleDAO.add(roleDO);

        return 1;
    }

    @Override
    public int existed(ProjectDTO projectDTO) {
        if (StringUtils.isBlank(projectDTO.getName())) {
            return 0;
        }
        return projectDAO.existed(projectDTO.getName());
    }

    @Override
    public List<ProjectVO> getAuthProjects() {
        UserBO userBO = RuntimeContext.getCurrUser();
        List<Integer> authorizedProjects = userBO.getAuthorizedProjects();
        List<ProjectVO> vos = Lists.newArrayList();

        List<ProjectDO> projectDOS = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(authorizedProjects)) {
            projectDOS = projectDAO.getMulti(authorizedProjects);
        }
        projectDOS.stream().forEach(r -> {
            ProjectVO vo = new ProjectVO();
            BeanUtils.copyProperties(r, vo);
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public List<ProjectDO> getAll() {
        return projectDAO.getAll();
    }
}
