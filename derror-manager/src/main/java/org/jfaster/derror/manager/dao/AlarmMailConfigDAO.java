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
import org.jfaster.derror.manager.pojo.domain.AlarmMailConfigDO;
import org.jfaster.derror.manager.pojo.dto.AlarmMailConfigDTO;
import org.jfaster.derror.manager.pojo.vo.AlarmMailConfigVO;
import java.util.List;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;

/**
 * @program: derror
 * @description: 查询邮件配置
 * @author: Amos.Wxz
 * @create: 2018-07-05 19:46
 **/
@DB(name = DbConstant.DB, table = "alarm_mail_config")
public interface AlarmMailConfigDAO {

    @SQL("select * from #table")
    List<AlarmMailConfigDO> getAlarmMailInfo(Integer alarmModelId);

    @SQL("select * from #table  ")
    List<AlarmMailConfigVO> queryAlarmMails(AlarmMailConfigDTO alarmMailConfigDTO);

    @SQL("insert into #table (host, port, username, password,create_date) values (:1.host,:1.port,"
            + ":1.username,:1.password,:1.createDate);")
    Integer addAlarmMail(AlarmMailConfigDTO alarmMailConfigDTO);

    @SQL("update #table set port=:1.port,username=:1.username,password=:1.password,update_date=:1.updateDate, "
            + " host=:1.host where id=:1.id")
    Integer updateAlarmMail(AlarmMailConfigDTO alarmMailConfigDTO);

    @SQL("delete from #table where id=:1.id")
    Integer delAlarmMail(AlarmMailConfigDTO alarmMailConfigDTO);

    @SQL("select * from #table where id=:1.id ")
    AlarmMailConfigVO queryAlarmMail(AlarmMailConfigDTO alarmMailConfigDTO);

}
