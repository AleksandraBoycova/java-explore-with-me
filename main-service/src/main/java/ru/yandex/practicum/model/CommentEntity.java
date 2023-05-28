package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Timestamp;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 20, max = 7000)
    @Column(name = "text")
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Timestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
}
