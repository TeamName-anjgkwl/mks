package memo.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "memo")
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long memoId;

    // ★ FK: 작성자 (User 테이블 참조)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ★ FK: 소속 팀 (TeamSpace 테이블 참조, 개인 메모일 경우 null 허용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_space_id")
    private TeamSpace teamSpace;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MemoStatus status;

    @Column(name = "m_title", length = 55, nullable = false)
    private String mTitle;


    @Column(name = "m_content", columnDefinition = "TEXT", nullable = false)
    private String mContent;

    @Column(name = "is_pinned", nullable = false)
    @Builder.Default
    private Boolean isPinned = false;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum MemoStatus {
        NORMAL, FIRE, ICE, TRASH
    }
}