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
import org.jfaster.derror.manager.pojo.domain.AlarmModeConfigDO;
import org.jfaster.derror.manager.pojo.vo.ModeConfigVO;
import java.util.List;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.crud.CrudDao;

/**
 * @author yangnan
 */
@DB(name = DbConstant.DB, table = "alarm_mode_config")
public interface AlarmModeConfigDAO extends CrudDao<AlarmModeConfigDO, Integer> {

    /**
     * 根据主键查找
     *
     * @param id
     * @return
     */
    @SQL("select * from #table where id=:1")
    AlarmModeConfigDO queryAlarmModeConfigById(Integer id);

    /**
     * 根据应用id查询
     * @param appId
     * @return
     */
    @SQL("select * from #table where app_id=:1")
    ModeConfigVO getModeConfigByAppId(Long appId);

    /**
     * 获取报警方式名称
     * @param alarmModelId
     * @return
     */
    @SQL("select name from #table where id=:1")
    String getModeName(Integer alarmModelId);

    /**
     * 获取开启报警方式
     * @return
     */
    @SQL("select * from #table")
    List<AlarmModeConfigDO> getModeConfigs();

    /**
     * 统计
     * @param name
     * @return
     */
    @SQL("select count(1) from #table where name = :1")
    Integer existed(String name);

    /**
     * 根据应用获取报警方式
     *
     * @param appId
     * @return
     */
    @SQL("select * from #table where app_id=:1")
    AlarmModeConfigDO queryAlarmModeConfigByAppId(Long appId);

    /**
     * 根据应用获取报警方式
     *
     * @param appIds
     * @return
     */
    @SQL("select * from #table where app_id in (:1)")
    List<AlarmModeConfigDO> getByAppIds(List<Long> appIds);
}
