package org.jboss.as.quickstarts.kitchensink.test.serviceImpl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.ValidationException;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistrationService;
import org.jboss.as.quickstarts.kitchensink.service.SequenceGeneratorService;
import org.jboss.as.quickstarts.kitchensink.service.impl.MemberResourceRESTService;
import org.jboss.as.quickstarts.kitchensink.service.impl.MemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberResourceRESTServiceTest {

    @Mock
    private MemberRepository repository;

    @Mock
    private SequenceGeneratorService sequenceGenerator;

    @Mock
    private MemberRegistrationService registration;

    @Mock
    private MemberValidator memberValidator;

    @InjectMocks
    private MemberResourceRESTService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMember_Success() {
        // Arrange
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");

        when(sequenceGenerator.generateSequence(Member.SEQUENCE_NAME)).thenReturn(1L);

        // Act
        ResponseEntity<?> response = service.createMember(member);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(memberValidator).validateMember(member);
        verify(registration).register(member);
    }

    @Test
    public void testCreateMember_ConstraintViolationException() {
        // Arrange
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");

        doThrow(new ConstraintViolationException(new HashSet<ConstraintViolation<?>>())).when(memberValidator).validateMember(member);

        // Act
        ResponseEntity<?> response = service.createMember(member);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateMember_ValidationException() {
        // Arrange
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");

        doThrow(new ValidationException("Unique Email Violation")).when(memberValidator).validateMember(member);

        // Act
        ResponseEntity<?> response = service.createMember(member);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateMember_OtherException() {
        // Arrange
        Member member = new Member();
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");

        doThrow(new RuntimeException("Unexpected error")).when(registration).register(member);

        // Act
        ResponseEntity<?> response = service.createMember(member);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateViolationResponse() {
        // Arrange
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("email");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be empty");
        violations.add(violation);

        // Act
        ResponseEntity<?> response = service.createViolationResponse(violations);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map<String, String>) response.getBody()).containsKey("email"));
    }

    @Test
    public void testEmailAlreadyExists() {
        // Arrange
        String email = "existing@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.of(new Member()));

        // Act
        boolean exists = service.emailAlreadyExists(email);

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testGetAllMembers() {
        // Arrange
        List<Member> members = Arrays.asList(new Member(), new Member());
        when(repository.findAllByOrderByNameAsc()).thenReturn(members);

        // Act
        List<Member> result = service.getAllMembers();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetMemberById_Success() {
        // Arrange
        String id = "1";
        Member member = new Member();
        when(repository.findById(id)).thenReturn(Optional.of(member));

        // Act
        Member result = service.getMemberById(id);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetMemberById_NotFound() {
        // Arrange
        String id = "1";
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> service.getMemberById(id));
    }

    @Test
    public void testGetMemberByEmail() {
        // Arrange
        String email = "test@example.com";
        Member member = new Member();
        when(repository.findByEmail(email)).thenReturn(Optional.of(member));

        // Act
        Optional<Member> result = service.getMemberByEmail(email);

        // Assert
        assertTrue(result.isPresent());
    }
}