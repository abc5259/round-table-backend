package com.roundtable.roundtable.presentation.auth.request;

import com.roundtable.roundtable.business.member.dto.RegisterMember;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank @Email
        String email,
        @NotBlank
        String password
) {
        public RegisterMember toRegisterMember() {
                return new RegisterMember(
                        email,
                        password
                );
        }
}
