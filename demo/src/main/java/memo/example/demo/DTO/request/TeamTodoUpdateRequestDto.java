package memo.example.demo.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamTodoUpdateRequestDto {
    private String title;
    private String content;
    private String dueDate;
    private Boolean isChecked;
    private Boolean sendPush;
}