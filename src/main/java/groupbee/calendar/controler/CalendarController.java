package groupbee.calendar.controler;

import groupbee.calendar.entity.CalendarEntity;
import groupbee.calendar.service.calendar.CalendarService;
import groupbee.calendar.service.feign.FeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;
    private final FeignClient feignClient;

    // 전체 리스트 출력
    @GetMapping("/list")
    ResponseEntity<List<CalendarEntity>> findByMemberId() {
        return calendarService.findByMemberId();
    }

    // 멤버 아이디를 기준으로 데이터 입력
    @PostMapping("/write")
    public ResponseEntity<CalendarEntity> save(@RequestBody CalendarEntity calendarEntity) {
        System.out.println(calendarEntity);
        return calendarService.save(calendarEntity);
    }

    @GetMapping("/test")
    public String test() {
        // OpenFeign 클라이언트를 통해 요청 보내기
        Map<String, Object> response = feignClient.getEmployeeInfo();
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        // 응답에서 potal_id 추출하기
        if (response.containsKey("data")) {
            return "Member ID: " + data.get("potal_id").toString();
        } else {
            return "Member ID not found in response";
        }
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean result = calendarService.deleteById(id);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<CalendarEntity> update(@PathVariable Long id, @RequestBody CalendarEntity calendarEntity) {
        calendarEntity.setId(id);
        return calendarService.update(calendarEntity);
    }

    // 수정하기 위한 데이터 가지고 오기
    @GetMapping("/{id}")
    public ResponseEntity<CalendarEntity> findById(@PathVariable Long id) {
        return calendarService.findById(id);
    }
}
