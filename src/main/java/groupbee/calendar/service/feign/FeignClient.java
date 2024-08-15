package groupbee.calendar.service.feign;

import groupbee.calendar.config.FeignConfig;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@org.springframework.cloud.openfeign.FeignClient(name = "employee", url = "${FEIGN_BASE_URL}", configuration = FeignConfig.class)
public interface FeignClient {

    @GetMapping("/api/employee/info")
    Map<String, Object> getEmployeeInfo();
}