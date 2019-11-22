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

package org.jfaster.derror.alarm;

/**
 * 报警接口
 * @author yangnan
 */
public interface Alarm {

    /**
     * 前置处理
     *
     * @param event
     * @return
     */
    boolean preHandle(AlarmEvent event);

    /**
     * 报警
     * @param event
     */
    void alarm(AlarmEvent event);

    /**
     * 后置处理
     * @param event
     */
    void complete(AlarmEvent event);
}
