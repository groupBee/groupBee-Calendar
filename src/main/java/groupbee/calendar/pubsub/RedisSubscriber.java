package groupbee.calendar.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper
    private final RedisTemplate<String, Object> redisTemplate; // RedisTemplate 을 사용하여 Redis 와 상호작용

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        try {
            // 메시지 바디를 문자열로 역직렬화
            String messageBody = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            // JSON 형식의 문자열을 Java 객체로 변환
            String data = objectMapper.readValue(messageBody, String.class);
            // 변환된 데이터를 출력
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
