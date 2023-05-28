package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.ParticipationRequestEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestsRepository extends JpaRepository<ParticipationRequestEntity, Long> {

    @Query("SELECT p FROM ParticipationRequestEntity p " +
            "WHERE p.requester.id = :userId " +
            "AND p.event.id = :eventId")
    Optional<ParticipationRequestEntity> findByRequesterIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);


    @Query("SELECT p FROM ParticipationRequestEntity p " +
            "JOIN FETCH p.event e " +
            "WHERE p.requester.id =:userId " +
            "AND e.initiator.id <> :userId")
    List<ParticipationRequestEntity> findAllByRequesterIdInForeignEvents(@Param("userId") Long userId);

    @Query("SELECT p FROM ParticipationRequestEntity p " +
            "JOIN FETCH p.event e " +
            "WHERE e.initiator.id =:userId " +
            "AND e.id = :eventId")
    List<ParticipationRequestEntity> findAllUserRequestsInEvent(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query("SELECT p FROM ParticipationRequestEntity p " +
            "JOIN FETCH p.event e " +
            "WHERE p.requester.id =:requesterId " +
            "AND e.id = :eventId " +
            "AND p.state = 'CONFIRMED'")
    Optional<ParticipationRequestEntity> findParticipationRequestByRequesterIdAndEventId(@Param("requesterId") Long requesterId,
                                                                                         @Param("eventId") Long eventId);
}
