package memo.example.demo.DAO;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", length = 55, unique = true)
    private String email;

    @Column(name = "login_id", length = 55, nullable = false)
    private String loginId;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "provider", length = 20, nullable = false)
    private String provider;

    @Column(name = "provider_id", length = 255)
    private String providerId;

    @Column(name = "nickname", length = 10, nullable = false)
    private String nickname;

    @Column(name = "profile_image_url", length = 1000)
    private String profileImageUrl;

    @Column(name = "allow_push", nullable = false)
    @Builder.Default
    private Boolean allowPush = true;

    @Column(name = "allow_event", nullable = false)
    @Builder.Default
    private Boolean allowEvent = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}