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

package org.jfaster.derror.remote.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.jfaster.derror.config.ClientConfig;
import org.jfaster.derror.constant.DerrorConstant;
import org.jfaster.derror.logging.InternalLogger;
import org.jfaster.derror.logging.InternalLoggerFactory;
import org.jfaster.derror.remote.IDownloadRemoteService;
import org.jfaster.derror.remote.config.BaseResponse;
import org.jfaster.derror.remote.config.ExceptionLog;
import org.jfaster.derror.remote.config.RemoteConfig;
import org.jfaster.derror.util.DerrorHttpUtil;
import org.jfaster.derror.util.ExceptionUtil;

import static org.jfaster.derror.constant.DerrorConstant.GET_CONFIG;
import static org.jfaster.derror.constant.DerrorConstant.SAVE_EXCEPTION_LOG;

/**
 * @author yangnan
 */
public class DownloadRemoteServiceImpl implements IDownloadRemoteService {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getLogger(DownloadRemoteServiceImpl.class);

    @Override
    public RemoteConfig getRemoteConfig(ClientConfig config) {
        StringBuilder url = new StringBuilder(config.getUrl());
        url.append(GET_CONFIG);
        url.append("?token=").append(config.getToken());
        url.append("&appName=").append(config.getAppName());
        try {
            String resp = DerrorHttpUtil.getInstance().doGet(url.toString());
            BaseResponse<RemoteConfig> response = JSON.parseObject(resp, new
                    TypeReference<BaseResponse<RemoteConfig>>(){});
            if (response != null && response.isSuccess()) {
                return response.getData();
            }
        } catch (Throwable e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DownloadRemote config fail url:{}", url, ExceptionUtil.handleException(e));
            }
        }
        return null;
    }

    @Override
    public void sendExceptionToServer(ClientConfig config, ExceptionLog exceptionLog) {
        StringBuilder url = new StringBuilder(config.getUrl());
        url.append(SAVE_EXCEPTION_LOG);
        try {
            String body = JSON.toJSONString(exceptionLog);
            DerrorHttpUtil.getInstance().doPostJson(url.toString(), body);
        } catch (Throwable e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DownloadRemote config fail url:{}", url, ExceptionUtil.handleException(e));
            }
        }
    }
}
