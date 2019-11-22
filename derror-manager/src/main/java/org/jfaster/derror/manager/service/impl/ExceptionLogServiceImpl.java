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

import org.jfaster.derror.manager.dao.AppConfigDAO;
import org.jfaster.derror.manager.dao.ExceptionLogDAO;
import org.jfaster.derror.manager.pojo.dto.dingding.ExceptionLogDTO;
import org.jfaster.derror.manager.pojo.vo.ExceptionLogStaticVO;
import org.jfaster.derror.manager.pojo.vo.ExceptionLogVO;
import org.jfaster.derror.manager.service.IExceptionLogService;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Amos
 * @create: 2018-07-13 16:07
 **/
@Service
public class ExceptionLogServiceImpl implements IExceptionLogService {

    @Autowired
    private ExceptionLogDAO exceptionLogDAO;

    @Autowired
    private AppConfigDAO appConfigDAO;

    @Override
    public List<ExceptionLogVO> query(ExceptionLogDTO exceptionLogDTO) {
        String host = exceptionLogDTO.getHost();
        if (StringUtils.isNotEmpty(host)) {
            exceptionLogDTO.setHost(host + "%");
        }
        List<ExceptionLogVO> exceptionLogVOS = exceptionLogDAO.queryExceptions(exceptionLogDTO);
        if(CollectionUtils.isEmpty(exceptionLogVOS)){
            return Collections.emptyList();
        }

        exceptionLogVOS.parallelStream().forEach(r -> {
            r.setContent(StringUtils.replace(r.getContent(), "\n", "</br>"));
            r.setContent(StringUtils.replace(r.getContent(), "\t", "&nbsp;&nbsp;&nbsp;&nbsp;"));
        });

        return exceptionLogVOS;
    }

    @Override
    public List<ExceptionLogStaticVO> statistics() {
        return exceptionLogDAO.queryExceptionStatis();
    }
}
