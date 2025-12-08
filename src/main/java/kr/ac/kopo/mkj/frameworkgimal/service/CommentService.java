package kr.ac.kopo.mkj.frameworkgimal.service;

import kr.ac.kopo.mkj.frameworkgimal.domain.Comment;
import kr.ac.kopo.mkj.frameworkgimal.domain.Member;
import kr.ac.kopo.mkj.frameworkgimal.domain.Role;
import kr.ac.kopo.mkj.frameworkgimal.repository.CommentRepository;
import kr.ac.kopo.mkj.frameworkgimal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public List<Comment> getComments(String pageId) {
        return commentRepository.findByPageIdOrderByCreatedAtDesc(pageId);
    }

    @Transactional
    public void saveComment(String pageId, String content, String authorEmail) {
        Member author = findMemberByEmail(authorEmail);

        Comment comment = new Comment();
        comment.setPageId(pageId);
        comment.setContent(content);
        comment.setAuthor(author);

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String requesterEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId));

        Member requester = findMemberByEmail(requesterEmail);

        validateDeletePermission(comment, requester);
        commentRepository.delete(comment);
    }

    public List<Comment> getCommentsByAuthor(Member author) {
        return commentRepository.findByAuthorOrderByCreatedAtDesc(author);
    }

    // ✅ 헬퍼 메서드들
    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + email));
    }

    private void validateDeletePermission(Comment comment, Member requester) {
        boolean isOwner = comment.getAuthor().getEmail().equals(requester.getEmail());
        boolean isAdmin = requester.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new IllegalStateException("댓글 삭제 권한이 없습니다.");
        }
    }
}
