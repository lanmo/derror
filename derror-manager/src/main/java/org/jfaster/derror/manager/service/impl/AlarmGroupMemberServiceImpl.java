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
import org.jfaster.derror.manager.dao.AlarmGroupMembersDAO;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.domain.AlarmGroupConfigDO;
import org.jfaster.derror.manager.pojo.domain.AlarmGroupMembersDO;
import org.jfaster.derror.manager.pojo.dto.AlarmGroupMemberDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmGroupConfigVO;
import org.jfaster.derror.manager.pojo.vo.AlarmGroupMemberVO;
import org.jfaster.derror.manager.service.IAlarmGroupMemberService;
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
public class AlarmGroupMemberServiceImpl implements IAlarmGroupMemberService {

    @Autowired
    private AlarmGroupMembersDAO membersDAO;

    @Autowired
    private AlarmGroupConfigDAO groupConfigDAO;

    @Override
    public List<AlarmGroupMemberVO> load() {
        UserBO userBO = RuntimeContext.getCurrUser();
        List<AlarmGroupMembersDO> dos = membersDAO.getAll();
        if (CollectionUtils.isEmpty(dos)) {
            return Collections.emptyList();
        }
        List<AlarmGroupMemberVO> vos = Lists.newArrayListWithCapacity(dos.size());
        dos.stream().forEach(r -> {
            AlarmGroupMemberVO vo = new AlarmGroupMemberVO();
            BeanUtils.copyProperties(r, vo);
            vo.setGroupName(groupConfigDAO.getGroupName(vo.getAlarmGroupId()));
            vo.setCreateDate(DateUtil.format(r.getCreateDate(), DateUtil.DEFAULT_TIME));
            vo.setUpdateDate(DateUtil.format(r.getUpdateDate(), DateUtil.DEFAULT_TIME));
            vo.setRoleId(userBO.getRoleId());
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public AlarmGroupMemberVO getMember(AlarmGroupMemberDTO memberDTO) {
        AlarmGroupMembersDO membersDO = membersDAO.getOne(memberDTO.getId());
        AlarmGroupMemberVO memberVO = new AlarmGroupMemberVO();
        if (membersDO == null) {
            return memberVO;
        }
        BeanUtils.copyProperties(membersDO, memberVO);
        return memberVO;
    }

    @Override
    public Integer delete(AlarmGroupMemberDTO memberDTO) {
        return membersDAO.delete(memberDTO.getId());
    }

    @Override
    public Integer addAndUpdate(AlarmGroupMemberDTO memberDTO) {
        if (memberDTO.getId() == null) {
            return add(memberDTO);
        }
        return update(memberDTO);
    }

    /**
     * 更新
     * @param memberDTO
     * @return
     */
    private Integer update(AlarmGroupMemberDTO memberDTO) {

        AlarmGroupMembersDO membersDO = membersDAO.getOne(memberDTO.getId());
        if (membersDO == null) {
            return 0;
        }
        BeanUtils.copyProperties(memberDTO, membersDO);
        membersDO.setUpdateDate(new Date());
        return membersDAO.update(membersDO);
    }

    private Integer add(AlarmGroupMemberDTO memberDTO) {
        AlarmGroupMembersDO membersDO = new AlarmGroupMembersDO();
        BeanUtils.copyProperties(memberDTO, membersDO);
        Date now = new Date();
        membersDO.setCreateDate(now);
        membersDO.setUpdateDate(now);
        membersDAO.add(membersDO);
        return 1;
    }

    @Override
    public List<AlarmGroupConfigVO> getGroups() {
        List<AlarmGroupConfigDO> dos = groupConfigDAO.getAll();
        if (CollectionUtils.isEmpty(dos)) {
            return Collections.emptyList();
        }
        List<AlarmGroupConfigVO> vos = Lists.newArrayListWithCapacity(dos.size());
        dos.stream().forEach(r -> {
            AlarmGroupConfigVO vo = new AlarmGroupConfigVO();
            BeanUtils.copyProperties(r, vo);
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public Integer phoneExisted(AlarmGroupMemberDTO memberDTO) {
        if (StringUtils.isBlank(memberDTO.getPhone())) {
            return 0;
        }
        return membersDAO.countPhone(memberDTO.getPhone());
    }

    @Override
    public Integer mailExisted(AlarmGroupMemberDTO memberDTO) {
        if (StringUtils.isBlank(memberDTO.getMail())) {
            return 0;
        }
        return membersDAO.countMail(memberDTO.getMail());
    }
}
