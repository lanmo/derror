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

import com.alibaba.fastjson.JSON;
import org.jfaster.derror.manager.pojo.dto.dingding.AiTeDTO;
import org.jfaster.derror.manager.pojo.dto.dingding.DingTextDTO;
import org.jfaster.derror.manager.pojo.dto.dingding.DingTextMessageDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yangnan
 * @date 18/1/19
 *
 * 钉钉报警专用
 */
public class DingDingUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(DingDingUtil.class);

    public static void sendMessage(DingTextDTO textDTO, String dingUrl) {
        if(StringUtils.isEmpty(dingUrl)){
            LOGGER.warn("钉钉发送消息url为空不发送消息");
            return;
        }
        DingTextMessageDTO textMessageDTO = new DingTextMessageDTO();
        textMessageDTO.setText(textDTO);
        textMessageDTO.setAt(new AiTeDTO(true));
        String resp = DerrorHttpUtil.getIntance().postJSON(dingUrl, JSON.toJSONString(textMessageDTO));
        LOGGER.info("钉钉发送消息成功:{}", resp);
    }

}
