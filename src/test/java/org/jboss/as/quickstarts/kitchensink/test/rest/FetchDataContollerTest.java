package org.jboss.as.quickstarts.kitchensink.test.rest;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.rest.FetchDataContoller;
import org.jboss.as.quickstarts.kitchensink.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FetchDataContollerTest {

    @MockBean
    private MemberService memberService;

    private FetchDataContoller fetchDataContoller;

    private Member testMember;

    @BeforeEach
    public void setUp() {
        testMember = new Member();
        testMember.setId("some-id");
        testMember.setEmail("test@example.com");
        testMember.setName("Test Name");

        fetchDataContoller = new FetchDataContoller();
        fetchDataContoller.memberService = memberService; // Manually set the mock service
    }

    @Test
    public void testGetAllMembers() {
        // Arrange
        when(memberService.getAllMembers()).thenReturn(Collections.singletonList(testMember));

        // Act
        List<Member> members = fetchDataContoller.getAllMembers();

        // Assert
        assertEquals(Collections.singletonList(testMember), members);
    }

    @Test
    public void testGetMemberById() {
        // Arrange
        when(memberService.getMemberById("some-id")).thenReturn(testMember);

        // Act
        Member result = fetchDataContoller.getMemberById("some-id");

        // Assert
        assertEquals(testMember, result);
    }

    @Test
    public void testGetMemberByEmail() {
        // Arrange
        when(memberService.getMemberByEmail("test@example.com")).thenReturn(Optional.of(testMember));

        // Act
        Optional<Member> result = fetchDataContoller.getMemberByEmail("test@example.com");

        // Assert
        assertEquals(Optional.of(testMember), result);
    }
}
