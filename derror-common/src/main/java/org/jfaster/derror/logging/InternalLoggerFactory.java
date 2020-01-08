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

package org.jfaster.derror.logging;

/**
 * logger工厂类
 *
 * @author yangnan
 */
public abstract class InternalLoggerFactory {

    /**默认工厂类*/
    private static volatile InternalLoggerFactory defaultFactory;

    static {
        String name = InternalLoggerFactory.class.getName();
        InternalLoggerFactory factory = null;
        try {
            factory = new Slf4JLoggerFactory(true);
            factory.newInstance(name).debug("Using SLF4J as the default logging framework");
        } catch (Throwable e) {
            try {
                factory = Log4J2LoggerFactory.INSTANCE;
                factory.newInstance(name).debug("Using Log4J2 as the default logging framework");
            } catch (Throwable var6) {
                try {
                    factory = Log4JLoggerFactory.INSTANCE;
                    factory.newInstance(name).debug("Using Log4J as the default logging framework");
                } catch (Throwable var5) {
                    factory = NoLoggerFactory.INSTANCE;
                }
            }
        }
        defaultFactory = factory;
    }

    /**
     * Returns the default factory.  The initial default factory is
     * @return
     */
    private static InternalLoggerFactory getDefaultFactory() {
        return defaultFactory;
    }

    /**
     * Changes the default factory.
     * @param defaultFactory
     */
    public static void setDefaultFactory(InternalLoggerFactory defaultFactory) {
        if (defaultFactory != null) {
            InternalLoggerFactory.defaultFactory = defaultFactory;
        }
    }

    /**
     * Creates a new logger instance with the specified name.
     * @return
     */
    public static InternalLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * Creates a new logger instance with the specified name.
     * @return
     */
    public static InternalLogger getLogger(String name) {
        return getDefaultFactory().newInstance(name);
    }

    /**
     * Creates a new logger instance with the specified name.
     * @param name
     * @return
     */
    protected abstract InternalLogger newInstance(String name);
}
