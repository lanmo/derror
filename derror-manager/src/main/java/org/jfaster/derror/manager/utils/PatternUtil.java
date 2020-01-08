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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配替换工具
 *
 * @author yangnan
 */
public class PatternUtil {

    /**
     * 线程安全的
     */
    private static Pattern pattern = Pattern.compile("\\$\\{\\w+\\}");

    /**
     * 根据map中的格式化模板
     */
    public static String getKey(String template, Map<String, String> params) {
        Matcher m = pattern.matcher(template);
        StringBuffer buffer = new StringBuffer();
        m.reset();
        while (m.find()) {
            String group = m.group(0);
            int ln = group.length();
            group = group.substring(2, ln - 1);
            m.appendReplacement(buffer, String.valueOf(params.get(group)));
        }
        m.appendTail(buffer);

        return buffer.toString();
    }

}
