
package org.jboss.as.quickstarts.kitchensink.rest;



import jakarta.validation.Valid;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;



import java.util.Optional;


@Controller
@RequestMapping("/members")
public class MemberController {


    private static final Logger log = LoggerFactory.getLogger(MemberController.class);



    @Autowired
    @Qualifier("memberService")
     MemberService memberService;


    private Member newMember;


    @PostConstruct
    public void initNewMember() {
        newMember = new Member();
    }

    @GetMapping("/index")
    public String showIndex() {
        log.info("showIndex() method called");
        try{
            return "index";
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }

    }


    @PostMapping("/register")
    @ResponseBody
    public String register(@Valid @RequestBody Member newMember, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            log.info("Error encountered while registering member: " + errorMessage);
            return "error";
        }
        try {
            org.springframework.http.ResponseEntity<?> builder = memberService.createMember(newMember);
            if (builder.getStatusCode().equals(org.springframework.http.HttpStatus.OK)) {
                initNewMember();
                return "success";
            } else {
                String errorMessage = "Either something is null or email already exists";
                //redirectAttributes.addFlashAttribute("error", errorMessage);
                log.info("Error encountered while registering member: " + errorMessage);
                return "error";
            }
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            String errorMessage = "Email already exists";
            log.info("Error encountered while registering member: " + errorMessage);
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = getRootErrorMessage(e);
            log.info("Error encountered while registering member: " + errorMessage);
            return "error";
        }
    }





    private String getRootErrorMessage(Exception e) {
        return Optional.ofNullable(e)
                .map(Throwable::getLocalizedMessage)
                .orElse("Registration failed. See server log for more information");
    }


    }
