package groupbee.calendar.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarBookSubscriber implements MessageListener {
    //private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {

    }
}
