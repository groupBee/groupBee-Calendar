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
                .corporateCarBookId(carBookDto.getId())
                .corporateCarId(carBookDto.getCorporateCarId())
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

    public void deleteCarBookEvent(Long corporateCarBookId) {
        CalendarEntity calendarEntity = calendarRepository.findByCorporateCarBookId(corporateCarBookId);
        log.info("CarBookService deleteCarBookEvent: {}", calendarEntity);
        if (calendarEntity != null) {
            calendarRepository.delete(calendarEntity);
        } else {
            log.info("deleteCarBookEvent: {}", corporateCarBookId);
        }
    }

    public void updateCarBookEvent(Long corporateCarBookId, CarBookDto carBookDto) {
        CalendarEntity calendarEntity = calendarRepository.findByCorporateCarBookId(corporateCarBookId);

        if (calendarEntity != null) {
            calendarEntity.setMemberId(carBookDto.getMemberId());
            calendarEntity.setStartDay(carBookDto.getRentDay());
            calendarEntity.setEndDay(carBookDto.getReturnDay());
            calendarEntity.setContent(carBookDto.getReason());
            calendarRepository.save(calendarEntity);
        } else {
            log.info("CarBookService updateCarBookEvent: {}", corporateCarBookId);
        }
    }
}
