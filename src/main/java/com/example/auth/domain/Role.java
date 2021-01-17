package com.example.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Role {

    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "사용자"),
    UNKNOWN("UNKNOWN", "알수없는 권한");

    private String code;
    private String desc;

    /**
     * Role 코드를 입력하면 Roel Enum을 리턴
     * @param code
     * @return
     */
    public static Role of(String code) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getCode().equals(code))
                .findAny()
                .orElse(UNKNOWN);
    }
}
