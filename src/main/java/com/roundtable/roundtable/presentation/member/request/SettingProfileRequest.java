package com.roundtable.roundtable.presentation.member.request;

import com.roundtable.roundtable.entity.member.Gender;
import com.roundtable.roundtable.implement.member.MemberProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SettingProfileRequest(
        @NotBlank
        String name,

        @NotNull
        Gender gender
) {

        public MemberProfile toMemberProfile() {
                return new MemberProfile(
                        name,
                        gender
                );
        }
}
