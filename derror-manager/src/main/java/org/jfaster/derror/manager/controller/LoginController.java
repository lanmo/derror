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

package org.jfaster.derror.manager.controller;

import org.jfaster.derror.manager.pojo.dto.LoginDTO;
import org.jfaster.derror.manager.pojo.dto.LoginForm;
import org.jfaster.derror.manager.service.ILoginService;
import org.jfaster.derror.manager.utils.ApiResponse;
import org.jfaster.derror.manager.utils.LoginUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 用户登录
 * @author: Amos
 * @create: 2018-07-25 11:09
 **/
@Controller
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @PostMapping("/user/login")
    @ResponseBody
    public ApiResponse login(LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        ApiResponse login = loginService.login(loginDTO);
        if (login.getCode() == ApiResponse.SUCCESS_CODE) {
            LoginForm loginForm = new LoginForm();
            BeanUtils.copyProperties(loginDTO, loginForm);
            LoginUtil.login(loginForm, request, response);
            return login;
        }
        return login;
    }

}
