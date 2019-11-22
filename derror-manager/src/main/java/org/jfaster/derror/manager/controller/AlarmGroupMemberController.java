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

import org.jfaster.derror.manager.pojo.dto.AlarmGroupMemberDTO;
import org.jfaster.derror.manager.service.IAlarmGroupMemberService;
import org.jfaster.derror.manager.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangnan
 * 报警组成员管理
 */
@RestController
@RequestMapping("/group/member")
public class AlarmGroupMemberController {

    @Autowired
    private IAlarmGroupMemberService alarmGroupMemberService;

    /**
     * 加载所有元素
     *
     * @return
     */
    @GetMapping("/load")
    public ApiResponse load() {
        return ApiResponse.success(alarmGroupMemberService.load());
    }

    /**
     * 获取单个配置
     *
     * @return
     */
    @PostMapping("/getMember")
    public ApiResponse getMember(@RequestBody AlarmGroupMemberDTO memberDTO) {
        return ApiResponse.success(alarmGroupMemberService.getMember(memberDTO));
    }

    /**
     * 删除
     *
     * @return
     */
    @PostMapping("/delete")
    public ApiResponse delete(@RequestBody AlarmGroupMemberDTO memberDTO) {
        return ApiResponse.success(alarmGroupMemberService.delete(memberDTO));
    }

    /**
     * 添加修改
     *
     * @return
     */
    @PostMapping("/addAndUpdate")
    public ApiResponse addAndUpdate(@RequestBody AlarmGroupMemberDTO memberDTO) {
        return ApiResponse.success(alarmGroupMemberService.addAndUpdate(memberDTO));
    }

    /**
     * 获取报警组
     *
     * @return
     */
    @GetMapping("/getGroups")
    public ApiResponse getGroups() {
        return ApiResponse.success(alarmGroupMemberService.getGroups());
    }

    /**
     * 判断手机号是否存在
     *
     * @return
     */
    @PostMapping("/phone/existed")
    public ApiResponse phoneExisted(@RequestBody AlarmGroupMemberDTO memberDTO) {
        return ApiResponse.success(alarmGroupMemberService.phoneExisted(memberDTO));
    }

    /**
     * 判断手机号是否存在
     *
     * @return
     */
    @PostMapping("/mail/existed")
    public ApiResponse mailExisted(@RequestBody AlarmGroupMemberDTO memberDTO) {
        return ApiResponse.success(alarmGroupMemberService.mailExisted(memberDTO));
    }

}
