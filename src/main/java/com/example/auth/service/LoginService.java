package com.example.auth.service;

import com.example.web.member.dto.MemberDTO;

public interface LoginService {
    public MemberDTO login (String id, String password);
}
