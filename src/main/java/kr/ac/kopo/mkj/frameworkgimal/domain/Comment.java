package kr.ac.kopo.mkj.frameworkgimal.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String pageId;   // INFO, PHOTO_IN, PHOTO_OUT, VIDEO_IN, VIDEO_OUT

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}