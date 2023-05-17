package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Timestamp;
import lombok.*;
import ru.practicum.event.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "participation_requests")
public class ParticipationRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Timestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private UserEntity requester;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    public ParticipationRequestEntity(LocalDateTime created, EventEntity event, UserEntity requester, State state) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.state = state;
    }
}
