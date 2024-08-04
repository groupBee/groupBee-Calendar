package groupbee.calendar.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupbee.calendar.dto.GetBookMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper
    public static List<String> messageList = new ArrayList<>();

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        try {
            GetBookMessage bookMessage = objectMapper.readValue(message.getBody(), GetBookMessage.class);
            messageList.add(message.toString());

            System.out.println("받은 메세지 : " + message.toString());
            System.out.println("GetBookMessage.getCarID() : " + bookMessage.getCarID());
            System.out.println("GetBookMessage.getCarType() : " + bookMessage.getCarType());
            System.out.println("GetBookMessage.getRentDay() : " + bookMessage.getRentDay());
            System.out.println("GetBookMessage.getReturnDay() : " + bookMessage.getReturnDay());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
