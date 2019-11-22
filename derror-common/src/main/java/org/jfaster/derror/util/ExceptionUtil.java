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

package org.jfaster.derror.util;

import org.jfaster.derror.constant.DerrorConstant;
import org.jfaster.derror.exception.DerrorFrameException;
import org.jfaster.derror.exception.DerrorTimeoutException;

/**
 * 异常处理工具类
 * @author yangnan
 */
public final class ExceptionUtil {

    /**
     * 异常处理
     *
     * @param e
     * @return
     */
    public static RuntimeException handleException(Throwable e) {
        if (e instanceof DerrorFrameException) {
            return (DerrorFrameException) e;
        }

        if (e instanceof DerrorTimeoutException) {
            return (DerrorTimeoutException) e;
        }

        return new DerrorFrameException(e);
    }

    /**
     * 获取异常栈信息
     *
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(512);
        builder.append(throwable).append("\n");
        StackTraceElement[] trace = throwable.getStackTrace();
        for (StackTraceElement element : trace) {
            builder.append(DerrorConstant.TAB).append("at ").append(element).append("\n");
        }
        return builder.delete(builder.length() - 2, builder.length()).toString();
    }
}
