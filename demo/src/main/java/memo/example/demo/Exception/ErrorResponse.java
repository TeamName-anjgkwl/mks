package memo.example.demo.Exception;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, String> fieldErrors; // 유효성 검사 실패 시 필드별 에러 내용
}