package com.roundtable.roundtable.entity.house;

import com.roundtable.roundtable.entity.BaseEntity;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class House extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "house")
    private List<Member> members = new ArrayList<>();



    public static final int MAX_MEMBER_SIZE = 50;

    private House(String name) {
        this.name = name;
    }

    public static House of(String name) {
        return new House(name);
    }

    public void addMember(Member member) {
        if(!members.contains(member)) {
            members.add(member);
        }
    }
}
