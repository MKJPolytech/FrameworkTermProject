package kr.ac.kopo.mkj.frameworkgimal.service;

import kr.ac.kopo.mkj.frameworkgimal.domain.Member;
import kr.ac.kopo.mkj.frameworkgimal.domain.Role;
import kr.ac.kopo.mkj.frameworkgimal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
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

    @Transactional
    public void deleteByEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(memberRepository::delete);
    }

    // ✅ ID로 삭제 (관리자용)
    @Transactional
    public void deleteById(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다. ID: " + id);
        }
        memberRepository.deleteById(id);
    }

    public Member getByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다: " + email));
    }

    public Member getById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id = " + id));
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public void updateProfile(String email, String name, String bio, String rawNewPassword) {
        Member member = getByEmail(email);

        member.setName(name);
        member.setBio(bio);

        if (rawNewPassword != null && !rawNewPassword.isBlank()) {
            member.setPassword(passwordEncoder.encode(rawNewPassword));
        }
    }
}