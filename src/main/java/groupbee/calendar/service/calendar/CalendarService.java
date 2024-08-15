package groupbee.calendar.service.calendar;

import groupbee.calendar.entity.CalendarEntity;
import groupbee.calendar.repository.CalendarRepository;
import groupbee.calendar.service.feign.FeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final FeignClient feignClient;

    // 멤버아이디를 기준으로 모든 데이터 출력
    public List<CalendarEntity> findByMemberId() {
        Map<String, Object> response = feignClient.getEmployeeInfo();
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        String potalId = (String) data.get("potal_id");
        return calendarRepository.findByMemberId(potalId);
    }

    public CalendarEntity save(CalendarEntity calendarEntity) {
        // OpenFeign 클라이언트를 통해 potal_id 가져오기
        Map<String, Object> response = feignClient.getEmployeeInfo();
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        calendarEntity.setMemberId((String) data.get("potal_id"));

        CalendarEntity saveEntity = calendarRepository.save(calendarEntity);

        String originalDate = String.valueOf(saveEntity.getCreateDay());
        System.out.println(originalDate);

        return saveEntity;
    }

    public CalendarEntity update(CalendarEntity calendarEntity) {
        calendarEntity.setCreateDay(LocalDateTime.now());

        CalendarEntity saveEntity = calendarRepository.save(calendarEntity);
        System.out.println(saveEntity.getCreateDay());

        return saveEntity;
    }

    public void deleteById(Long id) {
        calendarRepository.deleteById(id);
    }
}
