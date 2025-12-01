package kr.ac.kopo.mkj.frameworkgimal.service;

import kr.ac.kopo.mkj.frameworkgimal.domain.Comment;
import kr.ac.kopo.mkj.frameworkgimal.domain.Member;
import kr.ac.kopo.mkj.frameworkgimal.domain.Role;
import kr.ac.kopo.mkj.frameworkgimal.repository.CommentRepository;
import kr.ac.kopo.mkj.frameworkgimal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public List<Comment> getComments(String pageId) {
        return commentRepository.findByPageIdOrderByCreatedAtDesc(pageId);
    }

    public void saveComment(String pageId, String content, String authorEmail) {
        Member author = memberRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        Comment comment = new Comment();
        comment.setPageId(pageId);
        comment.setContent(content);
        comment.setAuthor(author);

        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, String requesterEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        boolean isOwner = comment.getAuthor().getEmail().equals(requesterEmail);
        boolean isAdmin = requester.getRole() == Role.ADMIN;

        if (isOwner || isAdmin) {
            commentRepository.delete(comment);
        } else {
            // 권한 없으면 무시하거나 예외
            throw new IllegalStateException("삭제 권한 없음");
        }
    }

    // ✅ 회원 기준 댓글 조회 (admin + 마이페이지 양쪽에서 사용)
    public List<Comment> getCommentsByAuthor(Member author) {
        return commentRepository.findByAuthorOrderByCreatedAtDesc(author);
    }
}
