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
import org.jfaster.derror.manager.dao.AlarmGroupConfigDAO;
import org.jfaster.derror.manager.dao.AlarmModeConfigDAO;
import org.jfaster.derror.manager.dao.AppConfigDAO;
import org.jfaster.derror.manager.enums.AlarmModeEnum;
import org.jfaster.derror.manager.enums.SwitchEnum;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.bo.UserProjectRoleBO;
import org.jfaster.derror.manager.pojo.domain.AlarmGroupConfigDO;
import org.jfaster.derror.manager.pojo.domain.AlarmModeConfigDO;
import org.jfaster.derror.manager.pojo.dto.ModeConfigDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmGroupConfigVO;
import org.jfaster.derror.manager.pojo.vo.AlarmModeConfigVO;
import org.jfaster.derror.manager.service.IModeConfigService;
import org.jfaster.derror.manager.service.IUserProjectRoleService;
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
public class ModeConfigServiceImpl implements IModeConfigService {

    @Autowired
    private AlarmModeConfigDAO alarmModeConfigDAO;
    @Autowired
    private AlarmGroupConfigDAO alarmGroupConfigDAO;
    @Autowired
    private IUserProjectRoleService roleService;
    @Autowired
    private AppConfigDAO appConfigDAO;

    @Override
    public Integer addAndUpdate(ModeConfigDTO configDTO) {
        if (configDTO.getId() == null) {
            return add(configDTO);
        }
        return update(configDTO);
    }

    /**
     * 修改
     *
     * @param configDTO
     * @return
     */
    private Integer update(ModeConfigDTO configDTO) {
        AlarmModeConfigDO configDO = alarmModeConfigDAO.getOne(configDTO.getId());
        if (configDO == null) {
            return 0;
        }
        BeanUtils.copyProperties(configDTO, configDO);
        configDO.setAlarmGroupId(configDTO.getAlarmGroup());
        Date now = new Date();
        configDO.setUpdateDate(now);
        return alarmModeConfigDAO.update(configDO);
    }

    /**
     * 添加记录
     *
     * @param configDTO
     * @return
     */
    private Integer add(ModeConfigDTO configDTO) {
        AlarmModeConfigDO configDO = new AlarmModeConfigDO();
        BeanUtils.copyProperties(configDTO, configDO);
        configDO.setAlarmGroupId(configDTO.getAlarmGroup());

        Integer alarmSwitch = configDTO.getAlarmSwitch() == null ? SwitchEnum.OPEN.getSwitchType() : configDTO.getAlarmSwitch();
        Integer mailAlarmSwitch = configDTO.getMailAlarmSwitch() == null ? SwitchEnum.OPEN.getSwitchType() :
                configDTO.getMailAlarmSwitch();
        Integer smsAlarmSwitch = configDTO.getSmsAlarmSwitch() == null ? SwitchEnum.OPEN.getSwitchType() : configDTO
                .getSmsAlarmSwitch();
        Integer dingAlarmSwitch = configDTO.getDingAlarmSwitch() == null ? SwitchEnum.OPEN.getSwitchType() :
                configDTO.getDingAlarmSwitch();
        Integer alarmClientSwitch = configDTO.getAlarmClientSwitch() == null ? SwitchEnum.CLOSE.getSwitchType() :
                configDTO.getAlarmClientSwitch();
        Integer alarmServerSwitch = configDTO.getAlarmServerSwitch() == null ? SwitchEnum.OPEN.getSwitchType() :
                configDTO.getAlarmServerSwitch();

        String mail = StringUtils.isBlank(configDTO.getMailContent()) ? AlarmModeEnum.MAIL.getContent() : configDTO
                .getMailContent();
        String ding = StringUtils.isBlank(configDTO.getDingContent()) ? AlarmModeEnum.DING.getContent() : configDTO
                .getDingContent();

        configDO.setAlarmClientSwitch(alarmClientSwitch);
        configDO.setAlarmGroupId(configDO.getAlarmGroupId());
        configDO.setAlarmServerSwitch(alarmServerSwitch);
        configDO.setAlarmSwitch(alarmSwitch);
        configDO.setDingAlarmSwitch(dingAlarmSwitch);
        configDO.setMailAlarmSwitch(mailAlarmSwitch);
        configDO.setSmsAlarmSwitch(smsAlarmSwitch);
        configDO.setDingAlarmSwitch(dingAlarmSwitch);
        configDO.setDingContent(ding);
        configDO.setMailContent(mail);
        Date now = new Date();
        configDO.setCreateDate(now);
        configDO.setUpdateDate(now);
        configDO.setAppId(configDTO.getAppId());
        alarmModeConfigDAO.add(configDO);
        return 1;
    }

    @Override
    public AlarmModeConfigVO getModeConfig(ModeConfigDTO configDTO) {
        AlarmModeConfigDO configDO = alarmModeConfigDAO.getOne(configDTO.getId());
        AlarmModeConfigVO configVO = new AlarmModeConfigVO();
        if (configDO == null) {
            return configVO;
        }
        BeanUtils.copyProperties(configDO, configVO);
        configVO.setAppName(appConfigDAO.getAppName(configDO.getAppId()));
        return configVO;
    }

    @Override
    public List<AlarmModeConfigVO> load(ModeConfigDTO configDTO) {

        if (configDTO.getProjectId() == null || configDTO.getProjectId() <= 0) {
            return Collections.emptyList();
        }
        UserBO userBO = RuntimeContext.getCurrUser();
        UserProjectRoleBO roleBO = roleService.getAuthAppIds(userBO, configDTO.getProjectId());
        if (CollectionUtils.isEmpty(roleBO.getAppIds())) {
            return Collections.emptyList();
        }

        List<AlarmModeConfigDO> configDOS = alarmModeConfigDAO.getByAppIds(roleBO.getAppIds());
        List<AlarmModeConfigVO> configVOS = Lists.newArrayList();
        if (CollectionUtils.isEmpty(configDOS)) {
            return configVOS;
        }
        configDOS.stream().forEach(r -> {
            AlarmModeConfigVO configVO = new AlarmModeConfigVO();
            BeanUtils.copyProperties(r, configVO);
            configVO.setCreateDate(DateUtil.format(r.getCreateDate(), DateUtil.DEFAULT_TIME));
            configVO.setUpdateDate(DateUtil.format(r.getUpdateDate(), DateUtil.DEFAULT_TIME));
            configVO.setRoleId(roleBO.getRoleId());
            if (r.getAlarmGroupId() != null) {
                configVO.setAlarmGroup(alarmGroupConfigDAO.getGroupName(r.getAlarmGroupId()));
            }
            configVOS.add(configVO);
        });
        return configVOS;
    }

    @Override
    public List<AlarmGroupConfigVO> getAlarmGroup() {
        List<AlarmGroupConfigDO> dos = alarmGroupConfigDAO.getAll();
        List<AlarmGroupConfigVO> vos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(dos)) {
            return vos;
        }
        dos.stream().forEach(r -> {
            AlarmGroupConfigVO vo = new AlarmGroupConfigVO();
            vo.setId(r.getId());
            vo.setName(r.getName());
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public Integer existed(ModeConfigDTO configDTO) {
        if (configDTO.getId() != null) {
            return 0;
        }
        if (StringUtils.isBlank(configDTO.getName())) {
            return 0;
        }
        return alarmModeConfigDAO.existed(configDTO.getName().trim());
    }
}
