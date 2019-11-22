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
import com.google.common.collect.Sets;
import org.jfaster.derror.manager.constant.CommonConstant;
import org.jfaster.derror.manager.dao.AlarmModeConfigDAO;
import org.jfaster.derror.manager.dao.AppConfigDAO;
import org.jfaster.derror.manager.enums.RoleEnum;
import org.jfaster.derror.manager.enums.SwitchEnum;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.bo.UserProjectRoleBO;
import org.jfaster.derror.manager.pojo.domain.AlarmModeConfigDO;
import org.jfaster.derror.manager.pojo.domain.AppConfigDO;
import org.jfaster.derror.manager.pojo.domain.UserProjectRoleDO;
import org.jfaster.derror.manager.pojo.dto.AppConfigDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmModeConfigVO;
import org.jfaster.derror.manager.pojo.vo.AppConfigurationVO;
import org.jfaster.derror.manager.service.IAppConfigService;
import org.jfaster.derror.manager.service.IUserProjectRoleService;
import org.jfaster.derror.manager.utils.AppUtil;
import org.jfaster.derror.manager.utils.DateUtil;
import org.jfaster.derror.manager.utils.RuntimeContext;
import org.jfaster.derror.manager.utils.TokenUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangnan
 */
@Service
public class AppConfigServiceImpl implements IAppConfigService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppConfigDAO appConfigDAO;
    @Autowired
    private AlarmModeConfigDAO alarmModeConfigDAO;
    @Autowired
    private IUserProjectRoleService projectRoleService;

    @Override
    public List<AppConfigurationVO> load(AppConfigDTO appConfigDTO) {
        if (appConfigDTO.getProjectId() == null || appConfigDTO.getProjectId() == 0) {
            return Collections.emptyList();
        }

        UserBO userBO = RuntimeContext.getCurrUser();

        UserProjectRoleDO projectRoleDO = projectRoleService.getProjectAppRole(userBO, appConfigDTO.getProjectId());
        if (projectRoleDO == null) {
            return Collections.emptyList();
        }

        UserProjectRoleBO roleBO = projectRoleService.getAuthAppIds(userBO, appConfigDTO.getProjectId());
        if (CollectionUtils.isEmpty(roleBO.getAppIds())) {
            return Collections.emptyList();
        }

        List<AppConfigDO> configDOS = appConfigDAO.getMulti(roleBO.getAppIds());
        List<AppConfigurationVO> vos = Lists.newArrayListWithCapacity(configDOS.size());
        configDOS.stream().forEach(r -> {
            AppConfigurationVO configurationVO = new AppConfigurationVO();
            BeanUtils.copyProperties(r, configurationVO);
            configurationVO.setCreateDate(DateUtil.format(r.getCreateDate(), DateUtil.DEFAULT_TIME));
            configurationVO.setUpdateDate(DateUtil.format(r.getUpdateDate(), DateUtil.DEFAULT_TIME));
            Integer status = r.getStatus() == null ? SwitchEnum.CLOSE.getSwitchType() : r.getStatus();
            configurationVO.setStatus(status);
            if (userBO.isSuperAdmin()) {
                configurationVO.setRoleId(userBO.getRoleId());
            } else {
                configurationVO.setRoleId(projectRoleDO.getRoleId());
            }
            vos.add(configurationVO);
        });

        return vos;
    }

    @Override
    public Integer changeStatus(AppConfigDTO configDTO) {
        int newStatus = SwitchEnum.CLOSE.getSwitchType();
        if (SwitchEnum.CLOSE.getSwitchType().equals(configDTO.getStatus())) {
            newStatus = SwitchEnum.OPEN.getSwitchType();
        }
        if (SwitchEnum.OPEN.getSwitchType().equals(configDTO.getStatus())) {
            newStatus = SwitchEnum.CLOSE.getSwitchType();
        }
        return appConfigDAO.changeStatus(configDTO.getAppName(), newStatus, configDTO.getStatus());
    }

    public Integer add(AppConfigDTO configDTO) {
        Date now = new Date();
        AppConfigDO appConfigDO = new AppConfigDO();
        appConfigDO.setAppName(configDTO.getAppName());
        appConfigDO.setAppKey(TokenUtil.getToken());
        appConfigDO.setCreateDate(now);
        appConfigDO.setUpdateDate(now);
        appConfigDO.setFrequency(configDTO.getFrequency());
        appConfigDO.setFrequencyTime(CommonConstant.DEFAULT_FREQUENCY_TIME);
        Integer retain = configDTO.getRetain();
        if (retain == null) {
            retain = CommonConstant.DEFAULT_RETAIN;
        }
        if (retain <= 0) {
            retain = CommonConstant.DEFAULT_RETAIN;
        }
        appConfigDO.setRetain(retain);
        appConfigDO.setRemark(configDTO.getRemark());
        Integer status = configDTO.getStatus();
        if (status == null) {
            status = SwitchEnum.CLOSE.getSwitchType();
        }
        appConfigDO.setStatus(status);
        appConfigDO.setProjectId(configDTO.getProjectId());
        appConfigDAO.add(appConfigDO);
        return 1;
    }

    @Override
    public AppConfigurationVO getAppConfig(AppConfigDTO configDTO) {
        AppConfigurationVO configurationVO = new AppConfigurationVO();
        AppConfigDO configDO = appConfigDAO.getOne(configDTO.getId());
        if (configDO == null) {
            return configurationVO;
        }
        BeanUtils.copyProperties(configDO, configurationVO);
        return configurationVO;
    }

    @Override
    public Integer addAndUpdate(AppConfigDTO configDTO) {
        if (configDTO.getId() == null) {
            return add(configDTO);
        }
        return update(configDTO);
    }

    @Override
    public List<AlarmModeConfigVO> getMode() {
        List<AlarmModeConfigVO> vos = Lists.newArrayList();
        List<AlarmModeConfigDO> configDOS = alarmModeConfigDAO.getModeConfigs();
        if (CollectionUtils.isEmpty(configDOS)) {
            return vos;
        }
        configDOS.stream().forEach(r -> {
            AlarmModeConfigVO vo = new AlarmModeConfigVO();
            vo.setName(r.getName());
            vo.setId(r.getId());
            vos.add(vo);
        });

        return vos;
    }

    @Override
    public Integer existed(AppConfigDTO configDTO) {
        if (configDTO.getId() != null) {
            return 0;
        }
        if (StringUtils.isBlank(configDTO.getAppName())) {
            return 0;
        }
        return appConfigDAO.existed(configDTO.getAppName().trim());
    }

    @Override
    public List<AppConfigurationVO> getAuthApps() {
        UserBO userBO = RuntimeContext.getCurrUser();
        if (userBO.isSuperAdmin()) {
            List<AppConfigDO> appConfigDOS = appConfigDAO.getAll();
            List<AppConfigurationVO> vos = appConfigDOS.stream().map(r -> {
                AppConfigurationVO vo = new AppConfigurationVO();
                BeanUtils.copyProperties(r, vo);
                return vo;
            }).collect(Collectors.toList());
            return vos;
        }

        List<UserProjectRoleDO> roleDOS = projectRoleService.getProjectAppRole(userBO.getId());
        if (CollectionUtils.isEmpty(roleDOS)) {
            return Collections.emptyList();
        }

        List<Long> appIds = Lists.newArrayList();
        Set<Integer> projectIds = Sets.newHashSet();
        roleDOS.stream().forEach(r -> {
            if (RoleEnum.canOperator(r.getRoleId())) {
                projectIds.add(r.getProjectId());
            } else {
                appIds.addAll(AppUtil.getAppIds(r.getAuthorizedAppIds()));
            }
        });

        List<AppConfigDO> appConfigDOS = Lists.newArrayListWithCapacity(roleDOS.size());
        if (CollectionUtils.isNotEmpty(projectIds)) {
            appConfigDOS.addAll(appConfigDAO.getApp(projectIds));
        }

        if (CollectionUtils.isNotEmpty(appIds)) {
            appConfigDOS.addAll(appConfigDAO.getMulti(appIds));
        }

        return appConfigDOS.stream().map(r -> {
            AppConfigurationVO vo = new AppConfigurationVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 修改
     * @param configDTO
     * @return
     */
    private Integer update(AppConfigDTO configDTO) {
        AppConfigDO configDO = appConfigDAO.getOne(configDTO.getId());
        if (configDO == null) {
            return 0;
        }

        if (configDTO.getRetain() != null) {
            configDO.setRetain(configDTO.getRetain());
        }

        if (configDTO.getStatus() != null) {
            configDO.setStatus(configDTO.getStatus());
        }

        if (configDTO.getRemark() != null) {
            configDO.setRemark(configDTO.getRemark());
        }

        if (configDTO.getFrequency() != null) {
            configDO.setFrequency(configDTO.getFrequency());
        }

        return appConfigDAO.update(configDO);
    }
    @Override
    public List<AppConfigurationVO> getConfigByProjectId(){
        UserBO userBO = RuntimeContext.getCurrUser();
       return appConfigDAO.getConfigsByProjectIds(userBO.getAuthorizedProjects());
    }
}
