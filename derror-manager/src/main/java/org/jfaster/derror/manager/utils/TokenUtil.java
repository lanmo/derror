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

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author yangnan
 */
public final class TokenUtil {

    private static final int MAX = 10;

    private static char[] TOKENS = {'a','b','c','d','e','f','g','h','i','j','k','m','n','p','q','r','s', 't', 'u',
            'v', 'w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','M','N','P','Q','R','S','T','U','V','W',
            'X','Y','Z', '1','2','3','4','5','6','7','8','9'};

    /**
     * 返回一个token值
     * @return
     */
    public static String getToken() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder token = new StringBuilder();
        int tokenLen = TOKENS.length;
        for (int i=0; i<MAX; i++) {
            token.append(TOKENS[random.nextInt(tokenLen)]);
        }
        return token.toString();
    }
}
