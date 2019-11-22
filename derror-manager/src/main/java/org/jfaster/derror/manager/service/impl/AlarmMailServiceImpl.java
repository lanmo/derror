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

import org.jfaster.derror.manager.dao.AlarmMailConfigDAO;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.dto.AlarmMailConfigDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmMailConfigVO;
import org.jfaster.derror.manager.service.IAlarmMailService;
import org.jfaster.derror.manager.utils.DateUtil;
import org.jfaster.derror.manager.utils.RuntimeContext;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 邮件报警信息
 * @author: Amos
 * @create: 2018-07-13 16:43
 **/
@Service
public class AlarmMailServiceImpl implements IAlarmMailService {

    @Autowired
    private AlarmMailConfigDAO alarmMailConfigDAO;

    @Override
    public List<AlarmMailConfigVO> queryAlarmMails(AlarmMailConfigDTO alarmMailConfigDTO) {
        UserBO userBO = RuntimeContext.getCurrUser();
        List<AlarmMailConfigVO> alarmMailConfigVOS = alarmMailConfigDAO.queryAlarmMails(alarmMailConfigDTO);
        alarmMailConfigVOS.parallelStream().forEach(r -> r.setRoleId(userBO.getRoleId()));
        return alarmMailConfigVOS;
    }

    @Override
    public AlarmMailConfigVO queryAlarmMail(AlarmMailConfigDTO alarmMailConfigDTO) {
        AlarmMailConfigVO alarmMailConfigVO = alarmMailConfigDAO.queryAlarmMail(alarmMailConfigDTO);
        return alarmMailConfigVO;
    }

    @Override
    public Integer addAndUpdate(AlarmMailConfigDTO alarmMailConfigDTO) {
        Integer id = alarmMailConfigDTO.getId();
        int row;
        if (id == null) {
            alarmMailConfigDTO.setCreateDate(DateUtil.format(new Date(),DateUtil.DEFAULT_TIME));
            row = alarmMailConfigDAO.addAlarmMail(alarmMailConfigDTO);
        } else {
            alarmMailConfigDTO.setUpdateDate(DateUtil.format(new Date(),DateUtil.DEFAULT_TIME));
            row = alarmMailConfigDAO.updateAlarmMail(alarmMailConfigDTO);
        }
        return row;
    }

    @Override
    public Integer delete(AlarmMailConfigDTO alarmMailConfigDTO) {
        return alarmMailConfigDAO.delAlarmMail(alarmMailConfigDTO);
    }
}
