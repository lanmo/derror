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

import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jfaster.derror.manager.dao.FilterExceptionConfigDAO;
import org.jfaster.derror.manager.pojo.vo.FilterExceptionConfigVO;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 忽略异常报警本地缓存
 * @author: Amos
 * @create: 2018-07-09 18:28
 **/
@Service
public class FilterExceptionConfigSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterExceptionConfigSupport.class);

    @Autowired
    private FilterExceptionConfigDAO filterExceptionConfigDAO;

    private LoadingCache<String, Optional<List<FilterExceptionConfigVO>>> cache = CacheBuilder.newBuilder()
            .maximumSize(500).refreshAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Optional<List<FilterExceptionConfigVO>>>() {
                @Override
                public Optional<List<FilterExceptionConfigVO>> load(String appIdAndStatus) throws Exception {
                    List<String> str = Splitter.on(",").splitToList(appIdAndStatus);
                    List<FilterExceptionConfigVO> filterExceptions = filterExceptionConfigDAO
                            .getFilterExceptions(Long.valueOf(str.get(0)), Integer.valueOf(str.get(1)));
                    return Optional.ofNullable(filterExceptions);
                }
            });

    public List<FilterExceptionConfigVO> queryFilterException(String appIdAndStatus) {
        try {
            Optional<List<FilterExceptionConfigVO>> filterExceptionConfigVOS = cache.get(appIdAndStatus);
            return filterExceptionConfigVOS.isPresent() ? filterExceptionConfigVOS.get() : null;
        } catch (ExecutionException e) {
            LOGGER.error("alarm get filter exception loadCache error", e);
        }
        return null;
    }

}
