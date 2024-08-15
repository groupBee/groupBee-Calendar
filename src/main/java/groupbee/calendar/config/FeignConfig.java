package groupbee.calendar.config;

import feign.RequestInterceptor;
import groupbee.calendar.service.redis.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenFeignConfig {
    private final SessionService sessionService;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 세션 ID를 SessionService 에서 가져옴
            String sessionCookie = sessionService.getSessionId();
            System.out.println("OpenFeignConfig : " + sessionCookie);
            if (sessionCookie != null) {
                requestTemplate.header("Cookie", sessionCookie);
            }
        };
    }
}
