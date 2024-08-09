package groupbee.calendar.service.openfeign;

import groupbee.calendar.dto.CarBookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "car-book-service", url = "http://localhost:9522")
public interface OpenFeignClient {

    @GetMapping("/api/redis/books/car/{memberId}")
    List<CarBookDto> getCarBookEvents(@PathVariable("memberId") Long memberId); // <1>
}
