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

import static org.joda.time.DateTimeConstants.MILLIS_PER_DAY;

import org.jfaster.derror.manager.constant.CommonConstant;
import org.jfaster.derror.manager.pojo.dto.LoginForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yangnan
 * 登录工具类
 */
public class LoginUtil {

    private static final int COOKIE_LENGTH = 3;

    /**
     * 获取登录用户名
     * @param request
     * @return
     */
    public static String getLoginName(HttpServletRequest request) {
        String cookieValue = CookieUtil.getCookieValue(request, CommonConstant.COOKIE_KEY_LOGIN);
        if (StringUtils.isBlank(cookieValue)) {
            return null;
        }
        cookieValue = CryptoUtil.decodeRC(cookieValue);
        if (StringUtils.isBlank(cookieValue)) {
            return null;
        }

        String[] values = StringUtils.split(cookieValue, CommonConstant.SEPARATOR);
        if (values.length == COOKIE_LENGTH) {
            String time = values[0];
            String name = values[1];
            String md5 = getMd5Value(name, time);
            long loginTime = 0;
            try {
                loginTime = Long.parseLong(time);
            } catch (Exception e) {
            }
            long diff = (System.currentTimeMillis() - loginTime) / MILLIS_PER_DAY;
            if (diff < 1 && md5.equals(values[2])) {
                return name;
            }
        }
        return null;
    }

    /**
     * 登录
     *
     * @param user
     * @param request
     * @param response
     */
    public static void login(LoginForm user, final HttpServletRequest request, final HttpServletResponse response) {
        long timeMillis = System.currentTimeMillis();
        String md5 = getMd5Value(user.getUserName(), timeMillis);
        String value = timeMillis + CommonConstant.SEPARATOR + user.getUserName() + CommonConstant.SEPARATOR + md5;
        value = CryptoUtil.encodeRC(value);

        CookieUtil.setExpiryCookie(response, CommonConstant.COOKIE_KEY_LOGIN, value, request.getServerName(), CommonConstant.SECONDS_PER_HOUR);
    }

    /**
     * 获取MD5值
     * @param userName
     * @param timeMillis
     * @return
     */
    private static String getMd5Value(String userName, String timeMillis) {
        return DigestUtils.md5Hex(userName + CryptoUtil.KEY + timeMillis).substring(4, 20);
    }

    /**
     * 获取MD5值
     * @param userName
     * @param timeMillis
     * @return
     */
    private static String getMd5Value(String userName, long timeMillis) {
        return DigestUtils.md5Hex(userName + CryptoUtil.KEY + timeMillis).substring(4, 20);
    }
}
