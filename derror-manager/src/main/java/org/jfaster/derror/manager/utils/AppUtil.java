/*
 *
 *   Copyright 2018 org.jfaster.derror.
 *     <p>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   </p>
 */

package org.jfaster.derror.manager.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yangnan
 */
public final class AppUtil {

    /**
     * 解析appId
     *
     * @param authAppId
     * @return
     */
    public static List<Long> getAppIds(String authAppId) {
        if (StringUtils.isBlank(authAppId)) {
            return Collections.emptyList();
        }

        String[] apps = StringUtils.split(authAppId, ",");
        return Arrays.stream(apps).map(r -> Long.parseLong(r)).collect(Collectors.toList());
    }
}
