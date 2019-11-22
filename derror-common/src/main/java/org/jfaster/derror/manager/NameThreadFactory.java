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

package org.jfaster.derror.manager;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yangnan
 */
public class NameThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private ThreadGroup threadGroup;
    private String namePrefix;
    private boolean daemon;
    private int priority;
    private String name;

    public NameThreadFactory(String name, boolean daemon, int priority) {

        if (priority > Thread.MAX_PRIORITY || priority<Thread.MIN_PRIORITY) {
            throw new IllegalArgumentException("priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
        }

        this.name = name;
        this.daemon = daemon;
        this.priority = priority;
        SecurityManager sm = System.getSecurityManager();
        this.threadGroup = sm != null ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-" + name;
    }

    public NameThreadFactory(String name) {
        this(name, false, Thread.NORM_PRIORITY);
    }

    public NameThreadFactory(String name, boolean daemon) {
       this(name, daemon, Thread.NORM_PRIORITY);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(this.threadGroup, r, this.namePrefix + "-" + this.threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) {
            if (!this.daemon) {
                t.setDaemon(false);
            }
        } else if (t.isDaemon()){
            t.setDaemon(true);
        }

        if (t.getPriority() != this.priority) {
            t.setPriority(Thread.NORM_PRIORITY);
        }

        return t;
    }

    public String getName() {
        return name;
    }
}
