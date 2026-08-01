package memo.example.demo.repository;

import memo.example.demo.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 특정 팀 스페이스의 일정 조회
    List<Schedule> findByTeamSpace_TeamSpaceId(Long teamSpaceId);

    // 개인 일정 조회
    List<Schedule> findByUser_UserIdAndTeamSpaceIsNull(Long userId);

    // 통합 검색용 쿼리 (제목, 내용)
    @Query("SELECT s FROM Schedule s WHERE s.sTitle LIKE %:keyword% OR s.sContent LIKE %:keyword%")
    List<Schedule> searchByKeyword(@Param("keyword") String keyword);

    // V10: API 명세에 명시된 '월별 일정 조회'를 위한 커스텀 쿼리 (startAt 기준)
    @Query("SELECT s FROM Schedule s WHERE s.teamSpace.teamSpaceId = :teamSpaceId " +
            "AND YEAR(s.startAt) = :year AND MONTH(s.startAt) = :month")
    List<Schedule> findByTeamSpaceAndMonth(
            @Param("teamSpaceId") Long teamSpaceId,
            @Param("year") int year,
            @Param("month") int month);

    // 개인 일정 월별 조회용
    @Query("SELECT s FROM Schedule s WHERE s.user.userId = :userId AND s.teamSpace IS NULL " +
            "AND YEAR(s.startAt) = :year AND MONTH(s.startAt) = :month")
    List<Schedule> findByUserAndMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month);
}