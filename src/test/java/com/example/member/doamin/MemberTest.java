package com.example.member.doamin;

import com.example.web.member.domain.Member;
import com.example.web.member.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 사용자_생성_조회() {
        //given
        String username="myname";
        String password = "mypassword";
        String email ="aaa@naver.com";
        String role = "ROLE_USER";

        //when
        Member entity = memberRepository.save(Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .build());

        Member myEntity = memberRepository.findById(entity.getId()).orElseThrow(() -> new IllegalArgumentException("ID로 사용자를 찾을수 없음"));

        //then
        assertThat(myEntity.getUsername()).isEqualTo(username);
        assertThat(myEntity.getPassword()).isEqualTo(password);
        assertThat(myEntity.getEmail()).isEqualTo(email);
        assertThat(myEntity.getRole()).isEqualTo(role);
    }
}
