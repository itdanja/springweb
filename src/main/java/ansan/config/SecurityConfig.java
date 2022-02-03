package ansan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
                .ignoringAntMatchers("/**"); // ignoringAntMatchers("url") : 요청위조 보안 제외 할 URL
    }

    @Autowired
    public PasswordEncoder passwordEncoder(){   // 패스워드 암호화 관련 클래스
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {   // 웹 리소스 접근 보안
        super.configure(web);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {  // 인증 관련 보안
        super.configure(auth);
    }



}
