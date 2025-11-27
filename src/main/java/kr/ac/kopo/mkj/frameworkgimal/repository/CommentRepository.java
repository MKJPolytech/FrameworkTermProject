package kr.ac.kopo.mkj.frameworkgimal.repository;

import kr.ac.kopo.mkj.frameworkgimal.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPageIdOrderByCreatedAtDesc(String pageId);
}
