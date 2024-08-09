package groupbee.calendar.repository;

import groupbee.calendar.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {
    CalendarEntity findByMemberId(Long id);
}
