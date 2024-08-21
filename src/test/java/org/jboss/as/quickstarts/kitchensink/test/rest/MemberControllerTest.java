package org.jboss.as.quickstarts.kitchensink.test.rest;




import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.rest.MemberController;
import org.jboss.as.quickstarts.kitchensink.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    public void testShowIndex() {
        // Act
        String viewName = memberController.showIndex();

        // Assert
        assertEquals("index", viewName);
    }


    @Test
    public void testRegister_Success() throws Exception {
        Member member = new Member();
        member.setEmail("test@example.com");
        when(memberService.createMember(Mockito.any(Member.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(bindingResult.hasErrors()).thenReturn(false);

        String response = memberController.register(member, bindingResult);
        assertEquals("success", response);
    }

    @Test
    public void testRegister_ValidationError() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("email", "Invalid email")));

        Member member = new Member();
        member.setEmail("invalid-email");

        String response = memberController.register(member, bindingResult);
        assertEquals("error", response);
    }

    @Test
    public void testRegister_DuplicateEmail() throws Exception {
        when(memberService.createMember(Mockito.any(Member.class))).thenThrow(new DuplicateKeyException("Email already exists"));
        when(bindingResult.hasErrors()).thenReturn(false);

        Member member = new Member();
        member.setEmail("test@example.com");

        String response = memberController.register(member, bindingResult);
        assertEquals("error", response);
    }

    @Test
    public void testRegister_OtherException() throws Exception {
        when(memberService.createMember(Mockito.any(Member.class))).thenThrow(new RuntimeException("Unexpected error"));
        when(bindingResult.hasErrors()).thenReturn(false);

        Member member = new Member();
        member.setEmail("test@example.com");

        String response = memberController.register(member, bindingResult);
        assertEquals("error", response);
    }
}
