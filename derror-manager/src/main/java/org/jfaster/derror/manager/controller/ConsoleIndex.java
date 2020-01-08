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

package org.jfaster.derror.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yangnan
 */
@Controller
public class ConsoleIndex extends AbstractErrorController {

    @Autowired
    public ConsoleIndex(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping("/")
    public String index() {
        return "login";
    }

    @RequestMapping("/error")
    public String error(HttpServletRequest request, HttpServletResponse response) {

        HttpStatus status = super.getStatus(request);

        if (status == HttpStatus.NOT_FOUND) {
            return "login";
        }

        if (status == HttpStatus.FORBIDDEN || status == HttpStatus.UNAUTHORIZED) {
            return "";
        }

        return "login";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }




}
