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
import org.jfaster.derror.manager.pojo.domain.AppConfigDO;
import org.jfaster.derror.manager.pojo.vo.AppConfigVO;
import org.jfaster.derror.manager.pojo.vo.AppConfigurationVO;
import java.util.List;
import java.util.Set;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.crud.CrudDao;

/**
 * @description:应用配置表
 * @author: Amos.Wxz
 * @create: 2018-07-04 18:33
 **/
@DB(name = DbConstant.DB,table = "app_config")
public interface AppConfigDAO extends CrudDao<AppConfigDO, Long> {

    /**
     * 根据appid查询
     * @param id
     * @return
     */
    @SQL("select * from #table where id=:1")
    AppConfigDO queryAppConfigById(Long id);

    /**
     * 根据appName, appKey
     *
     * @param appName
     * @param appKey
     * @return
     */
    @SQL("select id, app_name, app_key, frequency, frequency_time, status from #table where app_name "
            + "= :1 and app_key = :2")
    AppConfigVO getConfigByNameAndToken(String appName, String appKey);

    /**
     * 加载所有配置
     *
     * @return
     */
    @SQL("select * from #table")
    List<AppConfigDO> load();

    /**
     * 修改状态
     *
     * @param appName
     * @param newStatus
     * @param oldStatus
     * @return
     */
    @SQL("update #table set status = :2 where app_name = :1 and status = :3")
    Integer changeStatus(String appName, Integer newStatus, Integer oldStatus);

    /**
     * 判断应用名是否存在
     *
     * @param appName
     * @return
     */
    @SQL("select count(id) from #table where app_name=:1")
    Integer existed(String appName);

    /**
     * 获取应用名
     *
     * @param appId
     * @return
     */
    @SQL("select app_name from #table where id=:1")
    String getAppName(Long appId);

    /**
     * 获取开启的应用
     * @param switchType
     * @return
     */
    @SQL("select id, app_name from #table where status=:1")
    List<AppConfigDO> getAll(Integer switchType);

    /**
     * 根据appId查询appName
     * @param appIds
     */
    @SQL("select * from #table where id in (:1)")
    List<AppConfigDO> getAllAppName(List<Long> appIds);

    /**
     * 获取应用
     * @param projectIds
     * @return
     */
    @SQL("select * from #table where project_id in(:1)")
    List<AppConfigDO> getApp(Set<Integer> projectIds);

    /**
     * 获取项目下的应用
     * @param projectId
     * @return
     */
    @SQL("select * from #table where project_id = :1")
    List<AppConfigDO> getProjectApps(Integer projectId);

    @SQL("select * from #table where project_id  in  (:1)")
    List<AppConfigurationVO> getConfigsByProjectIds(List<Integer> authorizedProjects);
}
