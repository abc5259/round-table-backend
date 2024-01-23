package com.roundtable.roundtable.member.domain;

import com.roundtable.roundtable.global.domain.BaseEntity;
import com.roundtable.roundtable.house.domain.House;
import com.roundtable.roundtable.member.exception.MemberException.MemberUnAuthorizationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(columnDefinition = "char(1) default 'M'")
    private String gender;

    @ManyToOne
    @JoinColumn(name = "HOUSE_ID")
    private House house;

    private Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Member of(String email, String password, PasswordEncoder passwordEncoder) {
        validateCreateMember(email, password);
        return new Member(email, passwordEncoder.encode(password));
    }

    private static void validateCreateMember(String email, String password) {
        Assert.notNull(email, "이메일은 필수 입니다.");
        Assert.notNull(password, "비밀번호는 필수 입니다.");
    }

    public void checkPassword(String password, PasswordEncoder passwordEncoder) {
        boolean isMatchPassword = passwordEncoder.matches(password, this.password);
        if(!isMatchPassword) {
            throw new MemberUnAuthorizationException("아이디와 패스워드를 다시 확인 후 로그인해주세요.");
        }
    }

    public void settingProfile(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }
}
