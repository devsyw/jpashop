package com.jpabook.jpashop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    // 1대 다 관계
    // mappedBy는 FK가 없는쪽에 적어주는것을 권장
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
