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

package org.jfaster.derror.manager.service.impl;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.jfaster.derror.manager.dao.ExceptionLogDAO;
import org.jfaster.derror.manager.enums.SwitchEnum;
import org.jfaster.derror.manager.local.AlarmGroupConfigSupport;
import org.jfaster.derror.manager.local.AlarmGroupMembersSupport;
import org.jfaster.derror.manager.local.AlarmMailConfigSupport;
import org.jfaster.derror.manager.local.AppConfigSupport;
import org.jfaster.derror.manager.local.ModeConfigSupport;
import org.jfaster.derror.manager.pojo.domain.AlarmGroupConfigDO;
import org.jfaster.derror.manager.pojo.domain.AlarmGroupMembersDO;
import org.jfaster.derror.manager.pojo.domain.AlarmMailConfigDO;
import org.jfaster.derror.manager.pojo.domain.AlarmModeConfigDO;
import org.jfaster.derror.manager.pojo.domain.AppConfigDO;
import org.jfaster.derror.manager.pojo.domain.ExceptionLogDO;
import org.jfaster.derror.manager.pojo.dto.ExceptionDTO;
import org.jfaster.derror.manager.pojo.dto.dingding.DingTextDTO;
import org.jfaster.derror.manager.service.IExceptionAlarmService;
import org.jfaster.derror.manager.utils.DingDingUtil;
import org.jfaster.derror.manager.utils.PatternUtil;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import static org.jfaster.derror.manager.constant.CommonConstant.*;

/**
 * @description: 异常报警
 * @author: Amos
 * @create: 2018-07-04 18:17
 **/
@Service
public class ExceptionAlarmServiceImpl implements IExceptionAlarmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAlarmServiceImpl.class);

    @Autowired
    private AlarmGroupMembersSupport alarmGroupMembersSupport;

    @Autowired
    private AlarmGroupConfigSupport alarmGroupConfigSupport;

    @Autowired
    private AlarmMailConfigSupport alarmMailConfigSupport;

    @Autowired
    private ExceptionLogDAO exceptionLogDAO;

    @Autowired
    private AppConfigSupport appConfigSupport;

    @Autowired
    private ModeConfigSupport modeConfigSupport;

    @Value("${spring.profiles.active}")
    private String env;


    @Override
    public void alarm(ExceptionDTO exceptionDTO) {
        Long appId = exceptionDTO.getAppId();
        if (appId == null) {
            LOGGER.warn("alarm appId is null  appId={}", appId);
            return;
        }
        AppConfigDO appConfigDO = appConfigSupport.queryAppConfigById(appId);
        if (appConfigDO == null) {
            LOGGER.warn("alarm appConfig  is null  appId={}", appId);
            return;
        }
        RateLimiter rateLimiter = AppConfigSupport.concurrentHashMap.get(appId);
        if (!rateLimiter.tryAcquire()) {
            LOGGER.warn("alarm get rateLimiter fail appId={}", appId);
            return;
        }

        AlarmModeConfigDO alarmModeConfigDO = modeConfigSupport.queryAlarmModeAppById(appId);
        //获取报警配置
        if (alarmModeConfigDO == null) {
            LOGGER.warn("alarm mode config is null ");
            return;
        }
        if (alarmSwitch(alarmModeConfigDO)) {
            return;
        }

        Map<String, String> map = objectToMap(exceptionDTO, appConfigDO);
        //钉钉报警
        alarmDing(alarmModeConfigDO, map);
        //邮件报警
        alarmMail(alarmModeConfigDO, map);
        //插入异常信息
        ExceptionLogDO exceptionLogDO = new ExceptionLogDO();
        BeanUtils.copyProperties(exceptionDTO, exceptionLogDO);
        exceptionLogDO.setCreateDate(new Date());
        exceptionLogDO.setUpdateDate(new Date());
        exceptionLogDO.setAppName(appConfigDO.getAppName());
        exceptionLogDO.setEnv(env);
        exceptionLogDAO.add(exceptionLogDO);

    }

    private Map<String, String> objectToMap(ExceptionDTO exceptionDTO, AppConfigDO appConfigDO) {
        Map<String, String> valueMap = Maps.newHashMap();
        valueMap.put(APP_NAME, appConfigDO.getAppName());
        valueMap.put(ENV, env);
        valueMap.put(HOST, exceptionDTO.getHost());
        valueMap.put(TRACE_ID, exceptionDTO.getTraceId());
        valueMap.put(EXCEPTION_MSG, exceptionDTO.getExceptionMsg());
        putValue(exceptionDTO.getExt(), valueMap);
        putValue(exceptionDTO.getMdcValue(), valueMap);
        return valueMap;
    }

    /**
     * 设置扩展属性
     *
     * @param value
     * @param valueMap
     */
    private void putValue(String value, Map<String, String> valueMap) {
        String[] mdcValues = StringUtils.split(value, "&");
        if (mdcValues == null || mdcValues.length <=0) {
            return;
        }
        for (String mdcValue : mdcValues) {
            String[] vs = StringUtils.split(mdcValue, "=");
            if (vs != null && vs.length == 2) {
                valueMap.put(vs[0], vs[1]);
            }
        }
    }

    /**
     * 判断开关
     *
     * @param alarmModeConfigDO
     * @return
     */
    private boolean alarmSwitch(AlarmModeConfigDO alarmModeConfigDO) {
        Integer alarmSwitch = alarmModeConfigDO.getAlarmSwitch();
        Integer alarmServerSwitch = alarmModeConfigDO.getAlarmServerSwitch();
        if (SwitchEnum.CLOSE.getSwitchType().equals(alarmSwitch) || SwitchEnum.CLOSE.getSwitchType()
                .equals(alarmServerSwitch)) {
            LOGGER.warn("alarm switch is closed");
            return true;
        }
        return false;
    }

    /**
     * 邮件报警
     */
    private void alarmMail(AlarmModeConfigDO alarmModeConfigDO, Map<String, String> map) {
        if (SwitchEnum.CLOSE.getSwitchType().equals(alarmModeConfigDO.getMailAlarmSwitch())) {
            LOGGER.warn("mail alarm is closed");
            return;
        }
        //获取报警组
        Integer alarmGroupId = alarmModeConfigDO.getAlarmGroupId();
        AlarmGroupConfigDO alarmGroupConfigDO = alarmGroupConfigSupport.queryAlarmGroupConfigById(alarmGroupId);
        if (alarmGroupConfigDO == null) {
            LOGGER.warn("alarm group config is null ");
            return;
        }
        Integer alarmGroupConfigId = alarmGroupConfigDO.getId();
        //获取报警组成员
        List<AlarmGroupMembersDO> membersByGroups = alarmGroupMembersSupport.getMembersByGroupId(alarmGroupConfigId);
        if (CollectionUtils.isEmpty(membersByGroups)) {
            LOGGER.warn("alarm group members is null ");
            return;
        }
        List<String> receivers = membersByGroups.stream().map(r -> r.getMail()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(receivers)) {
            LOGGER.warn("alarm group members mail is null ");
            return;
        }
        String[] mailMembers = receivers.toArray(new String[receivers.size()]);
        buildMailSender(mailMembers, alarmModeConfigDO, map);
    }

    private void buildMailSender(String[] mailMembers, AlarmModeConfigDO alarmModeConfigDO, final Map<String, String> map) {
        //获取邮件信息配置
        Integer alarmModeConfigId = alarmModeConfigDO.getId();
        List<AlarmMailConfigDO> alarmMailInfo = alarmMailConfigSupport.getAlarmMailInfo(alarmModeConfigId);
        if (CollectionUtils.isEmpty(alarmMailInfo)) {
            LOGGER.warn("alarm get mail config is null modeConfigId={}", alarmModeConfigId);
            return;
        }
        alarmMailInfo.forEach(r -> {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(r.getHost());
            sender.setPassword(r.getPassword());
            sender.setPort(r.getPort());
            sender.setDefaultEncoding("UTF-8");
            sender.setUsername(r.getUsername());
            Properties javaMailProperties = new Properties();
            javaMailProperties.setProperty("mail.smtp.auth", "false");
            javaMailProperties.setProperty("mail.smtp.timeout", "25000");
            sender.setJavaMailProperties(javaMailProperties);
            if (sender == null) {
                return;
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(alarmModeConfigDO.getMailTitle());
            message.setTo(mailMembers);
            message.setFrom(sender.getUsername());
            message.setText(PatternUtil.getKey(alarmModeConfigDO.getMailContent(), map));
            //发送邮件
            sender.send(message);
        });
    }

    /**
     * 钉钉报警
     */
    private void alarmDing(AlarmModeConfigDO alarmModeConfigDO, Map<String, String> map) {
        try {
            if (SwitchEnum.CLOSE.getSwitchType().equals(alarmModeConfigDO.getDingAlarmSwitch())) {
                LOGGER.warn("ding alarm is closed");
                return;
            }
            DingTextDTO dingTextDTO = new DingTextDTO();
            String dingContent = alarmModeConfigDO.getDingContent();
            dingTextDTO.setContent(PatternUtil.getKey(dingContent, map));
            DingDingUtil.sendMessage(dingTextDTO, alarmModeConfigDO.getDingUrl());
        } catch (Exception e) {
            LOGGER.error("ding alarm is error ", e);
        }
    }

}
