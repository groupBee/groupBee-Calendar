package groupbee.calendar.service.calendar;

import groupbee.calendar.dto.CarBookDto;
import groupbee.calendar.entity.CalendarEntity;
import groupbee.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarBookService {
    private final CalendarRepository calendarRepository;

    public void saveCarBookEvent(CarBookDto carBookDto) {
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .corporateCarId(carBookDto.getId())
                .memberId(carBookDto.getMemberId())
                .startDay(carBookDto.getRentDay())
                .endDay(carBookDto.getReturnDay())
                .content(carBookDto.getReason())
                .roomId(-1L) // 회의실 ID는 없기 때문에 -1
                .bookType(1L) // Car 는 1로 지정
                .title("차량 예약")
                .build();
        calendarRepository.save(calendarEntity);
    }

    public void deleteCarBookEvent(Long corporateCarId) {
        CalendarEntity calendarEntity = calendarRepository.findByCorporateCarId(corporateCarId);
        if (calendarEntity != null) {
            calendarRepository.delete(calendarEntity);
        } else {
            log.info("deleteCarBookEvent: {}", corporateCarId);
        }
    }

    public void updateCarBookEvent(CarBookDto carBookDto) {
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .corporateCarId(carBookDto.getId())
                .memberId(carBookDto.getMemberId())
                .startDay(carBookDto.getRentDay())
                .endDay(carBookDto.getReturnDay())
                .content(carBookDto.getReason())
                .roomId(-1L) // 회의실 ID는 없기 때문에 -1
                .bookType(1L) // Car 는 1로 지정
                .title("차량 예약")
                .build();
        calendarRepository.save(calendarEntity);
    }
}
