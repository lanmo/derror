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

package org.jfaster.derror.manager.local;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jfaster.derror.manager.dao.AlarmMailConfigDAO;
import org.jfaster.derror.manager.pojo.domain.AlarmMailConfigDO;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 查询邮件配置
 * @author: Amos
 * @create: 2018-07-10 10:46
 **/
@Service
public class AlarmMailConfigSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmMailConfigSupport.class);

    @Autowired
    private AlarmMailConfigDAO alarmMailConfigDAO;


    private LoadingCache<Integer, Optional<List<AlarmMailConfigDO>>> cache = CacheBuilder.newBuilder()
            .maximumSize(500).refreshAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, Optional<List<AlarmMailConfigDO>>>() {
                @Override
                public Optional<List<AlarmMailConfigDO>> load(Integer appId) {
                    List<AlarmMailConfigDO> alarmMailInfo = alarmMailConfigDAO.getAlarmMailInfo(appId);
                    return Optional.ofNullable(alarmMailInfo);
                }
            });

    public List<AlarmMailConfigDO> getAlarmMailInfo(Integer appId) {
        try {
            Optional<List<AlarmMailConfigDO>> alarmMailConfigDO = cache.get(appId);
            return alarmMailConfigDO.isPresent() ? alarmMailConfigDO.get() : null;
        } catch (ExecutionException e) {
            LOGGER.error("alarm get mail config  loadCache error", e);
        }
        return null;
    }

}
