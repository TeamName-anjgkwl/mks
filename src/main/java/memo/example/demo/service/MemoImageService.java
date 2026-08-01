package memo.example.demo.service;

import lombok.RequiredArgsConstructor;
import memo.example.demo.controller.MemoImageController.*;
import memo.example.demo.domain.Memo;
import memo.example.demo.domain.MemoImage;
import memo.example.demo.repository.MemoImageRepository;
import memo.example.demo.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoImageService {

    private final MemoImageRepository memoImageRepository;
    private final MemoRepository memoRepository;

    public void addImageToMemo(Long memoId, String imageUrl) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메모입니다."));

        MemoImage memoImage = MemoImage.builder()
                .memo(memo)
                .imageUrl(imageUrl)
                .build();

        memoImageRepository.save(memoImage);
    }

    @Transactional(readOnly = true)
    public List<MemoImageResponse> getImagesByMemo(Long memoId) {
        return memoImageRepository.findByMemo_MemoId(memoId).stream()
                .map(img -> new MemoImageResponse(
                        img.getImageId(),
                        img.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    public void deleteImage(Long imageId) {
        memoImageRepository.deleteById(imageId);
    }
}