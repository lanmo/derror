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
import org.jfaster.derror.manager.pojo.domain.UserProjectRoleDO;
import java.util.List;
import java.util.Set;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.crud.CrudDao;

/**
 * @author yangnan
 */
@DB(name = DbConstant.DB, table = "user_project_role")
public interface UserProjectRoleDAO extends CrudDao<UserProjectRoleDO, Integer> {

    /**
     * 获取用户拥有的权限
     *
     * @param userId
     * @return
     */
    @SQL("select * from #table where user_id = :1")
    List<UserProjectRoleDO> getProjectAppRole(Long userId);

    /**
     * 获取用户的项目权限
     *
     * @param userId
     * @param projectIds
     * @return
     */
    @SQL("select * from #table where user_id = :1 and project_id in (:2)")
    List<UserProjectRoleDO> getUserProjects(Long userId, Set<Integer> projectIds);

    /**
     * 获取用户的项目权限
     * @param operateId
     * @param projectId
     * @return
     */
    @SQL("select * from #table where user_id = :1 and project_id = :2 limit 1")
    UserProjectRoleDO getUserProject(Long operateId, Integer projectId);

    /**
     * 删除用户的项目权限
     * @param operateId
     * @param projectId
     * @return
     */
    @SQL("delete from #table where user_id = :1 and project_id = :2")
    int deleteUserRole(Long operateId, Integer projectId);

}
