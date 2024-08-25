package groupbee.calendar.service.feign;

import groupbee.calendar.config.FeignConfig;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@org.springframework.cloud.openfeign.FeignClient(name = "employee", url = "${FEIGN_BASE_URL}", configuration = FeignConfig.class)
public interface FeignClient {

    @GetMapping("/api/employee/info")
    Map<String, Object> getEmployeeInfo();

    @DeleteMapping("/api/cars/delete/{id}")
    void deleteCar(@PathVariable Long id);
}