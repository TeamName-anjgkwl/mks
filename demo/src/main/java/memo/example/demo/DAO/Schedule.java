package memo.example.demo.DAO;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    // ★ FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ★ FK (개인 일정이면 null 가능)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_space_id")
    private TeamSpace teamSpace;

    @Column(name = "s_title", length = 55, nullable = false)
    private String sTitle;

    @Column(name = "s_content", length = 255)
    private String sContent;

    @Column(name = "schedule_date", nullable = false)
    private LocalDateTime scheduleDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}