package com.example.member.doamin;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.auth.service.LoginService;
import com.example.web.member.domain.Member;
import com.example.web.member.domain.MemberRepository;
import com.example.web.member.dto.MemberDTO;
import com.example.auth.service.LoginServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class LoginServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LoginService loginService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    public void 삭제() {
        memberRepository.deleteAll();
    }

    @Test
    public void 로그인() {
        //given
        String username="myname";
        String password = "mypassword";
        String email ="aaa@naver.com";
        String role = "ROLE_USER";

        //when
        Member entity = memberRepository.save(Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(role)
                .build());

        System.out.println(entity.toString());

        //when
        MemberDTO dto = loginService.login(email, password);

        //then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 시큐리티가 가지고 있는 인증객체, 정상로그인이 되어있으면 인증객체가 있다.
        User loginUser = (User)authentication.getPrincipal(); // 로그인한 유저정보

        assertThat(email).isEqualTo(loginUser.getUsername());
        assertThat(role).isEqualTo(loginUser.getAuthorities().stream().findFirst().get().toString());
    }

    @Test
    void loginFailedTest() {

        //given, when, then
        assertThrows(BadCredentialsException.class, () -> {
            loginService.login("sieunkr@gmail.com", "invalid_password");
        });
    }
}
