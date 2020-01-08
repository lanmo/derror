/*
 *  Copyright 2018 derror.jfaster.org
 *
 *  The Derror Project licenses this file to you under the Apache License,
 *  version 2.0 (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.jfaster.derror.log4j.plugin.event.test;

import java.util.UUID;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.jfaster.derror.util.ExtendUtil;

public class Log4jTest {

    private static Logger logger = LogManager.getLogger(Log4jTest.class);

    public static void main(String[] args) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        ExtendUtil.put("abc", "111111");
        ExtendUtil.put("ddd", "2222");
        for (int i=0; i<5; i++) {
            try {
                System.out.println(ExtendUtil.getValue());
                int a = 1/0;
            } catch (Exception e) {
                logger.error("异常", e);
            }
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ExtendUtil.clear();
        System.out.println(ExtendUtil.getValue());
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
