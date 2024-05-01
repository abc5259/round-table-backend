package com.roundtable.roundtable.presentation.auth.request;

import com.roundtable.roundtable.business.member.dto.LoginMember;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @Email @NotBlank
        String email,
        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "비밀번호는 대문자와 숫자를 포함해 8자 이상이어야 합니다.")
        String password
) {
        public LoginMember toLoginMember() {
                return new LoginMember(
                        email,
                        password
                );
        }
}
