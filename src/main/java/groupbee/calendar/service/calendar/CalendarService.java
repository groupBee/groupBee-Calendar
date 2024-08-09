package groupbee.calendar.service.calendar;

import groupbee.calendar.entity.CalendarEntity;
import groupbee.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;

    // 멤버 아이디로 데이터 가져오기
    public CalendarEntity findByMemberId(Long id) {
        return calendarRepository.findByMemberId(id);
    }

    public CalendarEntity save(CalendarEntity calendarEntity) {
        return calendarRepository.save(calendarEntity);
    }

    public CalendarEntity update(CalendarEntity calendarEntity) {
        return calendarRepository.save(calendarEntity);
    }

    public void deleteById(Long id) {
        calendarRepository.deleteById(id);
    }
}
