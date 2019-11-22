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

package org.jfaster.derror.manager.service;

import org.jfaster.derror.manager.pojo.dto.AlarmGroupMemberDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmGroupConfigVO;
import org.jfaster.derror.manager.pojo.vo.AlarmGroupMemberVO;

import java.util.List;

/**
 * @author yangnan
 */
public interface IAlarmGroupMemberService {

    /**
     * 获取所有的
     * @return
     */
    List<AlarmGroupMemberVO> load();

    /**
     * 获取单个配置
     * @param memberDTO
     * @return
     */
    AlarmGroupMemberVO getMember(AlarmGroupMemberDTO memberDTO);

    /**
     * 删除
     * @param memberDTO
     * @return
     */
    Integer delete(AlarmGroupMemberDTO memberDTO);

    /**
     * 添加并修改
     * @param memberDTO
     * @return
     */
    Integer addAndUpdate(AlarmGroupMemberDTO memberDTO);

    /**
     * 获取报警组
     * @return
     */
    List<AlarmGroupConfigVO> getGroups();

    /**
     * 判断手机号是否存在
     * @param memberDTO
     * @return
     */
    Integer phoneExisted(AlarmGroupMemberDTO memberDTO);

    /**
     * 判断邮箱是否存
     * @param memberDTO
     * @return
     */
    Integer mailExisted(AlarmGroupMemberDTO memberDTO);
}
