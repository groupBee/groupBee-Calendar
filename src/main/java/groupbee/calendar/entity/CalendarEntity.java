package groupbee.calendar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "calendar")
public class CalendarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="member_id")
    private Long memberId;

    @Column(name="year")
    private Long year;

    @Column(name="month")
    private Long month;

    @Column(name="day")
    private Long day;

    @Column(name="content")
    private String content;

    @Column(name="create_day")
    private String createDay;

    @Column(name="title")
    private String title;
}
