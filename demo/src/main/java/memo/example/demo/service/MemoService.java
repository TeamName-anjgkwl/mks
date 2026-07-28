package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.controller.MemoController.*;
import memo.example.demo.domain.Memo;
import memo.example.demo.domain.Memo.MemoStatus;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.User;
import memo.example.demo.repository.MemoRepository;
import memo.example.demo.repository.TeamSpaceRepository;
import memo.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoService {

    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final TeamSpaceRepository teamSpaceRepository;

    public void createMemo(Long userId, MemoCreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        TeamSpace teamSpace = request.teamSpaceId() != null ?
                teamSpaceRepository.findById(request.teamSpaceId()).orElse(null) : null;

        Memo memo = Memo.builder()
                .user(user)
                .teamSpace(teamSpace)
                .status(request.status() != null ? MemoStatus.valueOf(request.status()) : MemoStatus.NORMAL)
                .mTitle(request.title())
                .mContent(request.content())
                .expiredAt(request.expiredAt() != null ? LocalDateTime.parse(request.expiredAt()) : null)
                .build();

        memoRepository.save(memo);
    }

    @Transactional(readOnly = true)
    public List<MemoListResponse> getUserMemos(Long userId) {
        return memoRepository.findByUser_UserIdAndTeamSpaceIsNull(userId).stream()
                .map(m -> new MemoListResponse(m.getMemoId(), m.getMTitle(), m.getStatus().name(), m.getIsPinned()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemoListResponse> getTeamMemos(Long teamSpaceId) {
        return memoRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).stream()
                .map(m -> new MemoListResponse(m.getMemoId(), m.getMTitle(), m.getStatus().name(), m.getIsPinned()))
                .collect(Collectors.toList());
    }

    public void updateMemo(Long memoId, MemoUpdateRequest request) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        if(request.title() != null) memo.setMTitle(request.title());
        if(request.content() != null) memo.setMContent(request.content());
    }

    // V10: 명시적인 Status 변경
    public void updateStatus(Long memoId, MemoStatus status) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        memo.setStatus(status);
    }

    // V10: 명시적인 핀 상태 변경 (Boolean value 처리)
    public void updatePin(Long memoId, boolean isPinned) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        memo.setIsPinned(isPinned);
    }

    public void moveMemoToTrash(Long memoId) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        memo.setStatus(MemoStatus.TRASH);
        memo.setDeletedAt(LocalDateTime.now());
    }
}