package groupbee.calendar.service.calendar;

import groupbee.calendar.dto.RoomBookDto;
import groupbee.calendar.entity.CalendarEntity;
import groupbee.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomBookService {
    private final CalendarRepository calendarRepository;

    public void saveRoomBookEvent(RoomBookDto roomBookDto) {
        CalendarEntity calendarEntity = CalendarEntity.builder()
                .roomId(roomBookDto.getId())
                .memberId(roomBookDto.getMemberId())
                .startDay(roomBookDto.getEnter())
                .endDay(roomBookDto.getLeave())
                .content(roomBookDto.getPurpose())
                .corporateCarBookId(-1L) // 차량 ID는 없기 때문에 -1
                .bookType(2L) // Room 는 2로 지정
                .title("회의실 예약")
                .build();
        calendarRepository.save(calendarEntity);
    }

    public void deleteRoomBookEvent(Long roomId) {
        CalendarEntity calendarEntity = calendarRepository.findByRoomId(roomId);
        log.info("RoomBookService deleteRoomBookEvent: {}", calendarEntity);
        if (calendarEntity != null) {
            calendarRepository.delete(calendarEntity);
        } else {
            log.info("deleteRoomBookEvent: {}", roomId);
        }
    }

    public void updateRoomBookEvent(Long roomId, RoomBookDto roomBookDto) {
        CalendarEntity calendarEntity = calendarRepository.findByRoomId(roomId);

        if(calendarEntity != null) {
            calendarEntity.setMemberId(roomBookDto.getMemberId());
            calendarEntity.setStartDay(roomBookDto.getEnter());
            calendarEntity.setEndDay(roomBookDto.getLeave());
            calendarEntity.setContent(roomBookDto.getPurpose());
            calendarRepository.save(calendarEntity);
        } else {
            log.info("RoomBookService updateRoomBookEvent: {}", roomId);
        }
    }
}
