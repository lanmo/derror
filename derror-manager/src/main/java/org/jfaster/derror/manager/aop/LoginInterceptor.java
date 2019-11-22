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

package org.jfaster.derror.manager.aop;

import org.jfaster.derror.manager.pojo.bo.UserBO;
import org.jfaster.derror.manager.pojo.domain.ProjectDO;
import org.jfaster.derror.manager.pojo.domain.UserProjectRoleDO;
import org.jfaster.derror.manager.service.IProjectService;
import org.jfaster.derror.manager.service.IUserProjectRoleService;
import org.jfaster.derror.manager.service.IUserService;
import org.jfaster.derror.manager.utils.LoginUtil;
import org.jfaster.derror.manager.utils.RuntimeContext;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 登录拦截器
 *
 * @author yangnan
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final static String XMLHTTPREQUEST = "XMLHttpRequest";
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserProjectRoleService roleService;
    @Autowired
    private IProjectService projectService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String userName = LoginUtil.getLoginName(request);
        if (StringUtils.isBlank(userName)) {
            logger.warn("用户登录失效 userName:", userName);
            String redirectUrl = request.getRequestURL().toString();
            if (XMLHTTPREQUEST.equals(request.getHeader("X-Requested-With"))) {
                //告诉ajax这是重定向
                response.setHeader("REDIRECT", "REDIRECT");
                //重定向地址
                response.setHeader("CONTEXTPATH", "/");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            } else {
                response.sendRedirect("/");
            }
            return false;
        }
        UserBO user = userService.getByUserName(userName);
        if (user == null) {
            user = userService.addUser(userName);
        }

        if (user == null) {
            logger.warn("未获取到用户信息 userName:{}", userName);
            return false;
        }

        if (user.isForbidden()) {
            logger.warn("该用户被禁用 userName:{}", userName);
            return false;
        }

        List<Integer> authorizedProjects = null;
        if (user.isSuperAdmin()) {
            List<ProjectDO> projectDOS = projectService.getAll();
            authorizedProjects = projectDOS.stream().map(ProjectDO::getId).collect(Collectors.toList());
        } else {
            //获取权限
            List<UserProjectRoleDO> projectRoleDOS = roleService.getProjectAppRole(user.getId());
            authorizedProjects = projectRoleDOS.stream().map(UserProjectRoleDO::getProjectId).collect(
                    Collectors.toList());
        }
        user.setAuthorizedProjects(authorizedProjects);

        RuntimeContext.setCurrUser(user);
        request.setAttribute("isSuperAdmin", user.isSuperAdmin());

        return true;
    }
}
