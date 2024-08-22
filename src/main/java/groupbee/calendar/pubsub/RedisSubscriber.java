package groupbee.calendar.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import groupbee.calendar.dto.CarBookDto;
import groupbee.calendar.dto.RoomBookDto;
import groupbee.calendar.service.calendar.CalendarService;
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
    private final CalendarService calendarService;

    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(pattern); // 채널 이름을 추출
        String messageBody = new String(message.getBody()); // 메세지 본문을 추출

        log.info("Received JSON: {}", message);
        log.info("Received message: {} from channel: {}", messageBody, channel);

        if ("car-book-events".equals(channel)) {
            try {
                // 수동으로 JSON을 파싱해보기 위해 단계별로 접근
                JsonNode rootNode = objectMapper.readTree(messageBody);
                String id = rootNode.get("id").asText();  // "id" 필드 가져오기
                String memberId = rootNode.get("memberId").asText();  // "memberId" 필드 가져오기
                String rentDay = rootNode.get("rentDay").asText();  // "rentDay" 필드 가져오기
                String returnDay = rootNode.get("returnDay").asText();  // "returnDay" 필드 가져오기
                String reson = rootNode.get("reason").asText();  // "reason" 필드 가져오기


                // LocalDateTime으로 수동 변환 시도
                LocalDateTime parsedRentDay = LocalDateTime.parse(rentDay, DateTimeFormatter.ISO_DATE_TIME);
                LocalDateTime parsedReturnDay = LocalDateTime.parse(returnDay, DateTimeFormatter.ISO_DATE_TIME);

                // 필요한 필드들만 모아서 직접 객체 생성해보기
                CarBookDto carBookDto = new CarBookDto();
                carBookDto.setId(Long.parseLong(id));
                carBookDto.setMemberId(memberId);
                carBookDto.setRentDay(parsedRentDay);
                carBookDto.setReturnDay(parsedReturnDay);
                carBookDto.setReason(reson);

                //log.info("Successfully created CarBookDto: {}", carBookDto);
                processCarBookEvent(carBookDto);
            } catch (JsonProcessingException e) {
                log.error("messageBody is NOT a valid JSON", e);
            }
        } else if ("rome-book-events".equals(channel)) {
            try {
                // 수동으로 JSON을 파싱해보기 위해 단계별로 접근
                JsonNode rootNode = objectMapper.readTree(messageBody);
                String id = rootNode.get("id").asText();  // "id" 필드 가져오기
                String rentDay = rootNode.get("rentDay").asText();  // "rentDay" 필드 가져오기

                log.info("Parsed ID: {}", id);
                log.info("Parsed rentDay: {}", rentDay);

                // LocalDateTime으로 수동 변환 시도
                LocalDateTime parsedRentDay = LocalDateTime.parse(rentDay, DateTimeFormatter.ISO_DATE_TIME);
                log.info("Successfully parsed rentDay: {}", parsedRentDay);

                // 필요한 필드들만 모아서 직접 객체 생성해보기
                CarBookDto carBookDto = new CarBookDto();
                carBookDto.setId(Long.parseLong(id));
                carBookDto.setRentDay(parsedRentDay);
                // 다른 필드들 추가적으로 세팅...

                log.info("Successfully created CarBookDto: {}", carBookDto);
            } catch (JsonProcessingException e) {
                log.error("messageBody is NOT a valid JSON", e);
            }
        }
    }

    private void processCarBookEvent(CarBookDto carBookDto) {
        log.info("Processing car book event: {}", carBookDto);
        calendarService.saveCarBookEvent(carBookDto);
    }

    private void processRoomBookEvent(RoomBookDto roomBookDto) {
        log.info("Processing room book event: {}", roomBookDto);
    }
}
