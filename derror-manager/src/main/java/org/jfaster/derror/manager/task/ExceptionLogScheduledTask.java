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

package org.jfaster.derror.manager.task;

import org.jfaster.derror.manager.dao.AppConfigDAO;
import org.jfaster.derror.manager.dao.ExceptionLogDAO;
import org.jfaster.derror.manager.pojo.domain.AppConfigDO;
import org.jfaster.derror.manager.utils.DateUtil;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description: 删除日志task
 * @author: Amos
 * @create: 2018-07-18 13:34
 **/
@Component
public class ExceptionLogScheduledTask {

    @Autowired
    private AppConfigDAO appConfigDAO;


    @Autowired
    private ExceptionLogDAO exceptionLogDAO;

    @Scheduled(cron = "0 0 1 * * ?")
    public void reportCurrentTime() {
        //查询所有配置
        List<AppConfigDO> appConfigs = appConfigDAO.load();
        if (CollectionUtils.isEmpty(appConfigs)) {
            return;
        }
        appConfigs.stream().forEach(r->{
            Integer retain = r.getRetain();
            Date date = DateUtil.minusRetainDays(new Date(), retain);
            Date plusDate = DateUtil.plusRetainDays(date, 1);
            exceptionLogDAO.delExceptionLogs(DateUtil.format(date, DateUtil.YYYY_MM_DD),DateUtil.format(plusDate, DateUtil.YYYY_MM_DD));
        });

    }


}
