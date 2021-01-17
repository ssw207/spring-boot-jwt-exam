package com.example.auth.service;

import com.example.auth.domain.Role;
import com.example.auth.service.LoginService;
import com.example.web.member.domain.MemberRepository;
import com.example.web.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder; //시큐리티 인증객체를 생성하는 클래스
    private final MemberRepository memberRepository;

    public MemberDTO login(String email, String password) {
        //1)입력받은 id와 password로 인증토큰을 맏는다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);

        //2)비밀번호를 체크한다. (어떻게?? getObject() 하면 무슨 객체를 가져오지?)비밀번호가 일치하지 않으면 Exception발생 (무슨 익셉션이지?)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //3)로그인 성공시 시큐리티에 인증객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Role role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(Role::of)
                .orElse(Role.UNKNOWN);

        return MemberDTO.builder()
                .username(authentication.getName())
                .email(email)
                .role(role)
                .build();
    }
}
