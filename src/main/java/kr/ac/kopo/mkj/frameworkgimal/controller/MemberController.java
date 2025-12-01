package kr.ac.kopo.mkj.frameworkgimal.controller;

import kr.ac.kopo.mkj.frameworkgimal.domain.Member;
import kr.ac.kopo.mkj.frameworkgimal.service.CommentService;
import kr.ac.kopo.mkj.frameworkgimal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CommentService commentService;

    // ✅ 마이페이지 화면
    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal UserDetails userDetails,
                         Model model) {

        String email = userDetails.getUsername();
        Member member = memberService.getByEmail(email);

        model.addAttribute("member", member);
        return "mypage";   // templates/mypage.html
    }

    // ✅ 마이페이지 정보 수정 처리
    @PostMapping("/mypage")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String name,
                                @RequestParam(required = false) String bio,
                                @RequestParam(required = false) String newPassword) {

        String email = userDetails.getUsername();
        memberService.updateProfile(email, name, bio, newPassword);

        return "redirect:/mypage?success";
    }

    // (선택) 내가 쓴 댓글 목록 - 필요하면 사용
    @GetMapping("/mypage/comments")
    public String myComments(@AuthenticationPrincipal UserDetails userDetails,
                             Model model) {

        String email = userDetails.getUsername();
        Member member = memberService.getByEmail(email);

        model.addAttribute("member", member);
        model.addAttribute("comments", commentService.getCommentsByAuthor(member));

        return "mypage-comments";  // templates/mypage-comments.html (옵션)
    }
}
