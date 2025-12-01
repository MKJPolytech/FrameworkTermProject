package kr.ac.kopo.mkj.frameworkgimal.service;

import kr.ac.kopo.mkj.frameworkgimal.domain.Member;
import kr.ac.kopo.mkj.frameworkgimal.domain.Role;
import kr.ac.kopo.mkj.frameworkgimal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    // ✅ 이메일로 회원 한 명 조회
    public Member getByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다: " + email));
    }

    // ✅ ID로 회원 한 명 조회 (admin용)
    public Member getById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id = " + id));
    }

    // ✅ 전체 회원 목록 (admin용)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // ✅ 마이페이지에서 프로필/비밀번호 수정
    @Transactional
    public void updateProfile(String email, String name, String bio, String rawNewPassword) {
        Member member = getByEmail(email);

        member.setName(name);
        member.setBio(bio);

        if (rawNewPassword != null && !rawNewPassword.isBlank()) {
            member.setPassword(passwordEncoder.encode(rawNewPassword));
        }
        // JPA dirty checking → save() 안 해도 update 반영
    }
}
