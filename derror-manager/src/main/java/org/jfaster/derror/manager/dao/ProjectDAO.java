/*
 *
 *   Copyright 2018 org.jfaster.derror.
 *     <p>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   </p>
 */

package org.jfaster.derror.manager.dao;

import org.jfaster.derror.manager.constant.DbConstant;
import org.jfaster.derror.manager.pojo.domain.ProjectDO;
import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.crud.CrudDao;

/**
 * @author yangnan
 */
@DB(name = DbConstant.DB, table = "project")
public interface ProjectDAO extends CrudDao<ProjectDO, Integer> {

    /**
     * 根据名称获取项目
     *
     * @param name
     * @return
     */
    @SQL("select * from #table where name = :1")
    ProjectDO getProjectByName(String name);

    /**
     * 判断名称是否存在
     *
     * @param name
     * @return
     */
    @SQL("select count(id) from #table where name = :1")
    int existed(String name);
}
