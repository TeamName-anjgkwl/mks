package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.response.DeviceResponseDto;
import memo.example.demo.DTO.response.MessageResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    // 1. 로그인된 기기 목록 조회
    @GetMapping
    public ResponseEntity<?> getDevices() {
        return ResponseEntity.ok(List.of(
                DeviceResponseDto.builder().deviceId(1L).deviceName("iPhone 16 Pro").build()
        ));
    }

    // 2. 특정 기기 강제 로그아웃 (연결 해제)
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<MessageResponseDto> logoutDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(new MessageResponseDto("성공적으로 기기에서 로그아웃 되었습니다."));
    }
}