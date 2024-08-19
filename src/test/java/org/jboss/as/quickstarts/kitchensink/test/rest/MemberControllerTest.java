package org.jboss.as.quickstarts.kitchensink.test.rest;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.rest.MemberController;
import org.jboss.as.quickstarts.kitchensink.service.MemberService;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.ObjectError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
public class MemberControllerTest {


    @Autowired
    private MemberController memberController;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        memberController = new MemberController();
        ReflectionTestUtils.setField(memberController, "memberService", memberService);
    }

    @Test
    public void testRegister_Success() {
        Member member = new Member();
        member.setEmail("test@example.com");
        when(memberService.createMember(Mockito.any(Member.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(bindingResult.hasErrors()).thenReturn(false);

        String response = memberController.register(member, bindingResult, redirectAttributes);
        assertEquals("success", response);
    }

    @Test
    public void testRegister_ValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("email", "Invalid email")));

        Member member = new Member();
        member.setEmail("invalid-email");

        String response = memberController.register(member, bindingResult, redirectAttributes);
        assertEquals("error", response);
    }

    @Test
    public void testRegister_DuplicateEmail() {
        when(memberService.createMember(Mockito.any(Member.class))).thenThrow(new DuplicateKeyException("Email already exists"));
        when(bindingResult.hasErrors()).thenReturn(false);

        Member member = new Member();
        member.setEmail("test@example.com");

        String response = memberController.register(member, bindingResult, redirectAttributes);
        assertEquals("error", response);
    }

    @Test
    public void testRegister_OtherException() {
        when(memberService.createMember(Mockito.any(Member.class))).thenThrow(new RuntimeException("Unexpected error"));
        when(bindingResult.hasErrors()).thenReturn(false);

        Member member = new Member();
        member.setEmail("test@example.com");

        String response = memberController.register(member, bindingResult, redirectAttributes);
        assertEquals("error", response);
    }
}