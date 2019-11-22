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

import org.jfaster.derror.manager.pojo.dto.FilterExceptionDTO;
import org.jfaster.derror.manager.pojo.vo.AppConfigurationVO;
import org.jfaster.derror.manager.pojo.vo.FilterExceptionVO;

import java.util.List;

/**
 * @author yangnan
 */
public interface IFilterExceptionService {

    /**
     * 获取列表
     * @return
     * @param exceptionDTO
     */
    List<FilterExceptionVO> load(FilterExceptionDTO exceptionDTO);

    /**
     * 添加和更新
     *
     * @param exceptionDTO
     * @return
     */
    Integer addAndUpdate(FilterExceptionDTO exceptionDTO);

    /**
     * 获取异常
     * @param exceptionDTO
     * @return
     */
    FilterExceptionVO getException(FilterExceptionDTO exceptionDTO);

    /**
     * 删除异常
     * @param exceptionDTO
     * @return
     */
    Integer delete(FilterExceptionDTO exceptionDTO);

    /**
     * 获取应用配置
     *
     * @return
     */
    List<AppConfigurationVO> getApps();

    /**
     * 修改状态
     * @param exceptionDTO
     * @return
     */
    Integer changeStatus(FilterExceptionDTO exceptionDTO);

}
