package org.jboss.as.quickstarts.kitchensink.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.logging.Logger;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
public class MemberRegistrationIT {

   // @Autowired
    MemberRegistration memberRegistration;

   // @Autowired
    Logger log;

   // @Test
    public void testRegister() throws Exception {
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");
        memberRegistration.register(newMember);
       // assertNotNull(newMember.getId());
        log.info(newMember.getName() + " was persisted with id " + newMember.getId());
    }
}