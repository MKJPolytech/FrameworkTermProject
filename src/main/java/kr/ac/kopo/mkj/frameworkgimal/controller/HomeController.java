package kr.ac.kopo.mkj.frameworkgimal.controller;

import kr.ac.kopo.mkj.frameworkgimal.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CommentService commentService;

    @GetMapping({"/", "/home"})
    public String home() {
        return "HomePage"; // HomePage.html
    }

    @GetMapping("/info")
    public String info(Model model) {
        String pageId = "INFO";
        model.addAttribute("pageId", pageId);
        model.addAttribute("comments", commentService.getComments(pageId));
        return "InfoPage";
    }

    // 사진
    @GetMapping("/photos")
    public String photosRoot() {
        return "PhotoPageInitial";
    }

    @GetMapping("/photos/indoors")
    public String photosIndoors(Model model) {
        String pageId = "PHOTO_IN";
        model.addAttribute("pageId", pageId);
        model.addAttribute("comments", commentService.getComments(pageId));
        return "PhotoPageInDoors";
    }

    @GetMapping("/photos/outdoors")
    public String photosOutdoors(Model model) {
        String pageId = "PHOTO_OUT";
        model.addAttribute("pageId", pageId);
        model.addAttribute("comments", commentService.getComments(pageId));
        return "PhotoPageOutDoors";
    }

    // 동영상
    @GetMapping("/videos")
    public String videosRoot() {
        return "VideosPageInitial";
    }

    @GetMapping("/videos/indoors")
    public String videosIndoors(Model model) {
        String pageId = "VIDEO_IN";
        model.addAttribute("pageId", pageId);
        model.addAttribute("comments", commentService.getComments(pageId));
        return "VideosPageInDoors";
    }

    @GetMapping("/videos/outdoors")
    public String videosOutdoors(Model model) {
        String pageId = "VIDEO_OUT";
        model.addAttribute("pageId", pageId);
        model.addAttribute("comments", commentService.getComments(pageId));
        return "VideosPageOutDoors";
    }
}
