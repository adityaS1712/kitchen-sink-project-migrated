
package org.jboss.as.quickstarts.kitchensink.service.impl;

import java.util.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;


import jakarta.validation.Valid;

import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistrationService;
import org.jboss.as.quickstarts.kitchensink.service.MemberService;
import org.jboss.as.quickstarts.kitchensink.service.SequenceGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Service
@Validated
public class MemberResourceRESTService implements MemberService {


    private static final Logger log = LoggerFactory.getLogger(MemberResourceRESTService.class);


    @Autowired
    private MemberRepository repository;

    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    MemberRegistrationService registration;

    @Autowired
    private MemberValidator memberValidator;


@Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public org.springframework.http.ResponseEntity<?> createMember(@Valid @RequestBody Member member) {

        org.springframework.http.ResponseEntity<?> builder = null;

        try {
            member.setId(String.valueOf(sequenceGenerator.generateSequence(Member.SEQUENCE_NAME)));
            memberValidator.validateMember(member);
            registration.register(member);
            builder = org.springframework.http.ResponseEntity.ok().build();
        } catch (ConstraintViolationException ce) {
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            builder = org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).body(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(responseObj);
        }

        return builder;
    }


@Override
    public org.springframework.http.ResponseEntity<?> createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.info("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(responseObj);
    }

   @Override
    public boolean emailAlreadyExists(String email) {
        Optional<Member> member = repository.findByEmail(email);
        return member.isPresent();
    }
    @Override
    public List<Member> getAllMembers() {
        return repository.findAllByOrderByNameAsc();
    }
    @Override
    public Member getMemberById(@PathVariable("id") String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
@Override
    public Optional<Member> getMemberByEmail(@PathVariable("email") String email) {
        return repository.findByEmail(email);
    }

}
