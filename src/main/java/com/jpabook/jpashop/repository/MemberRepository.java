package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
