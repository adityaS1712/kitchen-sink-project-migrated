package org.jboss.as.quickstarts.kitchensink.test;

//import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.logging.Logger;

import org.jboss.as.quickstarts.kitchensink.model.Member;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
public class RemoteMemberRegistrationIT {

    private static final Logger log = Logger.getLogger(RemoteMemberRegistrationIT.class.getName());
//
//    @Autowired
//    private RestTemplate restTemplate;

    protected String getHTTPEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080/kitchensink";
        }
        return host + "/rest/members";
    }

    private String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

   // @Test
    public void testRegister() throws Exception {
        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane@mailinator.com");
        newMember.setPhoneNumber("2125551234");

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Member> request = new HttpEntity<>(newMember, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(getHTTPEndpoint(), HttpMethod.POST, request, String.class);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("", response.getBody());
    }
}