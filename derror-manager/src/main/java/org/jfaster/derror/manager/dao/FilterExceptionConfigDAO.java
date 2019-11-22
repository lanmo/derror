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
import org.jfaster.derror.manager.pojo.domain.FilterExceptionConfigDO;
import org.jfaster.derror.manager.pojo.vo.FilterExceptionConfigVO;
import java.util.List;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.crud.CrudDao;

/**
 * @author yangnan
 */
@DB(name = DbConstant.DB, table = "filter_exception_config")
public interface FilterExceptionConfigDAO extends CrudDao<FilterExceptionConfigDO, Integer> {

    /**
     * 获取启用的异常过滤列表
     *
     * @param appId
     * @param status
     * @return
     */
    @SQL("select class_name from #table where app_id = :1 and status = :2")
    List<FilterExceptionConfigVO> getFilterExceptions(Long appId, Integer status);

    /**
     * 修改状态
     *
     * @param id
     * @param status
     * @param oldStatus
     * @return
     */
    @SQL("update #table set status = :2 where id = :1 and status = :3")
    Integer updateStatus(Integer id, Integer status, Integer oldStatus);

    /**
     * 获取具有权限的
     *
     * @param appIds
     * @return
     */
    @SQL("select * from #table where app_id in (:1)")
    List<FilterExceptionConfigDO> getExceptionByAppIds(List<Long> appIds);
}
