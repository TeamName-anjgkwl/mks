package memo.example.demo.repository;

import memo.example.demo.domain.ServiceNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceNoticeRepository extends JpaRepository<ServiceNotice, Long> {
    List<ServiceNotice> findAllByOrderByCreatedAtDesc();
}