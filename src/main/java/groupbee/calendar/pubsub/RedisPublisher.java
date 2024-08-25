package groupbee.calendar.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPublisher {
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final ChannelTopic topic;
//    private final ObjectMapper objectMapper;
//
//    public void publish(CalendarDto calendarDto) {
//        try {
//            String messageJson = objectMapper.writeValueAsString(calendarDto);
//            log.info("RedisPublisher: {}", messageJson);
//            redisTemplate.convertAndSend(topic.getTopic(), messageJson);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }
}
