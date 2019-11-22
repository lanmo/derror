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

import org.jfaster.derror.manager.constant.CommonConstant;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yangnan
 * 加密解密
 */
public class CryptoUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(CryptoUtil.class);

    public static final String KEY = "!@#$%^&*(qazwsxedc)";
    /**
     * 字符串加密
     *
     * @param source
     * @return
     */
    public static String encodeRC(String source) {
        String code = source;
        try {
            final byte[] result = new RC4Crypt(code, KEY).result;
            code = new String(Base64.encodeBase64(result), CommonConstant.UTF_8);
            code = URLEncoder.encode(code, CommonConstant.UTF_8);
        } catch (Exception e) {
            LOGGER.error("encodeRC e.message:{}", e.getMessage(), e);
        }
        return code;
    }

    /**
     * 字符串解密 2013-8-9
     *
     * @param code
     * @return
     */
    public static String decodeRC(String code) {
        String source = "";
        try {
            source = URLDecoder.decode(code, CommonConstant.UTF_8);
            source = new RC4Crypt(Base64.decodeBase64(source), KEY).recoverToString();
        } catch (Exception e) {
            LOGGER.error("decodeRC e.message:{}", e.getMessage(), e);
        }
        return source;
    }
}
