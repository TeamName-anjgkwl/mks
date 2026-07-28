package memo.example.demo.repository;

import memo.example.demo.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByRequester_UserId(Long requesterId);
    List<Friend> findByReceiver_UserId(Long receiverId);
}