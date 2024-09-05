package groupbee.calendar.dto;

import com.google.api.services.calendar.model.Event;
import groupbee.calendar.entity.CalendarEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CombinedCalendarResponseDto {
    private List<CalendarEntity> dbEvents;
    private List<Event> googleEvents;
}
