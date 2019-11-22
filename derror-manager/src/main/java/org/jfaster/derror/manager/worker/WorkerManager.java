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

import org.jfaster.derror.manager.utils.NameThreadFactory;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:线程池管理
 * @author: Amos
 * @create: 2018-07-09 17:01
 **/
public class WorkerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerManager.class);
    private volatile static ThreadPoolExecutor executor;

    private static void init() {
        if (executor == null) {
            synchronized (WorkerManager.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(10, 30, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(200),
                            new NameThreadFactory("exceptionAlarm"));
                    executor.allowCoreThreadTimeOut(true);
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (executor != null && executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdown();
            }
        } catch (InterruptedException e) {
            LOGGER.error("exception alarm worker manager destroy error", e);
        }
    }

    public static void submit(AbstractWorker worker) {
        init();
        executor.submit(worker);
    }


}
