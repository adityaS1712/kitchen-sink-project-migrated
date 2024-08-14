package org.jboss.as.quickstarts.kitchensink.test.rest;

import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.rest.MemberController;
import org.jboss.as.quickstarts.kitchensink.service.MemberService;
import org.jboss.as.quickstarts.kitchensink.service.impl.MemberResourceRESTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
public class MemberControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MemberService memberService;
//
//    @MockBean
//    private MemberRepository memberRepository;
//
//    @MockBean
//    private MemberResourceRESTService memberResourceRESTService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testShowIndex() throws Exception {
//        mockMvc.perform(get("/members/index"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("index"));
//    }
//
//    @Test
//    public void testGetAllMembers() throws Exception {
//        List<Member> members = Arrays.asList(new Member(), new Member());
//        when(memberService.getAllMembers()).thenReturn(members);
//
//        mockMvc.perform(get("/members/all"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    public void testGetMemberById() throws Exception {
//        Member member = new Member();
//        when(memberService.getMemberById("1")).thenReturn(member);
//
//        mockMvc.perform(get("/members/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    public void testGetMemberByEmail() throws Exception {
//        Member member = new Member();
//        when(memberService.getMemberByEmail("test@example.com")).thenReturn(Optional.of(member));
//
//        mockMvc.perform(get("/members/email/test@example.com"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    public void testRegister_Success() throws Exception {
//        String newMemberJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"1234567890\"}";
//        Member member = new Member();
//        member.setName("John Doe");
//        member.setEmail("john.doe@example.com");
//
//        when(memberService.createMember(any(Member.class))).thenReturn(org.springframework.http.ResponseEntity.ok().build());
//
//        mockMvc.perform(post("/members/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(newMemberJson))
//                .andExpect(status().isOk())
//                .andExpect(view().name("success"));
//    }
//
//    @Test
//    public void testRegister_ValidationError() throws Exception {
//        String newMemberJson = "{\"name\":\"\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"1234567890\"}";
//
//        mockMvc.perform(post("/members/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(newMemberJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(view().name("error"));
//    }
//
//    @Test
//    public void testRegister_DuplicateKeyException() throws Exception {
//        String newMemberJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"1234567890\"}";
//        Member member = new Member();
//        member.setName("John Doe");
//        member.setEmail("john.doe@example.com");
//
//        when(memberService.createMember(any(Member.class))).thenThrow(new org.springframework.dao.DuplicateKeyException("Email already exists"));
//
//        mockMvc.perform(post("/members/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(newMemberJson))
//                .andExpect(status().isConflict())
//                .andExpect(view().name("error"));
//    }
}