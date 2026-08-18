package memo.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // 추가됨
import java.util.TimeZone;

@EnableScheduling // 추가됨: 백그라운드 스케줄러 활성화
@SpringBootApplication
public class DemoApplication {
    // 날짜 밀림(Off-by-one) 방지를 위한 KST 시간대 고정
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}