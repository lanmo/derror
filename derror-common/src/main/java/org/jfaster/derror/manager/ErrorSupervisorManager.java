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

package org.jfaster.derror.manager;


import org.jfaster.derror.alarm.Alarm;
import org.jfaster.derror.alarm.AlarmEvent;
import org.jfaster.derror.alarm.AlarmMember;
import org.jfaster.derror.alarm.ext.AlarmClassLoader;
import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.config.LoggingEvent;
import org.jfaster.derror.constant.DerrorConstant;
import org.jfaster.derror.exception.DerrorFrameException;
import org.jfaster.derror.exception.DerrorTimeoutException;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.remote.IDownloadRemoteService;
import org.jfaster.derror.remote.RemoteWorker;
import org.jfaster.derror.remote.config.AlarmGroupMember;
import org.jfaster.derror.remote.config.AppConfig;
import org.jfaster.derror.remote.config.ModeConfig;
import org.jfaster.derror.remote.config.RemoteConfig;
import org.jfaster.derror.util.ClassUtil;
import org.jfaster.derror.util.ExceptionUtil;
import org.jfaster.derror.util.NetUtil;
import org.jfaster.derror.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.Data;

/**
 * 异常管理类
 * @author yangnan
 */
@Data
public final class ErrorSupervisorManager {

    private static final InternalLogger LOGGER = InternalLoggerFactory
            .getLogger(ErrorSupervisorManager.class);

    private final BlockingQueue<AlarmEvent> queue;
    private final ThreadPoolExecutor executor;
    private final IDownloadRemoteService downloadRemoteService;
    private AlarmWorker worker;
    private volatile boolean close = false;

    public ErrorSupervisorManager(BlockingQueue<AlarmEvent> queue, ThreadPoolExecutor executor,
            IDownloadRemoteService downloadRemoteService) {
        this.queue = queue;
        this.executor = executor;
        this.downloadRemoteService = downloadRemoteService;
        this.worker = new AlarmWorker();
        new Thread(this.worker).start();
    }

    public void shutdown() {
        close = true;
        if (executor != null) {
            executor.shutdown();
        }
    }

    public void putError(ClientConfig config, LoggingEvent event) {

        if (event.getThrowable() == null) {
            return;
        }
        if (event.getThrowable() instanceof DerrorFrameException) {
            return;
        }
        if (event.getThrowable() instanceof DerrorTimeoutException) {
            return;
        }

        String appName = config.getAppName();
        if (StringUtil.isEmpty(appName)) {
            return;
        }

       RemoteConfig remoteConfig = ConfigManager.getConfigFromCache(appName);
        if (remoteConfig == null) {
            LOGGER.debug("remoteConfig is null");
            return;
        }
        if (remoteConfig.getAppConfig() == null) {
            LOGGER.debug("appConfigVO is null");
            return;
        }
        if (remoteConfig.getAppConfig().getId() == null) {
            LOGGER.debug("appId is null");
            return;
        }

        try {
            executor.submit(new RemoteWorker(downloadRemoteService, event, config, remoteConfig));
        } catch (Exception e) {
            LOGGER.debug("executor reject");
        }

        AlarmEvent alarmEvent = new AlarmEvent(event, config.getSystem(), appName, config.getEnv());
        boolean alarmSwitch = alarmSwitch(remoteConfig, alarmEvent);
        if (alarmSwitch) {
            queue.offer(alarmEvent);
        }
    }

    /**
     * 获取开关状态
     *
     * @param remoteConfig
     * @param alarmEvent
     * @return
     */
    private boolean alarmSwitch(RemoteConfig remoteConfig, AlarmEvent alarmEvent) {
        if (remoteConfig == null) {
            return false;
        }
        AppConfig appConfig = remoteConfig.getAppConfig();
        if (appConfig == null || appConfig.getStatus() == DerrorConstant.CLOSE) {
            return false;
        }
        ModeConfig modeConfig = remoteConfig.getModeConfig();
        if (modeConfig == null || modeConfig.getAlarmSwitch() == DerrorConstant.CLOSE) {
            return false;
        }

        boolean mailAlarmSwitch = modeConfig.getMailAlarmSwitch() == DerrorConstant.OPEN;
        boolean dingAlarmSwitch = modeConfig.getDingAlarmSwitch() == DerrorConstant.OPEN;
        boolean smsAlarmSwitch = modeConfig.getSmsAlarmSwitch() == DerrorConstant.OPEN;
        boolean alarmClientSwitch = modeConfig.getAlarmClientSwitch() == DerrorConstant.OPEN;
        boolean alarmServerSwitch = modeConfig.getAlarmServerSwitch() == DerrorConstant.OPEN;
        //以服务器端的报警为主
        if (alarmServerSwitch) {
            return false;
        }
        //客户端报警开关关闭
        if (!alarmClientSwitch) {
            return false;
        }
        //开关都关闭
        if (!mailAlarmSwitch && !dingAlarmSwitch && !smsAlarmSwitch) {
            return false;
        }
        List<Class<?>> filters = remoteConfig.getFilterExceptions();
        if (ClassUtil.contains(filters, alarmEvent.getLoggingEvent().getThrowable().getClass())) {
            return false;
        }
        alarmEvent.setSmsAlarmSwitch(smsAlarmSwitch);
        alarmEvent.setMailAlarmSwitch(mailAlarmSwitch);
        alarmEvent.setDingAlarmSwitch(dingAlarmSwitch);
        return true;
    }

    private class AlarmWorker implements Runnable {
        @Override
        public void run() {
            int count = 0;
            int lastTime = 0;
            while (!close && !Thread.currentThread().isInterrupted()) {
                try {
                    AlarmEvent event = queue.take();
                    RemoteConfig remoteConfig = ConfigManager.getConfigFromCache(event.getAppName());
                    if (remoteConfig == null) {
                        continue;
                    }

                    boolean alarmSwitch = alarmSwitch(remoteConfig, event);
                    if (!alarmSwitch) {
                        continue;
                    }
                   AppConfig config = remoteConfig.getAppConfig();
                    int currentTime = getCurrentTime(config.getFrequencyTime());
                    if (currentTime != lastTime) {
                        count = 0;
                        lastTime = currentTime;
                    }

                    //报警数大于每分钟的数量 不报警
                    if (++count > config.getFrequency()) {
                        continue;
                    }
                    event.setHots(NetUtil.resolveLocalIps());
                    event.setMembers(copyMembers(remoteConfig.getMembers()));
                    alarm(event);
                } catch (Exception e) {
                    LOGGER.error("e.message:{}", e.getMessage(), ExceptionUtil.handleException(e));
                }
            }
        }
    }

    /**
     * 复制报警组成员
     *
     * @param members
     * @return
     */
    private List<AlarmMember> copyMembers(List<AlarmGroupMember> members) {
        List<AlarmMember> ms = new ArrayList<AlarmMember>();
        if (members == null) {
            return ms;
        }
        for (AlarmGroupMember groupMembers : members) {
            AlarmMember member = new AlarmMember();
            member.setMail(groupMembers.getMail());
            member.setPhone(groupMembers.getPhone());
            member.setUserName(groupMembers.getUserName());
            ms.add(member);
        }
        return ms;
    }

    /**
     * 获取当前时间
     * @param frequencyTime
     * @return
     */
    private int getCurrentTime(int frequencyTime) {
        return (int) (System.currentTimeMillis() / frequencyTime / 1000);
    }

    /***
     * 报警
     * @param event
     */
    private void alarm(AlarmEvent event) {
        List<Alarm> alarms = AlarmClassLoader.getClassLoader(Alarm.class).getExtensions();
        if (alarms == null || alarms.isEmpty()) {
            return;
        }
        for (Alarm alarm : alarms) {
            try {
                if (alarm.preHandle(event)) {
                    alarm.alarm(event);
                }
            } finally {
                alarm.complete(event);
            }
        }
    }

}
