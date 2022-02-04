package ansan.config;

import ansan.service.MemberService;
import ansan.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.hibernate.criterion.Restrictions.and;

@Configuration // 설정 클래스 설정
@EnableWebSecurity // 시큐리티
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 시큐리티 설정 연결 클래스 상속
    // 스프링 시큐리티 설정[ 보안 솔루션 ]
        // 1. 웹 페이지 접근시 시큐리티 로그인페이지 실행 [ 콘솔 password 확인 ]
        // 2. 읽기 쓰기 삭제 불가 => csrf[ 사이트 간 요청 위조 ]

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 웹 URL 접근 보안
        http.authorizeRequests()    // URL 인증요청
                .antMatchers("/admin/**").hasRole("ADMIN")  //  .antMatchers("URL").hasRole("권한명") : 권한이 ADMIN 이면 접근 가능
                .antMatchers("/member/info").hasRole("MEMBER") // info 페이지는 권한이 MEMBER 인 경우에만 접근 가능
                .antMatchers("/**").permitAll()    //  .antMatchers("URL").permitAll() : 모든 권한이 접근 가능
                .and()
                    .csrf() // 사이트 간 요청 위조 설정
                    .ignoringAntMatchers("/**") // ignoringAntMatchers("url") : 요청위조 보안 제외 할 URL
                .and()
                    .formLogin()    // 로그인페이지 보안 설정
                    .loginPage("/member/login") // 아이디/비밀번호 입력받을 페이지 URL
                    .loginProcessingUrl("/member/logincontroller") // 로그인처리할 URL
                    .defaultSuccessUrl("/") // 로그인 성공시 이동할 URL
                    .usernameParameter("mid")       // 시큐리티 로그인[ 아이디 ] 기본값은 : username  -> mid 으로 변수명 사용
                    .passwordParameter("m_password") // 시큐리티 로그인 [ 패스워드 ] 기본값은 : password -> m_password 으로 변수명 사용
                .and()
                    .logout()   // 로그아웃 관련 설정
                    .logoutRequestMatcher( new AntPathRequestMatcher("/member/logout") ) // 로그아웃 URL 설정
                    .logoutSuccessUrl( "/" ) // 로그아웃 성공시
                    .invalidateHttpSession(true) // 세션 초기화
                .and()
                    .exceptionHandling() // 예외[오류] 페이지 설정
                    .accessDeniedPage("/error")   // 오류 페이지 발생시 -> 오류페이지 URL
                .and()
                .oauth2Login()  // oauth2 로그인 설정
                .userInfoEndpoint()
                .userService( oauthService ); // oauth2 서비스
    }

    @Autowired
    private OauthService oauthService;

    @Autowired
    private MemberService memberService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {  // 인증 관련 보안
        auth.userDetailsService(memberService).passwordEncoder( passwordEncoder());
    }

    @Autowired
    public PasswordEncoder passwordEncoder(){   // 패스워드 암호화 관련 클래스
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {   // 웹 리소스 접근 보안
    }


}
