package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.request.MemoRequestDto;
import memo.example.demo.DTO.request.MemoUpdateRequestDto;
import memo.example.demo.DTO.response.MemoResponseDto;
import memo.example.demo.DTO.response.TrashResponseDto;
import memo.example.demo.domain.Memo;
import memo.example.demo.domain.Memo.MemoStatus;
import memo.example.demo.domain.MemoImage;
import memo.example.demo.domain.TeamSpace;
import memo.example.demo.domain.User;
import memo.example.demo.repository.MemoImageRepository;
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
    private final MemoImageRepository memoImageRepository;

    public Long createMemo(Long userId, MemoRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow();
        TeamSpace teamSpace = request.getTeamSpaceId() != null ?
                teamSpaceRepository.findById(request.getTeamSpaceId()).orElse(null) : null;

        Memo memo = Memo.builder()
                .user(user)
                .teamSpace(teamSpace)
                .status(request.getStatus() != null ? MemoStatus.valueOf(request.getStatus()) : MemoStatus.ICE)
                .mTitle(request.getTitle())
                .mContent(request.getContent())
                .mRichContent(request.getRichContent())
                .expiredAt(parseDateTimeSafe(request.getExpiredAt()))
                .build();

        return memoRepository.save(memo).getMemoId();
    }

    @Transactional(readOnly = true)
    public List<MemoResponseDto> getUserMemos(Long userId) {
        // 수정됨: deletedAt이 null인 것만 (정상 메모)
        return memoRepository.findByUser_UserIdAndTeamSpaceIsNull(userId).stream()
                .filter(m -> m.getDeletedAt() == null)
                .map(MemoResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemoResponseDto> getTeamMemos(Long teamSpaceId) {
        // 수정됨: deletedAt이 null인 것만 (정상 메모)
        return memoRepository.findByTeamSpace_TeamSpaceId(teamSpaceId).stream()
                .filter(m -> m.getDeletedAt() == null)
                .map(MemoResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemoResponseDto getMemoDetail(Long memoId) {
        Memo memo = memoRepository.findById(memoId).orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
        List<MemoImage> images = memoImageRepository.findByMemo_MemoId(memoId);
        return MemoResponseDto.from(memo, images);
    }

    @Transactional(readOnly = true)
    public List<TrashResponseDto> getTrashList(Long userId) {
        // 수정됨: deletedAt이 null이 아닌 것만 (휴지통)
        return memoRepository.findByUser_UserIdAndTeamSpaceIsNull(userId).stream()
                .filter(m -> m.getDeletedAt() != null)
                .map(TrashResponseDto::from)
                .collect(Collectors.toList());
    }

    public void updateMemo(Long memoId, MemoUpdateRequestDto request) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        if(request.getTitle() != null) memo.setMTitle(request.getTitle());
        if(request.getContent() != null) memo.setMContent(request.getContent());
        if(request.getRichContent() != null) memo.setMRichContent(request.getRichContent());
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
        // 수정됨: status를 덮어쓰지 않고 deletedAt만 기록
        memo.setDeletedAt(LocalDateTime.now());
    }

    public void restoreMemo(Long memoId) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();

        // 원본 status가 그대로 남아있으므로 상태를 바꿀 필요가 없습니다!
        // 다만 불(FIRE) 메모의 경우 복구 시 만료일만 12시간 연장해 줍니다.
        if (memo.getExpiredAt() != null) {
            memo.setExpiredAt(LocalDateTime.now().plusHours(12));
        }

        memo.setDeletedAt(null); // 휴지통에서 꺼냄
    }

    public void deleteMemosPermanently(List<Long> memoIds) {
        if (memoIds == null || memoIds.isEmpty()) return;
        for (Long memoId : memoIds) {
            List<MemoImage> images = memoImageRepository.findByMemo_MemoId(memoId);
            if (!images.isEmpty()) {
                memoImageRepository.deleteAll(images);
            }
        }
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

        // 수정됨: 레포지토리 메서드 파라미터 변경 대응
        int updatedCount = memoRepository.expireMemosToTrash(now);
        if (updatedCount > 0) {
            System.out.println("[Scheduler] " + updatedCount + "개의 불메모 휴지통 이동 처리 (" + now + ")");
        }

        LocalDateTime twentyFourHoursAgo = now.minusHours(24);
        // 수정됨: status 기반이 아닌 deletedAt 기반 조회로 변경
        List<Memo> trashMemosToDelete = memoRepository.findByDeletedAtIsNotNullAndDeletedAtLessThanEqual(twentyFourHoursAgo);

        if (!trashMemosToDelete.isEmpty()) {
            List<Long> idsToDelete = trashMemosToDelete.stream().map(Memo::getMemoId).collect(Collectors.toList());
            deleteMemosPermanently(idsToDelete);
            System.out.println("[Scheduler] 24시간 경과 휴지통 메모 " + idsToDelete.size() + "개 영구 삭제 완료");
        }
    }
}