package org.jboss.as.quickstarts.kitchensink.service;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberService {
    ResponseEntity<?> createMember(@Valid Member member) throws ConstraintViolationException, ValidationException;
    boolean emailAlreadyExists(String email);
    List<Member> getAllMembers();
    Member getMemberById(String id);
    Optional<Member> getMemberByEmail(String email);
    ResponseEntity<?> createViolationResponse(Set<ConstraintViolation<?>> violations);
}
