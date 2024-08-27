package groupbee.calendar.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import groupbee.calendar.dto.CarBookDto;
import groupbee.calendar.dto.RoomBookDto;
import groupbee.calendar.service.calendar.CarBookService;
import groupbee.calendar.service.calendar.RoomBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper
    private final CarBookService carBookService;
    private final RoomBookService roomBookService;

    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(pattern); // 채널 이름을 추출
        String messageBody = new String(message.getBody()); // 메세지 본문을 추출

        log.info("Received JSON: {}", message);
        log.info("Received message: {} from channel: {}", messageBody, channel);

        try {
            // 수동으로 JSON을 파싱해보기 위해 단계별로 접근
            JsonNode rootNode = objectMapper.readTree(messageBody);
            String eventType = rootNode.get("eventType").asText();
            log.info("RedisSubscriber: {}", eventType);

            if ("car-book-events".equals(channel)) {
                if ("insert".equals(eventType)) {
                    String id = rootNode.get("id").asText();  // "id" 필드 가져오기
                    String memberId = rootNode.get("memberId").asText();  // "memberId" 필드 가져오기
                    String rentDay = rootNode.get("rentDay").asText();  // "rentDay" 필드 가져오기
                    String returnDay = rootNode.get("returnDay").asText();  // "returnDay" 필드 가져오기
                    String reason = rootNode.get("reason").asText();  // "reason" 필드 가져오기
                    String corporateCarId = rootNode.get("corporateCarId").asText();


                    // LocalDateTime으로 수동 변환 시도
                    LocalDateTime parsedRentDay = LocalDateTime.parse(rentDay, DateTimeFormatter.ISO_DATE_TIME);
                    LocalDateTime parsedReturnDay = LocalDateTime.parse(returnDay, DateTimeFormatter.ISO_DATE_TIME);

                    // 필요한 필드들만 모아서 직접 객체 생성하기
                    CarBookDto carBookDto = new CarBookDto();
                    carBookDto.setId(Long.parseLong(id));
                    carBookDto.setCorporateCarId(Long.parseLong(corporateCarId));
                    carBookDto.setMemberId(memberId);
                    carBookDto.setRentDay(parsedRentDay);
                    carBookDto.setReturnDay(parsedReturnDay);
                    carBookDto.setReason(reason);

                    saveCarBookEvent(carBookDto);
                } else if ("delete".equals(eventType)) {
                    deleteCarBookEvent(rootNode);
                } else if ("update".equals(eventType)) {
                    updateCarBookEvent(rootNode);
                }
            } else if ("room-book-events".equals(channel)) {
                if ("insert".equals(eventType)) {
                    String id = rootNode.get("id").asText();  // "id" 필드 가져오기
                    String memberId = rootNode.get("memberId").asText();  // "memberId" 필드 가져오기
                    String enter = rootNode.get("enter").asText();  // "enter" 필드 가져오기
                    String leave = rootNode.get("leave").asText();  // "leave" 필드 가져오기
                    String purpose = rootNode.get("purpose").asText();  // "reason" 필드 가져오기
                    String roomId = rootNode.get("roomId").asText();

                    LocalDateTime parsedEnter = LocalDateTime.parse(enter, DateTimeFormatter.ISO_DATE_TIME);
                    LocalDateTime parsedLeave = LocalDateTime.parse(leave, DateTimeFormatter.ISO_DATE_TIME);

                    RoomBookDto roomBookDto = new RoomBookDto();
                    roomBookDto.setId(Long.parseLong(id));
                    roomBookDto.setMemberId(memberId);
                    roomBookDto.setEnter(parsedEnter);
                    roomBookDto.setLeave(parsedLeave);
                    roomBookDto.setPurpose(purpose);
                    roomBookDto.setRoomId(Long.parseLong(roomId));

                    saveRoomBookEvent(roomBookDto);
                } else if ("delete".equals(eventType)) {
                    deleteRoomBookEvent(rootNode);
                } else if ("update".equals(eventType)) {
                    updateRoomBookEvent(rootNode);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("[RedisSubscriber] messageBody is NOT a valid JSON", e);
        }
    }

    private void saveCarBookEvent(CarBookDto carBookDto) {
        log.info("RedisSubscriber 'saveCarBookEvent': {}", carBookDto);
        carBookService.saveCarBookEvent(carBookDto);
    }

    private void saveRoomBookEvent(RoomBookDto roomBookDto) {
        log.info("RedisSubscriber 'saveRoomBookEvent': {}", roomBookDto);
        roomBookService.saveRoomBookEvent(roomBookDto);
    }

    private void deleteCarBookEvent(JsonNode rootNode) {
        try {
            Long corporateCarId = rootNode.get("id").asLong();
            carBookService.deleteCarBookEvent(corporateCarId);
            log.info("RedisSubscriber 'deleteCarBookEvent': {}", corporateCarId);
        } catch (Exception e) {
            log.error("messageBody is NOT a valid JSON", e);
        }
    }

    private void deleteRoomBookEvent(JsonNode rootNode) {
        try {
            Long roomId = rootNode.get("id").asLong();
            roomBookService.deleteRoomBookEvent(roomId);
            log.info("RedisSubscriber 'deleteRoomBookEvent': {}", roomId);
        } catch (Exception e) {
            log.error("messageBody is NOT a valid JSON", e);
        }
    }

    private void updateCarBookEvent(JsonNode rootNode) {
        try {
            String id = rootNode.get("id").asText();  // "id" 필드 가져오기
            String memberId = rootNode.get("memberId").asText();  // "memberId" 필드 가져오기
            String rentDay = rootNode.get("rentDay").asText();  // "rentDay" 필드 가져오기
            String returnDay = rootNode.get("returnDay").asText();  // "returnDay" 필드 가져오기
            String reason = rootNode.get("reason").asText();  // "reason" 필드 가져오기
            String corporateCarId = rootNode.get("corporateCarId").asText();


            // LocalDateTime으로 수동 변환 시도
            LocalDateTime parsedRentDay = LocalDateTime.parse(rentDay, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime parsedReturnDay = LocalDateTime.parse(returnDay, DateTimeFormatter.ISO_DATE_TIME);

            // 필요한 필드들만 모아서 직접 객체 생성하기
            CarBookDto carBookDto = new CarBookDto();
            carBookDto.setId(Long.parseLong(id));
            carBookDto.setCorporateCarId(Long.parseLong(corporateCarId));
            carBookDto.setMemberId(memberId);
            carBookDto.setRentDay(parsedRentDay);
            carBookDto.setReturnDay(parsedReturnDay);
            carBookDto.setReason(reason);
            carBookService.updateCarBookEvent(Long.parseLong(id), carBookDto);
        } catch (Exception e) {
            log.error("messageBody is NOT a valid JSON", e);
        }
    }

    private void updateRoomBookEvent(JsonNode rootNode) {
        try{
            String id = rootNode.get("id").asText();  // "id" 필드 가져오기
            String memberId = rootNode.get("memberId").asText();  // "memberId" 필드 가져오기
            String enter = rootNode.get("enter").asText();  // "enter" 필드 가져오기
            String leave = rootNode.get("leave").asText();  // "leave" 필드 가져오기
            String purpose = rootNode.get("purpose").asText();  // "purpose" 필드 가져오기
            String roomId = rootNode.get("roomId").asText();

            LocalDateTime parsedEnter = LocalDateTime.parse(enter, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime parsedLeave = LocalDateTime.parse(leave, DateTimeFormatter.ISO_DATE_TIME);

            RoomBookDto roomBookDto = new RoomBookDto();
            roomBookDto.setId(Long.parseLong(id));
            roomBookDto.setMemberId(memberId);
            roomBookDto.setEnter(parsedEnter);
            roomBookDto.setLeave(parsedLeave);
            roomBookDto.setPurpose(purpose);
            roomBookDto.setRoomId(Long.parseLong(roomId));
            roomBookService.updateRoomBookEvent(Long.parseLong(id), roomBookDto);
        } catch (Exception e) {
            log.error("messageBody is NOT a valid JSON", e);
        }
    }
}
