package groupbee.calendar.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CarBookDto {
    private Long memberId;
    private Long corporateCarId;
    private LocalDate rentDay;
    private LocalDate returnDay;
    private String reason;
}
