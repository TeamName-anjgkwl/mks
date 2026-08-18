package memo.example.demo.repository;

import memo.example.demo.domain.Friend;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @EntityGraph(attributePaths = {"requester", "receiver"})
    List<Friend> findByRequester_UserId(Long requesterId);

    @EntityGraph(attributePaths = {"requester", "receiver"})
    List<Friend> findByReceiver_UserId(Long receiverId);

    @EntityGraph(attributePaths = {"requester", "receiver"})
    List<Friend> findByReceiver_UserIdAndStatus(Long receiverId, Friend.FriendStatus status);
}