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

import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yangnan
 * RC4加密解密算法
 */
public class RC4Crypt {
    private static final Logger LOGGER = LoggerFactory.getLogger(RC4Crypt.class);

    /** 明文的字符串;**/
    public String plaintext;
    /** 明文byte数组;**/
    public byte[] bytePlaintext;
    /** 密文结果的byte数组;**/
    public byte[] result;
    /** 输入密钥的字符串;**/
    public String mainKey;
    /** 子密钥的byte数组;*/
    public byte[] subKey;
    /** 本加/解密明/密的字节长度;*/
    public int length;
    /** S盒的byte数组;*/
    private byte[] box;

    public RC4Crypt() {
        // 顺序初始化S盒;
        makeBox();
    }

    /**
     *  用于实现明文加密的构造方法;
     * @param plaintext
     * @param mainKey
     */
    public RC4Crypt(String plaintext, String mainKey) {
        // 明文,密钥字符串参数传递;
        this.plaintext = plaintext;
        this.mainKey = mainKey;
        // 顺序初始化S盒;
        makeBox();
        // 用明文字符串初始化明文byte数组;
        getBytePlaintext();
        try {
            // 初始化本对象内容长度;
            length = bytePlaintext.length;
            // 初始化子密钥byte数组;
            getSubKey();
            // 进行加密计算;
            encrypt();
        } catch (Exception ex) {
            LOGGER.warn(ex.getMessage(), ex);
        }
    }

    /**
     * 实现密文解密的构造方法;
     * @param a
     * @param mainKey
     */
    public RC4Crypt(byte[] a, String mainKey) {
        // 密文byte数组,密钥字符串参数传递;
        result = a;
        this.mainKey = mainKey;
        // 顺序初始化S盒;
        makeBox();
        try {
            // 初始化本对象内容长度;
            length = result.length;
            // 初始化子密钥byte数组;
            getSubKey();
            // 进行解密运算;
            decrypt();
        } catch (Exception ex) {
            LOGGER.warn(ex.getMessage(), ex);
        }
    }

    /**
     * 初始化S盒内数据的方法;
     */
    private void makeBox() {
        box = new byte[256];
        // 用00000000B到11111111B的内容依次初始化S盒;
        // 注意byte类型数据表示0-255无符号数字的顺序;
        // 00000000B(0)-01111111B(127)-10000000B(-128)-11111111B(-1)
        for (int i = 0; i < 256; i++) {
            if (i < 127) {
                box[i] = (byte) i;
            } else {
                box[i] = (byte) (i - 256);
            }
        }
    }

    /**
     * 把明文改变为相应的byte数组的方法;
     */
    private void getBytePlaintext() {
        try {
            bytePlaintext = plaintext.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    /**
     * 把byte数组恢复成字符串的方法;
     * @return
     */
    public String recoverToString() {
        return new String(bytePlaintext);
    }

    /**
     * 获得自密钥的方法;
     * @throws Exception
     */
    private void getSubKey() throws Exception {
        // 声明所需要的局部变量
        int j = 0;
        byte temp = 0;
        byte[] key = mainKey.getBytes("UTF-8");
        // 通过j值的变化和密钥内容,逐步混淆S盒;
        for (int i = 0; i < 256; i++) {
            // j的混淆变化;
            j = (j + (box[i] > 0 ? box[i] : (box[i] + 256))
                    + (key[i % key.length] > 0 ? key[i % key.length] : (key[i % key.length] + 256))) % 256;
            // 空杯法换位;
            temp = box[i];
            box[i] = box[j];
            box[j] = temp;
        }
        int i = j = 0;
        // 声明一个容量等于本对象内容长度的byte数组来跟新子密钥数组;
        subKey = new byte[length];
        for (int k = 0; k < length; k++) {
            // 遍历i;
            i = ++i % 256;
            // j第二次混淆变化;
            j = (j + (box[i] > 0 ? box[i] : (box[i] + 256))) % 256;
            // 空杯发换位;
            temp = box[i];
            box[i] = box[j];
            box[j] = temp;
            // 子密钥byte数组各元素混淆赋值;
            subKey[k] = box[((box[i] > 0 ? box[i] : (box[i] + 256)) + (box[j] > 0 ? box[j] : (box[j] + 256))) % 256];
        }
    }

    /**
     * 加密计算方法;
     * @throws Exception
     */
    private void encrypt() throws Exception {
        result = new byte[length];
        // 明文byte数组与子密钥byte数组异或,得到密文byte数组;
        for (int i = 0; i < length; i++) {
            result[i] = (byte) (bytePlaintext[i] ^ subKey[i]);
        }
    }

    /**
     * 解密计算方法;
     * @throws Exception
     */
    private void decrypt() throws Exception {
        bytePlaintext = new byte[length];
        // 密文byte数组于子密钥byte数组异或,得到明文byte数组;
        for (int i = 0; i < length; i++) {
            bytePlaintext[i] = (byte) (result[i] ^ subKey[i]);
        }
    }
}
