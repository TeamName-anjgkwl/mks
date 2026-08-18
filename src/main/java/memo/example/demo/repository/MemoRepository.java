package memo.example.demo.repository;

import memo.example.demo.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByUser_UserIdAndTeamSpaceIsNull(Long userId);
    List<Memo> findByTeamSpace_TeamSpaceId(Long teamSpaceId);

    @Query("SELECT m FROM Memo m WHERE m.mTitle LIKE %:keyword% OR m.mContent LIKE %:keyword%")
    List<Memo> searchByKeyword(@Param("keyword") String keyword);

    // 수정됨: status를 TRASH로 바꾸지 않고 deletedAt만 업데이트
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Memo m SET m.deletedAt = :now " +
            "WHERE m.expiredAt IS NOT NULL " +
            "AND m.expiredAt <= :now " +
            "AND m.deletedAt IS NULL")
    int expireMemosToTrash(@Param("now") LocalDateTime now);

    // 수정됨: status 조건 제거, deletedAt 유무와 시간만으로 판별
    List<Memo> findByDeletedAtIsNotNullAndDeletedAtLessThanEqual(LocalDateTime dateTime);
}