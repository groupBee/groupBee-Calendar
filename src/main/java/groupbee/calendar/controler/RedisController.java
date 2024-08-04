package groupbee.calendar.controler;

import groupbee.calendar.dto.GetBookMessage;
import groupbee.calendar.pubsub.RedisPublisher;
import groupbee.calendar.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
public class RedisController {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisPublisher redisPublisher;

//    @PostMapping("/chat")
//    public String pubSub(@RequestBody GetBookMessage getBookMessage, @RequestParam String topic) {
//        redisPublisher.sendMessage(topic, getBookMessage);
//        return "success";
//    }

//    @GetMapping("/get/{key}")
//    public ResponseEntity<Object> getRedisKey(@PathVariable String key) {
//        try {
//            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//            String value = valueOperations.get(key);
//
//            if (value == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Key not found");
//            }
//
//            return ResponseEntity.status(HttpStatus.OK).body(value);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Redis error", e);
//        }
//    }

}
