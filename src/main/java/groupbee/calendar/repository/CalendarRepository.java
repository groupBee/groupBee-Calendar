package groupbee.calendar.repository;

import groupbee.calendar.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {
    List<CalendarEntity> findByMemberId(String memberId);
}
