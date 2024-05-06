package com.roundtable.roundtable.presentation.auth.request;

import com.roundtable.roundtable.business.member.dto.RegisterMember;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank @Email
        String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "비밀번호는 대문자와 숫자를 포함해 8자 이상이어야 합니다.")
        String password
) {
        public RegisterMember toRegisterMember() {
                return new RegisterMember(
                        email,
                        password
                );
        }
}
