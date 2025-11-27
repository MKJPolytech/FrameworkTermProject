package kr.ac.kopo.mkj.frameworkgimal.service;

import kr.ac.kopo.mkj.frameworkgimal.domain.Member;
import kr.ac.kopo.mkj.frameworkgimal.domain.Role;
import kr.ac.kopo.mkj.frameworkgimal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 앱 시작 시 admin 계정 자동 생성
    @PostConstruct
    public void initAdmin() {
        Optional<Member> existing = memberRepository.findByEmail("admin@buddy.com");
        if (existing.isEmpty()) {
            Member admin = new Member();
            admin.setEmail("admin@buddy.com");
            admin.setName("관리자");
            admin.setPassword(passwordEncoder.encode("admin1234"));
            admin.setRole(Role.ADMIN);
            memberRepository.save(admin);
        }
    }

    public void register(String name, String email, String rawPassword) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(rawPassword));
        member.setRole(Role.USER);

        memberRepository.save(member);
    }

    public void deleteByEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(memberRepository::delete);
    }
}
