package groupbee.calendar.service.redis;

import groupbee.calendar.service.feign.FeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarBookService {
    private final FeignClient feignClient;

}