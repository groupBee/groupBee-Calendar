package groupbee.calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients // Openfeign 클리이언트를 활성화하는 어노테이션
@EnableTransactionManagement // 트랜잭션을 하기 위하 활성화하는 어노테이션
public class CalendarApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendarApplication.class, args);
    }

}
