package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false) //테스트 완료 이후에 롤백하지 않음
class MemberJPARepositoryTest {
    @Autowired
    MemberJPARepository memberJPARepository;

    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");

        //when - 아래 멤버들은 같은 객체(JPA는 영속성을 보장한다)
        Member savedMember = memberJPARepository.save(member);
        Member findMember = memberJPARepository.find(savedMember.getId());

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member); // true

    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJPARepository.save(member1);
        memberJPARepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberJPARepository.findById(member1.getId()).get();
        Member findMember2 = memberJPARepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("member!!!!");

        // 리스트 조회 검증
        List<Member> all = memberJPARepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberJPARepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJPARepository.delete(member1);
        memberJPARepository.delete(member2);

        long deletedCount = memberJPARepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }
}