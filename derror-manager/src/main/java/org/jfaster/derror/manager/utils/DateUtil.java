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

package org.jfaster.derror.manager.utils;

import java.util.Date;
import org.joda.time.DateTime;

/**
 * @author yangnan 时间转化类
 */
public final class DateUtil {

    public final static String DEFAULT_TIME = "yyyy-MM-dd HH:mm:ss";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 格式化时间
     */
    public final static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return new DateTime(date.getTime()).toString(pattern);
    }

    /**
     * 添加min分钟，转为时间戳
     */
    public final static long format2Long(Date date, Integer min) {
        if (date == null) {
            return 0;
        }
        return new DateTime(date.getTime()).plusMinutes(min).getMillis();
    }

    /**
     * 当前时间减去retain天
     */
    public final static Date minusRetainDays(Date date, Integer retain) {
        if (date == null) {
            return null;
        }
        return new DateTime(date.getTime()).minusDays(retain).toDate();
    }

    /**
     * 当前时间加上retain天
     */
    public final static Date plusRetainDays(Date date, Integer retain) {
        if (date == null) {
            return null;
        }
        return new DateTime(date.getTime()).plusDays(retain).toDate();
    }
}
