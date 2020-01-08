package org.jfaster.derror.log4j2.plugin.event.test;
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

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.util.ExtendUtil;

/**
 * @author yangnan
 */
public class Log4j2EventTest {

    private static Logger logger = LogManager.getLogger(Log4j2EventTest.class);

    public static void main(String[] args) {
        String traceId = UUID.randomUUID().toString();
        ThreadContext.put("traceId", traceId);
        ThreadContext.put("orderNo", "123456");
        ExtendUtil.put("url", "http://1234567");
        ExtendUtil.put("uid", "22222222");

        for (int i=0; i<5; i++) {
            try {
                int a = 1/0;
            } catch (Exception e) {
                logger.error("异常", e);
            }
            try {
                System.out.println(ExtendUtil.getValue());
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ExtendUtil.clear();
        System.out.println(ExtendUtil.getValue());
        System.out.println(ThreadContext.getContext());
    }
}
