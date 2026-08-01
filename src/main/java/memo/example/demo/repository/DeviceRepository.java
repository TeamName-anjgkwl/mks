package memo.example.demo.repository;

import memo.example.demo.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByRefreshToken(String refreshToken);
    void deleteByUser_UserId(Long userId); // 전체 로그아웃 시 사용
}