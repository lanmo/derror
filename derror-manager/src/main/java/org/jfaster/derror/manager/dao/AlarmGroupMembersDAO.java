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

package org.jfaster.derror.manager.dao;

import org.jfaster.derror.manager.constant.DbConstant;
import org.jfaster.derror.manager.pojo.domain.AlarmGroupMembersDO;
import org.jfaster.derror.manager.pojo.vo.GroupMembersVO;
import java.util.List;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.crud.CrudDao;

/**
 * 报警组
 * @author yangnan
 */
@DB(name = DbConstant.DB, table = "alarm_group_members")
public interface AlarmGroupMembersDAO extends CrudDao<AlarmGroupMembersDO, Integer> {

    /**
     * 获取短信组的成员
     *
     * @param groupId
     * @return
     */
    @SQL("select user_name, phone, mail from #table where alarm_group_id = :1")
    List<GroupMembersVO> getMembersByGroupId(Integer groupId);


    /**
     * 获取报警组成员
     *
     * @param groupId
     * @return
     */
    @SQL("select user_name, phone, mail from #table where alarm_group_id = :1")
    List<AlarmGroupMembersDO> getAlarmMembersByGroupId(Integer groupId);

    /**
     * 统计手机号
     *
     * @param phone
     * @return
     */
    @SQL("select count(id) from #table where phone = :1")
    Integer countPhone(String phone);

    /**
     * 统计邮箱
     *
     * @param mail
     * @return
     */
    @SQL("select count(id) from #table where mail = :1")
    Integer countMail(String mail);
}
