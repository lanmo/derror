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

package org.jfaster.derror.manager.logback.plugin.test;

import org.jfaster.derror.manager.CacheManager;
import org.jfaster.derror.util.ExtendUtil;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ErrorTest {

    private static Logger logger = LoggerFactory.getLogger(ErrorTest.class);

    public static void main(String[] args) {
        errorTest();
    }
    public static void errorTest() {
        MDC.put("traceId", "ddddd");
        ExtendUtil.put("url", "http://localhost:8080");
        ExtendUtil.put("uid", "3");
        for (int i=0; i<5; i++) {
            try {
                int a = 1 / 0;
            } catch (Exception e) {
                logger.error("sadas", e);
            }
            try {
                System.out.println(ExtendUtil.getValue());
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ExtendUtil.clear();
        System.out.println(MDC.getCopyOfContextMap());
    }

    @Test
    public void testUnResolveClass() {
        for (int i=0; i<150; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CacheManager.addUnResolveClass(i + "");
        }
    }
}
