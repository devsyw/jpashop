package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.dto.MemberDTO;
import com.jpabook.jpashop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); // 쿼리메서드 기능

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername") // 없어도 됨, 제너릭 타입 + 메서드명으로 네임드쿼리를 먼저 찾음, 잘 안씀
//    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new com.jpabook.jpashop.dto.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    // jpa에서 조인하는 방법, 특정 타입의 DTO 객체(같은)를 사용할때는 new 를 이용하여 경로를 포함한 파일명을 생성해주어야 함
    List<MemberDTO> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건을 Optional 감싸기
}
