package memo.example.demo.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    // JwtTokenProvider가 Component 스캔으로 자동 등록되므로,
    // 필요 시 추가적인 JWT 관련 외부 빈 설정을 여기에 작성합니다.
}