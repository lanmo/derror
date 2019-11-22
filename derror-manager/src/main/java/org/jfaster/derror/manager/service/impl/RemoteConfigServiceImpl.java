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
import org.jfaster.derror.manager.dao.AlarmGroupMembersDAO;
import org.jfaster.derror.manager.dao.AlarmModeConfigDAO;
import org.jfaster.derror.manager.dao.AppConfigDAO;
import org.jfaster.derror.manager.dao.FilterExceptionConfigDAO;
import org.jfaster.derror.manager.enums.SwitchEnum;
import org.jfaster.derror.manager.pojo.dto.AppClientConfigDTO;
import org.jfaster.derror.manager.pojo.vo.AppConfigVO;
import org.jfaster.derror.manager.pojo.vo.FilterExceptionConfigVO;
import org.jfaster.derror.manager.pojo.vo.GroupMembersVO;
import org.jfaster.derror.manager.pojo.vo.ModeConfigVO;
import org.jfaster.derror.manager.pojo.vo.RemoteConfigVO;
import org.jfaster.derror.manager.service.IRemoteConfigService;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangnan
 */
@Service
public class RemoteConfigServiceImpl implements IRemoteConfigService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AlarmGroupMembersDAO alarmGroupMembersDAO;
    @Autowired
    private AlarmModeConfigDAO alarmModeConfigDAO;
    @Autowired
    private AppConfigDAO appConfigDAO;
    @Autowired
    private FilterExceptionConfigDAO filterExceptionConfigDAO;

    @Override
    public RemoteConfigVO getConfig(AppClientConfigDTO configDTO) {
        RemoteConfigVO remoteConfigVO = new RemoteConfigVO();
        AppConfigVO configVO = appConfigDAO.getConfigByNameAndToken(configDTO.getAppName(), configDTO.getToken());
        remoteConfigVO.setAppConfig(configVO);
        //关闭了应用配置
        if (configVO == null || SwitchEnum.CLOSE.getSwitchType().equals(configVO.getStatus())) {
            logger.warn("未找到应用配置信息或者应用配置关闭configVO:{}", JSON.toJSONString(configVO));
            return remoteConfigVO;
        }
        List<FilterExceptionConfigVO> exceptionConfigVOS = filterExceptionConfigDAO.getFilterExceptions(configVO.getId(), SwitchEnum.OPEN.getSwitchType());
        remoteConfigVO.setExceptionConfigs(exceptionConfigVOS);

        ModeConfigVO modeConfigVO = alarmModeConfigDAO.getModeConfigByAppId(configVO.getId());
        remoteConfigVO.setModeConfig(modeConfigVO);
        if (modeConfigVO == null) {
            return remoteConfigVO;
        }
        List<GroupMembersVO> membersVOS = alarmGroupMembersDAO.getMembersByGroupId(modeConfigVO.getAlarmGroupId());
        remoteConfigVO.setMembers(membersVOS);
        return remoteConfigVO;
    }
}
