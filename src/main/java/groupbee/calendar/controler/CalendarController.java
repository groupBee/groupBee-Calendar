package groupbee.calendar.controler;

import groupbee.calendar.entity.CalendarEntity;
import groupbee.calendar.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/{id}")
    public CalendarEntity findByMemberId(@PathVariable Long id) {
        return calendarService.findByMemberId(id);
    }
}
