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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yangnan
 * cookie操作
 */
public class CookieUtil {

    /**
     * 获取cookie的值
     *
     * @param request 请求对象
     * @param cookieKey cookie的key
     * @return cookie的值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieKey) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length == 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieKey)) {
                return cookie.getValue();
            }
        }

        return null;
    }

    /**
     * 设置cookie
     *
     * @param request
     * @param response
     * @param cookieKey
     * @param cookieValue
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieKey,
            String cookieValue) {
        String domain = getDomain(request);

        setCookie(response, cookieKey, cookieValue, domain);
    }

    /**
     * 设置cookie有效期
     *
     * @param response
     * @param key
     * @param value
     * @param domain
     * @param expiry
     * @return
     */
    public static boolean setExpiryCookie(HttpServletResponse response, String key, String value, String domain,
            int expiry) {
        checkDomainNotNull(domain);
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
        return true;
    }

    /**
     * 获取请求对应的域名
     *
     * @param request 请求对象
     * @return 域名
     */
    public static final String getDomain(HttpServletRequest request) {
        String url = request.getServerName();
        return getDomain(url);
    }

    /**
     * 获取url对应的域名
     *
     * @param url url
     * @return url对应域名
     */
    public static final String getDomain(String url) {
        url = url.replace("http://", "");
        url = url.replace("https://", "");
        if (url.contains("/")) {
            url = url.substring(0, url.indexOf("/"));
        }

        return url;
    }

    /**
     * 设置cookie
     *
     * @param response 响应对象
     * @param cookieKey cookie的key
     * @param cookieValue cookie的value
     * @param domain 域名
     */
    public static void setCookie(HttpServletResponse response, String cookieKey, String cookieValue, String domain) {
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        cookie.setPath("/");
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    public static boolean removeCookies(HttpServletRequest request, HttpServletResponse response, String...keys) {
        String domain = getDomain(request);

        return removeCookies(domain, request, response, keys);
    }

    public static boolean removeCookies(String domain, HttpServletRequest request, HttpServletResponse response,
            String...keys) {
        checkDomainNotNull(domain);
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }

        int delCount = 0;
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            for (String key : keys) {
                if (name.equals(key)) {
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setDomain(domain);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    ++delCount;
                }
            }
        }

        if (delCount == keys.length) {
            return true;
        } else {
            return false;
        }
    }

    private static void checkDomainNotNull(String domain) {
        if (StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("cookie domain is null!");
        }
    }


}
