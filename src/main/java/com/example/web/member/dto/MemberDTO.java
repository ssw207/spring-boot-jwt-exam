package com.example.web.member.dto;

import com.example.auth.domain.Role;
import lombok.Builder;

@Builder
public class MemberDTO {
    private String username;
    private String email;
    private Role role;
}
