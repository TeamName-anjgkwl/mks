package memo.example.demo.DTO.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class DeviceResponseDto {
    private Long deviceId;
    private String deviceName;
    private LocalDateTime lastLoginAt;
}