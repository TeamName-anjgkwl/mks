package memo.example.demo.repository;

import memo.example.demo.domain.Memo;
import memo.example.demo.domain.Memo.MemoStatus;
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

    // 벌크 업데이트 적용: 만료 시간이 지난 불메모를 단 한 번의 쿼리로 휴지통 이동 처리
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Memo m SET m.status = :trashStatus, m.deletedAt = :now " +
            "WHERE m.expiredAt IS NOT NULL " +
            "AND m.expiredAt <= :now " +
            "AND m.status != :trashStatus")
    int expireMemosToTrash(@Param("now") LocalDateTime now, @Param("trashStatus") MemoStatus trashStatus);
}