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
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.jfaster.derror.manager.dao.AppConfigDAO;
import org.jfaster.derror.manager.pojo.domain.AppConfigDO;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 查询配置cache
 * @author: Amos
 * @create: 2018-07-10 09:50
 **/
@Service
public class AppConfigSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfigSupport.class);

    @Autowired
    private AppConfigDAO appConfigDAO;

    public static ConcurrentMap<Long, RateLimiter> concurrentHashMap = Maps.newConcurrentMap();


    private LoadingCache<Long, Optional<AppConfigDO>> cache = CacheBuilder.newBuilder()
            .maximumSize(500).refreshAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, Optional<AppConfigDO>>() {
                @Override
                public Optional<AppConfigDO> load(Long appId) {
                    AppConfigDO appConfigDO = appConfigDAO.queryAppConfigById(appId);
                    Integer frequency = appConfigDO.getFrequency();
                    Integer frequencyTime = appConfigDO.getFrequencyTime();
                    if (frequency != null && frequencyTime != null) {
                        RateLimiter rateLimiter = RateLimiter.create(frequencyTime / frequency);
                        concurrentHashMap.put(appId, rateLimiter);
                    }
                    return Optional.ofNullable(appConfigDO);
                }
            });

    public AppConfigDO queryAppConfigById(Long appId) {
        try {
            Optional<AppConfigDO> appConfigDO = cache.get(appId);
            return appConfigDO.isPresent() ? appConfigDO.get() : null;
        } catch (ExecutionException e) {
            LOGGER.error("alarm get app config  loadCache error", e);
        }
        return null;
    }

}
