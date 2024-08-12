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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;



import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Component
@Scope("request")
@RestController
@RequestMapping("/members")
@Validated
public class MemberResourceRESTService {

    @Autowired
    private Logger log;

    @Autowired
    private javax.validation.Validator validator;

    @Autowired
    private MemberRepository repository;

    @Autowired
    MemberRegistration registration;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Member> listAllMembers() {
        return repository.findAllOrderedByName();
    }

    @GetMapping(path = "/{id:[0-9][0-9]*}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Member lookupMemberById(@PathVariable("id") long id) {
        Member member = repository.findById(id);
        if (member == null) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND);
        }
        return member;
    }

    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public org.springframework.http.ResponseEntity<?> createMember(@RequestBody Member member) {

        org.springframework.http.ResponseEntity.BodyBuilder builder = null;

        try {
            validateMember(member);
            registration.register(member);
            builder = org.springframework.http.ResponseEntity.ok();
        } catch (javax.validation.ConstraintViolationException ce) {
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (javax.validation.ValidationException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
         //   builder = org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).body(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
         //   builder = org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(responseObj);
        }

        return builder.build();
    }


    private void validateMember(Member member) throws javax.validation.ConstraintViolationException, javax.validation.ValidationException {
        Set<javax.validation.ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            throw new javax.validation.ConstraintViolationException(new HashSet<>(violations));
        }

        if (emailAlreadyExists(member.getEmail())) {
            throw new javax.validation.ValidationException("Unique Email Violation");
        }
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     *
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private org.springframework.http.ResponseEntity.BodyBuilder createViolationResponse(Set<javax.validation.ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (javax.validation.ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        org.springframework.http.ResponseEntity.BodyBuilder builder = org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST);
        builder.body(responseObj);
        return builder;

    }

    /**
     * Checks if a member with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
     *
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean emailAlreadyExists(String email) {
        Member member = null;
        try {
            member = repository.findByEmail(email);
        } catch (javax.persistence.NoResultException e) {

            // ignore
        }
        return member != null;
    }
}
