package org.jboss.as.quickstarts.kitchensink.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.ValidationException;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class MemberValidator {

    private static final Logger log = LoggerFactory.getLogger(MemberValidator.class);
    @Autowired
    private MemberRepository repository;

    public void validateMember(Member member) throws ConstraintViolationException, ValidationException {
        Set<ConstraintViolation<Member>> violations = new HashSet<>();

        if (member.getName() == null || member.getName().isEmpty()) {
            violations.add(new CustomConstraintViolation<>("Name cannot be null or empty", member, member.getName()));
        } else if (member.getName().length() < 1 || member.getName().length() > 25) {
            violations.add(new CustomConstraintViolation<>("Name must be between 1 and 25 characters", member, member.getName()));
        } else if (member.getName().matches(".*\\d.*")) {
            violations.add(new CustomConstraintViolation<>("Name must not contain numbers", member, member.getName()));
        }

        if (member.getEmail() == null || member.getEmail().isEmpty()) {
            violations.add(new ConstraintViolation<Member>() {
                @Override
                public String getMessage() {
                    return "Email cannot be null or empty";
                }

                @Override
                public String getMessageTemplate() {
                    return "Email cannot be null or empty";
                }

                @Override
                public Member getRootBean() {
                    return member;
                }

                @Override
                public Class<Member> getRootBeanClass() {
                    return Member.class;
                }

                @Override
                public Object getLeafBean() {
                    return member;
                }

                @Override
                public Object[] getExecutableParameters() {
                    return new Object[0];
                }

                @Override
                public Object getExecutableReturnValue() {
                    return null;
                }

                @Override
                public Path getPropertyPath() {
                    return null;
                }

                @Override
                public Object getInvalidValue() {
                    return member.getEmail();
                }

                @Override
                public ConstraintDescriptor<?> getConstraintDescriptor() {
                    return null;
                }

                @Override
                public <U> U unwrap(Class<U> type) {
                    return null;
                }
            });
        }

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        if (emailAlreadyExists(member.getEmail())) {
            log.info("Email already exist");
            throw new ValidationException("Unique Email Violation");
        }
    }

    private boolean emailAlreadyExists(String email) {
        Optional<Member> member = repository.findByEmail(email);
        return member.isPresent();
    }
}
