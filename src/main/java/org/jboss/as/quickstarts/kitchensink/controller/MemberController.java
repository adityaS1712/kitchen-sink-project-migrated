/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.controller;

import jakarta.annotation.PostConstruct;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RestController
@RequestMapping("/members")
public class MemberController {


    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private MemberRegistration memberRegistration;


    private Member newMember;

    @PostConstruct
    public void initNewMember() {
        newMember = new Member();
    }

    @GetMapping("/index")
    public String showIndex() {
        log.info("showIndex() method called");
        try{
            return "index";
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }

    }

    @PostMapping("/register")
    public String register(RedirectAttributes redirectAttributes) {
        try {
            memberRegistration.register(newMember);
            redirectAttributes.addFlashAttribute("message", "Registration successful");
            initNewMember();
            return "redirect:/members/index";
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = getRootErrorMessage(e);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/members/index";
        }
    }

    private String getRootErrorMessage(Exception e) {
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            return errorMessage;
        }
        Throwable t = e;
        while (t != null) {
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        return errorMessage;
    }


    }
