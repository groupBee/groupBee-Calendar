package groupbee.calendar.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@Table(name = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CalendarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="member_id")
    private String memberId;

    @Column(name="year")
    private int year;

    @Column(name="month")
    private int month;

    @Column(name="day")
    private int day;

    @Column(name="content")
    private String content;

    @Column(name="create_day")
    @CreationTimestamp
    private LocalDateTime createDay;

    @Column(name="title")
    private String title;
}
