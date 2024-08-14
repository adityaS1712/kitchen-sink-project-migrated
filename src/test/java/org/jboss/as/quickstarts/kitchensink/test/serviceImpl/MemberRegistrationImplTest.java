package org.jboss.as.quickstarts.kitchensink.test.serviceImpl;


import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.impl.MemberRegistrationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.logging.Logger;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberRegistrationImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private Logger log;

    @InjectMocks
    private MemberRegistrationImpl memberRegistration;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        // Arrange
        Member member = new Member();
        member.setName("John Doe");

        // Act
        memberRegistration.register(member);

        // Assert
        verify(log).info("Registering" + member.getName());
        verify(mongoTemplate).save(member);
    }
}
