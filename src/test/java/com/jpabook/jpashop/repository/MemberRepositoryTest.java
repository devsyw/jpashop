package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.dto.MemberDTO;
import com.jpabook.jpashop.entity.Member;
import com.jpabook.jpashop.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("memberA", 10);
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void testQueryMethod() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
//        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("bbb", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("aaa", 10);

        Assertions.assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("bbb", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        Assertions.assertThat(usernameList.get(0)).isEqualTo("aaa");
        Assertions.assertThat(usernameList.get(1)).isEqualTo("bbb");
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("aaa", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDTO> memberDto = memberRepository.findMemberDto();
        Assertions.assertThat(memberDto.get(0).getUsername()).isEqualTo("aaa");
        Assertions.assertThat(memberDto.get(0).getTeamName()).isEqualTo("teamA");
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("bbb", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> usernameList = memberRepository.findByNames(Arrays.asList("aaa", "bbb"));
        Assertions.assertThat(usernameList.get(0).getUsername()).isEqualTo("aaa");
        Assertions.assertThat(usernameList.get(1).getUsername()).isEqualTo("bbb");
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);

        // 타입을 유연하게 사용 가능, 아래의 경우는 단건 return을 보장함
        List<Member> aaa = memberRepository.findListByUsername("AAA"); // 데이터가 없을경우 emptyCollection 반환
        Member bbb = memberRepository.findMemberByUsername("AAA"); // 데이터가 없을경우 null 반환
        Optional<Member> ccc = memberRepository.findOptionalByUsername("AAA"); // 데이터가 없을경우 Optional.empty 반환
    }
}
