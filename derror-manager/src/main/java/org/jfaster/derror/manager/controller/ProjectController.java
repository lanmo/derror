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

import org.jfaster.derror.manager.pojo.dto.ProjectDTO;
import org.jfaster.derror.manager.pojo.vo.ProjectVO;
import org.jfaster.derror.manager.service.IProjectService;
import org.jfaster.derror.manager.utils.ApiResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangnan
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    /**
     * 加载项目
     *
     * @return
     */
    @GetMapping("/load")
    public ApiResponse load() {
        List<ProjectVO> projects = projectService.load();
        return ApiResponse.success(projects);
    }

    /**
     * 添加项目
     *
     * @return
     */
    @PostMapping("/add")
    public ApiResponse add(@RequestBody ProjectDTO projectDTO) {
        int cnt = projectService.add(projectDTO);
        return ApiResponse.success(cnt);
    }

    /**
     * 查看项目
     *
     * @return
     */
    @PostMapping("/existed")
    public ApiResponse existed(@RequestBody ProjectDTO projectDTO) {
        return ApiResponse.success(projectService.existed(projectDTO));
    }

    /**
     * 获取授权的project
     * @return
     */
    @GetMapping("/getAuthProjects")
    public ApiResponse getAuthProjects() {
        List<ProjectVO> vos = projectService.getAuthProjects();
        return ApiResponse.success(vos);
    }
}
