package com.roundtable.roundtable.presentation.member.request;

import com.roundtable.roundtable.implement.member.MemberProfile;
import jakarta.validation.constraints.NotBlank;

public record SettingProfileRequest(
        @NotBlank
        String name,

        @NotBlank
        String gender
) {

        public MemberProfile toMemberProfile() {
                return new MemberProfile(
                        name,
                        gender
                );
        }
}
