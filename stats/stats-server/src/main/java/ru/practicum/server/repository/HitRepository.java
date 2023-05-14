package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.server.model.HitEntity;
import ru.practicum.stat.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<HitEntity, Long> {

    @Query("select new  ru.practicum.stat.ViewStats(e.app, e.uri, count(e.ip)) " +
            "from HitEntity as e " +
            "where e.timestamp between :start and :end " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc")
    List<ViewStats> findAllByTimestampBetween(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

    @Query("select new  ru.practicum.stat.ViewStats(e.app, e.uri, count(e.ip)) " +
            "from HitEntity as e " +
            "where e.timestamp between :start and :end " +
            "and e.uri in (:uris) " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc ")
    List<ViewStats> findAllByTimestampBetweenAndUriIn(@Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end,
                                                      @Param("uris") List<String> uris);

    @Query("select new  ru.practicum.stat.ViewStats(e.app, e.uri, count(distinct e.ip)) " +
            "from HitEntity as e " +
            "where e.timestamp between :start and :end " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc ")
    List<ViewStats> findAllByTimestampBetweenUnique(@Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);

    @Query("select new  ru.practicum.stat.ViewStats(e.app, e.uri, count(distinct e.ip)) " +
            "from HitEntity as e " +
            "where e.timestamp between :start and :end " +
            "and e.uri in (:uris) " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc ")
    List<ViewStats> findAllByTimestampBetweenAndUriInUnique(@Param("start") LocalDateTime start,
                                                            @Param("end") LocalDateTime end,
                                                            @Param("uris") List<String> uris);

}
