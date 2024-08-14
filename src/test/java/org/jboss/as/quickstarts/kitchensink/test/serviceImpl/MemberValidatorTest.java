package org.jboss.as.quickstarts.kitchensink.test.serviceImpl;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.impl.MemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberValidatorTest {

    @Mock
    private MemberRepository repository;

    @InjectMocks
    private MemberValidator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateMemberWithNullName() {
        // Arrange
        Member member = new Member();
        member.setName(null);
        member.setEmail("test@example.com");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> validator.validateMember(member));
    }

    @Test
    public void testValidateMemberWithEmptyName() {
        // Arrange
        Member member = new Member();
        member.setName("");
        member.setEmail("test@example.com");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> validator.validateMember(member));
    }

    @Test
    public void testValidateMemberWithInvalidNameLength() {
        // Arrange
        Member member = new Member();
        member.setName("A very long name that exceeds the maximum length");
        member.setEmail("test@example.com");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> validator.validateMember(member));
    }

    @Test
    public void testValidateMemberWithNameContainingNumbers() {
        // Arrange
        Member member = new Member();
        member.setName("John123");
        member.setEmail("test@example.com");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> validator.validateMember(member));
    }

    @Test
    public void testValidateMemberWithNullEmail() {
        // Arrange
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail(null);

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> validator.validateMember(member));
    }

    @Test
    public void testValidateMemberWithEmptyEmail() {
        // Arrange
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> validator.validateMember(member));
    }

    @Test
    public void testValidateMemberWithExistingEmail() {
        // Arrange
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("existing@example.com");

        when(repository.findByEmail("existing@example.com")).thenReturn(Optional.of(member));

        // Act & Assert
        assertThrows(ValidationException.class, () -> validator.validateMember(member));
    }

    @Test
    public void testValidateMemberWithValidData() {
        // Arrange
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("valid@example.com");

        when(repository.findByEmail("valid@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        validator.validateMember(member); // Should not throw any exception
    }
}