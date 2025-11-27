package kr.ac.kopo.mkj.frameworkgimal.repository;

import kr.ac.kopo.mkj.frameworkgimal.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}