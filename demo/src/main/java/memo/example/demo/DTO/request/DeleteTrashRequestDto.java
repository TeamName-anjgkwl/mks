package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DeleteTrashRequestDto {
    private List<Long> memoIds;
}