package groupbee.calendar.service.calendar;

import feign.FeignException;
import groupbee.calendar.entity.CalendarEntity;
import groupbee.calendar.repository.CalendarRepository;
import groupbee.calendar.service.feign.FeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final FeignClient feignClient;

    // 멤버아이디를 기준으로 모든 데이터 출력
    public ResponseEntity<List<CalendarEntity>> findByMemberId() {
        try {
            Map<String, Object> response = feignClient.getEmployeeInfo();
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            String potalId = (String) data.get("potalId");

            List<CalendarEntity> calendarEntities = calendarRepository.findByMemberId(potalId);

            if (calendarEntities.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(calendarEntities);
            }
        } catch (FeignException.BadRequest e) {
            // 400 Bad Request 발생 시 처리
            System.out.println("Bad Request: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (FeignException e) {
            // 기타 FeignException 발생 시 처리
            System.out.println("Feign Exception: " + e.getMessage());
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            // 일반 예외 처리
            System.out.println("Exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<CalendarEntity> save(CalendarEntity calendarEntity) {
        try {
            // OpenFeign 클라이언트를 통해 potal_id 가져오기
            Map<String, Object> response = feignClient.getEmployeeInfo();
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            calendarEntity.setMemberId((String) data.get("potalId"));

            CalendarEntity saveEntity = calendarRepository.save(calendarEntity);

            String originalDate = String.valueOf(saveEntity.getCreateDay());
            System.out.println(originalDate);

            // 201 Created 상태 코드와 함께 저장된 엔티티 반환
            return ResponseEntity.status(HttpStatus.CREATED).body(saveEntity);
        } catch (FeignException.BadRequest e) {
            // 400 Bad Request 발생 시 처리
            System.out.println("Bad Request: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (FeignException e) {
            // 기타 FeignException 발생 시 처리
            System.out.println("Feign Exception: " + e.getMessage());
            return ResponseEntity.status(e.status()).build();
        } catch (Exception e) {
            // 일반 예외 처리
            System.out.println("Exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<CalendarEntity> update(CalendarEntity calendarEntity) {
        try{
            // OpenFeign 클라이언트를 통해 potal_id 가져오기
            Map<String, Object> response = feignClient.getEmployeeInfo();
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            calendarEntity.setMemberId((String) data.get("potalId"));
            calendarEntity.setCreateDay(LocalDateTime.now());

            CalendarEntity saveEntity = calendarRepository.save(calendarEntity);
            System.out.println(saveEntity.getCreateDay());
            return ResponseEntity.ok(saveEntity);
        } catch (FeignException.BadRequest e) {
            // 400 Bad Request 발생 시 처리
            System.out.println("Bad Request: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (FeignException e) {
            // 기타 FeignException 발생 시 처리
            System.out.println("Feign Exception: " + e.getMessage());
            return ResponseEntity.status(e.status()).build();
        } catch (Exception e) {
            // 일반 예외 처리
            System.out.println("Exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public boolean deleteById(Long id) {
        Optional<CalendarEntity> optionalCalendarEntity = calendarRepository.findById(id);

        if (optionalCalendarEntity.isPresent()) {
            CalendarEntity calendarEntity = optionalCalendarEntity.get();
            Long bookType = calendarEntity.getBookType();

            try {
                if (bookType == 1) {
                    Long carBookId = calendarEntity.getCorporateCarBookId();
                    feignClient.deleteCar(carBookId);
                } else if (bookType == 2) {
                    Long roomBookId = calendarEntity.getRoomId();
                    feignClient.deleteRoom(roomBookId);
                }
            } catch (Exception e) {
                log.error("Failed to delete related resources: {}", e.getMessage());
                return false;
            }

            try {
                deleteCalendarEntity(calendarEntity);
                return true;
            } catch (Exception e) {
                log.error("Failed to delete CalendarEntity: {}", e.getMessage());
                return false;
            }
        } else {

            log.warn("CalendarEntity with id {} not found", id);
            return false;
        }
    }

    @Transactional
    public void deleteCalendarEntity(CalendarEntity calendarEntity) {
        calendarRepository.delete(calendarEntity);
    }

    public ResponseEntity<CalendarEntity> findById(Long id) {
        try {
            Optional<CalendarEntity> event = calendarRepository.findById(id);
            return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
