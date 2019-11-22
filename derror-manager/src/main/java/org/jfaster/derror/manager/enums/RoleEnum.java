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


package org.jfaster.derror.manager.enums;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import lombok.Getter;

/**
 * @author yangnan
 * 角色
 */
public enum RoleEnum {
    NONE(0, "无权限"),
    SUPER_ADMIN(1, "超级管理员"),
    PROJECT_ADMIN(2, "项目管理员"),
    VIEWER(4, "查看人员"),
    ;

    RoleEnum(int roleId, String desc) {
        this.roleId = roleId;
        this.desc = desc;
    }

    /**
     * 可操作权限
     */
    private static Set<Integer> EFFECT_ROLE = Sets.newHashSet(SUPER_ADMIN.getRoleId(), PROJECT_ADMIN.getRoleId());

    private static List<Integer> ROLE = null;

    static {
        ROLE = Lists.newArrayList();
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.getRoleId() == SUPER_ADMIN.getRoleId()) {
                continue;
            }
            ROLE.add(roleEnum.getRoleId());
        }
    }

    /**
     * 获取权限id, 排除超级管理员
     *
     * @return
     */
    public static List<Integer> getRole() {
        return ROLE;
    }

    /**
     * 是否具有可操作权限
     *
     * @param roleId
     * @return
     */
    public static boolean canOperator(Integer roleId) {
        return EFFECT_ROLE.contains(roleId);
    }

    @Getter
    private int roleId;
    @Getter
    private String desc;

}
