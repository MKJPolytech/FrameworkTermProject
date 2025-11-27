package kr.ac.kopo.mkj.frameworkgimal.controller;

import kr.ac.kopo.mkj.frameworkgimal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";   // templates/login.html
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";  // templates/signup.html
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String password) {

        memberService.register(name, email, password);
        return "redirect:/login?signup";
    }

    // 회원탈퇴(옵션)
    @PostMapping("/members/delete")
    public String deleteAccount(java.security.Principal principal) {
        if (principal != null) {
            memberService.deleteByEmail(principal.getName());
        }
        return "redirect:/login?deleted";
    }
}
