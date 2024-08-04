package groupbee.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBookMessage {
    private String carID; // 차량 번호
    private String carType; // 차량 종류
    private String rentDay; // 대여시간
    private String returnDay; //반납시간
}
