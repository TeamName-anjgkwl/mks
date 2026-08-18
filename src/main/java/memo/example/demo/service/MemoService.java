package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.MemoRequestDto;
import memo.example.demo.DTO.request.MemoUpdateRequestDto;
import memo.example.demo.DTO.response.MemoResponseDto;
import memo.example.demo.DTO.response.TrashResponseDto;
import memo.example.demo.domain.Memo;
import memo.example.demo.domain.Memo.MemoStatus;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.User;
import memo.example.demo.repository.MemoRepository;
import memo.example.demo.repository.TeamSpaceRepository;
import memo.example.demo.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
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

    public Long createMemo(Long userId, MemoRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow();
        TeamSpace teamSpace = request.getTeamSpaceId() != null ?
                teamSpaceRepository.findById(request.getTeamSpaceId()).orElse(null) : null;

        Memo memo = Memo.builder()
                .user(user)
                .teamSpace(teamSpace)
                .status(request.getStatus() != null ? MemoStatus.valueOf(request.getStatus()) : MemoStatus.NORMAL)
                .mTitle(request.getTitle())
                .mContent(request.getContent())
                .expiredAt(parseDateTimeSafe(request.getExpiredAt()))
                .build();

        return memoRepository.save(memo).getMemoId();
    }

    @Transactional(readOnly = true)
    public List<MemoResponseDto> getUserMemos(Long userId) {
        return memoRepository.findByUser_UserIdAndTeamSpaceIsNull(userId).stream()
                .filter(m -> m.getStatus() != MemoStatus.TRASH)
                .map(MemoResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemoResponseDto> getTeamMemos(Long teamSpaceId) {
        return memoRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).stream()
                .filter(m -> m.getStatus() != MemoStatus.TRASH)
                .map(MemoResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemoResponseDto getMemoDetail(Long memoId) {
        Memo memo = memoRepository.findById(memoId).orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
        return MemoResponseDto.from(memo);
    }

    @Transactional(readOnly = true)
    public List<TrashResponseDto> getTrashList(Long userId) {
        return memoRepository.findByUser_UserIdAndTeamSpaceIsNull(userId).stream()
                .filter(m -> m.getStatus() == MemoStatus.TRASH)
                .map(TrashResponseDto::from)
                .collect(Collectors.toList());
    }

    public void updateMemo(Long memoId, MemoUpdateRequestDto request) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        if(request.getTitle() != null) memo.setMTitle(request.getTitle());
        if(request.getContent() != null) memo.setMContent(request.getContent());
    }

    public void updateStatus(Long memoId, MemoStatus status) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        memo.setStatus(status);
    }

    public void updatePin(Long memoId, boolean isPinned) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        memo.setIsPinned(isPinned);
    }

    public void moveMemoToTrash(Long memoId) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        memo.setStatus(MemoStatus.TRASH);
        memo.setDeletedAt(LocalDateTime.now());
    }

    public void deleteMemosPermanently(List<Long> memoIds) {
        memoRepository.deleteAllById(memoIds);
    }

    private LocalDateTime parseDateTimeSafe(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) return null;
        String cleaned = dateTimeStr.length() >= 19 ? dateTimeStr.substring(0, 19) : dateTimeStr;
        return LocalDateTime.parse(cleaned);
    }

    @Scheduled(cron = "0 * * * * *")
    public void processExpiredMemos() {
        LocalDateTime now = LocalDateTime.now();
        int updatedCount = memoRepository.expireMemosToTrash(now, MemoStatus.TRASH);

        if (updatedCount > 0) {
            System.out.println("[Scheduler] " + updatedCount + "개의 불메모가 휴지통으로 자동 이동되었습니다. (" + now + ")");
        }
    }
}