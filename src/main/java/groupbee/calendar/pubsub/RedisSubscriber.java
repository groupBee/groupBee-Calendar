package groupbee.calendar.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import groupbee.calendar.dto.CarBookDto;
import groupbee.calendar.dto.RoomBookDto;
import groupbee.calendar.service.calendar.CarBookService;
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
                    String reson = rootNode.get("reason").asText();  // "reason" 필드 가져오기


                    // LocalDateTime으로 수동 변환 시도
                    LocalDateTime parsedRentDay = LocalDateTime.parse(rentDay, DateTimeFormatter.ISO_DATE_TIME);
                    LocalDateTime parsedReturnDay = LocalDateTime.parse(returnDay, DateTimeFormatter.ISO_DATE_TIME);

                    // 필요한 필드들만 모아서 직접 객체 생성하기
                    CarBookDto carBookDto = new CarBookDto();
                    carBookDto.setId(Long.parseLong(id));
                    carBookDto.setMemberId(memberId);
                    carBookDto.setRentDay(parsedRentDay);
                    carBookDto.setReturnDay(parsedReturnDay);
                    carBookDto.setReason(reson);

                    saveCarBookEvent(carBookDto);
                } else if ("delete".equals(eventType)) {
                    deleteCarBookEvent(rootNode);
                } else if ("update".equals(eventType)) {
                    updateCarBookEvent(rootNode);
                }
            } else if ("room-book-events".equals(channel)) {

            }
        } catch (JsonProcessingException e) {
            log.error("[RedisSubscriber] messageBody is NOT a valid JSON", e);
        }
    }

    private void saveCarBookEvent(CarBookDto carBookDto) {
        log.info("RedisSubscriber 'saveCarBookEvent': {}", carBookDto);
        carBookService.saveCarBookEvent(carBookDto);

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

    private void updateCarBookEvent(JsonNode rootNode) {
        try {
            String id = rootNode.get("id").asText();  // "id" 필드 가져오기
            String memberId = rootNode.get("memberId").asText();  // "memberId" 필드 가져오기
            String rentDay = rootNode.get("rentDay").asText();  // "rentDay" 필드 가져오기
            String returnDay = rootNode.get("returnDay").asText();  // "returnDay" 필드 가져오기
            String reson = rootNode.get("reason").asText();  // "reason" 필드 가져오기


            // LocalDateTime으로 수동 변환 시도
            LocalDateTime parsedRentDay = LocalDateTime.parse(rentDay, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime parsedReturnDay = LocalDateTime.parse(returnDay, DateTimeFormatter.ISO_DATE_TIME);

            // 필요한 필드들만 모아서 직접 객체 생성하기
            CarBookDto carBookDto = new CarBookDto();
            carBookDto.setId(Long.parseLong(id));
            carBookDto.setMemberId(memberId);
            carBookDto.setRentDay(parsedRentDay);
            carBookDto.setReturnDay(parsedReturnDay);
            carBookDto.setReason(reson);
            carBookService.updateCarBookEvent(carBookDto);
        } catch (Exception e) {
            log.error("messageBody is NOT a valid JSON", e);
        }
    }

    private void processRoomBookEvent(RoomBookDto roomBookDto) {
        log.info("Processing room book event: {}", roomBookDto);
    }
}
