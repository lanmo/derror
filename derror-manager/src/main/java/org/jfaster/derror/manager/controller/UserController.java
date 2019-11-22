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

package org.jfaster.derror.manager.controller;

import com.google.common.collect.Lists;
import org.jfaster.derror.manager.enums.RoleEnum;
import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.dto.UserProjectDTO;
import org.jfaster.derror.manager.pojo.vo.RoleVO;
import org.jfaster.derror.manager.pojo.vo.UserProjectRoleVO;
import org.jfaster.derror.manager.service.IUserProjectRoleService;
import org.jfaster.derror.manager.service.IUserService;
import org.jfaster.derror.manager.utils.ApiResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yangnan
 * 用户管理
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserProjectRoleService projectRoleService;

    /**
     * 获取当前登录用户权限
     *
     * @return
     */
    @GetMapping("/auth")
    @ResponseBody
    public ApiResponse getAuth() {
        int auth = userService.getAuth();
        return ApiResponse.success(auth);
    }

    /**
     * 获取角色
     * @return
     */
    @GetMapping("/getRoles")
    @ResponseBody
    public ApiResponse getRoles() {
        List<RoleVO> vos = Lists.newArrayList();
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.getRoleId() == RoleEnum.NONE.getRoleId()) {
                continue;
            }
            RoleVO roleVO = new RoleVO();
            roleVO.setRoleId(roleEnum.getRoleId());
            roleVO.setRoleName(roleEnum.getDesc());
        }
        return ApiResponse.success(vos);
    }

    /**
     * 获取用户
     * @return
     */
    @GetMapping("/load")
    @ResponseBody
    public ApiResponse load() {
        List<UserBO> users = userService.load();
        return ApiResponse.success(users);
    }

    /**
     * 获取用户的应用权限列表
     * @return
     */
    @GetMapping("/auth/load")
    @ResponseBody
    public ApiResponse authLoad(UserProjectDTO projectDTO) {
        List<UserProjectRoleVO> users = userService.authLoad(projectDTO);
        return ApiResponse.success(users);
    }

    /**
     * 获取用户的应用权限列表
     * @return
     */
    @GetMapping("/goAuth")
    public String goAuth(UserProjectDTO projectDTO, Model model) {
        model.addAttribute("userId", projectDTO.getUserId());
        return "auth/user_auth_center";
    }

    /**
     * 获取用户的应用权限列表
     * @return
     */
    @PostMapping("/auth/update")
    @ResponseBody
    public ApiResponse update(@RequestBody UserProjectDTO projectDTO) {
        int cnt = projectRoleService.addAndUpdate(projectDTO);
        return ApiResponse.success(cnt);
    }

}
