package memo.example.demo.repository;

import memo.example.demo.domain.TeamTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamTodoRepository extends JpaRepository<TeamTodo, Long> {

    // 특정 팀 스페이스의 할 일 목록 조회
    List<TeamTodo> findByTeamSpace_TeamSpaceId(Long teamSpaceId);

    // V10: 엔티티에 title이 추가되었으므로 제목(title)과 내용(content) 모두에서 키워드 검색
    @Query("SELECT t FROM TeamTodo t WHERE t.title LIKE %:keyword% OR t.content LIKE %:keyword%")
    List<TeamTodo> searchByKeyword(@Param("keyword") String keyword);
}