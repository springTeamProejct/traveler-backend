package traveler.travel.global.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import traveler.travel.domain.account.repository.UserRepository;
import traveler.travel.domain.account.service.LoginService;
import traveler.travel.global.login.filter.JsonUsernamePasswordAuthenticationFilter;
import traveler.travel.global.login.handler.LoginFailureHandler;
import traveler.travel.global.login.handler.LoginSuccessJWTProviderHandler;
import traveler.travel.jwt.JwtAuthenticationProcessingFilter;
import traveler.travel.jwt.JwtService;

@RequiredArgsConstructor
@EnableWebSecurity    //시큐리티 필터가 등록이 된다.
@EnableGlobalMethodSecurity(prePostEnabled = true)	//특정 주소로 접근하면 권한 인증을 미리 체크
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final LoginService loginService;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Bean
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManger(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encodePWD());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()
                //httpBasic 인증방법 비활성화(특정 리소스에 접근할 때 username과 password 물어봄)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers("/users/**", "/")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterAfter(jsonUsernamePasswordLoginFilter(), LogoutFilter.class);

        return http.build();
    }

    @Bean
    public LoginSuccessJWTProviderHandler loginSuccessJWTProviderHandler(){
        return new LoginSuccessJWTProviderHandler(jwtService, userRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter(){
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManger());
        jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessJWTProviderHandler());
        jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter(){
        JwtAuthenticationProcessingFilter jsonUsernamePasswordLoginFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);

        return jsonUsernamePasswordLoginFilter;
    }
}
