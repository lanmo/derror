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
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.domain.AlarmGroupConfigDO;
import org.jfaster.derror.manager.pojo.dto.AlarmGroupDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmGroupConfigVO;
import org.jfaster.derror.manager.service.IAlarmGroupService;
import org.jfaster.derror.manager.utils.DateUtil;
import org.jfaster.derror.manager.utils.RuntimeContext;
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
public class AlarmGroupServiceImpl implements IAlarmGroupService {

    @Autowired
    private AlarmGroupConfigDAO groupConfigDAO;

    @Override
    public List<AlarmGroupConfigVO> load() {
        UserBO userBO = RuntimeContext.getCurrUser();
        List<AlarmGroupConfigDO> dos = groupConfigDAO.getAll();
        List<AlarmGroupConfigVO> vos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(dos)) {
            return vos;
        }
        dos.stream().forEach(r -> {
            AlarmGroupConfigVO vo = new AlarmGroupConfigVO();
            BeanUtils.copyProperties(r, vo);
            vo.setCreateDate(DateUtil.format(r.getCreateDate(), DateUtil.DEFAULT_TIME));
            vo.setUpdateDate(DateUtil.format(r.getUpdateDate(), DateUtil.DEFAULT_TIME));
            vo.setRoleId(userBO.getRoleId());
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public AlarmGroupConfigVO getGroup(AlarmGroupDTO groupDTO) {
        AlarmGroupConfigVO vo = new AlarmGroupConfigVO();
        AlarmGroupConfigDO configDO = groupConfigDAO.getOne(groupDTO.getId());
        if (configDO == null) {
            return vo;
        }
        BeanUtils.copyProperties(configDO, vo);
        return vo;
    }

    @Override
    public Integer addAndUpdate(AlarmGroupDTO groupDTO) {
        if (groupDTO.getId() == null) {
            return add(groupDTO);
        }
        return update(groupDTO);
    }

    private Integer update(AlarmGroupDTO groupDTO) {
        AlarmGroupConfigDO configDO = new AlarmGroupConfigDO();
        BeanUtils.copyProperties(groupDTO, configDO);
        configDO.setUpdateDate(new Date());
        return groupConfigDAO.update(configDO);
    }

    private Integer add(AlarmGroupDTO groupDTO) {
        AlarmGroupConfigDO configDO = new AlarmGroupConfigDO();
        BeanUtils.copyProperties(groupDTO, configDO);
        configDO.setCreateDate(new Date());
        configDO.setUpdateDate(new Date());
        groupConfigDAO.add(configDO);
        return 1;
    }

    @Override
    public Integer delete(AlarmGroupDTO groupDTO) {
        return groupConfigDAO.delete(groupDTO.getId());
    }

    @Override
    public Integer existed(AlarmGroupDTO groupDTO) {
        if (StringUtils.isBlank(groupDTO.getName())) {
            return 0;
        }
        return groupConfigDAO.existed(groupDTO.getName());
    }
}
