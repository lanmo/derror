/*
 *
 *   Copyright 2018 derror.jfaster.org.
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

package org.jfaster.derror.logback.plugin.event;

import org.jfaster.derror.config.AbstractExtendAdapter;
import org.slf4j.MDC;

import java.util.Map;

/**
 * @author yangnan
 */
public class LogbackExtendAdapter extends AbstractExtendAdapter {

    @Override
    protected Map<String, String> getExtendMap() {
        return MDC.getCopyOfContextMap();
    }

    @Override
    protected void putExtend(String key, String value) {
        MDC.put(key, value);
    }

    @Override
    protected void removeExtend(String key) {
        MDC.remove(key);
    }

    @Override
    protected String getExtendValue(String key) {
        return MDC.get(key);
    }
}
