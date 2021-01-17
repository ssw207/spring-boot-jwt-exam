package com.example.web.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class Member {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Size(min = 4, max = 20)
    @NotNull
    @Column(length = 30, unique = true)
    private String username;

    @NotNull
    @Size(min = 4, max = 100)
    @Column(length = 100)
    private String password;

    @Column(length = 50)
    private String email;

    @NotNull
    private String role;

    @Builder
    public Member(String username, String password, String email, String role) {
        this.username=username;
        this.password=password;
        this.email=email;
        this.role=role;
    }
}
