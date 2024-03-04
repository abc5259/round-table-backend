package com.roundtable.roundtable.presentation.auth.request;

import com.roundtable.roundtable.business.member.LoginMember;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email @NotBlank
        String email,
        @NotBlank
        String password
) {
        public LoginMember toLoginMember() {
                return new LoginMember(
                        email,
                        password
                );
        }
}
