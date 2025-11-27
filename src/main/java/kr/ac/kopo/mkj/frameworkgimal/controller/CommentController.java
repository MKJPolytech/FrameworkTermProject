package kr.ac.kopo.mkj.frameworkgimal.controller;

import kr.ac.kopo.mkj.frameworkgimal.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public String addComment(@RequestParam String pageId,
                             @RequestParam String content,
                             Principal principal) {

        commentService.saveComment(pageId, content, principal.getName());

        // pageId 에 따라서 원래 페이지로 리다이렉트
        return switch (pageId) {
            case "INFO" -> "redirect:/info";
            case "PHOTO_IN" -> "redirect:/photos/indoors";
            case "PHOTO_OUT" -> "redirect:/photos/outdoors";
            case "VIDEO_IN" -> "redirect:/videos/indoors";
            case "VIDEO_OUT" -> "redirect:/videos/outdoors";
            default -> "redirect:/home";
        };
    }

    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable Long id,
                                @RequestParam String pageId,
                                Principal principal) {

        commentService.deleteComment(id, principal.getName());

        return switch (pageId) {
            case "INFO" -> "redirect:/info";
            case "PHOTO_IN" -> "redirect:/photos/indoors";
            case "PHOTO_OUT" -> "redirect:/photos/outdoors";
            case "VIDEO_IN" -> "redirect:/videos/indoors";
            case "VIDEO_OUT" -> "redirect:/videos/outdoors";
            default -> "redirect:/home";
        };
    }
}
