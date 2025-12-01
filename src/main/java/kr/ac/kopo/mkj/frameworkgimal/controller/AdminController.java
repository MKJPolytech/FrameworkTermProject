package kr.ac.kopo.mkj.frameworkgimal.controller;

import kr.ac.kopo.mkj.frameworkgimal.domain.Member;
import kr.ac.kopo.mkj.frameworkgimal.service.CommentService;
import kr.ac.kopo.mkj.frameworkgimal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MemberService memberService;
    private final CommentService commentService;

    // ✅ 관리자: 회원 리스트
    @GetMapping("/members")
    public String memberList(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "admin/member-list";   // templates/admin/member-list.html
    }

    // ✅ 관리자: 특정 회원이 쓴 댓글 목록
    @GetMapping("/members/{id}/comments")
    public String memberComments(@PathVariable Long id,
                                 Model model) {

        Member member = memberService.getById(id);

        model.addAttribute("member", member);
        model.addAttribute("comments", commentService.getCommentsByAuthor(member));

        return "admin/member-comments";   // templates/admin/member-comments.html
    }
}
