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

package org.jfaster.derror.manager.worker;

import org.jfaster.derror.manager.pojo.dto.ExceptionDTO;
import org.jfaster.derror.manager.service.IExceptionAlarmService;
import org.jfaster.derror.manager.utils.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 异常报警worker
 * @author: Amos
 * @create: 2018-07-09 16:45
 **/
public class ExceptionAlarmWorker extends AbstractWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAlarmWorker.class);

    private ExceptionDTO exceptionDTO;

    private IExceptionAlarmService exceptionAlarmService;


    public ExceptionAlarmWorker(ExceptionDTO exceptionDTO) {
        this.exceptionAlarmService = SpringContext.getBean(IExceptionAlarmService.class);
        this.exceptionDTO = exceptionDTO;
    }

    @Override
    protected void execute() {
        try {
            exceptionAlarmService.alarm(exceptionDTO);
        } catch (Exception e) {
            LOGGER.error("exception alarm thread error", e);
        }
    }
}
