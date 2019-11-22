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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Amos.Wxz
 */
@Controller
public class PageController {

    @RequestMapping("/index/page")
    public String indexPage() {
        return "index";
    }

    @RequestMapping("/auth/project_center/page")
    public String projectCenter() {
        return "auth/project_center";
    }

    @RequestMapping("/auth/user_center/page")
    public String userCenter() {
        return "auth/user_center";
    }

    @RequestMapping("/query/query_exception/page")
    public String queryException() {
        return "query/query_exception";
    }

    @RequestMapping("/global/appconfig_center/page")
    public String appconfigCenter() {
        return "global/appconfig_center";
    }

    @RequestMapping("/global/alarm_mode/page")
    public String alarmMode() {
        return "global/alarm_mode";
    }

    @RequestMapping("/global/filter_exception/page")
    public String filterException() {
        return "global/filter_exception";
    }

    @RequestMapping("/global/alarm_group/page")
    public String alarmGroup() {
        return "global/alarm_group";
    }

    @RequestMapping("/global/alarm_group_member/page")
    public String alarmGroupMember() {
        return "global/alarm_group_member";
    }

    @RequestMapping("/help/help/page")
    public String help() {
        return "help/help";
    }

    @RequestMapping("/global/alarm_mail/page")
    public String alarmMail() {
        return "global/alarm_mail";
    }

    @RequestMapping("/query/statistics/page")
    public String statistics() {
        return "query/statistics";
    }

}
