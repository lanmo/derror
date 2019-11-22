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

package org.jfaster.derror.manager.pojo.vo;

import java.util.List;
import lombok.Data;

/**
 * @author yangnan
 */
@Data
public class AlarmModeConfigVO {

    private Integer id;
    private Integer appId;
    private String appName;
    /**
     * 报警组名称
     */
    private String name;
    /**
     * 总开关 1开启 0关闭
     */
    private Integer alarmSwitch;
    /**
     * 报警组
     */
    private String alarmGroup;
    /**
     * 报警组
     */
    private Integer alarmGroupId;
    /**
     * 1开启 0关闭
     */
    private Integer mailAlarmSwitch;
    /**
     * 1开启 0关闭
     */
    private Integer smsAlarmSwitch;
    /**
     * 1开启 0关闭
     */
    private Integer dingAlarmSwitch;
    /**
     * 1开启 0关闭	 服务端报警优先使用默认开启
     */
    private Integer alarmClientSwitch;
    /**
     * 1开启 0关闭 乘客端报警
     */
    private Integer alarmServerSwitch;
    /**
     * 短信内容
     */
    private String smsContent;
    /**
     * 邮件标题
     */
    private String mailTitle;
    /**
     * 钉钉报警内容
     */
    private String dingContent;
    /**
     * 钉钉报警内容
     */
    private String mailContent;
    /**
     * 描述
     */
    private String remark;
    /**
     * 创建时间
     */
    private String createDate;
    /**
     * 更新时间
     */
    private String updateDate;
    /**
     * 钉钉url
     */
    private String dingUrl;
    /**
     * 报警组
     */
    private List<AlarmGroupConfigVO> alarmGroups;
    /**
     * 角色id
     */
    private Integer roleId;
}
