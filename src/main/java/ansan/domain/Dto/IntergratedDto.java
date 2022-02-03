package ansan.domain.Dto;

import ansan.domain.Entity.Member.MemberEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
public class IntergratedDto implements UserDetails {

    // 일반회원 + oauth[ 소셜 계정 ] => 통합 dto
    private int m_num; // 회원번호
    private String mid; // 회원아이디3
    private String m_password; // 회원패스워드
    private final Set<GrantedAuthority> authorities; // 인증

    // 생성자
    public IntergratedDto(MemberEntity memberEntity , Collection< ? extends GrantedAuthority> authorities ){
        this.mid = memberEntity.getMid();
        this.m_password = memberEntity.getM_password();
        this.m_num = memberEntity.getM_num();
        this.authorities = Collections.unmodifiableSet( new LinkedHashSet<>( this.sortAuthorities(authorities) ) );
    }

    //
    private Set<GrantedAuthority> sortAuthorities( Collection<? extends GrantedAuthority> authorities ){
        SortedSet<GrantedAuthority> sortAuthorities = new TreeSet<>( Comparator.comparing( GrantedAuthority::getAuthority) );
        sortAuthorities.addAll( authorities );
        return sortAuthorities;
    }


    @Override
    public String getPassword() {
        return this.m_password;
    }

    @Override
    public String getUsername() {
        return this.mid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
