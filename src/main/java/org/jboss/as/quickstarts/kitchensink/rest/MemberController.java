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
package org.jboss.as.quickstarts.kitchensink.rest;


// Ensure the package name is correct and the class exists in the specified package
import jakarta.validation.Valid;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberService;
import org.jboss.as.quickstarts.kitchensink.service.impl.MemberResourceRESTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/members")
public class MemberController {


    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberResourceRESTService memberResourceRESTService;
    private final MemberRepository memberRepository;

    @Autowired
    public MemberController(MemberRepository memberRepository, MemberResourceRESTService memberResourceRESTService) {
        this.memberRepository = memberRepository;
        this.memberResourceRESTService = memberResourceRESTService;
    }



    @Autowired
    private MemberService memberService;


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

    @GetMapping("/all")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }


    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable String id) {
        return memberService.getMemberById(id);
    }

  @GetMapping("/email/{email}")
    public Optional<Member> getMemberByEmail(@PathVariable String email) {
        return memberService.getMemberByEmail(email);
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody Member newMember, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", errorMessage);
            log.info("Error encountered while registering member: " + errorMessage);
            return "error";
        }
        try {
            memberService.createMember(newMember);
            redirectAttributes.addFlashAttribute("message", "Registration successful");
            initNewMember();
            return "success";
        } catch (DuplicateKeyException e) {
            String errorMessage = "Email already exists";
            redirectAttributes.addFlashAttribute("error", errorMessage);
            log.info("Error encountered while registering member: " + errorMessage);
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = getRootErrorMessage(e);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            log.info("Error encountered while registering member: " + errorMessage);
            return "error";
        }
    }



    private String getRootErrorMessage(Exception e) {
        return Optional.ofNullable(e)
                .map(Throwable::getLocalizedMessage)
                .orElse("Registration failed. See server log for more information");
    }


    }
