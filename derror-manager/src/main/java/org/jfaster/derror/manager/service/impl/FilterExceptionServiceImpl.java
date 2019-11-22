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
import org.jfaster.derror.manager.dao.AppConfigDAO;
import org.jfaster.derror.manager.dao.FilterExceptionConfigDAO;
import org.jfaster.derror.manager.enums.SwitchEnum;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.bo.UserProjectRoleBO;
import org.jfaster.derror.manager.pojo.domain.AppConfigDO;
import org.jfaster.derror.manager.pojo.domain.FilterExceptionConfigDO;
import org.jfaster.derror.manager.pojo.dto.FilterExceptionDTO;
import org.jfaster.derror.manager.pojo.vo.AppConfigurationVO;
import org.jfaster.derror.manager.pojo.vo.FilterExceptionVO;
import org.jfaster.derror.manager.service.IFilterExceptionService;
import org.jfaster.derror.manager.service.IUserProjectRoleService;
import org.jfaster.derror.manager.utils.DateUtil;
import org.jfaster.derror.manager.utils.RuntimeContext;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangnan
 */
@Service
public class FilterExceptionServiceImpl implements IFilterExceptionService {

    @Autowired
    private FilterExceptionConfigDAO exceptionConfigDAO;
    @Autowired
    private AppConfigDAO appConfigDAO;
    @Autowired
    private IUserProjectRoleService roleService;

    @Override
    public List<FilterExceptionVO> load(FilterExceptionDTO exceptionDTO) {

        if (exceptionDTO.getProjectId() == null || exceptionDTO.getProjectId() <=0) {
            return Collections.emptyList();
        }

        UserBO userBO = RuntimeContext.getCurrUser();
        UserProjectRoleBO roleBO = roleService.getAuthAppIds(userBO, exceptionDTO.getProjectId());
        if (CollectionUtils.isEmpty(roleBO.getAppIds())) {
            return Collections.emptyList();
        }

        List<FilterExceptionConfigDO> configDOS = exceptionConfigDAO.getExceptionByAppIds(roleBO.getAppIds());
        List<FilterExceptionVO> vos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(configDOS)) {
            return vos;
        }
        configDOS.stream().forEach(r -> {
            FilterExceptionVO vo = new FilterExceptionVO();
            BeanUtils.copyProperties(r, vo);
            vo.setCreateDate(DateUtil.format(r.getCreateDate(), DateUtil.DEFAULT_TIME));
            vo.setUpdateDate(DateUtil.format(r.getUpdateDate(), DateUtil.DEFAULT_TIME));
            vo.setAppName(appConfigDAO.getAppName(r.getAppId()));
            vo.setRoleId(roleBO.getRoleId());
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public Integer addAndUpdate(FilterExceptionDTO exceptionDTO) {
        if (exceptionDTO.getId() == null) {
            return add(exceptionDTO);
        }
        return update(exceptionDTO);
    }

    @Override
    public FilterExceptionVO getException(FilterExceptionDTO exceptionDTO) {
        FilterExceptionConfigDO configDO = exceptionConfigDAO.getOne(exceptionDTO.getId());
        FilterExceptionVO vo = new FilterExceptionVO();
        if (configDO == null) {
            return vo;
        }
        BeanUtils.copyProperties(configDO, vo);
        return vo;
    }

    @Override
    public Integer delete(FilterExceptionDTO exceptionDTO) {
        return exceptionConfigDAO.delete(exceptionDTO.getId());
    }

    @Override
    public List<AppConfigurationVO> getApps() {
        List<AppConfigDO> dos = appConfigDAO.getAll(SwitchEnum.OPEN.getSwitchType());
        List<AppConfigurationVO> vos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(dos)) {
            return vos;
        }
        dos.stream().forEach(r -> {
            AppConfigurationVO vo = new AppConfigurationVO();
            vo.setId(r.getId());
            vo.setAppName(r.getAppName());
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public Integer changeStatus(FilterExceptionDTO exceptionDTO) {
        FilterExceptionConfigDO configDO = exceptionConfigDAO.getOne(exceptionDTO.getId());
        if (configDO == null) {
            return 0;
        }
        Integer status = exceptionDTO.getStatus() == null ? SwitchEnum.OPEN.getSwitchType() : exceptionDTO.getStatus();
        return exceptionConfigDAO.updateStatus(exceptionDTO.getId(), status, configDO.getStatus());
    }

    /**
     * 修改记录
     *
     * @param exceptionDTO
     * @return
     */
    private Integer update(FilterExceptionDTO exceptionDTO) {
        FilterExceptionConfigDO configDO = exceptionConfigDAO.getOne(exceptionDTO.getId());
        if (configDO == null) {
            return 0;
        }
        BeanUtils.copyProperties(exceptionDTO, configDO);
        configDO.setUpdateDate(new Date());
        return exceptionConfigDAO.update(configDO);
    }

    /**
     * 添加记录
     * @param exceptionDTO
     * @return
     */
    private Integer add(FilterExceptionDTO exceptionDTO) {
        FilterExceptionConfigDO configDO = new FilterExceptionConfigDO();
        BeanUtils.copyProperties(exceptionDTO, configDO);
        Date now = new Date();
        configDO.setCreateDate(now);
        configDO.setUpdateDate(now);
        Integer status = exceptionDTO.getStatus() == null ? SwitchEnum.OPEN.getSwitchType() : exceptionDTO.getStatus();
        configDO.setStatus(status);
        exceptionConfigDAO.add(configDO);
        return 1;
    }

}
