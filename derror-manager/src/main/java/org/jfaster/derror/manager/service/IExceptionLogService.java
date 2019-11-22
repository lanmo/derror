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

package org.jfaster.derror.manager.service;

import org.jfaster.derror.manager.pojo.dto.dingding.ExceptionLogDTO;
import org.jfaster.derror.manager.pojo.vo.ExceptionLogStaticVO;
import org.jfaster.derror.manager.pojo.vo.ExceptionLogVO;

import java.util.List;

/**
 * @program: derror
 * @description: 异常信息
 * @author: Amos.Wxz
 * @create: 2018-07-13 15:58
 **/
public interface IExceptionLogService {

    /**
     * 查询异常列表
     * @param exceptionLogDTO
     * @return
     */
    List<ExceptionLogVO> query(ExceptionLogDTO exceptionLogDTO);

    /**
     * 异常统计
     */
    List<ExceptionLogStaticVO> statistics();
}
