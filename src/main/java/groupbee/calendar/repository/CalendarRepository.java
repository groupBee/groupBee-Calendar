package groupbee.calendar.repository;

import groupbee.calendar.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {
    List<CalendarEntity> findByMemberId(String memberId);

    CalendarEntity findByCorporateCarBookId(Long corporateCarBookId);
}
