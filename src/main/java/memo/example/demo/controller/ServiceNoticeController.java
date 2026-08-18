package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.response.ServiceNoticeResponseDto;
import memo.example.demo.repository.ServiceNoticeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/service-notices")
@RequiredArgsConstructor
public class ServiceNoticeController {

    private final ServiceNoticeRepository serviceNoticeRepository;

    @GetMapping
    public ResponseEntity<?> getServiceNotices() {
        return ResponseEntity.ok(serviceNoticeRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(ServiceNoticeResponseDto::from)
                .collect(Collectors.toList()));
    }
}