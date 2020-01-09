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

import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yangnan
 * 获取网卡信息
 */
public class NetUtil {

    private static InternalLogger LOGGER = InternalLoggerFactory.getLogger(NetUtil.class);
    private static final String LOCAL_IP = "127.0.0.1";
    private static String NATIVE_IP = LOCAL_IP;

    /**
     * 获取本地ip地址，有可能会有多个地址, 若有多个网卡则会搜集多个网卡的ip地址
     */
    private static Set<InetAddress> resolveLocalAddresses() {
        Set<InetAddress> addrs = new HashSet<>();
        Enumeration<NetworkInterface> ns = null;
        try {
            ns = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            LOGGER.error("获取网卡信息错误", e);
        }
        while (ns != null && ns.hasMoreElements()) {
            NetworkInterface n = ns.nextElement();
            Enumeration<InetAddress> is = n.getInetAddresses();
            while (is.hasMoreElements()) {
                InetAddress i = is.nextElement();
                if (!i.isLoopbackAddress() && !i.isLinkLocalAddress() && !i.isMulticastAddress()
                        && !isSpecialIp(i.getHostAddress())) {
                    addrs.add(i);
                }
            }
        }
        return addrs;
    }

    /**
     * 获取本地ip
     *
     * @return
     */
    public static String getLocalIp() {
        if (!NATIVE_IP.equals(LOCAL_IP)) {
            return NATIVE_IP;
        }

        Set<String> ips = resolveLocalIps();
        if (ips == null || ips.size() <= 0) {
            return NATIVE_IP;
        }
        NATIVE_IP = ips.stream().filter(r -> !LOCAL_IP.equals(r)).findFirst().orElse(LOCAL_IP);
        return NATIVE_IP;
    }

    /**
     * 获取ip异常返回值为null
     *
     * @return
     */
    public static Set<String> resolveLocalIps() {

        try {
            Set<InetAddress> addrs = resolveLocalAddresses();
            Set<String> ret = new HashSet<String>();
            for (InetAddress addr : addrs) {
                ret.add(addr.getHostAddress());
            }
            return ret;
        } catch (Throwable e) {
            LOGGER.error("获取本机ip异常", ExceptionUtil.handleException(e));
        }

        return null;
    }

    /**
     * 169.254.***.***地址段的含义：169.254地址段也属私有保留地址，
     * 一般开启了dhcp服务的设备但又无法获取到dhcp的会随机使用这个网段的ip。
     * 出现此IP地址段，一般表示互联网无法通信
     */
    private static final String NOT_WORKING = "169.254.";

    /**
     * 广播地址
     */
    private static final String BRODCAST_IP = "255.255.255.255";

    protected static final String SEPARATOR = ":";

    private static boolean isSpecialIp(String ip) {

        if (StringUtil.isEmpty(ip)) {
            return false;
        }
        if (ip.contains(SEPARATOR)) {
            return true;
        }
        if (LOCAL_IP.equals(ip)) {return true;}
        if (ip.startsWith(NOT_WORKING)) {return true;}
        if (BRODCAST_IP.equals(ip)) {return true;}

        return false;
    }

}
