package traveler.travel.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import traveler.travel.domain.account.entity.User;
import traveler.travel.domain.account.enums.Authority;
import traveler.travel.domain.account.repository.UserRepository;
import traveler.travel.jwt.JwtService;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JwtFilterAuthenticationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Autowired
    JwtService jwtService;

    PasswordEncoder passEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String USERNAME = "user11@naver.com";
    private static final String PASSWORD = "User12";

    private String LOGIN_URL = "/users/login";

    private final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private final String BEARER = "Bearer";

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void init() {
        userRepository.save(User.builder()
                .email(USERNAME)
                .password(passEncoder.encode(PASSWORD))
                .nickname("user")
                .authority(Authority.ROLE_USER)
                .build());
        clear();
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    private Map getUsernamePasswordMap(String email, String password) {
        Map<String, String> map = new HashMap<>();
        map.put(KEY_USERNAME, USERNAME);
        map.put(KEY_PASSWORD, PASSWORD);
        return map;
    }

    private Map getAccessAndRefreshToken() throws Exception {

        Map<String, String> map = getUsernamePasswordMap(USERNAME, PASSWORD);

        MvcResult result = mockMvc.perform(post(LOGIN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(map)))
                .andReturn();

        String accessToken = result.getResponse().getHeader(accessHeader);
        String refreshToken = result.getResponse().getHeader(refreshHeader);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(accessHeader, accessToken);
        tokenMap.put(refreshHeader, refreshToken);

        return tokenMap;
    }

    /*
    * AccessToken: 존재하지 않음,
    * RefreshToken: 존재하지 않음.
    */
//    @Test
//    public void Access_Refresh_모두_존재_X() throws Exception {
//        mockMvc.perform(get(LOGIN_URL+"123"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void AccessToken만_보내서_인증() throws Exception{
//        Map accessAndRefreshToken = getAccessAndRefreshToken();
//        String accessToken = (String) accessAndRefreshToken.get(accessHeader);
//
//        mockMvc.perform(get(LOGIN_URL+"12").header(accessHeader, BEARER+accessToken))
//                .andExpectAll(status().isNotFound());
//    }
//
//    @Test
//    public void 안유효한AccessToken만_보내서_인증X_상태코드는_403() throws Exception{
//        Map accessAndRefreshToken = getAccessAndRefreshToken();
//        String accessToken = (String) accessAndRefreshToken.get(accessHeader);
//
//        mockMvc.perform(get(LOGIN_URL+"123").header(accessHeader, accessToken+"1"))
//                .andExpectAll(status().isForbidden());
//    }

//    @Test
//    public void 유효한RefreshToken만_보내서_AccessToken_재발급_200() throws Exception{
//        Map accessAndRefreshToken = getAccessAndRefreshToken();
//        String refreshToken = (String) accessAndRefreshToken.get(refreshHeader);
//
//        MvcResult result = mockMvc.perform(get(LOGIN_URL+"123").header(refreshHeader, BEARER+refreshToken))
//                .andExpect(status().isOk()).andReturn();
//
//        String accessToken = result.getResponse().getHeader(accessHeader);
//
//        String subject = JWT.require(Algorithm.HMAC512(secret)).build().verify(accessToken).getSubject();
//        assertThat(subject).isEqualTo(ACCESS_TOKEN_SUBJECT);
//    }

    @Test
    public void 로그인_주소로_보내면_필터작동_X() throws Exception{
        Map accessAndRefreshToken = getAccessAndRefreshToken();
        String accessToken = (String) accessAndRefreshToken.get(accessHeader);
        String refreshToken = (String) accessAndRefreshToken.get(refreshHeader);

        MvcResult result = mockMvc.perform(post(LOGIN_URL)
                //get인 경우 config에서 permitAll을 했기에 notFound
                .header(refreshHeader, BEARER+refreshToken)
                .header(accessHeader, BEARER+accessToken))
        .andExpect(status().isOk())
                .andReturn();
    }

}
