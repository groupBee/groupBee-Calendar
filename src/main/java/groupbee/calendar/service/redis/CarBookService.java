package groupbee.calendar.service.redis;

import groupbee.calendar.dto.CarBookDto;
import groupbee.calendar.service.openfeign.OpenFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarBookService {
    private final OpenFeignClient openFeignClient;

    public List<CarBookDto> getCarBookEvents(Long memberId) {
        return openFeignClient.getCarBookEvents(memberId);
    }
}