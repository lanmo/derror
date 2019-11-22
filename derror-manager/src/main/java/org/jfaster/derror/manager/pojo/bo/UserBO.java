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

package org.jfaster.derror.manager.pojo.bo;

import org.jfaster.derror.manager.enums.RoleEnum;
import java.util.List;

import lombok.Data;

/**
 * @author yangnan
 */
@Data
public class UserBO {

    /** 用户状态正常 */
    public static final int STATUS_NORMAL = 0;
    /** 用户被封禁 */
    public static final int STATUS_FORBIDDEN = -1;

    private Long id;
    private String userName;
    private Integer roleId;
    private Integer status;

    /**
     * key projectId
     * */
    private List<Integer> authorizedProjects;

    /**
     * 判断用户是否被封禁
     *
     * @return
     */
    public boolean isForbidden() {
        return status == STATUS_FORBIDDEN;
    }

    /**
     * 判断用户是否为超管
     *
     * @return
     */
    public boolean isSuperAdmin() {
        return this.roleId == RoleEnum.SUPER_ADMIN.getRoleId();
    }

    /**
     * 判断用户是否有项目管理权限
     *
     * @return
     */
    public boolean isProjectManager() {
        return RoleEnum.canOperator(this.roleId);
    }

}
