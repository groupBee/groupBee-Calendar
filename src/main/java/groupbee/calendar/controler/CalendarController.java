package groupbee.calendar.controler;

import groupbee.calendar.entity.CalendarEntity;
import groupbee.calendar.service.calendar.CalendarService;
import groupbee.calendar.service.feign.FeignClient;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;
    private final FeignClient feignClient;

    @Operation(
            summary = "캘린더 전체 리스트 출력",
            description = "관리자 페이지에서 사용하기 위함"
    )
    @GetMapping("/list")
    ResponseEntity<List<CalendarEntity>> findByMemberId() {
        return calendarService.findByMemberId();
    }

    @Operation(
            summary = "캘린더 일정 추가",
            description = "멤버 아이디 별 캘린더 일정 추가"
    )
    @PostMapping("/write")
    public ResponseEntity<CalendarEntity> save(@RequestBody CalendarEntity calendarEntity) {
        return calendarService.save(calendarEntity);
    }

    @Operation(
            summary = "feignClient 를 통해 요청",
            description = "JSON 에서 data 의 정보를 가져오기(테스트용)"
    )
    @GetMapping("/test")
    public String test() {
        // OpenFeign 클라이언트를 통해 요청 보내기
        Map<String, Object> response = feignClient.getEmployeeInfo();
        log.info("response: {}", response.get("potalId"));
        return "test";
    }

    @Operation(
            summary = "캘린더 일정 삭제",
            description = "캘린더 일정 삭제"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean result = calendarService.deleteById(id);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "캘린더 일정 수정",
            description = "캘린더 일정 수정"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CalendarEntity> update(@PathVariable Long id, @RequestBody CalendarEntity calendarEntity) {
        calendarEntity.setId(id);
        return calendarService.update(calendarEntity);
    }

    @Operation(
            summary = "캘린더 일정 수정을 위한 정보 출력",
            description = "캘린더 일정 수정을 위한 정보 출력"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CalendarEntity> findById(@PathVariable Long id) {
        return calendarService.findById(id);
    }

    @GetMapping("/list/{year}")
    public CompletableFuture<ResponseEntity<List<Object>>> getGoogleEvents(@PathVariable int year) throws Exception {
        return calendarService.fetchCombinedEvents(year);
    }
}
