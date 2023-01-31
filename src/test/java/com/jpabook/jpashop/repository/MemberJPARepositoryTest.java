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
        Member member = new Member("memberA", 10);

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
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
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

    @Test
    public void testQueryMethod() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberJPARepository.save(m1);
        memberJPARepository.save(m2);

        List<Member> result = memberJPARepository.findByUsernameAndAgeGreaterThen("aaa", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
    
    @Test
    public void paging() {
        memberJPARepository.save(new Member("member1", 10));
        memberJPARepository.save(new Member("member2", 10));
        memberJPARepository.save(new Member("member3", 10));
        memberJPARepository.save(new Member("member4", 10));
        memberJPARepository.save(new Member("member5", 10));
        memberJPARepository.save(new Member("member6", 10));
        memberJPARepository.save(new Member("member7", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = memberJPARepository.findByPage(age, offset, limit);
        long totalCount = memberJPARepository.totalCount(age);

        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(7);
    }
}