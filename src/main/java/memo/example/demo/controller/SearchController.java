package memo.example.demo.controller;

import lombok.RequiredArgsConstructor;
import memo.example.demo.DTO.response.SearchResponseDto;
import memo.example.demo.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponseDto> globalSearch(@RequestParam(name = "keyword") String keyword) {
        return ResponseEntity.ok(searchService.globalSearch(keyword));
    }
}