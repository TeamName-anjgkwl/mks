package memo.example.demo.repository;

import memo.example.demo.domain.MemoImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemoImageRepository extends JpaRepository<MemoImage, Long> {
    List<MemoImage> findByMemo_MemoId(Long memoId);
}