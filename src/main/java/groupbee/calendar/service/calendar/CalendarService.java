package groupbee.calendar.service.calendar;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import feign.FeignException;
import groupbee.calendar.entity.CalendarEntity;
import groupbee.calendar.repository.CalendarRepository;
import groupbee.calendar.service.feign.FeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final FeignClient feignClient;

    private final String API_KEY = System.getenv("GOOGLE_API_KEY");
    private final static JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // 멤버아이디를 기준으로 모든 데이터 출력
    public ResponseEntity<List<CalendarEntity>> findByMemberId() {
        try {
            Map<String, Object> response = feignClient.getEmployeeInfo();
            String potalId = (String) response.get("potalId");

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
            String potalId = (String) response.get("potalId");
            calendarEntity.setMemberId(potalId);

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
            String potalId = (String) response.get("potalId");
            calendarEntity.setMemberId(potalId);
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

    @Transactional
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

    @Async
    public CompletableFuture<List<Map<String, Object>>> fetchGoogleHolidays(int year) throws IOException, GeneralSecurityException {
        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                null)
                .setApplicationName("GroupBee")
                .build();

        String calendarId = "ko.south_korea#holiday@group.v.calendar.google.com";
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59);
        DateTime timeMin = new DateTime(startOfYear.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        DateTime timeMax = new DateTime(endOfYear.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        Events events = service.events().list(calendarId)
                .setKey(API_KEY)
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Map<String, Object>> googleHolidays = events.getItems().stream()
                .map(event -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", null);
                    map.put("title", event.getSummary());
                    LocalDate date = LocalDate.parse(event.getStart().getDate().toString());
                    LocalDateTime startDateTime = date.atStartOfDay();
                    LocalDateTime endDateTime = startDateTime.plusDays(1);
                    String formattedStartDateTime = startDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
                    String formattedEndDateTime = endDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
                    map.put("startDay", formattedStartDateTime);
                    map.put("endDay", formattedEndDateTime);
                    map.put("type", 100);
                    return map;
                })
                .toList();

        return CompletableFuture.completedFuture(googleHolidays);
    }

    @Async
    public CompletableFuture<List<Map<String, Object>>> fetchDbEvents() {
        Map<String, Object> response = feignClient.getEmployeeInfo();
        String potalId = (String) response.get("potalId");

        List<CalendarEntity> calendarEntities = calendarRepository.findByMemberId(potalId);
        List<Map<String, Object>> dbEvents = calendarEntities.stream()
                .map(event -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", event.getId());
                    map.put("title", event.getTitle());
                    map.put("startDay", event.getStartDay());
                    map.put("endDay", event.getEndDay());
                    return map;
                })
                .toList();

        return CompletableFuture.completedFuture(dbEvents);
    }

    public CompletableFuture<ResponseEntity<List<Object>>> fetchCombinedEvents(int year) throws GeneralSecurityException, IOException {
        CompletableFuture<List<Map<String, Object>>> googleHolidaysFuture = fetchGoogleHolidays(year);
        CompletableFuture<List<Map<String, Object>>> dbEventsFuture = fetchDbEvents();

        return googleHolidaysFuture.thenCombine(dbEventsFuture, (googleHolidays, dbEvents) -> {
            List<Object> combinedEvents = new ArrayList<>();
            combinedEvents.addAll(googleHolidays);
            combinedEvents.addAll(dbEvents);
            return ResponseEntity.ok(combinedEvents);
        });
    }
}
