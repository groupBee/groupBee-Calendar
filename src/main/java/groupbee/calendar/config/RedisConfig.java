package groupbee.calendar.config;

import groupbee.calendar.pubsub.RedisSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private String redisPort;

    // Redis 와의 연결을 설정하고 관리
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        // Host, Port, Password 설정
        config.setHostName(redisHost);
        config.setPort(Integer.parseInt(redisPort));
        // ConnectionFactory 생성
        return new LettuceConnectionFactory(config);
    }

    // serializer 설정으로 redis-cli 를 통해 직접 데이터를 조회할 수 있도록 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    // redis 에 발행 데이터가 있는지 확인
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory redisConnectionFactory,
                                                              MessageListenerAdapter CarBookSubscriber,
                                                              MessageListenerAdapter RoomBookSubscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(CarBookSubscriber, CarBookTopic());
        container.addMessageListener(RoomBookSubscriber, RoomBookTopic());
        return container;
    }

    // 메시지 리스너와 채널 간의 연결을 관리
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "onMessage");
    }

    // channelTopic 설정
    // Redis 에서 pub/sub 할 채널을 지정.
    @Bean
    public ChannelTopic CarBookTopic() {
        return new ChannelTopic("car-book-events");
    }
    @Bean
    public ChannelTopic RoomBookTopic() {
        return new ChannelTopic("room-book-events");
    }
}
