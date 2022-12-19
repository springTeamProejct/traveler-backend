package traveler.travel.global.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import traveler.travel.entity.User;
import traveler.travel.enums.Authority;
import traveler.travel.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    ObjectMapper objectMapper = new ObjectMapper();

    private static String KEY_USERNAME = "username";
    private static String KEY_PASSWORD = "password";
    private static String USERNAME = "user10@naver.com";
    private static String PASSWORD = "Password12";

    private static String LOGIN_URL = "/login";

    private void clear(){
        em.flush();
        em.clear();
    }

    @BeforeEach
    private void init(){
        userRepository.save(User.builder()
                .email(USERNAME)
                .password(encoder.encode(PASSWORD))
                .nickname("Nickname")
                .authority(Authority.ROLE_USER)
                .build());
        clear();
    }

    private Map getUsernamePasswordMap(String email, String password){
        Map<String, String> map = new HashMap<>();
        map.put(KEY_USERNAME, email);
        map.put(KEY_PASSWORD, password);
        return map;
    }

    private ResultActions perform(String url, MediaType mediaType, Map usernamePasswordMap) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(mediaType)
                .content(objectMapper.writeValueAsString(usernamePasswordMap)));
    }

    @Test
    public void 로그인_성공() throws Exception{
        Map<String, String> map = getUsernamePasswordMap(USERNAME, PASSWORD);

        MvcResult result = perform(LOGIN_URL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
