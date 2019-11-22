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

package org.jfaster.derror.manager.pojo.domain;

import org.jfaster.derror.manager.enums.RoleEnum;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfaster.mango.annotation.ID;

/**
 * @author yangnan
 */
@Data
@NoArgsConstructor
public class UserDO {

    @ID
    private Long id;
    private String userName;
    private Integer roleId;
    private Integer status;
    private String ext;
    private Date createDate;
    private Date updateDate;

    public UserDO(String userName) {
        this.userName = userName;
        this.roleId = RoleEnum.NONE.getRoleId();
        this.status = UserBO.STATUS_NORMAL;
        this.createDate = new Date();
        this.updateDate = new Date();
    }
}
