/*
 *
 *  * Copyright 2018 org.jfaster.derror.
 *  *   <p>
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  * </p>
 *
 */

package org.jfaster.derror.manager.utils;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author yangnan
 * @date 18/1/29
 */
public final class ApiResponse {
    /**
     * 0 成功
     */
    public static final int SUCCESS_CODE = 0;
    /**
     * 1 失败
     */
    public static final int ERROR_CODE = 1;

    /**
     * 成功
     */
    private static final ApiResponse SUCCESS = new ApiResponse(SUCCESS_CODE, "SUCCESS", null);
    /**
     * 失败
     */
    private static final ApiResponse ERROR = new ApiResponse(ERROR_CODE, "ERROR", null);

    /**
     * 返回状态码
     */
    @Getter
    @Setter
    private int code;

    /**
     * 提示信息
     */
    @Getter
    @Setter
    private String msg;

    /**
     * 数据
     */
    @Getter
    @Setter
    private final Object data;

    public ApiResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResponse error(int code, String msg, Object data) {
        return new ApiResponse(code, msg, data);
    }

    public static ApiResponse error(String msg, Object data) {
        return error(ERROR.getCode(), msg, data);
    }

    public static ApiResponse error(Object data) {
        return error(ERROR.getMsg(), data);
    }

    public static ApiResponse error(String msg) {
        return error(msg, null);
    }

    public static ApiResponse success(int code, String msg, Object data) {
        return new ApiResponse(code, msg, data);
    }

    public static ApiResponse success(String msg, Object data) {
        return success(SUCCESS.getCode(), msg, data);
    }

    public static ApiResponse success(Object data) {
        return success(SUCCESS.getMsg(), data);
    }

    public static ApiResponse success(String msg) {
        return success(msg, null);
    }
    public static ApiResponse success() {
        return success(SUCCESS.getMsg(), null);
    }
}
