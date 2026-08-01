package memo.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_id", length = 55, nullable = false, unique = true)
    private String loginId;

    @Column(name = "email", length = 55, unique = true)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    // 추가됨: 사용자 실명
    @Column(name = "name", length = 55, nullable = false)
    private String name;

    @Column(name = "nickname", length = 10, nullable = false)
    private String nickname;

    // 추가됨: 휴대폰 번호
    @Column(name = "phone_number", length = 20, unique = true)
    private String phoneNumber;

    @Column(name = "provider", length = 20, nullable = false)
    private String provider;

    @Column(name = "provider_id", length = 255)
    private String providerId;

    @Column(name = "profile_image_url", length = 1000)
    private String profileImageUrl;

    // 추가됨: 환경 설정 필드들
    @Column(name = "timezone", length = 50, nullable = false)
    @Builder.Default
    private String timezone = "Asia/Seoul";

    @Column(name = "date_format", length = 20, nullable = false)
    @Builder.Default
    private String dateFormat = "YYYY-MM-DD";

    @Column(name = "language", length = 20, nullable = false)
    @Builder.Default
    private String language = "ko-KR";

    // 추가됨: 2FA 사용 여부
    @Column(name = "use_2fa", nullable = false)
    @Builder.Default
    private Boolean use2fa = false;

    @Column(name = "two_factor_secret", length = 255)
    private String twoFactorSecret;

    @Column(name = "allow_push", nullable = false)
    @Builder.Default
    private Boolean allowPush = true;

    @Column(name = "allow_event", nullable = false)
    @Builder.Default
    private Boolean allowEvent = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}