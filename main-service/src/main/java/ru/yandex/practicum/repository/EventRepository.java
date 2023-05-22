package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.State;
import ru.yandex.practicum.model.EventEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query("SELECT e FROM EventEntity e " +
            "JOIN e.category " +
            "WHERE ((:categoryIds) is null or e.category.id IN (:categoryIds)) " +
            "AND e.state = 'PUBLISHED' " +
            "AND (:paid is null or paid is :paid) " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    Page<EventEntity> searchPublishedEvents(@Param("categoryIds") List<Long> categoryIds,
                                            @Param("paid") Boolean paid,
                                            @Param("rangeStart") LocalDateTime start,
                                            @Param("rangeEnd") LocalDateTime end,
                                            Pageable pageable);


    @Query("SELECT e FROM EventEntity e " +
            "JOIN e.initiator " +
            "JOIN e.category " +
            "WHERE e.initiator.id IN :userIds " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    Page<EventEntity> findAllWithAllParameters(@Param("userIds") List<Long> userIds,
                                               @Param("states") List<State> states,
                                               @Param("categories") List<Long> categories,
                                               @Param("rangeStart") LocalDateTime rangeStart,
                                               @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM EventEntity e " +
            "JOIN e.category " +
            "WHERE e.category.id IN :categories " +
            "AND e.state IN :states " +
            "AND e.eventDate BETWEEN:rangeStart AND:rangeEnd")
    Page<EventEntity> findAllEventsWithoutIdList(@Param("categories") List<Long> categories, @Param("states") List<State> states,
                                                 @Param("rangeStart") LocalDateTime rangeStart,
                                                 @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM EventEntity e " +
            "JOIN e.initiator " +
            "JOIN e.category " +
            "WHERE e.initiator.id = :userId")
    Page<EventEntity> findAllByUserWithPage(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT e FROM EventEntity e " +
            "WHERE e.id = :id " +
            "AND e.state ='PUBLISHED'")
    Optional<EventEntity> findEventByIdAndStatePublished(@Param("id") Long eventId);

    @Query("SELECT e FROM EventEntity e " +
            "JOIN e.category " +
            "WHERE e.category.id = :catId")
    Optional<List<EventEntity>> findAllByCategoryId(Long catId);

    List<EventEntity> findAllByIdIn(Iterable<Long> ids);

}
