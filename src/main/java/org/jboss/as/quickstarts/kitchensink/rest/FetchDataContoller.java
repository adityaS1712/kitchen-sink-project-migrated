package org.jboss.as.quickstarts.kitchensink.rest;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fetch")
public class FetchDataContoller {



    @Autowired
    @Qualifier("memberService")
    public MemberService memberService;


    @GetMapping("/all")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }


    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable String id) {
        return memberService.getMemberById(id);
    }

    @GetMapping("/email/{email}")
    public Optional<Member> getMemberByEmail(@PathVariable String email) {
        return memberService.getMemberByEmail(email);
    }
}
